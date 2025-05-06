package com.example.car_rental_system_final.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.car_rental_system_final.UserDB;
import com.example.car_rental_system_final.models.User;
import com.example.car_rental_system_final.models.UserInfo;

public class SocialAuthProvider {
    public static UserInfo verifyGoogleToken(String idToken) {
        try {
            // Декодируем JWT токен
            DecodedJWT jwt = JWT.decode(idToken);
            
            // Получаем информацию о пользователе из токена
            String email = jwt.getClaim("email").asString();
            String name = jwt.getClaim("name").asString();
            
            // Проверяем существование пользователя в базе данных
            User user = UserDB.getUserByEmail(email);
            
            if (user == null) {
                // Если пользователь не существует, создаем нового
                user = new User();
                user.setUser_email(email);
                user.setUser_name(name);
                user.setUser_password(""); // Пустой пароль для социальной аутентификации
                user.setUser_role("user"); // По умолчанию роль "user"
                UserDB.addUser(user);
            }
            
            // Создаем и возвращаем UserInfo
            return new UserInfo(String.valueOf(user.getUser_id()), user.getUser_email(), user.getUser_name());
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
} 