package com.project;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class Controller1 implements initialize {

    @FXML
    private Button button0, button1;
    @FXML
    private AnchorPane container;
    @FXML
    private Label loading;
    

    @FXML
    public void initialize() {
        loading.setVisible(false);
    }

    @FXML
    private void animateToView0(ActionEvent event) {
        UtilsViews.setViewAnimating("View0");
    }

    @FXML
    private void loadImage() {
        ImageView[] imgArray = new ImageView[24];
        for (int i = 1; i <= 24; i++) {
        imgArray[i - 1] = new ImageView();
        imgArray[i - 1].setId("img" + i);
        imgArray[i - 1].setImage(null);
        }
        Label[] labels = new Label[24];
        for (int i = 1; i <= 24; i++) {
        labels[i - 1] = new Label();
        labels[i - 1].setId("Loading" + i);
        labels[i-1].setVisible(true);
        }
        System.out.println("Loading image...");
        loading.setVisible(true);
        for(int i = 1; i<=24; i++){
        imgArray[i - 1].setImage(null);
        }
        loadImageBackground((image) -> {
        System.out.println("Image loaded");
        for(int i = 1; i<=24; i++){
            imgArray[i - 1].setImage(image);
        }

        for (int i = 1; i <= 24; i++) {
        labels[i-1].setVisible(false);
        }
        });
    }

    public void loadImageBackground(Consumer<Image> callBack) {
        // Use a thread to avoid blocking the UI
        CompletableFuture<Image> futureImage = CompletableFuture.supplyAsync(() -> {
            try {
                // Wait a second to simulate a long loading time
                Thread.sleep(1000);

                // Load the data from the assets file
                Image image = new Image(getClass().getResource("/assets/image.png").toString());
                return image;

            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        })
        .exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });

        futureImage.thenAcceptAsync(result -> {
            callBack.accept(result);
        }, Platform::runLater);
    }
}