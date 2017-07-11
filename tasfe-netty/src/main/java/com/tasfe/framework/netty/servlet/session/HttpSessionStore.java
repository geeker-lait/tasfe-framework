package com.tasfe.framework.netty.servlet.session;

import javax.servlet.http.HttpSession;

public interface HttpSessionStore {

    String generateNewSessionId();

    HttpSession findSession(String sessionId);

    HttpSession createSession();

    void destroySession(String sessionId);

    void destroyInactiveSessions();

}
