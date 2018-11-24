/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class TestFXChartScrollZoom extends Application {

    @Override
    public void start(Stage stage) {

        LineChart<Number, Number> chart = chart();
        final NumberAxis axis = (NumberAxis) chart.getXAxis();
        final double lowerX = axis.getLowerBound();
        final double upperX = axis.getUpperBound();

        chart.setOnScroll(new EventHandler<ScrollEvent>() {

            @Override
            public void handle(ScrollEvent event) {
                final double minX = axis.getLowerBound();
                final double maxX = axis.getUpperBound();
                double threshold = minX + (maxX - minX) / 2d;
                double x = event.getX();
                double value = axis.getValueForDisplay(x).doubleValue();
                double direction = event.getDeltaY();
                if (direction > 0) {
                    if (maxX - minX <= 1) {
                        return;
                    }
                    if (value < threshold) { // if(value > threshold)
                        axis.setLowerBound(minX + 1);
                    } else {
                        axis.setUpperBound(maxX - 1);
                    }
                } else {
                    if (value < threshold) {
                        double nextBound = Math.max(lowerX, minX - 1);
                        axis.setLowerBound(nextBound);
                    } else {
                        double nextBound = Math.min(upperX, maxX + 1);
                        axis.setUpperBound(nextBound);
                    }
                }

            }
        });

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(chart);
        Scene scene = new Scene(borderPane, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    private LineChart<Number, Number> chart() {
        XYChart.Series series = new LineChart.Series<>();
        final String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        NumberAxis xAxis = new NumberAxis(0, days.length - 1, 1);
        xAxis.setAutoRanging(false);
        xAxis.setTickLabelFormatter(new StringConverter<Number>() {

            @Override
            public String toString(Number t) {
                int index = t.intValue();
                return (index >= 0 && index < days.length) ? days[index] : null;
            }

            @Override
            public Number fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        NumberAxis yAxis = new NumberAxis();
        final LineChart<Number, Number> sc = new LineChart<>(xAxis, yAxis);
        sc.setCreateSymbols(false);
        sc.setPrefSize(1200, 210);
        series.setName("Line Chart Series");
        sc.getData().add(series);
        series.getData().add(new LineChart.Data(0, 23));
        series.getData().add(new LineChart.Data(1, 57));
        series.getData().add(new LineChart.Data(2, 54));
        series.getData().add(new LineChart.Data(3, 44));
        series.getData().add(new LineChart.Data(4, 14));
        series.getData().add(new LineChart.Data(5, 35));
        series.getData().add(new LineChart.Data(6, 65));
        return sc;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
