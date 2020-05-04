package session;

import com.mysql.cj.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class SessionOffsetCleaner extends TimerTask {

    @Override
    public void run() {
        ConcurrentHashMap<String,SessionOffset> sessionOffsetMap = SessionOffsetManager.getSessionOffsetMap();
        List<String> sessionIdsToBeRemoved = new ArrayList<>();
        for(Map.Entry<String,SessionOffset> entry: sessionOffsetMap.entrySet()) {
            String key = entry.getKey();
            SessionOffset offset = entry.getValue();
            if(offset.isStale())
                sessionIdsToBeRemoved.add(key);
        }
        for(String sessionSearch: sessionIdsToBeRemoved) {
            sessionOffsetMap.remove(sessionSearch);
        }
    }
}
