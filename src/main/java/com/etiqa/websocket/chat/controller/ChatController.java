package com.etiqa.websocket.chat.controller;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import com.etiqa.websocket.chat.config.ChatBotConfiguration;
import com.etiqa.websocket.chat.service.ProducerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.cloud.dialogflow.v2beta1.AudioEncoding;
import com.google.cloud.dialogflow.v2beta1.DetectIntentRequest;
import com.google.cloud.dialogflow.v2beta1.DetectIntentResponse;
import com.google.cloud.dialogflow.v2beta1.InputAudioConfig;
import com.google.cloud.dialogflow.v2beta1.QueryInput;
import com.google.cloud.dialogflow.v2beta1.QueryResult;
import com.google.cloud.dialogflow.v2beta1.SessionName;
import com.google.cloud.dialogflow.v2beta1.SessionsClient;
import com.google.cloud.dialogflow.v2beta1.TextInput;
import com.google.cloud.dialogflow.v2beta1.TextInput.Builder;
import com.google.protobuf.ByteString;
import com.etiqa.websocket.chat.model.ChatMessage;

@RestController
public class ChatController {

private static final Logger logger = LogManager.getLogger();
	@Value("${kafka.topic.chat-topic1}")
	String chatTopic;
	@Value("${kafka.topic.chat-topic2}")
	String responseTopic;
	private final ProducerService producer;
	private AtomicLong atomicLong = new AtomicLong();
	ChatController(ProducerService producer) {
		this.producer = producer;
	}

	@Autowired
	private ChatBotConfiguration chatBotConfiguration;

	@MessageMapping("/chat.sendMessage")
	@SendToUser
	public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
		String projectId = chatBotConfiguration.getProjectid();
		String sessionId = UUID.randomUUID().toString();
		String languageCode = chatBotConfiguration.getLanguageCode();
		System.out.println("User send Message: projectId = " + projectId + ",sessionId = " + sessionId + ",languageCode = " + languageCode);
		// chat
		this.producer.sendMessage(chatTopic, new ChatMessage(atomicLong.addAndGet(1), ChatMessage.MessageType.CHAT,chatMessage.getContent(), chatMessage.getSender()));

		//response
		chatMessage.setSender(chatBotConfiguration.getName());
		String responseContent = detectIntentTexts(projectId, chatMessage.getContent(), sessionId, languageCode);
		chatMessage.setContent(responseContent);
		System.out.println("ChatBot response Message: projectId = " + projectId + ",sessionId = " + sessionId + ",languageCode = " + languageCode + ",responseContent = " + responseContent);
		this.producer.sendMessage(responseTopic, new ChatMessage(atomicLong.addAndGet(1), ChatMessage.MessageType.RESPONSE,chatMessage.getContent(), chatMessage.getSender()));
		return chatMessage;
	}

//	@RequestMapping("/detectIntentTexts")
	public String detectIntentTexts(String projectId, String text, String sessionId, String languageCode) {
		// Instantiates a client
		QueryResult queryResult = null;
		try (SessionsClient sessionsClient = SessionsClient.create()) {
			// Set the session name using the sessionId (UUID) and projectID (my-project-id)
			SessionName session = SessionName.of(projectId, sessionId);
			System.out.println("Session Path: " + session.toString());

			// Detect intents for each text input
			// Set the text (hello) and language code (en-US) for the query
			Builder textInput = TextInput.newBuilder().setText(text).setLanguageCode(languageCode);

			// Build the query with the TextInput
			QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();

			// Performs the detect intent request
			DetectIntentResponse response = sessionsClient.detectIntent(session, queryInput);

			// Display the query result
			queryResult = response.getQueryResult();


			logger.info("====================");
			logger.info(String.format("Query Text: '%s'\n", queryResult.getQueryText()));
			logger.info(String.format("Detected Intent: %s (confidence: %f)\n",
					queryResult.getIntent().getDisplayName(), queryResult.getIntentDetectionConfidence()));
			logger.info(String.format("Fulfillment Text: '%s'\n", queryResult.getFulfillmentText()));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return String.format("'%s'", queryResult.getFulfillmentText());
	}

	@RequestMapping("/detectIntentAudio")
	public String detectIntentAudio(String projectId, byte[] inputAudio, String sessionId, String languageCode) {
		// Instantiates a client
		QueryResult queryResult = null;
		try (SessionsClient sessionsClient = SessionsClient.create()) {
			// Set the session name using the sessionId (UUID) and projectID (my-project-id)
			SessionName session = SessionName.of(projectId, sessionId);
			System.out.println("Session Path: " + session.toString());

			// Note: hard coding audioEncoding and sampleRateHertz for simplicity.
			// Audio encoding of the audio content sent in the query request.
			AudioEncoding audioEncoding = AudioEncoding.AUDIO_ENCODING_LINEAR_16;
			int sampleRateHertz = 16000;

			// Instructs the speech recognizer how to process the audio content.
			InputAudioConfig inputAudioConfig = InputAudioConfig.newBuilder().setAudioEncoding(audioEncoding)
					.setLanguageCode(languageCode) // languageCode = "en-US"
					.setSampleRateHertz(sampleRateHertz) // sampleRateHertz = 16000
					.build();

			// Build the query with the InputAudioConfig
			QueryInput queryInput = QueryInput.newBuilder().setAudioConfig(inputAudioConfig).build();

			// Read the bytes from the audio file

			// Build the DetectIntentRequest
			DetectIntentRequest request = DetectIntentRequest.newBuilder().setSessionWithSessionName(session)
					.setQueryInput(queryInput).setInputAudio(ByteString.copyFrom(inputAudio)).build();

			// Performs the detect intent request
			DetectIntentResponse response = sessionsClient.detectIntent(request);

			// Display the query result
			queryResult = response.getQueryResult();
			logger.info("====================");
			logger.info(String.format("Query Text: '%s'\n", queryResult.getQueryText()));
			logger.info(String.format("Detected Intent: %s (confidence: %f)\n",
					queryResult.getIntent().getDisplayName(), queryResult.getIntentDetectionConfidence()));
			logger.info(String.format("Fulfillment Text: '%s'\n", queryResult.getFulfillmentText()));
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
