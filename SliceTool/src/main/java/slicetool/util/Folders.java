package slicetool.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Folders {

    private static final List<File> TMP_FILES = new ArrayList<File>();

    static {
        deleteTmpFilesOnExit();
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
