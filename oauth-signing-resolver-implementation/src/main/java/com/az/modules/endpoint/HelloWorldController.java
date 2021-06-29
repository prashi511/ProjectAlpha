package com.az.modules.endpoint;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

	@RequestMapping("/v1/hello")
	public String helloWorld(@RequestParam(value="name", defaultValue="World") String name) {
		return "Hello "+name+"!!";
	}
	@GetMapping("/v1/graph")
	@ResponseBody
	public String graph(
			@RegisteredOAuth2AuthorizedClient("graph") OAuth2AuthorizedClient graphClient
	) {
		// toJsonString() is just a demo.
		// oAuth2AuthorizedClient contains access_token. We can use this access_token to access resource server.
		graphClient.getAccessToken();
		return "Hello graph";
	}
}
