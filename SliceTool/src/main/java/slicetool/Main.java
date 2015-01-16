/*
 * Main.java
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

import org.apache.commons.io.filefilter.WildcardFileFilter;
import slicetool.config.persister.DirectoryMappingPersister;

import java.io.FileFilter;

/**
 * Created by Zoltán Farkas on 2015.01.16.
 */
public class Main {

    public static void main(String[] args) {
        try {
            FileFilter filter = new WildcardFileFilter(new String[]{"*.png"});
            System.out.println(DirectoryMappingPersister.getInstance().readConfig().listFiles(filter));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
