package com.example.car_rental_system_final.auth;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;

public class GoogleOAuthDesktop {
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final String CREDENTIALS_FILE_PATH = "src/main/resources/client_secret.json";
    private static final String REDIRECT_URI = "http://localhost:8887/Callback";
    private static final String[] SCOPES = {
        "https://www.googleapis.com/auth/userinfo.email",
        "https://www.googleapis.com/auth/userinfo.profile"
    };

    public static GoogleIdToken.Payload getIdTokenPayload() throws Exception {
        // Load client secrets
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
            JacksonFactory.getDefaultInstance(),
            new InputStreamReader(new FileInputStream(CREDENTIALS_FILE_PATH))
        );

        // Build flow and trigger user authorization request
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
            new NetHttpTransport(),
            JacksonFactory.getDefaultInstance(),
            clientSecrets,
            Arrays.asList(SCOPES)
        )
        .setAccessType("offline")
        .build();

        // Create local server receiver
        LocalServerReceiver receiver = new LocalServerReceiver.Builder()
            .setPort(8887)
            .build();

        // Authorize and get credentials
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver)
            .authorize("user");

        // Get the access token
        String accessToken = credential.getAccessToken();
        if (accessToken == null) {
            throw new Exception("No access token received from Google");
        }

        // Create a payload with the user info
        GoogleIdToken.Payload payload = new GoogleIdToken.Payload();
        
        try {
            // Create HTTP transport and JSON factory
            HttpTransport httpTransport = new NetHttpTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

            // Create a request to the userinfo endpoint
            HttpRequest request = httpTransport.createRequestFactory(new HttpRequestInitializer() {
                @Override
                public void initialize(HttpRequest request) {
                    request.getHeaders().setAuthorization("Bearer " + accessToken);
                }
            }).buildGetRequest(new com.google.api.client.http.GenericUrl("https://www.googleapis.com/oauth2/v2/userinfo"));

            // Execute the request
            com.google.api.client.http.HttpResponse response = request.execute();
            String userInfoJson = response.parseAsString();

            // Parse the JSON response
            com.google.gson.JsonObject userInfo = new com.google.gson.JsonParser().parse(userInfoJson).getAsJsonObject();
            
            // Extract user information
            if (userInfo.has("email")) {
                payload.setEmail(userInfo.get("email").getAsString());
            }
            if (userInfo.has("name")) {
                payload.set("name", userInfo.get("name").getAsString());
            }
        } catch (Exception e) {
            System.err.println("Error getting user info: " + e.getMessage());
            throw e;
        }

        return payload;
    }
} 