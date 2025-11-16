package com.antonov.is1.websocket;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
@ServerEndpoint("/creatures-updates")
public class CreaturesWebSocket {

    private static Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        System.out.println("WebSocket connected: " + session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        System.out.println("WebSocket disconnected: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        sessions.remove(session);
        System.out.println("WebSocket error: " + throwable.getMessage());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // Обработка входящих сообщений (если нужно)
        System.out.println("Received message: " + message);
    }

    // Метод для рассылки обновлений всем подключенным клиентам
    public static void broadcast(String message) {
        synchronized (sessions) {
            for (Session session : sessions) {
                if (session.isOpen()) {
                    try {
                        session.getBasicRemote().sendText(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    // Методы для разных типов событий
    public static void notifyCreatureCreated(Long creatureId) {
        broadcast("CREATURE_CREATED:" + creatureId);
    }

    public static void notifyCreatureUpdated(Long creatureId) {
        broadcast("CREATURE_UPDATED:" + creatureId);
    }

    public static void notifyCreatureDeleted(Long creatureId) {
        broadcast("CREATURE_DELETED:" + creatureId);
    }
}