package com.example.car_rental_system_final;

import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Modality;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.List;
import javafx.stage.Modality;
import javafx.util.Duration;

public class AdminPageController {
    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private Button Logout;
    @FXML
    private Label UserName;
    @FXML
    private ImageView Logo;
    @FXML
    private Slider PriceSlider;
    @FXML
    private Label Price;
    @FXML
    private TableView<Car> tableView;
    @FXML
    private TableColumn<Car, String> brandColumn;
    @FXML
    private TableColumn<Car, String> typeColumn;
    @FXML
    private TableColumn<Car, Double> priceColumn;
    @FXML
    private TextField CaType;
    @FXML
    private TextField CarBrand;
    @FXML
    private TextField CarColor;
    @FXML
    private TextField CarCapacity;
    @FXML
    private ImageView CarImage;
    @FXML
    private TextField CarVolume;
    @FXML
    private TextField CarPrice;
    @FXML
    private CheckBox TypeSport;
    @FXML
    private CheckBox TypeSUV;
    @FXML
    private CheckBox TypeMPV;
    @FXML
    private CheckBox TypeSedan;
    @FXML
    private CheckBox TypeCoupe;
    @FXML
    private CheckBox TypeHatchback;
    @FXML
    private CheckBox Capacity2Person;
    @FXML
    private CheckBox Capacity4Person;
    @FXML
    private CheckBox Capacity6Person;
    @FXML
    private CheckBox Capacity8PersonOrMore;
    @FXML
    private Button Delete;
    @FXML
    private Button Save;
    @FXML
    private Button AddImage;
    @FXML
    private Label imgName;
    @FXML
    private Button Filter;
    private CarDB carDB;
    private String filterText = "";

    @FXML
    public void initialize() {
        UserInfo userInfo = UserInfo.getInstance();
        UserName.setText(userInfo.getUserName());
        Image image = new Image(getClass().getResource("/images/logo.png").toExternalForm());
        Logo.setImage(image);

        PriceSlider.setMin(100);
        PriceSlider.setMax(400);

        PriceSlider.setValue(PriceSlider.getMax());

        PriceSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Price.setText("$" + String.format("%.2f", newValue.doubleValue()));
            }
        });

        brandColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBrand()));
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        priceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPricePerDay()).asObject());

        carDB = new CarDB();
        fetchCarsFromDatabase();

        tableView.getSelectionModel().selectFirst();
        selectCarInTable();
    }
    @FXML
    public void onClickLogoutButton(){
        navigateToSignIn();
    }
    @FXML
    public void onClickFilterButton() {
        filterCars();
    }
    @FXML
    private void onSelectCarMouse(MouseEvent event) {
        selectCarInTable();
    }

    @FXML
    private void onClickAddImageButton() {
        Car selectedCar = tableView.getSelectionModel().getSelectedItem();

        if (selectedCar == null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Image File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
            );

            File selectedFile = fileChooser.showOpenDialog(primaryStage);

            if (selectedFile != null) {
                String imageName = selectedFile.getName();
                String destinationPath = "src/main/resources/images/" + imageName;

                try {
                    Files.copy(selectedFile.toPath(), Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING);

                    imgName.setText(imageName);

                    Image carImage = new Image(selectedFile.toURI().toString());
                    CarImage.setImage(carImage);

                    System.out.println("Image added successfully.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Cannot add image when a car is selected.");
        }
    }

    private void fetchCarsFromDatabase() {
        try {
            List<Car> carsFromDB = carDB.getAllCars();
            ObservableList<Car> carObservableList = FXCollections.observableArrayList(carsFromDB);
            tableView.setItems(carObservableList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onClickSaveButton() {
        Car selectedCar = tableView.getSelectionModel().getSelectedItem();

        if (selectedCar != null) {
            updateCarValues(selectedCar);
            updateCarInDatabase(selectedCar);

            fetchCarsFromDatabase();
        } else {
            if (    !CaType.getText().isEmpty() &&
                    !CarBrand.getText().isEmpty() &&
                    !CarColor.getText().isEmpty() &&
                    !CarCapacity.getText().isEmpty() &&
                    !CarVolume.getText().isEmpty() &&
                    !CarPrice.getText().isEmpty() &&
                    !imgName.getText().isEmpty())
            {
                Car car = new Car();
                car.setType(CaType.getText());
                car.setBrand(CarBrand.getText());
                car.setColor(CarColor.getText());
                car.setCapacity(Integer.parseInt(CarCapacity.getText()));
                car.setVolume(Integer.parseInt(CarVolume.getText()));
                car.setPricePerDay(Double.parseDouble(CarPrice.getText()));
                car.setImagePath(imgName.getText());
                try {
                    carDB.insertCar(car);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                clearCarDetails();
            }
        }
    }

    @FXML
    private void onClickDeleteButton() {
        Car selectedCar = tableView.getSelectionModel().getSelectedItem();

        if (selectedCar != null) {
            deleteCarFromDatabase(selectedCar);

            fetchCarsFromDatabase();

            clearCarDetails();
        } else {
            clearCarDetails();
        }
    }

    private void clearCarDetails() {
        CaType.clear();
        CarBrand.clear();
        CarColor.clear();
        CarCapacity.clear();
        CarVolume.clear();
        CarPrice.clear();
        imgName.setText("");
        CarImage.setImage(null);
    }

    private void fetchFilteredCarsFromDatabase() {
        try {
            List<Car> filteredCarsFromDB = carDB.getFilteredCars(filterText);
            ObservableList<Car> filteredCarObservableList = FXCollections.observableArrayList(filteredCarsFromDB);
            tableView.setItems(filteredCarObservableList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void filterCars(){
        int check = 0;
        filterText = "where 1=1 ";

        if (TypeCoupe.isSelected() || TypeHatchback.isSelected() || TypeMPV.isSelected() || TypeSedan.isSelected() || TypeSport.isSelected() || TypeSUV.isSelected()) {
            filterText += "and type in (";

            if (TypeCoupe.isSelected()) {
                filterText += "'Coupe',";
                check++;
            }

            if (TypeHatchback.isSelected()) {
                filterText += "'Hatchback',";
                check++;
            }

            if (TypeMPV.isSelected()) {
                filterText += "'MPV',";
                check++;
            }

            if (TypeSedan.isSelected()) {
                filterText += "'Sedan',";
                check++;
            }

            if (TypeSport.isSelected()) {
                filterText += "'Sport',";
                check++;
            }

            if (TypeSUV.isSelected()) {
                filterText += "'SUV',";
                check++;
            }

            if (check > 0) {
                filterText = filterText.substring(0, filterText.length() - 1);
            }

            filterText += ") ";
        }

        check = 0;
        if (Capacity2Person.isSelected() || Capacity4Person.isSelected() || Capacity6Person.isSelected() || Capacity8PersonOrMore.isSelected()) {
            filterText += "and capacity in (";

            if (Capacity2Person.isSelected()) {
                filterText += "2,";
                check++;
            }

            if (Capacity4Person.isSelected()) {
                filterText += "4,";
                check++;
            }

            if (Capacity6Person.isSelected()) {
                filterText += "6,";
                check++;
            }

            if (Capacity8PersonOrMore.isSelected()) {
                filterText += "8,";
                check++;
            }

            if (check > 0) {
                filterText = filterText.substring(0, filterText.length() - 1);
            }

            filterText += ") ";
        }

        double maxPrice = PriceSlider.getValue();
        filterText += "and price_per_day <= " + maxPrice + " ";

        fetchFilteredCarsFromDatabase();
    }

    private Car currentlySelectedCar;

    private void selectCarInTable() {
        Car selectedCar = tableView.getSelectionModel().getSelectedItem();

        if (selectedCar != null) {
            if (selectedCar.equals(currentlySelectedCar)) {
                tableView.getSelectionModel().clearSelection();
                currentlySelectedCar = null;

                CaType.clear();
                CarBrand.clear();
                CarColor.clear();
                CarCapacity.clear();
                CarVolume.clear();
                CarPrice.clear();
                imgName.setText("");
                CarImage.setImage(null);
            } else {
                currentlySelectedCar = selectedCar;

                CaType.setText(selectedCar.getType());
                CarBrand.setText(selectedCar.getBrand());
                CarColor.setText(selectedCar.getColor());
                CarCapacity.setText(Integer.toString(selectedCar.getCapacity()));
                CarVolume.setText(String.valueOf(selectedCar.getVolume()));
                CarPrice.setText(String.valueOf(selectedCar.getPricePerDay()));
                imgName.setText(selectedCar.getImagePath());

                String imagePath = "src/main/resources/images/" + selectedCar.getImagePath();

                File file = new File(imagePath);
                Image carImage = new Image(file.toURI().toString());
                CarImage.setImage(carImage);
            }
            addZoomOnHover(CarImage);
        } else {
            CaType.clear();
            CarBrand.clear();
            CarColor.clear();
            CarCapacity.clear();
            CarVolume.clear();
            CarPrice.clear();
            imgName.setText("");
            CarImage.setImage(null);
            currentlySelectedCar = null;
        }
    }

    private void updateCarValues(Car car) {
        car.setType(CaType.getText());
        car.setBrand(CarBrand.getText());
        car.setColor(CarColor.getText());
        car.setCapacity(Integer.parseInt(CarCapacity.getText()));
        car.setVolume(Integer.parseInt(CarVolume.getText()));
        car.setPricePerDay(Double.parseDouble(CarPrice.getText()));
        car.setImagePath(imgName.getText());
    }

    private void updateCarInDatabase(Car car) {
        try {
            carDB.updateCar(car);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteCarFromDatabase(Car car) {
        String imageName = car.getImagePath();
        try {
            carDB.deleteCar(car.getId());
            deleteImageFile(imageName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteImageFile(String imageName) {
        String imagePath = "src/main/resources/images/" + imageName;

        File imageFile = new File(imagePath);

        if (imageFile.exists()) {
            boolean deleted = imageFile.delete();

            if (deleted) {
                System.out.println("Image file deleted: " + imagePath);
            } else {
                System.out.println("Failed to delete image file: " + imagePath);
            }
        } else {
            System.out.println("Image file does not exist: " + imagePath);
        }
    }

    public void navigateToSignIn(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Sign_in.fxml"));
            Parent signInPage = fxmlLoader.load();

            Sign_in_Controller signInController = fxmlLoader.getController();
            signInController.setPrimaryStage(primaryStage);

            primaryStage.getScene().setRoot(signInPage);
            primaryStage.setTitle("Sign In Page");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void addZoomOnHover(ImageView imageView) {
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(200), imageView);
        scaleIn.setToX(1.1);
        scaleIn.setToY(1.1);

        ScaleTransition scaleOut = new ScaleTransition(Duration.millis(200), imageView);
        scaleOut.setToX(1.0);
        scaleOut.setToY(1.0);

        PauseTransition pause = new PauseTransition(Duration.millis(200));

        imageView.setOnMouseEntered(event -> {
            scaleIn.playFromStart();
        });

        imageView.setOnMouseExited(event -> {
            scaleIn.stop();
            scaleIn.setToX(1.1);
            scaleIn.setToY(1.1);
            scaleOut.setRate(-1);
            scaleOut.setOnFinished(e -> scaleOut.setRate(1));
            pause.setOnFinished(e -> scaleOut.playFromStart());
            pause.play();
        });
    }
}
