/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import javafx.animation.PauseTransition;
import javafx.application.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

// hunt the Wumpus....
public class TestFXWumpus extends Application {
    private static final Insets SAFETY_ZONE = new Insets(10);
    private Label cowerInFear = new Label();
    private Stage mainStage;

    @Override
    public void start(final Stage stage) {
        // wumpus rulez
        mainStage = stage;
        mainStage.setAlwaysOnTop(true);

        // the wumpus doesn't leave when the last stage is hidden.
        Platform.setImplicitExit(false);

        // the savage Wumpus will attack
        // in the background when we least expect
        // (at regular intervals ;-).
        Timer timer = new Timer();
        timer.schedule(new WumpusAttack(), 0, 5_000);

        // every time we cower in fear
        // from the last savage attack
        // the wumpus will hide two seconds later.
        cowerInFear.setPadding(SAFETY_ZONE);
        cowerInFear.textProperty().addListener((observable, oldValue, newValue) -> {
            PauseTransition pause = new PauseTransition(
                    Duration.seconds(2)
            );
            pause.setOnFinished(event -> stage.hide());
            pause.play();
        });

        // when we just can't take it  anymore,
        // a simple click will quiet the Wumpus,
        // but you have to be quick...
        cowerInFear.setOnMouseClicked(event -> {
            timer.cancel();
            Platform.exit();
        });

        stage.setScene(new Scene(cowerInFear));
    }

    // it's so scary...
    public class WumpusAttack extends TimerTask {
        private String[] attacks = {
                "hugs you",
                "reads you a bedtime story",
                "sings you a lullaby",
                "puts you to sleep"
        };

        // the restaurant at the end of the universe.
        private Random random = new Random(42);

        @Override
        public void run() {
            // use runlater when we mess with the scene graph,
            // so we don't cross the streams, as that would be bad.
            Platform.runLater(() -> {
                cowerInFear.setText("The Wumpus " + nextAttack() + "!");
                mainStage.sizeToScene();
                mainStage.show();
            });
        }

        private String nextAttack() {
            return attacks[random.nextInt(attacks.length)];
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}