package com.az.modules.service;

import java.util.Locale;


import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

@Service("blob service")
public class BlobUploadService {

	private static final Logger log = LoggerFactory.getLogger(BlobUploadService.class);

	public BlobUploadService(){

	}

	public void uploadBlob(byte[] data, String fileName){
		BlobServiceClient storageClient;
		String containerName = System.getProperty("UPLOAD_CONTAINER_NAME");
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

}
