package slicetool;

import slicetool.config.persister.DirectoryMappingPersister;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        try {
            System.out.println(DirectoryMappingPersister.getInstance().readConfig());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
