package com.example.car_rental_system_final.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.util.Collections;

public class SocialAuthProvider {
    private static final String GOOGLE_CLIENT_ID = "YOUR_GOOGLE_CLIENT_ID"; // Замените на ваш Google Client ID

    public static UserInfo verifyGoogleToken(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(), new JacksonFactory())
                    .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                Payload payload = idToken.getPayload();
                return new UserInfo(
                    payload.getSubject(),
                    payload.getEmail(),
                    (String) payload.get("name"),
                    "GOOGLE"
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class UserInfo {
        private String id;
        private String email;
        private String name;
        private String provider;

        public UserInfo(String id, String email, String name, String provider) {
            this.id = id;
            this.email = email;
            this.name = name;
            this.provider = provider;
        }

        // Getters
        public String getId() { return id; }
        public String getEmail() { return email; }
        public String getName() { return name; }
        public String getProvider() { return provider; }
    }
} 