import java.nio.file.Path;
import java.util.concurrent.Callable;
import java.util.*;

public class ErrorLogAnalyzerTask implements Callable<Map<String, Object>> {
    private final Path file;

    public ErrorLogAnalyzerTask(Path file) {
        this.file = file;
    }

    @Override
    public Map<String, Object> call() throws Exception {
        // TODO: Map mit LogLevel-Zählung + Liste von WARN/ERROR-Zeilen zurückgeben
        return null;
    }
}