package com.az.modules.endpoint;


import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Enumeration;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;

import com.az.modules.service.BlobUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadBlobEndpoint {

	private static final Logger log = LoggerFactory.getLogger(UploadBlobEndpoint.class);

	@Autowired
	public UploadBlobEndpoint(BlobUploadService service) {
		this.service=service;
	}
	@Autowired
	BlobUploadService service;


	@GetMapping(name="oath get", path="/v1/hello")
	public ResponseEntity<String> getHello(HttpServletRequest httpServletRequest) throws InterruptedException, ExecutionException, MalformedURLException {
		String res = "hello " + " world";
		String token = httpServletRequest.getHeader("authorization");
		System.out.println("res " + res);
		System.out.println("token " + token);
		return ResponseEntity.status(HttpStatus.OK).body(res);
	}

	@PostMapping(name="oath get", path="/v1/uploadblob")
	public ResponseEntity<String> uploadBlob(HttpServletRequest httpServletRequest, @RequestPart("file") MultipartFile file) {

		String token = httpServletRequest.getHeader("authorization");

		log.info("upload call");
		try {
			if(file.isEmpty()){
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("file is empty");
			}

			service.uploadBlob(file.getBytes(), file.getOriginalFilename());
			return ResponseEntity.status(HttpStatus.OK).body("SUCCESSFULLY UPLOADED");
		}
		catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getMessage());
		}

	}


}
