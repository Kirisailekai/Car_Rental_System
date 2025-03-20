package com.example.car_rental_system_final;

import java.sql.*;

public class UserDB {
    // Обновленные параметры подключения для MySQL
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/car_rental_system_db?useSSL=false&serverTimezone=UTC";
    private static final String JDBC_USER = "root";  // замените на вашего пользователя MySQL
    private static final String JDBC_PASSWORD = "mnp300921Kiri";  // замените на ваш пароль MySQL

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    public static int createUser(User user) {
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
        String checkUserSQL = "SELECT COUNT(*) FROM \"user\" WHERE email = ? OR phone = ?";
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
        String checkAdminSQL = "SELECT COUNT(*) FROM administrator WHERE (email = ? OR phone = ?) AND administrator = 1";
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
        String selectAdminSQL = "SELECT * FROM administrator WHERE (email = ? OR phone = ?) AND administrator = 1";
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
        String selectUserSQL = "SELECT * FROM \"user\" WHERE email = ? OR phone = ?";
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

}
