/*
 * Const.java
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
package slicetool;

import slicetool.util.Folders;
import java.io.File;

/**
 * Created by Zoltán Farkas on 2015.01.16.
 */
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

    public static File fromSource(String path) {
        return new File(getSourceDirectory(), path);
    }

}
