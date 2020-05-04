package session;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SessionOffsetManagerTest {
    private ConcurrentHashMap<String, SessionOffset> sessionOffsetMap;
    private static final String sessionId = "a";
    private static final String search = "AA";
    @BeforeEach
    public void setup() {
        SessionOffsetManager.getSessionOffsetMap().clear();
        sessionOffsetMap = new ConcurrentHashMap<>();
        sessionOffsetMap.put(sessionId+search, new SessionOffset(5));
        SessionOffsetManager.setSessionOffsetMap(sessionOffsetMap);
    }

    @Test
    public void test_getSessionOffset_with_valid_sessionId() {
        String sessionId = "a";
        String search = "AA";
        int result = SessionOffsetManager.getSessionOffset(sessionId,search);
        assertEquals(5,result);
    }

    @Test
    public void test_getSessionOffset_with_invalid_sessionId() {
        String sessionId = "c";
        String search = "AA";
        int result = SessionOffsetManager.getSessionOffset(sessionId,search);
        assertEquals(0, result);
    }

    @Test
    public void test_setSessionOffset_with_new_valid_value() {
        String sessionId = "a";
        String search = "AA";
        int newValue = 100;
        SessionOffsetManager.setSessionOffset(sessionId,search, newValue);
        int result = SessionOffsetManager.getSessionOffset(sessionId,search);
        assertEquals(newValue,result);
    }

    @Test
    public void test_setSessionOffset_with_new_invalid_value() {
        String sessionId = "a";
        String search = "AA";
        int newValue = -100;
        SessionOffsetManager.setSessionOffset(sessionId,search, newValue);
        int result = SessionOffsetManager.getSessionOffset(sessionId,search);
        assertEquals(0,result);
    }
}