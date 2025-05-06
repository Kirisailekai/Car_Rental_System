package com.example.car_rental_system_final;

import com.example.car_rental_system_final.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import java.sql.SQLException;
import java.io.IOException;
import java.sql.Date;

public class Sign_up_Controller {
    @FXML
    private ImageView Car;

    @FXML
    private AnchorPane signUp1;

    @FXML
    private AnchorPane signUp2;
    private Stage primaryStage;
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private Hyperlink SignIn;
    @FXML
    private TextField NameField;
    @FXML
    private TextField SurnameField;
    @FXML
    private TextField EmailField;
    @FXML
    private TextField PhoneField;
    @FXML
    private TextField AddressField;
    @FXML
    private DatePicker BirthdayPick;
    @FXML
    private RadioButton MaleGender;
    @FXML
    private RadioButton FemaleGender;
    @FXML
    private PasswordField PasswordField;
    @FXML
    private PasswordField ConfirmPassword;
    @FXML
    private Button SignUpButton;
    @FXML
    private Circle Page1;
    @FXML
    private Circle Page2;
    @FXML
    private Label CautionLabel;
    @FXML
    private TextField Passport;
    @FXML
    private TextField DriverLicence;
    @FXML
    private CheckBox A;
    @FXML
    private CheckBox A1;
    @FXML
    private CheckBox B;
    @FXML
    private CheckBox B1;
    @FXML
    private CheckBox BE;
    @FXML
    private CheckBox C;
    @FXML
    private CheckBox C1;
    @FXML
    private CheckBox C1E;
    @FXML
    private CheckBox CE;
    @FXML
    private CheckBox D;
    @FXML
    private CheckBox DE;
    @FXML
    private CheckBox D1;
    @FXML
    private CheckBox D1E;
    @FXML
    private CheckBox T;

    @FXML
    public void initialize() {
        Image image = new Image(getClass().getResource("/images/car.png").toExternalForm());
        Car.setImage(image);
    }
    @FXML
    public void onClickSignUpButton() {
        if (validateInput()) {
            User user = new User();
            user.setUser_name(NameField.getText());
            user.setUser_surname(SurnameField.getText());
            user.setUser_email(EmailField.getText());
            user.setUser_phone(PhoneField.getText());
            user.setUser_address(AddressField.getText());
            user.setUser_password(PasswordField.getText());
            user.setUser_licenseNumber(DriverLicence.getText());
            user.setUser_passportNumber(Passport.getText());
            user.setUser_birthday(Date.valueOf(BirthdayPick.getValue()));

            // Set the categories based on checkboxes
            String category = "";
            if (A.isSelected()) category += "A ";
            if (A1.isSelected()) category += "A1 ";
            if (B.isSelected()) category += "B ";
            if (B1.isSelected()) category += "B1 ";
            if (BE.isSelected()) category += "BE ";
            if (C.isSelected()) category += "C ";
            if (C1.isSelected()) category += "C1 ";
            if (C1E.isSelected()) category += "C1E ";
            if (CE.isSelected()) category += "CE ";
            if (D.isSelected()) category += "D ";
            if (DE.isSelected()) category += "DE ";
            if (D1.isSelected()) category += "D1 ";
            if (D1E.isSelected()) category += "D1E ";
            if (T.isSelected()) category += "T ";
            user.setUser_licenseCategoria(category);

            int userId = UserDB.createUser(user);
            if (userId != -1) {
                // Успешная регистрация
                navigateToSignIn();
            } else {
                CautionLabel.setText("Ошибка при регистрации");
            }
        }
    }

    @FXML
    public void onClickCirclePage1() {
        navigateToSignUp();
    }

    @FXML
    public void onClickCirclePage2() {
        navigateToSignUp2();
    }

    @FXML
    private void onClickLinkSignIn() {
        try {
            // Load the FXML file for the sign-in page
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Sign_in.fxml"));
            Parent signInPage = fxmlLoader.load();

            Sign_in_Controller signInController = fxmlLoader.getController();
            signInController.setPrimaryStage(primaryStage);

            // Set the sign-in page as the root of the existing scene
            primaryStage.getScene().setRoot(signInPage);
            primaryStage.setTitle("Sign In Page");
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
    private void navigateToSignUp() {
        primaryStage.setTitle("Sign Up Page");
        signUp1.setVisible(true);
        signUp2.setVisible(false);
    }
    private void navigateToSignUp2() {
        signUp1.setVisible(false);
        signUp2.setVisible(true);
        primaryStage.setTitle("Sign Up Page2");
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

    private void navigateToSignIn() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Sign_in.fxml"));
            Parent signInPage = fxmlLoader.load();

            Sign_in_Controller signInController = fxmlLoader.getController();
            signInController.setPrimaryStage(primaryStage);

            primaryStage.getScene().setRoot(signInPage);
            primaryStage.setTitle("Sign in page");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean validateInput() {
        if (NameField.getText().isEmpty() ||
                EmailField.getText().isEmpty() ||
                PhoneField.getText().isEmpty() ||
                AddressField.getText().isEmpty() ||
                (BirthdayPick.getValue() == null) ||
                PasswordField.getText().isEmpty() ||
                Passport.getText().isEmpty() ||
                DriverLicence.getText().isEmpty() ||
                ConfirmPassword.getText().isEmpty()) {
            CautionLabel.setText("Every field should be filled");
            return false;
        } else if (!PasswordField.getText().equals(ConfirmPassword.getText())) {
            CautionLabel.setText("Password and Confirm Password do not match.");
            return false;
        } else {
            CautionLabel.setText("");
            return true;
        }
    }
}
