package com.project;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class Controller1 {

    @FXML
    private Button button0, button1, button11;
    @FXML
    private Label label1;
    @FXML
    private ProgressBar barra;

    @FXML
    private AnchorPane container;
    private List<String> imageUrls = new ArrayList<>();

    private List<ImageView> imageViews = new ArrayList<>();

    private int progreso = 0;
    private volatile boolean stopThread = false;

    public void initialize() {
        // Llena la lista de rutas de imágenes
        for (int i = 0; i <= 23; i++) {
            imageUrls.add("/assets/img" + i + ".png");
        }

        List<Node> nodes = container.getChildren();
        for (Node node : nodes) {
            if (node instanceof ImageView) {
                imageViews.add((ImageView) node);
            }
        }
    }

    @FXML
    private void animateToView0(ActionEvent event) {
        UtilsViews.setViewAnimating("View0");
    }

    @FXML
    private void stopExecutor(ActionEvent event) {
        progreso = 0;
        stopThread = true;
        // Limpia todas las ImageView
        imageViews.forEach(imageView -> imageView.setImage(null));
        label1.setText(String.valueOf(0) + " de 24:");
        barra.setProgress((double) 0 / 24);
    }

    @FXML
    private void loadImage() {
        System.out.println("Loading images...");
        stopThread = false;
        progreso = 0;
        imageViews.forEach(imageView -> imageView.setImage(null));
        label1.setText(String.valueOf(0) + " de 24:");
        barra.setProgress((double) 0 / 24);

        loadNextImages();
    }

    private void loadNextImages() {
        for (int i = 0; i < imageViews.size(); i++) {
            int finalI = i;
            loadImageBackground(imageUrls.get(i), (image) -> {
                if (!stopThread) {
                    Platform.runLater(() -> {
                        ImageView currentImageView = imageViews.get(finalI);
                        currentImageView.setImage(image);
                        progreso++;
                        label1.setText(String.valueOf(progreso) + " de 24:");
                        barra.setProgress((double) progreso / 24);
                    });
                }
            });
        }
    }

    public void loadImageBackground(String imageUrl, Consumer<Image> callBack) {
        CompletableFuture.supplyAsync(() -> {
            if (stopThread) {
                return null;
            }
            try {
                Thread.sleep(numeroAleatorio());
                Image image = new Image(getClass().getResource(imageUrl).toString());
                return image;
            } catch (InterruptedException e) {
                // Maneja la interrupción del hilo
                return null;
            }
        })
                .thenAcceptAsync(result -> {
                    if (!stopThread && result != null) {
                        callBack.accept(result);
                    }
                }, Platform::runLater);
    }

    private int numeroAleatorio() {
        Random random = new Random();
        int numero = random.nextInt(5) + 1;
        return numero * 1000;
    }
}
