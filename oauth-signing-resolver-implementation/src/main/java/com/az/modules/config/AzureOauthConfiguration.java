package com.az.modules.config;

import java.util.Properties;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@ConfigurationProperties(prefix = "cloud.oauth")
@ConditionalOnProperty(value = "cloud.oauth.enable", havingValue = "true")
public class AzureOauthConfiguration implements InitializingBean {

	@Autowired
	Environment env;

	@Autowired
	private AzOauthCloud azOauthCloud;

	@Autowired
	AzureOauthConfiguration(Environment env,  AzOauthCloud azOauthCloud){
		this.env= env;
		this.azOauthCloud= azOauthCloud;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		String clientId = azOauthCloud.getClientId();
		String tenantId = azOauthCloud.getTenantId();
		String clientKey = azOauthCloud.getClientKey();
		String scope = azOauthCloud.getScope();
		String url = azOauthCloud.getRedirectUrl();
		String issuerUrl = transformUrl(azOauthCloud.getIssuerUrl());
		String authUrl = azOauthCloud.getMsAuthorityUrl()+"/oauth2/v2.0/token/";
		Properties prop = System.getProperties();

		prop.put("APP_OAUTH_CLIENT_ID",clientId);
		prop.put("APP_OAUTH_CLIENT_SECRET",clientKey);
		prop.put("APP_OAUTH_TENANT_ID",tenantId);
		prop.put("APP_OAUTH_SCOPE",scope);
		prop.put("APP_REDIRECT_URL", url);
		prop.put("APP_MS_AUTH_URL", transformUrl(authUrl));
		prop.put("TOKEN_ISSUER", transformUrl(issuerUrl));
		prop.put("AZURE_STORAGE_CONNECTION_STRING", "");
		System.getProperties().putAll(prop);

	}

	private String transformUrl(String url){
		if(!url.endsWith("/")){
			url = url + "/";
		}
		return  url;
	}
}
