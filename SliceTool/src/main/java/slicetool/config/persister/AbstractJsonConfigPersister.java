/*
 * AbstractJsonConfigPersister.java
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

import java.io.*;

/**
 * Created by Zoltán Farkas on 2015.01.16.
 */
abstract class AbstractJsonConfigPersister<T extends Serializable> extends AbstractConfigPersister<T> {

    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Class<T> mClass;

    protected AbstractJsonConfigPersister(Class<T> clazz) {
        mClass = clazz;
    }

    public T readConfigImpl() throws Exception {
        Reader reader = new FileReader(getConfigFile());
        T config = GSON.fromJson(reader, mClass);
        reader.close();
        return config;
    }

    public void saveConfigImpl(T cfg) throws Exception {
        Writer writer = new FileWriter(getConfigFile());
        GSON.toJson(cfg, writer);
        writer.flush();
        writer.close();
    }

}
