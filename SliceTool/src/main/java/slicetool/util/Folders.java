/*
 * Folders.java
 *
 * This file is part of SliceTool.
 *
 * SliceTool is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SliceTool is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SliceTool.  If not, see <http ://www.gnu.org/licenses/>.
 */
package slicetool.util;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zoltán Farkas on 2015.01.16.
 */
public class Folders {

    private static final List<File> TMP_FILES = new ArrayList<File>();

    static {
        deleteTmpFilesOnExit();
    }

    public static interface FileProcessor {
        public FileFilter[] getFilters();
        public void onMatch(File f);
        public void onMissmatch(File f);
        public void onMissingFile(File f);
    }

    public static abstract class FileProcessorAdapter implements FileProcessor {

        private final FileFilter[] mFilters;

        public FileProcessorAdapter(FileFilter... filters) {
            mFilters = filters;
        }

        @Override
        public FileFilter[] getFilters() {
            return mFilters;
        }

        @Override
        public abstract void onMatch(File f);

        @Override
        public void onMissmatch(File f) {
            ;
        }

        @Override
        public void onMissingFile(File f) {
            ;
        }

    }

    public static class ListFileProcessor extends FileProcessorAdapter {

        private final File mRoot;
        private final List<File> mFiles = new ArrayList<File>();
        private final List<String> mRelativePaths = new ArrayList<String>();

        public ListFileProcessor(File root, FileFilter... filters) {
            super(filters);
            mRoot = root;
        }

        @Override
        public void onMatch(File f) {
            mFiles.add(f);
            if (mRoot != null && mRoot.isDirectory()) {
                mRelativePaths.add(Folders.toRelativePath(mRoot, f));
            }
        }

        public List<File> getFiles() {
            return mFiles;
        }

        public List<String> getRelativePaths() {
            return mRelativePaths;
        }

    }

    public static ListFileProcessor listFiles(File file, FileFilter... filters) {
        return listFiles(file, -1, filters);
    }

    public static ListFileProcessor listFiles(File file, int limit, FileFilter... filters) {
        ListFileProcessor processor = new ListFileProcessor(file, filters);
        processFile(file, limit, processor);
        return processor;
    }

    public static void processFile(File file, int limit, FileProcessor processor) {
        if (file.isDirectory()) {
            if (limit == -1 || limit > 0) {
                for (File f : file.listFiles()) {
                    processFile(f, Math.max(-1, limit - 1), processor);
                }
            }
        }
        else {
            if (file.exists()) {
                boolean matches = accept(file, processor.getFilters());
                if (matches) {
                    processor.onMatch(file);
                }
                else {
                    processor.onMissmatch(file);
                }
            }
            else {
                processor.onMissingFile(file);
            }
        }
    }

    private static boolean accept(File file, FileFilter... filters) {
        if (file == null) {
            return false;
        }
        if (filters == null || filters.length == 0) {
            return true;
        }
        for (FileFilter filter : filters) {
            if (filter.accept(file)) {
                return true;
            }
        }
        return false;
    }

    public static boolean createTempDir(File dir) {
        boolean result;
        synchronized (TMP_FILES) {
            if (dir.exists()) {
                return dir.isDirectory();
            }
            result = dir.mkdirs();
            if (result) {
                TMP_FILES.add(dir);
            }
        }
        return result;
    }

    public static String joinPath(String... names) {
        StringBuilder sb = new StringBuilder();
        for (String name : names) {
            sb.append(name);
            sb.append(File.separator);
        }
        return sb.toString();
    }

    public static boolean createDir(File dir) {
        if (dir.exists()) {
            return dir.isDirectory();
        }
        return dir.mkdirs();
    }

    public static void delete(File f) {
        if (f.isDirectory()) {
            File[] files = f.listFiles();
            if (files != null) {
                for (File c : files) {
                    delete(c);
                }
            }
        }
        if (f.exists()) {
            f.delete();
        }
    }

    public static String toRelativePath(File absolutePath, File relativeTo) {
        if (absolutePath == null || relativeTo == null) {
            return null;
        }
        return toRelativePath(absolutePath.getAbsolutePath(), relativeTo.getAbsolutePath());
    }

    public static String toRelativePath(String absolutePath, String relativeTo) {
        StringBuilder relativePath = null;
        // Thanks to:
        // http://mrpmorris.blogspot.com/2007/05/convert-absolute-path-to-relative-path.html
        absolutePath = absolutePath.replaceAll("\\\\", "/");
        relativeTo = relativeTo.replaceAll("\\\\", "/");

        if (absolutePath.equals(relativeTo) == true) {

        } else {
            String[] absoluteDirectories = absolutePath.split("/");
            String[] relativeDirectories = relativeTo.split("/");

            //Get the shortest of the two paths
            int length = absoluteDirectories.length < relativeDirectories.length ?
                    absoluteDirectories.length : relativeDirectories.length;

            //Use to determine where in the loop we exited
            int lastCommonRoot = -1;
            int index;

            //Find common root
            for (index = 0; index < length; index++) {
                if (absoluteDirectories[index].equals(relativeDirectories[index])) {
                    lastCommonRoot = index;
                } else {
                    break;
                    //If we didn't find a common prefix then throw
                }
            }
            if (lastCommonRoot != -1) {
                //Build up the relative path
                relativePath = new StringBuilder();
                //Add on the ..
                for (index = lastCommonRoot + 1; index < absoluteDirectories.length; index++) {
                    if (absoluteDirectories[index].length() > 0) {
                        relativePath.append("../");
                    }
                }
                for (index = lastCommonRoot + 1; index < relativeDirectories.length - 1; index++) {
                    relativePath.append(relativeDirectories[index] + "/");
                }
                relativePath.append(relativeDirectories[relativeDirectories.length - 1]);
            }
        }
        return relativePath == null ? null : relativePath.toString();
    }

    public static File getSourceDir() {
        try {
            return getSourceFile().getParentFile();
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static File getSourceFile() {
        try {
            return new File(Folders.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static String getCurrentDirectory() {
        return System.getProperty("user.dir");
    }

    public static boolean setCurrentDirectory(String directory_name) {
        boolean result = false;  // Boolean indicating whether directory was set
        File    directory;       // Desired current working directory
        directory = new File(directory_name).getAbsoluteFile();
        if (directory.exists() || directory.mkdirs()) {
            result = (System.setProperty("user.dir", directory.getAbsolutePath()) != null);
        }
        return result;
    }

    private static void deleteTmpFilesOnExit() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (TMP_FILES) {
                    for (File f : TMP_FILES) {
                        delete(f);
                    }
                }
            }
        }));
    }

}
