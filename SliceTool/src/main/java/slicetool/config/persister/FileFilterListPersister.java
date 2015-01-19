package slicetool.config.persister;

import slicetool.ConstFiles;
import slicetool.config.FileFilterList;
import slicetool.config.persister.exception.ConfigReadException;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by zoli on 2015.01.19..
 */
public final class FileFilterListPersister extends AbstractTextListConfigPersister {

    private static final FileFilterListPersister INSTANCE = new FileFilterListPersister();

    public static FileFilterListPersister getInstance() {
        return INSTANCE;
    }

    private FileFilterListPersister() {
    }

    private ArrayList<String> mArgs;

    public FileFilterListPersister args(ArrayList<String> args) {
        mArgs = args;
        return this;
    }

    @Override
    protected File getConfigFile() {
        return ConstFiles.getFileFilterListConfigFile();
    }

    @Override
    protected ArrayList<String> getDefaultConfig() {
        ArrayList<String> ls = new ArrayList<String>();
        ls.add("*.png");
        return ls;
    }

    @Override
    public FileFilterList readConfig() throws ConfigReadException {
        ArrayList<String> cfg;
        if (mArgs != null && !mArgs.isEmpty()) {
            cfg = mArgs;
        }
        else {
            cfg = super.readConfig();
        }
        return new FileFilterList(cfg);
    }

}
