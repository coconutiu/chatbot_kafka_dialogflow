package com.etiqa.websocket.chat.model;

import org.apache.commons.lang3.builder.ToStringBuilder;


public class ChatMessage {
	private Long messageId;
	private MessageType type;
	private String content;
	private String sender;
	private String session;
	private String email;
	private String magnitude;
	private String sentiment;

	public ChatMessage() {

	}

	public enum MessageType {
		CHAT, JOIN, RESPONSE, LEAVE
	}

	public ChatMessage(Long messageId, MessageType type, String content, String sender, String session, String email, String magnitude, String sentiment) {
		this.messageId = messageId;
		this.type = type;
		this.content = content;
		this.sender = sender;
		this.session = session;
		this.email = email;
		this.magnitude = magnitude;
		this.sentiment = sentiment;
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

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMagnitude() {
		return magnitude;
	}

	public void setMagnitude(String magnitude) {
		this.magnitude = magnitude;
	}

	public String getSentiment() {
		return sentiment;
	}

	public void setSentiment(String sentiment) {
		this.sentiment = sentiment;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("messageId", messageId)
				.append("type", type)
				.append("content", content)
				.append("sender", sender)
				.append("sessionId", session)
				.append("email", email)
				.append("magnitude", magnitude)
				.append("sentiment", sentiment)
				.toString();
	}
}
