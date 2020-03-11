package cezeri.gui;

import cezeri.factory.FactoryUtils;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

public class PanelMLP extends JPanel {

    private static final long serialVersionUID = 1L;
    public double true_positive;//hit
    public double false_positive;//false alarm
    public double true_negative;//hit
    public double false_negative;//severe
    public double true_positive_rate;//hit rate,recall,sensitivity	TPR=TP/(TP+FN) => TP / Positives (tüm hastalıklıların toplamı)
    public double false_positive_rate;//false alarm rate			FPR=FP/N 
    public double accuracy;// ACC=(TP+TN)/(P+N) => (TP+TN) / DataSet
    public double specifity;//SPC=1-FPR True Negative Rate
    private double last_tr_error = 0.0;
    private double last_validation_error = 0.0;
    private double last_test_error = 0.0;
    private double worst_case = 0.0;
    private double efficiency = 0.0;
    private List<Double> errorPoints = new ArrayList<>();  //  @jve:decl-index=0:
    public List<Double> testerrorPoints = new ArrayList<>();  //  @jve:decl-index=0:
    public List<Double> validationErrorPoints = new ArrayList<>();  //  @jve:decl-index=0:

    public void initializeValues() {
        true_positive = 0;
        false_positive = 0;
        true_negative = 0;
        false_negative = 0;
        true_positive_rate = 0;
        false_positive_rate = 0;
        accuracy = 0;
        specifity = 0;
    }

    public void setErrVectorEmpty() {
        errorPoints = new ArrayList<>();
    }

    public void setTestErrVectorEmpty() {
        testerrorPoints = new ArrayList<>();
    }

    public void setValidationErrVectorEmpty() {
        validationErrorPoints = new ArrayList<>();
    }

    public void setWorstCaseError(double worst_case) {
        this.worst_case = FactoryUtils.formatDouble(worst_case);
        if (worst_case == 0) {
            return;
        }
        this.efficiency = FactoryUtils.formatDouble((this.worst_case - last_test_error) / worst_case * 100);
        repaint();
    }

    public void setErrorPoint(double err) {
        this.last_tr_error = FactoryUtils.formatDouble(err);
        errorPoints.add(err);
        this.last_test_error = 0;
        this.worst_case = 0;
        this.efficiency = 0;
        repaint();
    }

    public void setValidationErrorPoint(double err) {
        this.last_validation_error = FactoryUtils.formatDouble(err);
        validationErrorPoints.add(err);
        repaint();
    }

    public void setTestErrorPoint(double err) {
        this.last_test_error = FactoryUtils.formatDouble(err);
        testerrorPoints.add(err);
        repaint();
    }

    @Override
    public void paint(Graphics gg) {
        Graphics2D g = (Graphics2D) gg;
        g.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
         g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.blue);
        g.drawString("Error", 30, 40);
        g.drawString("#Epoch", getWidth() - 100, getHeight() - 40);
        g.drawLine(50, getHeight() - 20, 50, 50);
        g.drawLine(10, getHeight() - 60, getWidth() - 50, getHeight() - 60);

        int x1 = 500;
        int tx = getWidth() - x1;
        int ty = 55;
        g.setColor(Color.darkGray);
        g.drawString("Confusion Matrix", tx + 60, 25);
        g.setColor(Color.darkGray);
        g.drawString("Prediction Outcome", tx + 50, ty);
        g.drawString("p", tx + 50, ty + 20);
        g.drawString("n", tx + 150, ty + 20);
        g.drawString("total", tx + 220, ty + 20);
        g.drawString("actual", tx - 70, ty + 70);
        g.drawString("value", tx - 67, ty + 90);
        g.drawString("p'", tx - 30, ty + 50);
        g.drawString("n'", tx - 30, ty + 110);
        g.drawString("total", tx - 30, ty + 150);

        g.setColor(Color.black);
        g.fillRect(tx, ty + 25, 100, 50);
        g.fillRect(tx + 105, ty + 25, 100, 50);
        g.fillRect(tx, ty + 80, 100, 50);
        g.fillRect(tx + 105, ty + 80, 100, 50);
        g.setColor(Color.red);
        g.drawRect(tx, ty + 25, 100, 50);
        g.drawRect(tx + 105, ty + 25, 100, 50);
        g.drawRect(tx, ty + 80, 100, 50);
        g.drawRect(tx + 105, ty + 80, 100, 50);

        g.setColor(Color.green);
        g.drawString("True Positive", tx + 5, ty + 40);
        g.drawString("Hit", tx + 5, ty + 55);
        g.drawString("" + true_positive + "", tx + 5, ty + 70);
        g.drawString("True Negative", tx + 110, ty + 95);
        g.drawString("Correct Rejection", tx + 110, ty + 110);
        g.drawString("" + true_negative, tx + 110, ty + 125);
        g.setColor(Color.orange);
        g.drawString("False Positive", tx + 5, ty + 95);
        g.drawString("False Alarm", tx + 5, ty + 110);
        g.drawString("" + false_positive, tx + 5, ty + 125);
        g.setColor(Color.RED);
        g.drawString("False Negative", tx + 110, ty + 40);
        g.drawString("Severe", tx + 110, ty + 55);
        g.drawString("" + false_negative, tx + 110, ty + 70);

        g.setColor(Color.black);
        g.drawString("" + (true_positive + false_negative), tx + 220, ty + 70);
        g.drawString("" + (false_positive + true_negative), tx + 220, ty + 125);
        g.drawString("" + (true_positive + false_positive), tx + 5, ty + 150);
        g.drawString("" + (false_negative + true_negative), tx + 110, ty + 150);

        g.setColor(Color.black);
        g.fillRect(tx, ty + 160, 205, 80);
        g.setColor(Color.orange);

        if (!Double.isNaN(true_positive_rate)) {
            g.drawString("True Positive Rate (Sensitivity):" + FactoryUtils.formatDouble(true_positive_rate) + "", tx + 5, ty + 180);
        } else {
            g.drawString("True Positive Rate (Sensitivity):" + FactoryUtils.formatDouble(1) + "", tx + 5, ty + 180);
        }
        if (!Double.isNaN(false_positive_rate)) {
            g.drawString("False Positive Rate:" + FactoryUtils.formatDouble(false_positive_rate) + "", tx + 5, ty + 195);
        } else {
            g.drawString("False Positive Rate:" + FactoryUtils.formatDouble(1) + "", tx + 5, ty + 195);
        }
        if (!Double.isNaN(specifity)) {
            g.drawString("Specifity:" + FactoryUtils.formatDouble(specifity) + "", tx + 5, ty + 210);
        } else {
            g.drawString("Specifity:" + FactoryUtils.formatDouble(1) + "", tx + 5, ty + 210);
        }
        if (!Double.isNaN(accuracy)) {
            g.drawString("Accuracy:" + FactoryUtils.formatDouble(accuracy) + "", tx + 5, ty + 225);
        } else {
            g.drawString("Accuracy:" + FactoryUtils.formatDouble(1) + "", tx + 5, ty + 225);
        }

        g.setColor(Color.green);
        for (int i = 0; i < errorPoints.size(); i++) {
            g.drawOval(50 + i / 10, getHeight() - (100 + (int) Math.round((errorPoints.get(i) * 1000))), 3, 3);
        }
        g.setColor(Color.orange);
        for (int i = 0; i < validationErrorPoints.size(); i++) {
            g.drawOval(50 + i / 10, getHeight() - (100 + (int) Math.round((validationErrorPoints.get(i) * 1000))), 3, 3);
        }
        int px = getWidth() - 200;
        int py = 100;

        g.setColor(Color.black);
        g.drawString("Training Error", px, 45);
        g.drawString(last_tr_error + "", px + 60, 65);
        g.setColor(Color.green);
        g.fillRect(px, py - 50, 50, 20);

        g.setColor(Color.black);
        g.drawString("Validation Error", px, py - 5);
        g.drawString(last_validation_error + "", px + 60, py + 15);
        g.setColor(Color.orange);
        g.fillRect(px, py, 50, 20);

        g.setColor(Color.black);
        g.drawString("Test Error", px, py + 45);
        g.drawString(last_test_error + "", px + 60, py + 65);
        g.setColor(Color.blue);
        g.fillRect(px, py + 50, 50, 20);

        g.drawString("WorstCase Error", px, py + 95);
        g.drawString(worst_case + "", px + 100, py + 95);
        g.drawString("Overall Efficiency", px, py + 115);
        g.drawString(efficiency + " %", px + 100, py + 115);

        g.setColor(Color.blue);
        for (int i = 0; i < testerrorPoints.size(); i++) {
            g.drawOval(50 + i / 5, getHeight() - (100 + (int) Math.round((testerrorPoints.get(i) * 100))), 3, 3);
        }
        g.setColor(Color.red);
        int w = this.getWidth();
        int h = this.getHeight();
        g.drawRect(0, 0, w - 1, h - 1);
        g.drawRect(1, 1, w - 3, h - 3);
    }
}
