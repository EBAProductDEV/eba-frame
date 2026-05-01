package com.qctv1.common.websocket;

public record WebSocketPayload<T>(
        String type,
        T payload
) {
}
