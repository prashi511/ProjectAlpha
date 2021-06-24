package com.az.modules.config;

import java.util.Properties;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@ConfigurationProperties(prefix = "cloud.blob")
public class BlobConfiguration implements InitializingBean{
	private static final Logger log = LoggerFactory.getLogger(BlobConfiguration.class);

	@Autowired
	Environment env;

	@Autowired
	private BlobSetup blobSetup;

	@Autowired
	BlobConfiguration(Environment env,  BlobSetup blobSetup){
		this.env= env;
		this.blobSetup= blobSetup;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		setUp();
	}

	private void setUp() {
		String connectionString = blobSetup.getConnectionString();
		String storageName = blobSetup.getStorageName();
		String containerName = blobSetup.getContainerName();
		System.out.println("connectionString " + connectionString);
		System.out.println("storageName " + storageName);
		System.out.println("containerName " + containerName);
		Properties prop = System.getProperties();
		prop.put("AZURE_STORAGE_CONNECTION_STRING", connectionString);
		prop.put("UPLOAD_STORAGE_NAME", storageName);
		prop.put("UPLOAD_CONTAINER_NAME", containerName);
		System.getProperties().putAll(prop);
		log.info("loaded blob properties");
	}
}
