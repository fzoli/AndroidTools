package slicetool;

import slicetool.util.Folders;
import java.io.File;

public class Const {

    private static final File SOURCE_DIR = Folders.getSourceDir();
    private static final File TEMP_DIR = new File(SOURCE_DIR, "tmp");
    private static final File CFG_DIR = new File(SOURCE_DIR, "config");

    private static final File DEF_IMAGES_DIR = new File(SOURCE_DIR, "images");

    private static final File DIRECTORY_MAPPING_CFG = new File(CFG_DIR, "directory_mapping.txt");

    static {
        Folders.createTempDir(TEMP_DIR);
        Folders.createDir(CFG_DIR);
    }

    public static File getSourceDirectory() {
        return SOURCE_DIR;
    }

    public static File getDefaultImagesDirectory() {
        return DEF_IMAGES_DIR;
    }

    public static File getDirectoryMappingConfigFile() {
        return DIRECTORY_MAPPING_CFG;
    }

}
