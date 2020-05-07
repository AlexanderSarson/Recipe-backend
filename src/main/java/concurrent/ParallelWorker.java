package concurrent;

import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ParallelWorker<T extends ParallelTask> {
    private final Logger logger = Logger.getLogger(ParallelWorker.class.getName());
    public List<T> getAsParallel(List<T> elements) {
        ExecutorService worker = Executors.newFixedThreadPool(4);
        elements.forEach(element -> {
            Runnable task = element::doWork;
            worker.submit(task);
        });
        worker.shutdown();
        try {
            worker.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            logger.log(Level.WARNING, e.getMessage());
            Thread.currentThread().interrupt();
        }
        return elements;
    }
}
