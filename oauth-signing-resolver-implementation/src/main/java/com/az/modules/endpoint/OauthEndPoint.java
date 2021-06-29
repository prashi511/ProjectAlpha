package com.az.modules.endpoint;


import javax.servlet.http.HttpServletRequest;

import com.az.modules.config.AzOauthCloud;
import com.az.modules.config.OauthConfig;
import com.az.modules.service.BlobUploadService;
import com.az.modules.service.WebApiService;
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
			AzOauthCloud azOauthCloud, WebApiService service, BlobUploadService uploadService) {
		this.azOauthCloud=azOauthCloud;
		this.service=service;
		this.uploadService=uploadService;
	}
	@Autowired
	WebApiService service;

	@Autowired
	BlobUploadService uploadService;


	/*@GetMapping(name="oath get", path="/v1/hello")
	public ResponseEntity<String> getHello() throws InterruptedException, ExecutionException, MalformedURLException {
		String res = "hello " + " world";
		System.out.println("res " + res);
		return ResponseEntity.status(HttpStatus.OK).body(res);
	}*/

	@GetMapping(path="/v1/getToken")
	public ResponseEntity<String> getToken() throws Exception {
		String result = oauth.acquireToken();
		System.out.println("Access token: " + result);

		//String response = service.callWebAppEnpoint(result,"");

		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

	@PostMapping(name="oath get", path="/v1/uploadfile")
	public ResponseEntity<String> uploadBlob(HttpServletRequest httpServletRequest, @RequestPart("file") MultipartFile file) throws Exception {

		//String accessToken = oauth.acquireToken();
		String accessToken = httpServletRequest.getHeader("authorization");
		log.info("token {}",accessToken);
		if(accessToken.startsWith("Bearer ")){
			accessToken = accessToken.replace("Bearer ","");
		}
		log.info("token {}",accessToken);

		if(file.isEmpty()){
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("file is empty");
		}
		boolean validatedToken = uploadService.validateToken(accessToken);
		log.info("upload call");

		//service.uploadFile(file, result, file.getOriginalFilename());
		if(!validatedToken) {
			log.info("invalid token");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Un authorized");
		}
		uploadService.uploadBlob(file, file.getOriginalFilename());
		return ResponseEntity.status(HttpStatus.OK).body("SUCCESSFULLY UPLOADED");

	}

	@PostMapping("/v1/user")
	public User login(@RequestParam("user") String username, @RequestParam("password") String pwd) throws Exception {

		String token = oauth.acquireToken();
		User user = new User();
		user.setUser(username);
		user.setToken(token);
		return user;

	}
}