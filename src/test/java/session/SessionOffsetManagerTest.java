package session;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SessionOffsetManagerTest {
    private ConcurrentHashMap<String, SessionOffset> sessionOffsetMap;

    @BeforeEach
    public void setup() {
        SessionOffsetManager.getSessionOffsetMap().clear();
        sessionOffsetMap = new ConcurrentHashMap<>();
        sessionOffsetMap.put("a", new SessionOffset(5));
        sessionOffsetMap.put("b", new SessionOffset(10));
        SessionOffsetManager.setSessionOffsetMap(sessionOffsetMap);
    }

    @Test
    public void test_getSessionOffset_with_valid_sessionId() {
        String sessionId = "a";
        int result = SessionOffsetManager.getSessionOffset(sessionId);
        assertEquals(5,result);
    }

    @Test
    public void test_getSessionOffset_with_invalid_sessionId() {
        String sessionId = "c";
        int result = SessionOffsetManager.getSessionOffset(sessionId);
        assertEquals(0, result);
    }

    @Test
    public void test_setSessionOffset_with_new_valid_value() {
        String sessionId = "a";
        int newValue = 100;
        SessionOffsetManager.setSessionOffset(sessionId, newValue);
        int result = SessionOffsetManager.getSessionOffset(sessionId);
        assertEquals(newValue,result);
    }

    @Test
    public void test_setSessionOffset_with_new_invalid_value() {
        String sessionId = "a";
        int newValue = -100;
        SessionOffsetManager.setSessionOffset(sessionId, newValue);
        int result = SessionOffsetManager.getSessionOffset(sessionId);
        assertEquals(0,result);
    }
}