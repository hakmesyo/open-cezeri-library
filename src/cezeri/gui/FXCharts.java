/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.gui;

import cezeri.controller.ZoomManager;
import cezeri.matrix.CMatrix;
import cezeri.utils.FactoryUtils;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 *
 * @author BAP1
 */
public class FXCharts extends Application {

    private static NumberAxis xAxis = new NumberAxis();
    private static NumberAxis yAxis = new NumberAxis();
    private static ObservableList<XYChart.Series<Double, Double>> lineChartData = FXCollections.observableArrayList();
    private static LineChart lineChart = new LineChart(xAxis, yAxis, lineChartData);
    private static String xAxisLabel = "X";
    private static String yAxisLabel = "Y";
    private static String title = "Java FX Chart";
    private static String seriesName = "my data";
    private static double[] xData;

    public static void setCMatrix(CMatrix cMatrix) {
        Platform.runLater(() -> {
            setChart(cMatrix);
        });
    }

    public static void setCMatrix(CMatrix cMatrix, String st) {
        Platform.runLater(() -> {
            title = st;
            setChart(cMatrix);
        });
    }

    public static void setCMatrix(CMatrix cMatrix, String st, String xLabel, String yLabel) {
        Platform.runLater(() -> {
            xAxisLabel = xLabel;
            yAxisLabel = yLabel;
            title = st;
            setChart(cMatrix);
        });
    }

//    public static void setXData(double[] xd) {
//        Platform.runLater(() -> {
//            xData=xd;
//        });
//    }
    private static void setChart(CMatrix cm) {
        List<String> names = cm.getColumnNames();
        double[] xData = cm.getXData4FX();
        xAxis = new NumberAxis();
        yAxis = new NumberAxis();
        
	xAxis.setAutoRanging(true);
	xAxis.setForceZeroInRange(false);
	yAxis.setAutoRanging(true);
	yAxis.setForceZeroInRange(false);
        
        lineChartData = FXCollections.observableArrayList();
        lineChart = new LineChart(xAxis, yAxis, lineChartData);
        lineChart.setAnimated(false);
        lineChart.setCreateSymbols(false);

        StackPane chartContainer = new StackPane();
        chartContainer.getChildren().add(lineChart);

        xAxis.setLabel(xAxisLabel);
        yAxis.setLabel(yAxisLabel);
        lineChart.setTitle(title);

        // populating the series with data
        double[][] d = cm.transpose().toDoubleArray2D();
        List<XYChart.Series<Double, Double>> lst = new ArrayList();
        for (int i = 0; i < d.length; i++) {
            XYChart.Series<Double, Double> series = new XYChart.Series();
            if (names != null && names.size() > i) {
                series.setName(names.get(i));
            } else {
                series.setName("series " + i);
            }
            if (xData != null && xData.length == d[i].length) {
                for (int j = 0; j < d[i].length; j++) {
                    XYChart.Data<Double, Double> data=new XYChart.Data<Double, Double>(xData[j], d[i][j]);
                    series.getData().add(data);
                    data.setNode(new FXCharts.HoveredThresholdNode(xData[j], d[i][j]));
                }
            } else {
                for (int j = 0; j < d[i].length; j++) {
                    XYChart.Data<Double, Double> data=new XYChart.Data<Double, Double>(j * 1.0, d[i][j]);
                    series.getData().add(data);
                    data.setNode(new FXCharts.HoveredThresholdNode(j * 1.0, d[i][j]));
                }
            }
            lst.add(series);
        }
        lineChartData.addAll(lst);

        Rectangle zoomRect = new Rectangle();
        zoomRect.setManaged(false);
        zoomRect.setFill(Color.LIGHTSEAGREEN.deriveColor(0, 1, 1, 0.5));
        chartContainer.getChildren().add(zoomRect);
        
        setUpZooming(zoomRect, lineChart);

        final HBox controls = new HBox(10);
        controls.setPadding(new Insets(10));
        controls.setAlignment(Pos.CENTER);

        final Button zoomButton = new Button("Zoom");
        final Button resetButton = new Button("Reset");
        zoomButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("zoom isteği");
                doZoom(zoomRect, lineChart);
            }
        });
        resetButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("reset isteği");
                final NumberAxis xAxis = (NumberAxis) lineChart.getXAxis();
                xAxis.setLowerBound(0);
                xAxis.setUpperBound(1000);
                final NumberAxis yAxis = (NumberAxis) lineChart.getYAxis();
                yAxis.setLowerBound(0);
                yAxis.setUpperBound(1000);

                zoomRect.setWidth(0);
                zoomRect.setHeight(0);
            }
        });
        final BooleanBinding disableControls
                = zoomRect.widthProperty().lessThan(5)
                        .or(zoomRect.heightProperty().lessThan(5));
        zoomButton.disableProperty().bind(disableControls);
        controls.getChildren().addAll(zoomButton, resetButton);

        BorderPane root = new BorderPane();
        root.setCenter(chartContainer);
        root.setBottom(controls);

        Stage stage = new Stage();
//        new ZoomManager(chartContainer, lineChart, lineChartData);
        Scene scene = new Scene(root, 800, 600);

        stage.setScene(scene);
//        stage.setOpacity(0.95);
        stage.show();
    }

    private static void doZoom(Rectangle zoomRect, LineChart<Number, Number> chart) {
        Point2D zoomTopLeft = new Point2D(zoomRect.getX(), zoomRect.getY());
        Point2D zoomBottomRight = new Point2D(zoomRect.getX() + zoomRect.getWidth(), zoomRect.getY() + zoomRect.getHeight());
        final NumberAxis yAxis = (NumberAxis) chart.getYAxis();
        Point2D yAxisInScene = yAxis.localToScene(0, 0);
        final NumberAxis xAxis = (NumberAxis) chart.getXAxis();
        Point2D xAxisInScene = xAxis.localToScene(0, 0);
        double xOffset = zoomTopLeft.getX() - yAxisInScene.getX();
        double yOffset = zoomBottomRight.getY() - xAxisInScene.getY();
        double xAxisScale = xAxis.getScale();
        double yAxisScale = yAxis.getScale();
        xAxis.setLowerBound(xAxis.getLowerBound() + xOffset / xAxisScale);
        xAxis.setUpperBound(xAxis.getLowerBound() + zoomRect.getWidth() / xAxisScale);
        yAxis.setLowerBound(yAxis.getLowerBound() + yOffset / yAxisScale);
        yAxis.setUpperBound(yAxis.getLowerBound() - zoomRect.getHeight() / yAxisScale);
        System.out.println(yAxis.getLowerBound() + " " + yAxis.getUpperBound());
        zoomRect.setWidth(0);
        zoomRect.setHeight(0);
    }

    private static void setUpZooming(final Rectangle rect, final Node zoomingNode) {
        final ObjectProperty<Point2D> mouseAnchor = new SimpleObjectProperty<>();
        zoomingNode.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mouseAnchor.set(new Point2D(event.getX(), event.getY()));
                rect.setWidth(0);
                rect.setHeight(0);
            }
        });
        zoomingNode.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double x = event.getX();
                double y = event.getY();
                rect.setX(Math.min(x, mouseAnchor.get().getX()));
                rect.setY(Math.min(y, mouseAnchor.get().getY()));
                rect.setWidth(Math.abs(x - mouseAnchor.get().getX()));
                rect.setHeight(Math.abs(y - mouseAnchor.get().getY()));
            }
        });
    }

    @Override
    public void start(Stage stage) {
    }

    /**
     * a node which displays a value on hover, but is otherwise empty
     */
    static class HoveredThresholdNode extends StackPane {

        HoveredThresholdNode(double x, double y) {
            setPrefSize(7, 7);

            final Label label = createDataThresholdLabel(x, y);

            setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    getChildren().setAll(label);
                    setCursor(Cursor.CROSSHAIR);
//                    setCursor(Cursor.NONE);
                    toFront();
                }
            });
            setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    getChildren().clear();
                    setCursor(Cursor.CROSSHAIR);
                }
            });
        }

        private Label createDataThresholdLabel(double x, double y) {
            x=FactoryUtils.formatDouble(x, 2);
            y=FactoryUtils.formatDouble(y, 2);
            final Label label = new Label(xAxisLabel+"= "+x+"\n"+yAxisLabel+"= "+y);
//            label.getStyleClass().addAll("default-color0", "chart-line-symbol", "chart-series-line");
            label.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");

//            if (priorValue == 0) {
//                label.setTextFill(Color.DARKGRAY);
//            } else if (value > priorValue) {
//                label.setTextFill(Color.FORESTGREEN);
//            } else {
//                label.setTextFill(Color.FIREBRICK);
//            }

            label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
            return label;
        }
    }

}
