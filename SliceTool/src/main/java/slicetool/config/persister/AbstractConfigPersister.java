/*
 * AbstractConfigPersister.java
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
package slicetool.config.persister;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import slicetool.config.persister.exception.ConfigReadException;
import slicetool.config.persister.exception.ConfigWriteException;
import slicetool.util.Folders;

import java.io.*;

/**
 * Created by Zoltán Farkas on 2015.01.16.
 */
abstract class AbstractConfigPersister<T extends Serializable> {

    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Class<T> mClass;

    protected abstract File getConfigFile();

    protected T getDefaultConfig() {
        return null;
    }

    protected AbstractConfigPersister(Class<T> clazz) {
        mClass = clazz;
        initDefaultConfigFile();
    }

    private void initDefaultConfigFile() {
        if (!getConfigFile().exists()) {
            T cfg = getDefaultConfig();
            if (cfg != null) {
                safeSaveConfig(cfg);
            }
        }
    }

    public T readConfig() throws ConfigReadException {
        synchronized (GSON) {
            try {
                Reader reader = new FileReader(getConfigFile());
                T config = GSON.fromJson(reader, mClass);
                reader.close();
                return config;
            } catch (Exception ex) {
                throw new ConfigReadException(ex);
            }
        }
    }

    public void saveConfig(T cfg) throws ConfigWriteException {
        synchronized (GSON) {
            try {
                if (cfg == null) {
                    Folders.delete(getConfigFile());
                    return;
                }
                Writer writer = new FileWriter(getConfigFile());
                GSON.toJson(cfg, writer);
                writer.flush();
                writer.close();
            } catch (Exception ex) {
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
