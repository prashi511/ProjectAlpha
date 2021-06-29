package com.az.modules.service;

import java.io.IOException;
import java.util.Locale;

import com.az.modules.oauth.validation.JwtValidationException;
import com.az.modules.oauth.validation.SigningKeyResolver;
import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobContainerAccessPolicies;
import com.azure.storage.blob.models.BlobSignedIdentifier;
import com.azure.storage.blob.models.PublicAccessType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.IncorrectClaimException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MissingClaimException;
import io.jsonwebtoken.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.io.InputStreamSource;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service("token service")
public class BlobUploadService {

	private static final Logger log = LoggerFactory.getLogger(BlobUploadService.class);
	BlobServiceClient storageClient;
	public BlobUploadService(){

		String storageName = System.getProperty("UPLOAD_STORAGE_NAME");
		String connString = System.getProperty("AZURE_STORAGE_CONNECTION_STRING");
		log.info("storageName {}", storageName);
		String endpoint = String.format(Locale.ROOT, "https://%s.blob.core.windows.net", storageName);
		String sas ="RVLBBUoVebmcOIwagc3gYhcYIqUeg+GwAtD8v/+fa4DZB5Y+OUQKcJSZQZO3opvWf/VXv8vW9tf3kV3WerBdkg==";
		log.info("endpoint {}", endpoint);
		log.info("connString {}", connString);

		storageClient = new BlobServiceClientBuilder()
				.endpoint(endpoint)
				//.credential(new DefaultAzureCredentialBuilder().build())
				//.sasToken(sas)
				.connectionString(connString)
				.buildClient();
		log.info("blob storage client is intiated");

	}

	public void uploadBlob(byte[] data, String fileName){
		String containerName = System.getProperty("UPLOAD_CONTAINER_NAME");
		BlobContainerClient container = storageClient.getBlobContainerClient(containerName);
		if(!container.exists()) {
			log.info("blob doesn't exists");
			container.create();
		}

		BlobClient blobClient = container.getBlobClient(fileName);

		log.info("filename {} created in blob", fileName);
		//blobClient.upload(file.getInputStream(), file.getSize(), true);
		blobClient.upload(BinaryData.fromBytes(data), true);

		log.info("uploaded successfully", fileName);

	}

	public void uploadBlob(MultipartFile file, String fileName) throws IOException {
		String containerName = System.getProperty("UPLOAD_CONTAINER_NAME");
		BlobContainerClient container = storageClient.getBlobContainerClient(containerName);
		if(!container.exists()) {
			log.info("blob doesn't exists");
			container.create();
		}
		log.info("access policy {}",container.getAccessPolicy());

		BlobClient blobClient = container.getBlobClient(fileName);

		log.info("filename {} created in blob", fileName);
		blobClient.upload(file.getInputStream(), file.getSize(), true);
		//blobClient.upload(BinaryData.fromBytes(data), true);

		log.info("uploaded successfully", fileName);

	}

	public boolean validateToken(String accessToken) throws Exception {
		log.info("validating token");
		boolean success = false;
		String authorityUrl = System.getProperty("APP_OAUTH_CLIENT_ID");
		String issuerUrl = System.getProperty("TOKEN_ISSUER");
		String appId = System.getProperty("APP_OAUTH_CLIENT_ID");
		log.info(" issuerUrl {}",issuerUrl);
		SigningKeyResolver signingKeyResolver = new SigningKeyResolver(issuerUrl);
		Jws<Claims> claims;
		JwtDecoder dcd = JwtDecoders.fromIssuerLocation(issuerUrl);


		try {
			claims = Jwts.parser()
					.setSigningKeyResolver(signingKeyResolver)
					.requireAudience(appId)
					.requireIssuer(issuerUrl)
					.parseClaimsJws(accessToken);
			success = true;

		} catch(SignatureException ex) {
			throw new JwtValidationException("Jwt validation failed: invalid signature", ex);

		} catch(ExpiredJwtException ex) {
			throw new JwtValidationException("Jwt validation failed: access token us expired", ex);

		} catch(MissingClaimException ex) {
			throw new JwtValidationException("Jwt validation failed: missing required claim", ex);

		} catch(IncorrectClaimException ex) {
			throw new JwtValidationException("Jwt validation failed: required claim has incorrect value", ex);

		}
		return  success;
	}

}
