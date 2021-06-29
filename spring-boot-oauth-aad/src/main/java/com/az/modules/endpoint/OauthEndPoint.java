package com.az.modules.endpoint;


import javax.servlet.http.HttpServletRequest;

import com.az.modules.config.AzOauthCloud;
import com.az.modules.config.OauthConfig;
import com.az.modules.service.BlobUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class OauthEndPoint {

	private static final Logger log = LoggerFactory.getLogger(OauthEndPoint.class);
	@Autowired
	OauthConfig oauth;

	@Autowired
	AzOauthCloud azOauthCloud;

	@Autowired
	public OauthEndPoint(
			AzOauthCloud azOauthCloud, BlobUploadService uploadService) {
		this.azOauthCloud=azOauthCloud;
		this.uploadService=uploadService;
	}

	@Autowired
	BlobUploadService uploadService;

	@GetMapping(path="/v1/getToken")
	public ResponseEntity<String> getToken() throws Exception {
		String result = oauth.acquireToken();

		//String response = service.callWebAppEnpoint(result,"");
		String jsonResult = String.format("{token: %s}", result);
		log.info("Passing token...");
		return ResponseEntity.status(HttpStatus.OK).body(jsonResult);
	}

	@PostMapping(name="oath get", path="/v1/uploadfile")
	public ResponseEntity<String> uploadBlob(HttpServletRequest httpServletRequest, @RequestPart("file") MultipartFile file) throws Exception {

		uploadService.uploadBlob(file.getBytes(), file.getOriginalFilename());

		return ResponseEntity.status(HttpStatus.OK).body("SUCCESSFULLY UPLOADED");

	}

}