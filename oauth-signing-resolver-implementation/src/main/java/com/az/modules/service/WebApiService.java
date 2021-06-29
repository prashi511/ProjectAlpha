package com.az.modules.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service("blob service")
public class WebApiService {

	private static final Logger log = LoggerFactory.getLogger(WebApiService.class);

	public WebApiService(){

	}

	public String callWebAppEnpoint(String result, String url) {
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		headers.set("Authorization", "Bearer " + result);

		HttpEntity<String> entity = new HttpEntity<>(null, headers);

		String response = restTemplate.exchange("http://localhost:8083/web-app/v1/hello", HttpMethod.GET,
				entity, String.class).getBody();
		log.info("response {}", response);
		return response;
	}

	public void uploadFile(MultipartFile file, String token, String fileName) {

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		MultiValueMap<String, Object> body
				= new LinkedMultiValueMap<>();
		body.add(fileName, file);

		headers.set("Authorization", "Bearer " + token);

		HttpEntity<String> entity = new HttpEntity(body, headers);
		log.info("before sending response");

		String response = restTemplate.postForEntity("http://localhost:8083/web-app/v1/uploadblob",
				entity, String.class).getBody();


	}
}
