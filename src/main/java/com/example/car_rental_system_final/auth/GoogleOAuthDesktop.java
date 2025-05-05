package com.example.car_rental_system_final.auth;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.MemoryDataStoreFactory;

import java.io.InputStreamReader;
import java.util.Collections;

public class GoogleOAuthDesktop {
    private static final String CLIENT_SECRET_JSON = "/client_secret.json"; // Положите файл в resources!
    private static final int PORT = 8888;
    private static final String CALLBACK_PATH = "/Callback";

    public static String getIdToken() throws Exception {
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                jsonFactory,
                new InputStreamReader(GoogleOAuthDesktop.class.getResourceAsStream(CLIENT_SECRET_JSON))
        );

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientSecrets,
                Collections.singletonList("openid email profile"))
                .setDataStoreFactory(new MemoryDataStoreFactory())
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder()
                .setPort(PORT)
                .setCallbackPath(CALLBACK_PATH)
                .build();

        Credential credential = new com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp(flow, receiver)
                .authorize("user");

        // Получаем id_token из accessToken (refreshToken не содержит id_token)
        // Для этого нужно получить токен вручную через flow.getCredentialDataStore()
        // Но проще получить accessToken и использовать его для запроса userinfo, если нужно
        // Если нужен именно id_token, его можно получить из Credential.getAccessToken() только если flow был настроен на выдачу id_token
        // Поэтому используем Credential.getAccessToken() как access_token
        // Если нужен id_token, используйте GoogleTokenResponse при первом получении токена
        // Здесь возвращаем access_token для дальнейшей работы
        return credential.getAccessToken();
    }
} 