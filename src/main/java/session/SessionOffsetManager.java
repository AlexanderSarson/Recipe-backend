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

    public static Integer getSessionOffset(String sessionId) {
        SessionOffset offset = sessionOffsetMap.getOrDefault(sessionId,null);
        if(offset == null) {
            return 0;
        } else {
            return offset.getOffset();
        }
    }

    public static void setSessionOffset(String sessionId, Integer newValue) {
        newValue = Math.max(0, newValue);
        SessionOffset offset;
        if(sessionOffsetMap.containsKey(sessionId)) {
             offset = sessionOffsetMap.get(sessionId);
            offset.setOffset(newValue);
        } else {
            offset = new SessionOffset(newValue);
        }
        sessionOffsetMap.put(sessionId, offset);
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
