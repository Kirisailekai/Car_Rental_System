package com.example.car_rental_system_final;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    // URL подключения, пользователь и пароль
    private static final String URL = "jdbc:mysql://localhost:3306/car_rental_system_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";  // замените на вашего пользователя MySQL
    private static final String PASSWORD = "mnp300921Kiri";  // замените на ваш пароль

    public static Connection connect() throws SQLException {
        try {
            // Регистрация драйвера MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Установление соединения с базой данных
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("Ошибка: не найден драйвер MySQL");
            e.printStackTrace();
            throw new SQLException("Ошибка подключения к базе данных", e);
        }
    }

    public static void main(String[] args) {
        try {
            Connection connection = connect();
            System.out.println("Соединение с базой данных установлено!");
            connection.close();  // Закрыть соединение после использования
        } catch (SQLException e) {
            System.out.println("Ошибка подключения к базе данных");
            e.printStackTrace();
        }
    }
}
