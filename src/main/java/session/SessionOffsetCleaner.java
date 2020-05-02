package session;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class SessionOffsetCleaner extends TimerTask {

    @Override
    public void run() {
        ConcurrentHashMap<String,SessionOffset> sessionOffsetMap = SessionOffsetManager.getSessionOffsetMap();
        List<String> sessionIdsToBeRemoved = new ArrayList<>();
        for(String sessionId: sessionOffsetMap.keySet()) {
            if(sessionOffsetMap.get(sessionId).isStale()) {
                sessionIdsToBeRemoved.add(sessionId);
            }
        }
        for(String sessionSearch: sessionIdsToBeRemoved) {
            sessionOffsetMap.remove(sessionSearch);
        }
    }
}
