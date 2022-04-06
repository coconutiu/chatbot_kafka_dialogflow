package com.etiqa.websocket.chat;

import com.coxautodev.graphql.tools.SchemaParser;
import com.etiqa.websocket.chat.resolver.CustomerResolver;
import com.etiqa.websocket.chat.resolver.Query;
import com.etiqa.websocket.chat.service.CustomerService;
import com.etiqa.websocket.chat.service.AgentService;
import graphql.schema.GraphQLSchema;
import graphql.servlet.SimpleGraphQLHttpServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WebsocketChatApplication {
	@Autowired
	private CustomerService customerService;

	@Autowired
	private AgentService agentService;

	public static void main(String[] args) {
		SpringApplication.run(WebsocketChatApplication.class, args);
	}

	@Bean
	public ServletRegistrationBean graphQLServlet() {
		return new ServletRegistrationBean(SimpleGraphQLHttpServlet.newBuilder(buildSchema(agentService, customerService)).build(),"/graphql");
	}

	private static GraphQLSchema buildSchema(AgentService agentService, CustomerService customerService) {
		return SchemaParser
				.newParser()
				.file("graphql/schema.graphqls")
//                .dictionary()
				.resolvers( new Query(agentService, customerService), new CustomerResolver(agentService))
				.build()
				.makeExecutableSchema();
	}
}
