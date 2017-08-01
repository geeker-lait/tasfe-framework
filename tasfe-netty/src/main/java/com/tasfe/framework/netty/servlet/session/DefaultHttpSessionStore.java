package com.tasfe.framework.netty.servlet.session;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

import com.tasfe.framework.netty.servlet.impls.HttpSessionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultHttpSessionStore implements HttpSessionStore {

    private static final Logger log = LoggerFactory.getLogger(DefaultHttpSessionStore.class);

    private static ConcurrentHashMap<String, HttpSession> sessions = new ConcurrentHashMap<String, HttpSession>();

    @Override
    public HttpSession createSession() {
        String sessionId = this.generateNewSessionId();
        log.debug("Creating new session with id {}", sessionId);

        HttpSession session = new HttpSessionImpl(sessionId);
        sessions.put(session.getId(), session);
        return session;
    }

    @Override
    public void destroySession(String sessionId) {
        HttpSession session = this.findSession(sessionId);
        if (null != session) {
            session = null;
        }

        log.debug("Destroying session with id {}", sessionId);
        sessions.remove(sessionId);
    }

    @Override
    public HttpSession findSession(String sessionId) {
        if (sessionId == null)
            return null;

        return sessions.get(sessionId);
    }

    public String generateNewSessionId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void destroyInactiveSessions() {
        for (Map.Entry<String, HttpSession> entry : sessions.entrySet()) {
            HttpSession session = entry.getValue();
            if (session.getMaxInactiveInterval() < 0)
                continue;

            long currentMillis = System.currentTimeMillis();

            if (currentMillis - session.getLastAccessedTime() > session.getMaxInactiveInterval() * 1000) {
                session.invalidate();
                destroySession(entry.getKey());
            }
        }
    }

}
