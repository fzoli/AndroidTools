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

import org.apache.commons.io.FileUtils;
import slicetool.util.Folders;

import java.io.File;
import java.io.IOException;

/**
 * Created by Zoltán Farkas on 2015.01.16.
 */
public class FileMeta {

    private final String mOriginFromRelPath, mOriginToRelPath;
    private final File mSrcDir, mDstDir, mOriginFrom;

    private File mTempCopy, mMoveFrom, mMoveTo;

    public FileMeta(File srcDir, File dstDir, File moveFrom, File moveTo) {
        mSrcDir = srcDir;
        mDstDir = dstDir;

        mOriginFromRelPath = Folders.toRelativePath(srcDir, moveFrom);
        mOriginFrom = moveFrom;
        mMoveFrom = moveFrom;

        mOriginToRelPath = Folders.toRelativePath(dstDir, moveTo);
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

    public File getTempCopy() throws IOException {
        if (mTempCopy == null && mOriginFrom != null && mOriginFrom.isFile()) {
            String relOrigin = Folders.toRelativePath(mSrcDir.getParentFile(), mOriginFrom);
            mTempCopy = new File(ConstFiles.getTempDirectory(), relOrigin);
            FileUtils.copyFile(mOriginFrom, mTempCopy, true);
        }
        return mTempCopy;
    }

    @Override
    public String toString() {
        return String.format("FileMeta(originFrom='%s', moveFrom='%s', moveTo='%s')", mOriginFrom, mMoveFrom, mMoveTo);
    }

}
