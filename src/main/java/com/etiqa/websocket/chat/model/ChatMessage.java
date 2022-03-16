package com.etiqa.websocket.chat.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.sql.DataSourceDefinition;


public class ChatMessage {
	private Long messageId;
	private MessageType type;
	private String content;
	private String sender;
	private String sessionId;

	public ChatMessage() {

	}

	public enum MessageType {
		CHAT, JOIN, RESPONSE, LEAVE
	}

	public ChatMessage(Long messageId, MessageType type, String content, String sender,String sessionId) {
		this.messageId = messageId;
		this.type = type;
		this.content = content;
		this.sender = sender;
		this.sessionId = sessionId;
	}

	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("messageId", messageId)
				.append("type", type)
				.append("content", content)
				.append("sender", sender)
				.append("sessionId", sessionId)
				.toString();
	}
}
