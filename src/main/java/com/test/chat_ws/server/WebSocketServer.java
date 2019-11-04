package com.test.chat_ws.server;

import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ServerEndpoint("/chat")
public class WebSocketServer {

    private static final Logger LOG = LoggerFactory.getLogger(WebSocketServer.class);

    private static Set<Session> sessions = new HashSet<>();

    @OnOpen
    public void onOpen(
            Session session) {
        boolean add = sessions.add(session);
        if (add) {
            LOG.debug("session {} opened", session.getId());
            session.getAsyncRemote().sendText("Session connected");
        }
    }

    @OnMessage
    public void onMessage(
            Session session,
            String message) {
        LOG.debug("message {} recieved form session {}", message, session.getId());
        if (sessions.contains(session)) {
            sessions.stream().filter(s -> !s.getId().equals(session.getId()))
                    .forEach(s -> s.getAsyncRemote().sendText(message));
        }
    }

    @OnClose
    public void onClose(
            Session session) {
        LOG.debug("session {} closed", session.getId());
        sessions.remove(session);
    }

    @OnError
    public void onError(
            Session session,
            Throwable e) {
        LOG.error("error on session {} - {}", session.getId(), e.getMessage());
        sessions.remove(session);
    }

}
