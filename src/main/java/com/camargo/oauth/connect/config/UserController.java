package com.camargo.oauth.connect.config;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@RequestMapping("/user")
	public Map<String, String> user(Principal principal) {
		final String username = principal.getName();
		logger.info(username);
		final Map<String, String> map = new LinkedHashMap<>();
		map.put("name", username);
		return map;
	}

}
