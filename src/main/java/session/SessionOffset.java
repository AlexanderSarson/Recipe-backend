package session;

import java.time.Duration;
import java.time.LocalDateTime;

public class SessionOffset {
    // Time in Minutes = if lastAccess is more than 30 min old it becomes stale.
    private static final int MAX_TIME = 30;
    private int offset;
    private LocalDateTime lastAccess;

    public SessionOffset(int offset) {
        this.offset = offset;
        lastAccess = LocalDateTime.now();
    }

    public void setOffset(int newValue) {
        offset = newValue;
        lastAccess = LocalDateTime.now();
    }

    public int getOffset() {
        lastAccess = LocalDateTime.now();
        return offset;
    }

    public boolean isStale() {
        Duration duration = Duration.between(lastAccess, LocalDateTime.now());
        return duration.toMinutes() >= MAX_TIME;
    }

    public void setLastAccess(LocalDateTime lastAccess) {
        this.lastAccess = lastAccess;
    }
}
