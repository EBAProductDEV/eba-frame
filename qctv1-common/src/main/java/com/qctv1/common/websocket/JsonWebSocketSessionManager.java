package com.qctv1.common.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 通用 WebSocket 会话管理器。
 *
 * 这个类只处理连接集合、JSON 序列化、单播、广播和快照去重，不包含任何业务语义。
 * 业务模块只需要传入自己的消息类型和 payload，例如任务中心、站内通知、聊天状态等。
 */
public class JsonWebSocketSessionManager<T> {

    private static final Logger log = LoggerFactory.getLogger(JsonWebSocketSessionManager.class);

    private final ObjectMapper objectMapper;
    private final String moduleName;
    private final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();
    private volatile String lastSnapshotMessage = "";

    public JsonWebSocketSessionManager(ObjectMapper objectMapper, String moduleName) {
        this.objectMapper = objectMapper;
        this.moduleName = moduleName == null || moduleName.isBlank() ? "WebSocket" : moduleName;
    }

    public void add(WebSocketSession session) {
        sessions.add(session);
        log.info("{} 已连接，sessionId={}，当前连接数={}", moduleName, session.getId(), sessions.size());
    }

    public void remove(WebSocketSession session) {
        sessions.remove(session);
        log.info("{} 已断开，sessionId={}，当前连接数={}", moduleName, session.getId(), sessions.size());
    }

    public boolean hasSessions() {
        return sessions.stream().anyMatch(WebSocketSession::isOpen);
    }

    public void send(WebSocketSession session, String type, T payload) {
        sendRaw(session, buildMessage(type, payload));
    }

    public void broadcast(String type, T payload) {
        broadcastRaw(buildMessage(type, payload));
    }

    public void broadcastIfChanged(String type, T payload) {
        String message = buildMessage(type, payload);
        if (message.equals(lastSnapshotMessage)) {
            return;
        }
        lastSnapshotMessage = message;
        broadcastRaw(message);
    }

    private void broadcastRaw(String message) {
        for (WebSocketSession session : sessions) {
            sendRaw(session, message);
        }
    }

    private void sendRaw(WebSocketSession session, String message) {
        if (!session.isOpen()) {
            remove(session);
            return;
        }
        try {
            session.sendMessage(new TextMessage(message));
        } catch (IOException ex) {
            log.warn("{} 推送失败，sessionId={}", moduleName, session.getId(), ex);
            remove(session);
        }
    }

    private String buildMessage(String type, T payload) {
        try {
            return objectMapper.writeValueAsString(new WebSocketPayload<>(type, payload));
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException(moduleName + " 消息序列化失败", ex);
        }
    }
}
