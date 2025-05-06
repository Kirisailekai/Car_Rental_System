package com.example.car_rental_system_final;

import com.example.car_rental_system_final.auth.GoogleOAuthDesktop;
import com.example.car_rental_system_final.auth.JwtTokenProvider;
import com.example.car_rental_system_final.auth.SocialAuthProvider;
import com.example.car_rental_system_final.models.User;
import com.example.car_rental_system_final.models.UserInfo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;

import java.io.IOException;

public class Sign_in_Controller {
    @FXML
    private ImageView Logo;

    @FXML
    private ImageView Car;
    @FXML
    private Button signIn;
    @FXML
    private Button googleSignIn;

    private Stage primaryStage;
    private String refreshToken;

    // Set the primary stage
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private Hyperlink SignUp;

    @FXML
    private TextField LoginField;
    @FXML
    private PasswordField PasswordField;
    @FXML
    private Label errorLabel;

    @FXML
    public void initialize(){
        Image image = new Image(getClass().getResource("/images/car.png").toExternalForm());
        Car.setImage(image);

        Image image1 = new Image(getClass().getResource("/images/logo.png").toExternalForm());
        Logo.setImage(image1);
    }

    @FXML
    private void onClickButtonSignIn(){
        User user = new User();
        user.setUser_email(LoginField.getText());
        user.setUser_phone(LoginField.getText());
        user.setUser_password(PasswordField.getText());
        
        boolean userExists = UserDB.isUserExist(user);
        boolean adminExists = UserDB.isAdminExist(user);

        if (userExists || adminExists) {
            user = userExists ? UserDB.getUserInfo(user) : UserDB.getAdminInfo(user);
            
            // Генерация токенов
            String accessToken = JwtTokenProvider.generateAccessToken(user.getUser_email(), String.valueOf(user.getUser_id()));
            refreshToken = JwtTokenProvider.generateRefreshToken(user.getUser_email(), String.valueOf(user.getUser_id()));
            
            // Сохранение refresh токена
            UserDB.storeRefreshToken(String.valueOf(user.getUser_id()), refreshToken);
            
            // Обновление информации о пользователе
            Car_Rental_System.updateUserInfo(user.getUser_name(), user.getUser_id());
            
            // Навигация на соответствующую страницу
            if (adminExists) {
                navigateToAdminPage();
            } else {
                navigateToMainPage();
            }
        } else {
            errorLabel.setText("Неверный email или пароль");
        }
    }

    @FXML
    private void onClickGoogleSignIn(ActionEvent event) {
        try {
            // Получаем тестовый токен
            String idToken = GoogleOAuthDesktop.getIdToken();
            
            // Проверяем токен и получаем информацию о пользователе
            UserInfo userInfo = SocialAuthProvider.verifyGoogleToken(idToken);
            
            if (userInfo != null) {
                // Успешная аутентификация
                System.out.println("Успешный вход через Google: " + userInfo.getEmail());
                
                // Генерация токенов
                String accessToken = JwtTokenProvider.generateAccessToken(userInfo.getEmail(), userInfo.getId());
                refreshToken = JwtTokenProvider.generateRefreshToken(userInfo.getEmail(), userInfo.getId());
                
                // Сохранение refresh токена
                UserDB.storeRefreshToken(userInfo.getId(), refreshToken);
                
                // Обновление информации о пользователе
                Car_Rental_System.updateUserInfo(userInfo.getName(), Integer.parseInt(userInfo.getId()));
                
                // Переходим на главную страницу
                navigateToMainPage();
            } else {
                // Ошибка аутентификации
                errorLabel.setText("Ошибка аутентификации через Google");
                System.out.println("Ошибка аутентификации через Google");
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Ошибка при входе через Google: " + e.getMessage());
            System.out.println("Ошибка при входе через Google: " + e.getMessage());
        }
    }

    @FXML
    private void onClickLinkSignUp() {
        try {
            // Load the FXML file for the sign-in page
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Sign_up.fxml"));
            Parent signUpPage = fxmlLoader.load();

            Sign_up_Controller signUpController = fxmlLoader.getController();
            signUpController.setPrimaryStage(primaryStage);

            // Set the sign-up page as the root of the existing scene
            primaryStage.getScene().setRoot(signUpPage);
            primaryStage.setTitle("Sign in page");
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    private void navigateToMainPage(){
        try {
            // Load the FXML file for the sign-in page
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Main_Page.fxml"));
            Parent mainPage = fxmlLoader.load();

            MainPageController mainPageController = fxmlLoader.getController();
            mainPageController.setPrimaryStage(primaryStage);

            // Set the sign-up page as the root of the existing scene
            primaryStage.getScene().setRoot(mainPage);
            primaryStage.setTitle("Main page");
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    private void navigateToAdminPage(){
        try {
            // Load the FXML file for the sign-in page
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Admin_Page.fxml"));
            Parent mainPage = fxmlLoader.load();

            AdminPageController adminPageController = fxmlLoader.getController();
            adminPageController.setPrimaryStage(primaryStage);

            // Set the sign-up page as the root of the existing scene
            primaryStage.getScene().setRoot(mainPage);
            primaryStage.setTitle("Admin page");
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
}
