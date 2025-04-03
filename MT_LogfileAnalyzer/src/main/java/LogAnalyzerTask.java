import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class LogAnalyzerTask implements Callable<Map<String, Integer>> {
    private final Path file;

    public LogAnalyzerTask(Path file) {
        this.file = file;
    }

    @Override
    public Map<String, Integer> call() {
        Map<String, Integer> counts = new HashMap<>();
        for (String level : new String[]{"TRACE", "DEBUG", "INFO", "WARN", "ERROR"}) {
            counts.put(level, 0);
        }

        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line;
            while ((line = reader.readLine()) != null) {
                for (String level : counts.keySet()) {
                    if (line.contains(level)) {
                        counts.put(level, counts.get(level) + 1);
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Fehler bei Datei: " + file);
        }

        return counts;
    }
}