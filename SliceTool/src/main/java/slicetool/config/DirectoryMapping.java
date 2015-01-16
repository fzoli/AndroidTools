/*
 * DirectoryMapping.java
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
package slicetool.config;

import slicetool.Const;
import slicetool.FileMeta;
import slicetool.util.Folders;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Zoltán Farkas on 2015.01.16.
 */
public final class DirectoryMapping extends HashMap<String, String> implements Serializable {

    public DirectoryMapping() {
    }

    public List<FileMeta> listFiles(FileFilter... filters) {
        List<FileMeta> list = new ArrayList<FileMeta>();
        Iterator<Entry<String, String>> it = entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, String> e = it.next();
            File srcDir = Const.fromSource(e.getKey());
            File dstDir = Const.fromSource(e.getValue());
            Folders.ListFileProcessor srcFiles = Folders.listFiles(srcDir, 1, filters);
            Folders.ListFileProcessor dstFiles = Folders.listFiles(dstDir, 1, filters);

            List<Integer> proceed = new ArrayList<Integer>();
            for (int srcIndex = 0; srcIndex < srcFiles.getRelativePaths().size(); srcIndex++) {
                String srcRel = srcFiles.getRelativePaths().get(srcIndex);
                int dstIndex = dstFiles.getRelativePaths().indexOf(srcRel);
                if (dstIndex != -1) {
                    list.add(new FileMeta(srcFiles.getList().get(srcIndex), dstFiles.getList().get(dstIndex)));
                    proceed.add(dstIndex);
                }
                else {
                    list.add(new FileMeta(srcFiles.getList().get(srcIndex), null));
                }
            }

            for (int dstIndex = 0; dstIndex < dstFiles.getRelativePaths().size(); dstIndex++) {
                if (!proceed.contains(dstIndex)) {
                    list.add(new FileMeta(null, dstFiles.getList().get(dstIndex)));
                }
            }

        }
        return list;
    }

}
