package com.example.car_rental_system_final.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.example.car_rental_system_final.UserDB;
import com.example.car_rental_system_final.models.User;
import com.example.car_rental_system_final.models.UserInfo;

public class SocialAuthProvider {
    public static UserInfo verifyGoogleToken(String accessToken) {
        try {
            // Get user information from Google OAuth
            Payload payload = GoogleOAuthDesktop.getIdTokenPayload();
            
            if (payload == null) {
                System.err.println("Failed to get user information from Google");
                return null;
            }
            
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            
            if (email == null || name == null) {
                System.err.println("Email or name is missing from Google user info");
                return null;
            }
            
            System.out.println("Successfully retrieved user info from Google: " + email);
            
            // Check if user exists in database
            User user = UserDB.getUserByEmail(email);
            
            if (user == null) {
                // Create new user if doesn't exist
                user = new User();
                user.setUser_email(email);
                user.setUser_name(name);
                user.setUser_password(""); // Empty password for social authentication
                UserDB.addUser(user);
                
                // Get the newly created user to ensure we have the correct ID
                user = UserDB.getUserByEmail(email);
                if (user == null) {
                    System.err.println("Failed to create new user in database");
                    return null;
                }
                System.out.println("Created new user in database: " + email);
            } else {
                System.out.println("Found existing user in database: " + email);
            }
            
            // Store the refresh token
            if (accessToken != null && !accessToken.isEmpty()) {
                UserDB.storeRefreshToken(String.valueOf(user.getUser_id()), accessToken);
            }
            
            // Create and return UserInfo
            return new UserInfo(String.valueOf(user.getUser_id()), user.getUser_email(), user.getUser_name());
            
        } catch (Exception e) {
            System.err.println("Error during Google authentication: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
} 