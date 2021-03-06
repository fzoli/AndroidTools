package slicetool.config.persister;

import slicetool.ConstFiles;
import slicetool.config.DirectoryMapping;
import slicetool.util.Folders;

import java.io.File;

public final class DirectoryMappingPersister extends AbstractJsonConfigPersister<DirectoryMapping> {

    private static final DirectoryMappingPersister INSTANCE = new DirectoryMappingPersister();

    public static DirectoryMappingPersister getInstance() {
        return INSTANCE;
    }

    private DirectoryMappingPersister() {
        super(DirectoryMapping.class);
    }

    @Override
    protected File getConfigFile() {
        return ConstFiles.getDirectoryMappingConfigFile();
    }

    @Override
    protected DirectoryMapping getDefaultConfig() {
        DirectoryMapping def = new DirectoryMapping();
        addDefaultEntry(def, "drawable-ldpi");
        addDefaultEntry(def, "drawable-mdpi");
        addDefaultEntry(def, "drawable-hdpi");
        addDefaultEntry(def, "drawable-xhdpi");
        addDefaultEntry(def, "drawable-xxhdpi");
        return def;
    }

    private static void addDefaultEntry(DirectoryMapping def, String dirName) {
        File fileFrom = new File(ConstFiles.getDefaultImagesDirectory(), dirName);
        String pathFrom = Folders.toRelativePath(ConstFiles.getSourceDirectory(), fileFrom);
        File fileTo = new File(ConstFiles.getSourceDirectory().getParentFile(), Folders.joinPath("src", "main", "res", dirName));
        String pathTo = Folders.toRelativePath(ConstFiles.getSourceDirectory(), fileTo);
        //if (fileTo.isDirectory()) {
            def.put(pathFrom, pathTo);
        //}
    }

}
