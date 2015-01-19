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
                    mArgFileList.clear();
                    mArgFileList.add("*");
                    break;
                }
                if (!arg.startsWith("-")) {
                    mArgFileList.add(arg);
                }
            }
        }
    }

    private void readConfigs() throws ConfigReadException{
        mDirectoryMapping = DirectoryMappingPersister.getInstance().readConfig();
        mFileFilterList = FileFilterListPersister.getInstance().args(mArgFileList).readConfig();
    }

    private List<FileMeta> getFileMetaList() {
        FileFilter filter = new WildcardFileFilter(mFileFilterList);
        return mDirectoryMapping.listFiles(filter);
    }

    @Override
    public void run() {
        try {
            readConfigs();
            List<FileMeta> metas = getFileMetaList();
            for (FileMeta meta : metas) {
                System.out.println(meta);
            }
        }
        catch (ConfigReadException ex) {
            throw new RuntimeException(ex);
        }
    }

}
