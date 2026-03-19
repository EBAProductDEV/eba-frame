package com.qctv1.tool;

import lombok.*;

import java.util.Map;

/**
 * @Author: 粪豆儿
 * @CreateTime: 2026-01-07 23:09:15
 * @Desc:
 */

public class StreamR<T> {

    /** 流式事件类型：delta / meta / end / error */
    private String type;

    /** 内容语义类型：text / markdown / image / tool / meta */
    private String contentType;

    /** 实际载荷（统一 String） */
    private T data;

    /** 当前流状态: streaming / success / error */
    private String status;

    /** 拼接策略: append / merge / replace */
    private String strategy;

    /** 消息ID */
    private String messageId;

    /** 扩展字段 */
    private Map<String, Object> ext;

    public StreamR(
            String type,
            String contentType,
            T data,
            String status,
            String strategy,
            String messageId,
            Map<String, Object> ext
    ) {
        this.type = type;
        this.contentType = contentType;
        this.data = data;
        this.status = status;
        this.strategy = strategy;
        this.messageId = messageId;
        this.ext = ext;
    }

    /* ================= 工厂方法 ================= */

    public static StreamR<String> deltaText(String message, String messageId) {
        return new StreamR<>(
                "delta",
                "text",
                message,
                "streaming",
                "append",
                messageId,
                null
        );
    }

    public static StreamR<String> deltaMarkdown(String message, String messageId) {
        return new StreamR<>(
                "delta",
                "markdown",
                message,
                "streaming",
                "append",
                messageId,
                null
        );
    }

    /** image 数据用 JSON String */
    public static StreamR<String> image(String imageJson, String messageId) {
        return new StreamR<>(
                "delta",
                "image",
                imageJson,
                "streaming",
                "replace",
                messageId,
                null
        );
    }

    public static StreamR<String> meta(String metaJson, String messageId) {
        return new StreamR<>(
                "meta",
                "meta",
                metaJson,
                "streaming",
                "merge",
                messageId,
                null
        );
    }

    public static StreamR<String> end(String messageId) {
        return new StreamR<>(
                "end",
                "meta",
                "",
                "success",
                null,
                messageId,
                null
        );
    }

    public static StreamR<String> error(String errorMsg, String messageId) {
        return new StreamR<>(
                "error",
                "meta",
                errorMsg,
                "error",
                null,
                messageId,
                null
        );
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Map<String, Object> getExt() {
        return ext;
    }

    public void setExt(Map<String, Object> ext) {
        this.ext = ext;
    }
}
