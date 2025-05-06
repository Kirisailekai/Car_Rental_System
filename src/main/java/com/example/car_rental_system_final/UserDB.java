package com.example.car_rental_system_final;

import com.example.car_rental_system_final.models.User;
import java.sql.*;

public class UserDB {
    // Обновленные параметры подключения для MySQL
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/car_rental_system_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String JDBC_USER = "root";  // замените на вашего пользователя MySQL
    private static final String JDBC_PASSWORD = "mnp300921Kiri";  // замените на ваш пароль MySQL

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    public static int createUser(User user) {
        //String checkUserSQL = "SELECT COUNT(*) FROM `user` WHERE email = ? OR phone = ?";
        String insertUserSQL = "INSERT INTO `user` (name, surname, email, phone, address, password, birthday) VALUES (?, ?, ?, ?, ?, ?, ?)";
        int userId = -1;

        try (Connection conn = getConnection();
             PreparedStatement userStmt = conn.prepareStatement(insertUserSQL, Statement.RETURN_GENERATED_KEYS)) {
            userStmt.setString(1, user.getUser_name());
            userStmt.setString(2, user.getUser_surname());
            userStmt.setString(3, user.getUser_email());
            userStmt.setString(4, user.getUser_phone());
            userStmt.setString(5, user.getUser_address());
            userStmt.setString(6, user.getUser_password());
            userStmt.setDate(7, user.getUser_birthday());
            userStmt.executeUpdate();

            try (ResultSet generatedKeys = userStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    userId = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userId;
    }


    public static boolean isUserExist(User user) {
        String checkUserSQL = "SELECT COUNT(*) FROM `user` WHERE email = ? OR phone = ?";
        boolean userExists = false;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(checkUserSQL)) {
            stmt.setString(1, user.getUser_email());
            stmt.setString(2, user.getUser_phone());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    userExists = count > 0; // If count is more than 0, user exists
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userExists;
    }

    public static boolean isAdminExist(User user) {
        String checkAdminSQL = "SELECT COUNT(*) FROM administrator WHERE email = ? OR phone = ?";
        boolean adminExists = false;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(checkAdminSQL)) {
            stmt.setString(1, user.getUser_email());
            stmt.setString(2, user.getUser_phone());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    adminExists = count > 0; // If count is more than 0, admin exists
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adminExists;
    }

    public static User getAdminInfo(User user) {
        String selectAdminSQL = "SELECT * FROM administrator WHERE email = ? OR phone = ?";
        User admin = null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(selectAdminSQL)) {
            stmt.setString(1, user.getUser_email());
            stmt.setString(2, user.getUser_phone());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    admin = new User();
                    admin.setUser_id(rs.getInt("id"));
                    admin.setUser_name(rs.getString("name"));
                    admin.setUser_surname(rs.getString("surname"));
                    admin.setUser_email(rs.getString("email"));
                    admin.setUser_phone(rs.getString("phone"));
                    admin.setUser_address(rs.getString("address"));
                    admin.setUser_password(rs.getString("password"));
                    admin.setUser_birthday(rs.getDate("birthday"));
                    // Add any other fields as necessary
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admin;
    }

    public static User getUserInfo(User user) {
        String selectUserSQL = "SELECT * FROM `user` WHERE email = ? OR phone = ?";
        User foundUser = null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(selectUserSQL)) {
            stmt.setString(1, user.getUser_email());
            stmt.setString(2, user.getUser_phone());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    foundUser = new User();
                    foundUser.setUser_id(rs.getInt("id"));
                    foundUser.setUser_name(rs.getString("name"));
                    foundUser.setUser_surname(rs.getString("surname"));
                    foundUser.setUser_email(rs.getString("email"));
                    foundUser.setUser_phone(rs.getString("phone"));
                    foundUser.setUser_address(rs.getString("address"));
                    foundUser.setUser_password(rs.getString("password"));
                    foundUser.setUser_birthday(rs.getDate("birthday"));
                    // Add any other fields as necessary
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foundUser;
    }

    public static void storeRefreshToken(String userId, String refreshToken) {
        String sql = "INSERT INTO refresh_tokens (user_id, token, created_at) VALUES (?, ?, NOW())";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.setString(2, refreshToken);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean validateRefreshToken(String userId, String refreshToken) {
        String sql = "SELECT * FROM refresh_tokens WHERE user_id = ? AND token = ? AND created_at > DATE_SUB(NOW(), INTERVAL 7 DAY)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.setString(2, refreshToken);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void deleteRefreshToken(String userId, String refreshToken) {
        String sql = "DELETE FROM refresh_tokens WHERE user_id = ? AND token = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.setString(2, refreshToken);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static User createOrUpdateSocialUser(String socialId, String email, String name, String provider) {
        String sql = "INSERT INTO users (social_id, user_email, user_name, provider) " +
                    "VALUES (?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE " +
                    "user_email = VALUES(user_email), " +
                    "user_name = VALUES(user_name)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, socialId);
            pstmt.setString(2, email);
            pstmt.setString(3, name);
            pstmt.setString(4, provider);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                User user = new User();
                user.setUser_id(rs.getInt(1));
                user.setUser_email(email);
                user.setUser_name(name);
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static User getUserByEmail(String email) {
        String query = "SELECT * FROM users WHERE user_email = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                User user = new User();
                user.setUser_id(rs.getInt("user_id"));
                user.setUser_email(rs.getString("user_email"));
                user.setUser_name(rs.getString("user_name"));
                user.setUser_password(rs.getString("user_password"));
                user.setUser_role(rs.getString("user_role"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void addUser(User user) {
        String query = "INSERT INTO users (user_email, user_name, user_password, user_role) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, user.getUser_email());
            pstmt.setString(2, user.getUser_name());
            pstmt.setString(3, user.getUser_password());
            pstmt.setString(4, user.getUser_role());
            
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                user.setUser_id(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
