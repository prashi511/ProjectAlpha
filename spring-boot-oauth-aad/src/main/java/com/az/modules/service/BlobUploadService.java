package com.az.modules.service;

import java.io.IOException;
import java.util.Locale;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service("token service")
public class BlobUploadService {

	private static final Logger log = LoggerFactory.getLogger(BlobUploadService.class);
	BlobServiceClient storageClient;
	public BlobUploadService(){

		String storageName = System.getProperty("UPLOAD_STORAGE_NAME");
		String connString = System.getProperty("AZURE_STORAGE_CONNECTION_STRING");
		String endpoint = String.format(Locale.ROOT, "https://%s.blob.core.windows.net", storageName);

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

		//blobClient.upload(file.getInputStream(), file.getSize(), true);
		log.info("Uploading data...");
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


}
