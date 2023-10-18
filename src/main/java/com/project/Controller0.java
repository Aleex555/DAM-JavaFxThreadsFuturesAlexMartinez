package com.project;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javafx.application.Platform;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;

public class Controller0 {

    @FXML
    private Button button0, button1, button11, button3;
    @FXML
    private AnchorPane container;
    @FXML
    private Label percentatge0, percentatge1, percentatge12;
    @FXML
    private ProgressBar pb0;
    @FXML
    private ProgressBar pb1;
    @FXML
    private ProgressBar pb2;

    private ExecutorService executor = Executors.newFixedThreadPool(3); // Creamos un pool de tres hilos

    private boolean isRunningTask0 = false;
    private boolean isRunningTask1 = false;
    private boolean isRunningTask2 = false;
    private Future<?> task0, task1, task2;
    private int progress0 = 0, progress1 = 0, progress2 = 0; // Variables para el progreso

    @FXML
    private void handleToggleButtonAction(ActionEvent event) {
        Button clickedButton = (Button) event.getSource(); // Obtén el botón que se ha pulsado

        if (clickedButton == button1) {
            if (isRunningTask0) {
                // Pausar la tarea asociada al botón 1
                task0.cancel(true);
                isRunningTask0 = false;
                button1.setText("Activar");
            } else {
                // Iniciar la tarea asociada al botón 1 en segundo plano
                task0 = backgroundTask(0, progress0);
                isRunningTask0 = true;
                button1.setText("Pausar");
            }
        } else if (clickedButton == button11) {
            if (isRunningTask1) {
                // Pausar la tarea asociada al botón 11
                task1.cancel(true);
                isRunningTask1 = false;
                button11.setText("Activar");
            } else {
                // Iniciar la tarea asociada al botón 11 en segundo plano
                task1 = backgroundTask(1, progress1);
                isRunningTask1 = true;
                button11.setText("Pausar");
            }
        } else if (clickedButton == button3) {
            if (isRunningTask2) {
                // Pausar la tarea asociada al botón 1
                task2.cancel(true);
                isRunningTask2 = false;
                button3.setText("Activar");
            } else {
                // Iniciar la tarea asociada al botón 1 en segundo plano
                task2 = backgroundTask(2, progress2);
                isRunningTask2 = true;
                button3.setText("Pausar");
            }
        }
    }

    @FXML
    private void animateToView1(ActionEvent event) {
        UtilsViews.setViewAnimating("View1");
    }

    private Future<?> backgroundTask(int index, int initialValue) {
        final int finalValue = initialValue;
        return executor.submit(() -> {
            int value = finalValue;
            try {
                Random random = new Random();
                while (value < 100) {
                    if (Thread.currentThread().isInterrupted()) {
                        return;
                    }
                    value += (index == 0) ? 1
                            : (index == 1) ? random.nextInt(3) + 2 : (index == 2) ? random.nextInt(3) + 4 : 0;
                    if (value > 100) {
                        value = 100;
                    }
                    final int currentValue = value;

                    Platform.runLater(() -> {
                        if (index == 0) {
                            progress0 = currentValue;
                            percentatge0.setText(String.valueOf(currentValue) + "%");
                            double progressValue = currentValue / 100.0;
                            pb0.setProgress(progressValue);
                        } else if (index == 1) {
                            progress1 = currentValue;
                            percentatge1.setText(String.valueOf(currentValue) + "%");
                            double progressValue = currentValue / 100.0;
                            pb1.setProgress(progressValue);
                        } else if (index == 2) {
                            progress2 = currentValue;
                            percentatge12.setText(String.valueOf(currentValue) + "%");
                            double progressValue = currentValue / 100.0;
                            pb2.setProgress(progressValue);
                        }
                        if (progress0 == 100) {
                            progress0 = 0;
                            button1.setText("Activar");
                        }
                        if (progress1 == 100) {
                            progress1 = 0;
                            button11.setText("Activar");
                        }
                        if (progress2 == 100) {
                            progress2 = 0;
                            button3.setText("Activar");
                        }
                    });

                    int sleepTime = (index == 0) ? 1000
                            : (index == 1) ? random.nextInt(3) * 1000 + 2000
                                    : (index == 2) ? random.nextInt(6) * 1000 + 3000 : 0;
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        // Vuelve a establecer la bandera de interrupción
                        Thread.currentThread().interrupt();
                    }

                    System.out.println("Updating label: " + index + ", Value: " + currentValue);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // Esta función la llamarías cuando quieras detener el executor (por ejemplo, al
    // cerrar tu aplicación)
    public void stopExecutor() {
        executor.shutdown();
    }
}
