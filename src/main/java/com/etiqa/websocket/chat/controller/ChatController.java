package com.etiqa.websocket.chat.controller;

import java.util.concurrent.atomic.AtomicLong;

import com.etiqa.websocket.chat.config.ChatBotConfiguration;
import com.etiqa.websocket.chat.service.ProducerService;
import com.google.cloud.dialogflow.v2.*;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.RestController;

import com.etiqa.websocket.chat.model.ChatMessage;

@RestController
public class ChatController {

private static final Logger logger = LogManager.getLogger();
	@Value("${kafka.topic.chat-topic1}")
	String chatTopic;
//	@Value("${kafka.topic.chat-topic2}")
//	String responseTopic;
	private final ProducerService producer;
	private AtomicLong atomicLong = new AtomicLong();
	ChatController(ProducerService producer) {
		this.producer = producer;
	}

	@Autowired
	private ChatBotConfiguration chatBotConfiguration;

	@MessageMapping("/chat.sendMessage")
	@SendToUser
	public ChatMessage sendMessage(@Payload ChatMessage chatMessage, @Header("simpSessionId") String sessionId) {
		String projectId = chatBotConfiguration.getProjectid();
		String languageCode = chatBotConfiguration.getLanguageCode();
		logger.info("User send Message: projectId = " + projectId + ",sessionId = " + sessionId + ",magnitude = " + chatMessage.getMagnitude()+ ",email = " + chatMessage.getEmail()+ ",sentiment = " + chatMessage.getSentiment());

		//response
//		chatMessage.setSender(username);
		String responseContent = detectIntentTexts(projectId, chatMessage.getContent(), sessionId, languageCode,chatMessage);
		ChatMessage cm = new ChatMessage( ChatMessage.MessageType.CHAT,chatMessage.getContent(), chatMessage.getSender(),sessionId,chatMessage.getEmail(),chatMessage.getMagnitude(),chatMessage.getSentiment());
		Gson gson = new Gson();
		String msg = gson.toJson(cm);
		logger.info("msg========="+msg);
		this.producer.sendMessage(chatTopic, msg);

		chatMessage.setSender(chatBotConfiguration.getName());
		chatMessage.setContent(responseContent);
		logger.info("ChatBot response Message: projectId = " + projectId + ",sessionId = " + sessionId + ",languageCode = " + languageCode + ",responseContent = " + responseContent);
//		this.producer.sendMessage(responseTopic, new ChatMessage(atomicLong.addAndGet(1), ChatMessage.MessageType.RESPONSE,chatMessage.getContent(), chatMessage.getSender(),sessionId,chatMessage.getEmail(),chatMessage.getMagnitude(),chatMessage.getSentiment()));
		return chatMessage;
	}

//	@RequestMapping("/detectIntentTexts")
	public String detectIntentTexts(String projectId, String text, String sessionId, String languageCode,ChatMessage chatMessage) {
		// Instantiates a client
		QueryResult queryResult = null;
		try (SessionsClient sessionsClient = SessionsClient.create()) {
			// Set the session name using the sessionId (UUID) and projectID (my-project-id)
			SessionName session = SessionName.of(projectId, sessionId);
			System.out.println("Session Path: " + session.toString());

			// Detect intents for each text input
			// Set the text (hello) and language code (en-US) for the query
			TextInput.Builder textInput = TextInput.newBuilder().setText(text).setLanguageCode(languageCode);

			// Build the query with the TextInput
			QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();

			//
			SentimentAnalysisRequestConfig sentimentAnalysisRequestConfig =
					SentimentAnalysisRequestConfig.newBuilder().setAnalyzeQueryTextSentiment(true).build();

			QueryParameters queryParameters =
					QueryParameters.newBuilder()
							.setSentimentAnalysisRequestConfig(sentimentAnalysisRequestConfig)
							.build();
			DetectIntentRequest detectIntentRequest =
					DetectIntentRequest.newBuilder()
							.setSession(session.toString())
							.setQueryInput(queryInput)
							.setQueryParams(queryParameters)
							.build();
			// Performs the detect intent request
			DetectIntentResponse response = sessionsClient.detectIntent(detectIntentRequest);

			// Display the query result
			queryResult = response.getQueryResult();
			chatMessage.setSentiment(String.valueOf(queryResult.getSentimentAnalysisResult().getQueryTextSentiment().getScore()));
			chatMessage.setMagnitude(String.valueOf(queryResult.getSentimentAnalysisResult().getQueryTextSentiment().getMagnitude()));
			logger.info("====================");
			logger.info(String.format("Query Text: '%s'\n", queryResult.getQueryText()));
			logger.info(String.format("Detected Intent: %s (confidence: %f)\n",
					queryResult.getIntent().getDisplayName(), queryResult.getIntentDetectionConfidence()));
			logger.info(String.format("Fulfillment Text: '%s'\n", queryResult.getFulfillmentText()));
			logger.info(String.format("Sentiment Score: '%s'\n", queryResult.getSentimentAnalysisResult().getQueryTextSentiment().getScore()));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return String.format("'%s'", queryResult.getFulfillmentText());
	}


	@MessageMapping("/chat.addUser")
	@SendTo("/channel/public")
	public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
		headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
		return chatMessage;
	}

	/**
	 * @return the chatBotConfiguration
	 */
	public ChatBotConfiguration getChatBotConfiguration() {
		return chatBotConfiguration;
	}

	/**
	 * @param chatBotConfiguration
	 *            the chatBotConfiguration to set
	 */
	public void setChatBotConfiguration(ChatBotConfiguration chatBotConfiguration) {
		this.chatBotConfiguration = chatBotConfiguration;
	}

}
