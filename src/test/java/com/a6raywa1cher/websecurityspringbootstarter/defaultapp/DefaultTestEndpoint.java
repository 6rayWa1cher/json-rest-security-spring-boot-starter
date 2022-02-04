package com.a6raywa1cher.websecurityspringbootstarter.defaultapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class DefaultTestEndpoint {
	private final ObjectMapper objectMapper;

	public DefaultTestEndpoint(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@GetMapping("/")
	public ObjectNode getCookies() {
		return objectMapper.createObjectNode().put("cookies", true);
	}
}
