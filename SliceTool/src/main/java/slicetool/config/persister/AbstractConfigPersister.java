package slicetool.config.persister;

import slicetool.config.persister.exception.ConfigReadException;
import slicetool.config.persister.exception.ConfigWriteException;
import slicetool.util.Folders;

import java.io.File;
import java.io.Serializable;

/**
 * Created by zoli on 2015.01.19..
 */
abstract class AbstractConfigPersister<T extends Serializable> {

    protected final Object LOCK = new Object();

    protected AbstractConfigPersister() {
        initDefaultConfigFile();
    }

    protected abstract File getConfigFile();

    protected T getDefaultConfig() {
        return null;
    }

    public abstract T readConfigImpl() throws Exception;

    public abstract void saveConfigImpl(T cfg) throws Exception;

    private void initDefaultConfigFile() {
        if (!getConfigFile().exists()) {
            T cfg = getDefaultConfig();
            if (cfg != null) {
                safeSaveConfig(cfg);
            }
        }
    }

    public T readConfig() throws ConfigReadException {
        synchronized (LOCK) {
            try {
                return readConfigImpl();
            }
            catch (Exception ex) {
                throw new ConfigReadException(ex);
            }
        }
    }

    public void saveConfig(T cfg) throws ConfigWriteException {
        synchronized (LOCK) {
            try {
                if (cfg == null) {
                    Folders.delete(getConfigFile());
                    return;
                }
                saveConfigImpl(cfg);
            }
            catch (Exception ex) {
                throw new ConfigWriteException(ex);
            }
        }
    }

    public boolean safeSaveConfig(T cfg) {
        try {
            saveConfig(cfg);
            return true;
        }
        catch (ConfigWriteException ex) {
            return false;
        }
    }

}
