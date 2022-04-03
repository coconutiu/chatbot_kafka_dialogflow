package com.etiqa.websocket.chat;

import com.coxautodev.graphql.tools.SchemaParser;
import com.etiqa.websocket.chat.resolver.Query;
import com.etiqa.websocket.chat.service.CustomerService;
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

	public static void main(String[] args) {
		System.out.println("PageTest==============");
		SpringApplication.run(WebsocketChatApplication.class, args);
	}
	@Bean
	public ServletRegistrationBean graphQLServlet() {
		return new ServletRegistrationBean(SimpleGraphQLHttpServlet.newBuilder(buildSchema(customerService)).build(),"/graphql");
	}

	private static GraphQLSchema buildSchema( CustomerService customerService) {
		return SchemaParser
				.newParser()
				.file("graphql/schema.graphqls")
//                .dictionary()
				.resolvers( new Query( customerService))
				.build()
				.makeExecutableSchema();
	}
}
