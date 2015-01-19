package com.cellum.filerenamer;

import java.io.*;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;

interface FileIterator {
    public void iterate(File f) throws Exception;
}

class FileRenamer implements FileIterator {

    private final Map<String, String> fileNames;

    FileRenamer(Map<String, String> fileNames) {
        this.fileNames = fileNames;
    }

    public void iterate(File f) throws Exception {
        String newName = fileNames.get(f.getName());
        if (newName != null) {
            rename(f, newName);
        }
    }

    private void rename(File from, String newName) throws Exception {
        if (newName.contains("/")) {
            throw new InvalidPropertiesFormatException("'" + newName + "' contains '/' character");
        }
        File to = new File(from.getParent(), newName);
        rename(from, to);
    }

    private void rename(File from, File to) throws IOException {
        if (to.exists()) throw new IOException("file '" + to + "'exists");
        if (!from.renameTo(to)) {
            System.err.println("'" + from + "' not changed!");
        }
    }

}

public class Main {

    private static final File CD = new File(System.getProperty("user.dir"));
    private static final String CFG_FILENAME = "filenames.txt";

    private static Map<String, String> loadFilenames(boolean toLowerCase) throws IOException {
        Map<String, String> names = new HashMap<String, String>();
        BufferedReader in = new BufferedReader(new FileReader(new File(CD, CFG_FILENAME)));
        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            String[] cols = line.split("\t");
            if (cols.length >= 2) {
                String oldName = cols[0];
                String newName = cols[1];
                names.put(oldName, newName);
            }
            else if (!line.isEmpty()) {
                System.err.println("Invalid line: " + line);
            }
        }
        in.close();
        return names;
    }

    private static void iterateDir(File dir, FileIterator it) throws Exception {
        if (dir.isDirectory()) {
            for (File f : dir.listFiles()) {
                it.iterate(f);
                if (f.isDirectory()) {
                    iterateDir(f, it);
                }
            }
        }
        else {
            it.iterate(dir);
        }
    }

    public static void main(String[] args) {
        try {
            Map<String, String> fileNames = loadFilenames(true);
            try {
                iterateDir(CD, new FileRenamer(fileNames));
            }
            catch (Exception ex) {
                System.err.println("Rename error: " + ex.getMessage());
            }
        }
        catch (IOException ex) {
            System.err.println("Config file read error: " + ex.getMessage());
        }
    }

}
