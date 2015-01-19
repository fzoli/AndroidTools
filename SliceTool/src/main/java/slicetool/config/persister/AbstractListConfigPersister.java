package slicetool.config.persister;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by zoli on 2015.01.19..
 */
abstract class AbstractListConfigPersister<T extends Serializable> extends AbstractConfigPersister<ArrayList<T>> {

    protected AbstractListConfigPersister() {
    }

    protected abstract T parseString(String line);

    protected abstract String toString(T obj);

    @Override
    public ArrayList<T> readConfigImpl() throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(getConfigFile()));
        ArrayList<T> config = new ArrayList<T>();
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (!line.isEmpty() && !line.startsWith("#")) {
                T obj = parseString(line);
                if (obj != null) {
                    config.add(obj);
                }
            }
        }
        reader.close();
        return config;
    }

    @Override
    public void saveConfigImpl(ArrayList<T> cfg) throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(getConfigFile()));
        for (T obj : cfg) {
            if (obj != null) {
                String line = toString(obj);
                if (line != null) {
                    line = line.trim();
                    if (!line.isEmpty()) {
                        writer.write(line);
                        writer.newLine();
                    }
                }
            }
        }
        writer.flush();
        writer.close();
    }

}
