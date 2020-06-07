package writer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TextWriter implements Writer {
    private static final Logger logger;

    static {
        logger = LogManager.getLogger(TextWriter.class);
    }

    @Override
    public boolean write(String fileName, String content, boolean append) {
        boolean result = true;
        try (FileWriter fileWriter = new FileWriter(fileName, append)) {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            fileWriter.write(content);
        } catch (IOException e) {
            logger.error(e);
            result = false;
        }
        return result;
    }
}
