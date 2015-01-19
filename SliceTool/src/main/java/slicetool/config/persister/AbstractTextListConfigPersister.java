package slicetool.config.persister;

/**
 * Created by zoli on 2015.01.19..
 */
abstract class AbstractTextListConfigPersister extends AbstractListConfigPersister<String> {

    @Override
    protected String parseString(String line) {
        return line;
    }

    @Override
    protected String toString(String obj) {
        return obj;
    }

}
