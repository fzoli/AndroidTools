package slicetool;

import slicetool.ui.ErrorDialog;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.logging.Logger;

/**
 * Created by zoli on 2015.01.19..
 */
public final class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static final Logger LOGGER = Logger.getLogger("SliceToolExceptionHandler");

    private static boolean dialogVisible = false;

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        LOGGER.severe(getStackTrace(throwable));
        if (!dialogVisible) {
            dialogVisible = true;
            new ErrorDialog(new Exception("Unexpected error :-(", throwable)).setVisible(true);
            System.exit(1);
            dialogVisible = false;
        }
    }

    private String getStackTrace(Throwable throwable) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        throwable.printStackTrace(printWriter);
        return result.toString();
    }

}
