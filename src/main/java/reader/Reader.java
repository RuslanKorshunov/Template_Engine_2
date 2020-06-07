package reader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Reader {
    private static final Logger logger;

    static {
        logger = LogManager.getLogger(Reader.class);
    }

    public List<String> read(String fileName) {
        List<String> values = new ArrayList<>();
        File file = new File(fileName);
        if (file.exists() && file.canRead()) {
            try {
                Stream<String> lineStream = Files.newBufferedReader(Paths.get(fileName)).lines();
                values.addAll(lineStream.collect(Collectors.toList()));
            } catch (IOException e) {
                logger.error(e);
            }
        }
        return values;
    }
}
