package com.test.chat.ws.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.test.chat.ws.JsonUtil;
import com.test.chat.ws.objects.Msg;
import com.test.chat.ws.objects.Sub;
import com.test.chat.ws.objects.Unsub;

@ServerEndpoint("/chat")
public class WebSocketServer {

    private static final Logger LOG = LoggerFactory.getLogger(WebSocketServer.class);

    private static Set<Session> sessions = new HashSet<>();

    private static Map<String, String> nameMap = new HashMap<>();

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
            String message) throws IOException {
        LOG.debug("message {} recieved form session {}", message, session.getId());

        if (message.contains("subscribe")) {
            Sub subscribe = JsonUtil.fromJson(message, Sub.class);
            if (subscribe.getSubscribe().equalsIgnoreCase("chat")) {
                nameMap.put(session.getId(), subscribe.getName());
            } else {
                session.getAsyncRemote().sendText(JsonUtil.toJson(new Msg(subscribe.getName(), "cannot subscribe")));
            }
        } else if (message.contains("unsubscribe")) {
            Unsub unsubscribe = JsonUtil.fromJson(message, Unsub.class);
            if (unsubscribe.getUnsubscribe().equalsIgnoreCase("chat")) {
                nameMap.remove(session.getId());
            }
        } else {
            Msg msg = JsonUtil.fromJson(message, Msg.class);
            if (msg.getUser() == null || msg.getUser().isBlank()) {
                msg.setUser(nameMap.get(session.getId()));
            }
            if (sessions.contains(session)) {
                sessions.stream().filter(s -> !s.getId().equals(session.getId()))
                        .forEach(s -> s.getAsyncRemote().sendText(JsonUtil.toJson(msg)));
            }
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
