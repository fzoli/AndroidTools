package slicetool;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import slicetool.config.DirectoryMapping;
import slicetool.config.FileFilterList;
import slicetool.config.persister.DirectoryMappingPersister;
import slicetool.config.persister.FileFilterListPersister;
import slicetool.config.persister.exception.ConfigReadException;

import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zoli on 2015.01.19..
 */
public class FileProcessor implements Runnable {

    private DirectoryMapping mDirectoryMapping;
    private FileFilterList mFileFilterList;
    private ArrayList<String> mArgFileList;

    public FileProcessor() {
        this(null);
    }

    public FileProcessor(String[] appArgs) {
        mArgFileList = new ArrayList<String>();
        if (appArgs != null) {
            for (String arg : appArgs) {
                if (arg.equals("-r")) {
                    mArgFileList.add("*");
                }
                if (!arg.startsWith("-")) {
                    mArgFileList.add(arg);
                }
            }
        }
    }

    @Override
    public void run() {
        try {
            mDirectoryMapping = DirectoryMappingPersister.getInstance().readConfig();
            mFileFilterList = FileFilterListPersister.getInstance().args(mArgFileList).readConfig();
            FileFilter filter = new WildcardFileFilter(mFileFilterList);
            List<FileMeta> metas = mDirectoryMapping.listFiles(filter);
            System.out.println(metas);
        }
        catch (ConfigReadException ex) {
            throw new RuntimeException(ex);
        }
    }

}
