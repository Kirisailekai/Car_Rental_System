package com.example.car_rental_system_final;

import java.sql.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class ReservationDB {
    // Обновленные параметры подключения для MySQL
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/car_rental_system_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String JDBC_USER = "root";  // замените на вашего пользователя MySQL
    private static final String JDBC_PASSWORD = "mnp300921Kiri";  // замените на ваш пароль MySQL

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    public static void insertReservation(Reservation reservation) {
        try (Connection conn = getConnection()) {
            UserInfo userInfo = UserInfo.getInstance();
            try (PreparedStatement reservationStmt = conn.prepareStatement(
                    "INSERT INTO reservation (user_id, car_id, starting_date, ending_date, pickup_location, return_location, total_cost, arrival_time, arrival_fine) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")
            ) {
                reservationStmt.setInt(1, userInfo.getUserId());
                reservationStmt.setInt(2, Car_Rental_System.getCar().getId());
                reservationStmt.setDate(3, new java.sql.Date(reservation.getPickUpDate().getTime()));
                reservationStmt.setDate(4, new java.sql.Date(reservation.getDropOfDate().getTime()));
                reservationStmt.setString(5, reservation.getPickUpLocation());
                reservationStmt.setString(6, reservation.getDropOfLocation());
                int daysBetween = (int) ChronoUnit.DAYS.between(
                        reservation.getPickUpDate().toInstant(),
                        reservation.getDropOfDate().toInstant()
                );
                reservationStmt.setDouble(7, Car_Rental_System.getCar().getPricePerDay() * daysBetween);
                reservationStmt.setDate(8, new java.sql.Date(0));
                reservationStmt.setDouble(9, 0.0);
                reservationStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Car_Rental_System.setErrorMessage(e.getMessage());
        }
    }

    public static boolean hasReservation() {
        UserInfo userInfo = UserInfo.getInstance();
        int userId = userInfo.getUserId();
        int carId = Car_Rental_System.getCar().getId();

        String sql = "SELECT COUNT(*) FROM reservation WHERE user_id = ? AND car_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, carId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static List<Reservation> getReservationsByUserAndCar() {
        List<Reservation> reservations = new ArrayList<>();
        UserInfo userInfo = UserInfo.getInstance();
        int userId = userInfo.getUserId();
        int carId = Car_Rental_System.getCar().getId();

        String sql = "SELECT * FROM reservation WHERE user_id = ? AND car_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, carId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Reservation reservation = new Reservation();

                    // Устанавливаем значения из базы данных в объект Reservation
                    reservation.setPickUpLocation(rs.getString("pickup_location"));
                    reservation.setDropOfLocation(rs.getString("return_location"));
                    reservation.setPickUpDate(rs.getDate("starting_date"));
                    reservation.setDropOfDate(rs.getDate("ending_date"));

                    reservations.add(reservation);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;
    }



}
