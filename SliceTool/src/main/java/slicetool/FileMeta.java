/*
 * FileMeta.java
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

import java.io.File;

/**
 * Created by Zoltán Farkas on 2015.01.16.
 */
public class FileMeta {

    private File mOriginFrom, mMoveFrom, mMoveTo;

    public FileMeta(File moveFrom, File moveTo) {
        mOriginFrom = moveFrom;
        mMoveFrom = moveFrom;
        mMoveTo = moveTo;
    }

    public File getOriginFrom() {
        return mOriginFrom;
    }

    public File getMoveFrom() {
        return mMoveFrom;
    }

    public File getMoveTo() {
        return mMoveTo;
    }

    public void setMoveFrom(File moveFrom) {
        mMoveFrom = moveFrom;
    }

    public void setMoveTo(File moveTo) {
        mMoveTo = moveTo;
    }

    @Override
    public String toString() {
        return String.format("FileMeta(originFrom='%s', moveFrom='%s', moveTo='%s')", mOriginFrom, mMoveFrom, mMoveTo);
    }

}
