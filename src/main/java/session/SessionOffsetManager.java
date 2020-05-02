package session;

import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

public class SessionOffsetManager {
    private final static long CLEAN_PERIOD = 1800000;
    private final static long CLEAN_DELAY = 0;
    private static ConcurrentHashMap<String, SessionOffset> sessionOffsetMap = new ConcurrentHashMap<>();
    private static final SessionOffsetManager instance = new SessionOffsetManager();

    private SessionOffsetManager() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SessionOffsetCleaner(),CLEAN_DELAY,CLEAN_PERIOD);
    }

    /**
     * Gets the offset from a given sessionId
     * @param sessionId the SessionId, which the offset is corresponding to.
     * @param search the search, which the offset is associated with.
     * @return the Integer Offset
     */
    public static Integer getSessionOffset(String sessionId, String search) {
        String combined = sessionId + search;
        SessionOffset offset = sessionOffsetMap.getOrDefault(combined,null);
        if(offset == null) {
            return 0;
        } else {
            return offset.getOffset();
        }
    }

    /**
     * Sets the value, sessionId is corresponding to - the value is clamped to min 0
     * @param sessionId the sessionId which the offset is associated with.
     * @param search the search, which the offset is associated with.
     * @param newValue the new offset value for the sessionId + search.
     */
    public static void setSessionOffset(String sessionId, String search, Integer newValue) {
        String combined = sessionId + search;
        newValue = Math.max(0, newValue);
        SessionOffset offset;
        if(sessionOffsetMap.containsKey(combined)) {
             offset = sessionOffsetMap.get(combined);
            offset.setOffset(newValue);
        } else {
            offset = new SessionOffset(newValue);
        }
        sessionOffsetMap.put(combined, offset);
    }

    public static ConcurrentHashMap<String,SessionOffset> getSessionOffsetMap() {
        return sessionOffsetMap;
    }

    public static void setSessionOffsetMap(ConcurrentHashMap<String,SessionOffset> newSessionOffsetMap) {
        sessionOffsetMap = newSessionOffsetMap;
    }

    public SessionOffsetManager getInstance() {
        return instance;
    }
}
