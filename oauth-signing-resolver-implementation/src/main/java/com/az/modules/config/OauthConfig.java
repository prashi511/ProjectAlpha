package com.az.modules.config;

import java.net.MalformedURLException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import com.azure.identity.implementation.MsalToken;
import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ClientCredentialParameters;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.microsoft.aad.msal4j.IClientCredential;
import com.microsoft.aad.msal4j.SilentParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Configuration;

@Configuration
public class OauthConfig {

	private static final Logger log = LoggerFactory.getLogger(OauthConfig.class);

	private ConfidentialClientApplication confidentialClientApplication;
	private MsalToken msalToken;


	public OauthConfig() throws MalformedURLException {

	}

	public String getToken(String scope) throws ExecutionException, InterruptedException, MalformedURLException {
		Set<String> scopes = Set.of(scope);
		IAuthenticationResult result = confidentialClientApplication
				.acquireTokenSilently(SilentParameters.builder(scopes).build()).join();
		//IAuthenticationResult result = futureResult.get();
		String accessToken = result.accessToken();

		log.info(" access token = {} ", accessToken);
		return accessToken;
	}

	private String getToken(ClientCredentialParameters parameters) {

		IAuthenticationResult result = confidentialClientApplication.acquireToken(parameters).join();

		return result.accessToken();
	}

	public String acquireToken() throws Exception {
		String CLIENT_ID = System.getProperty("APP_OAUTH_CLIENT_ID");
		String CLIENT_SECRET = System.getProperty("APP_OAUTH_CLIENT_SECRET");
		String TENANT_ID = System.getProperty("APP_OAUTH_TENANT_ID");
		String REDIRECT_URL =  System.getProperty("APP_REDIRECT_URL");
		String SCOPE =  System.getProperty("APP_OAUTH_SCOPE");
		String AUTHORITY =  System.getProperty("APP_MS_AUTH_URL");

		log.info("start");
		System.out.println( " CLIENT_ID " + CLIENT_ID );
		System.out.println( " TENANT_ID " + TENANT_ID );
		System.out.println( " CLIENT_SECRET " + CLIENT_SECRET );
		System.out.println( " SCOPE " + SCOPE );
		System.out.println( " AUTHORITY " + AUTHORITY );
			// This is the secret that is created in the Azure portal when registering the application
		IClientCredential credential = ClientCredentialFactory.createFromSecret(CLIENT_SECRET);

		confidentialClientApplication = ConfidentialClientApplication
				.builder(CLIENT_ID, credential)
				.authority(AUTHORITY)
				.build();
		log.info("start");
		String scope = System.getProperty("APP_OAUTH_SCOPE");
		log.info("scope {}", scope);

			// Client credential requests will by default try to look for a valid token in the
			// in-memory token cache. If found, it will return this token. If a token is not found, or the
			// token is not valid, it will fall back to acquiring a token from the AAD service. Although
			// not recommended unless there is a reason for doing so, you can skip the cache lookup
			// by using .skipCache(true) in ClientCredentialParameters.
			ClientCredentialParameters parameters =
					ClientCredentialParameters
							.builder(Collections.singleton(scope))
							.build();
		IAuthenticationResult result = confidentialClientApplication.acquireToken(parameters).join();

		return result.accessToken();
			//return getToken(parameters);
	}
}
