import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

public class ParallelLogAnalyzer {

    public static void main(String[] args) {
        Path folder = Paths.get(".");
        String prefix = "logs-";

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<Map<String, Integer>>> futures = new ArrayList<>();
        Map<String, Integer> totalCounts = initEmptyLogMap();

        long start = System.currentTimeMillis();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder, prefix + "*.log")) {
            for (Path file : stream) {
                LogAnalyzerTask task = new LogAnalyzerTask(file);
                futures.add(executor.submit(task));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int fileIndex = 1;
        for (Future<Map<String, Integer>> future : futures) {
            try {
                Map<String, Integer> result = future.get();
                System.out.println("Datei #" + fileIndex++);
                for (String level : result.keySet()) {
                    System.out.println("  " + level + ": " + result.get(level));
                    totalCounts.put(level, totalCounts.get(level) + result.get(level));
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        long end = System.currentTimeMillis();
        executor.shutdown();

        System.out.println("\nGesamtauswertung:");
        for (String level : totalCounts.keySet()) {
            System.out.println(level + ": " + totalCounts.get(level));
        }

        System.out.println("Laufzeit (parallel): " + (end - start) + "ms");
    }

    private static Map<String, Integer> initEmptyLogMap() {
        Map<String, Integer> map = new HashMap<>();
        for (String level : new String[]{"TRACE", "DEBUG", "INFO", "WARN", "ERROR"}) {
            map.put(level, 0);
        }
        return map;
    }
}


