package com.example;

import reactor.core.publisher.Mono;

import org.springframework.http.client.reactive.JdkClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class DemoController {

	private final WebClient webClient;

	public DemoController(WebClient.Builder builder) {
		this.webClient = builder
				.clientConnector(new ReactorClientHttpConnector())
				//.clientConnector(new JdkClientHttpConnector())
				.build();
	}

	@GetMapping("/")
	String hello() {
		return "Hello";
	}

	@GetMapping("/client")
	Mono<Integer> client() {
		final var cookies = new LinkedMultiValueMap<String, String>();
		cookies.add("cookie1", "foo");
		cookies.add("cookie2", "bar");
		return this.webClient.get()
				.uri("http://localhost:8080/")
				.cookies(cookiesConsumer -> {
					cookiesConsumer.addAll(cookies);
				})
				.retrieve()
				.toEntity(String.class)
				.map(entity -> entity.getStatusCode().value());
	}
}
