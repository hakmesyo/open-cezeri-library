/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.utils;

import au.com.bytecode.opencsv.CSVReader;
import cezeri.types.TDeviceState;
import cezeri.types.TWord;
import cezeri.types.TLearningType;
import cezeri.gui.FramePlot;
import cezeri.matrix.CMatrix;
import cezeri.matrix.CPoint;
import cezeri.matrix.FactoryMatrix;
import cezeri.reader.ReaderCSV;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigInteger;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.w3c.dom.Element;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

/**
 *
 * @author venap3
 */
public final class FactoryUtils {

    /**
     * deserialize to Object from given file. We use the general Object so as
     * that it can work for any Java Class.
     */
    public static Object deserialize(String fileName) throws IOException,
            ClassNotFoundException {
        FileInputStream fis = new FileInputStream(fileName);
        BufferedInputStream bis = new BufferedInputStream(fis);
        ObjectInputStream ois = new ObjectInputStream(bis);
        Object obj = ois.readObject();
        ois.close();
        return obj;
    }

    /**
     * serialize the given object and save it to given file
     */
    public static void serialize(Object obj, String fileName)
            throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.close();
    }

//    private static String curr_file;
    public static void plot(CMatrix cm) {
        new FramePlot(cm).setVisible(true);
    }

    public static ArrayList<Point> shuffleList(ArrayList<Point> lst) {
        long seed = System.nanoTime();
        Collections.shuffle(lst, new Random(seed));
        return lst;
    }
    // This method returns a buffered image with the contents of an image

//    public static Object readFromXML() {
//        Object out = null;
//        try {
//            JFileChooser chooser = new JFileChooser();
////            chooser.setCurrentDirectory(new java.io.File("C:\\Plotter\\GCODE"));
//            chooser.setDialogTitle("xml from file");
//            chooser.setSize(new java.awt.Dimension(45, 37)); // Generated
//            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
//                File file = chooser.getSelectedFile();
//                String modelFile = file.getAbsolutePath();
//                XStream xx = new XStream(new DomDriver());
//                FileReader fileReader;
//                fileReader = new FileReader(modelFile);
//                out = xx.fromXML(fileReader);
//                FactoryUtils.showMessage("B.loaded successfully");
//            }
//        } catch (FileNotFoundException e) {
//        }
//        return out;
//    }
//
//    public static void writeToXML(Object obj) {
//        JFileChooser chooser = new JFileChooser();
////        chooser.setCurrentDirectory(new java.io.File("C:\\Plotter\\GCODE"));
//        chooser.setDialogTitle("save xml as a file");
//        chooser.setSize(new java.awt.Dimension(45, 37)); // Generated
//        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
//            File file = chooser.getSelectedFile();
//            String fName = file.getAbsolutePath();
//            writeToFile(fName, toXML(obj));
//            showMessage("B. Object serialized to " + fName);
//        }
//    }
//
//    public static String toXML(Object obj) {
//        return new XStream().toXML(obj);
//    }
//
//    public static Object fromXML(String str) {
//        return new XStream(new DomDriver()).fromXML(str);
//    }
//
//    public static Object fromXMLFile() {
//        return readFromXML();
//    }
    public static File getFileFromChooserForPNG() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("save panel as a png file");
        chooser.setSize(new java.awt.Dimension(45, 37)); // Generated
        chooser.setFileFilter(new FileNameExtensionFilter("png", "png"));
        File file = new File("C:\\deneme.png");
        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
            if (file.getName().indexOf(".png") == -1) {
                File file2 = new File(file.getParent() + "\\" + file.getName() + ".png");
                file = file2;
            }
            return file;
        }
        return null;
    }

    public static File getFileFromChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("save as file");
        chooser.setSize(new java.awt.Dimension(45, 37)); // Generated
//        chooser.setFileFilter(new FileNameExtensionFilter("png", "png"));
        File file = null;
        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
            return file;
        }
        return file;
    }

    public static File getFileFromChooser(String folderPath) {
        JFileChooser chooser = new JFileChooser(folderPath);
        chooser.setDialogTitle("save as file");
        chooser.setSize(new java.awt.Dimension(45, 37)); // Generated
//        chooser.setFileFilter(new FileNameExtensionFilter("png", "png"));
        File file = null;
        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
            return file;
        }
        return file;
    }

    public static File getFileFromChooserSave() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("save as file");
        chooser.setSize(new java.awt.Dimension(45, 37)); // Generated
//        chooser.setFileFilter(new FileNameExtensionFilter("png", "png"));
        File file = null;
        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
            return file;
        }
        return file;
    }

    public static File getFileFromChooserSave(String folderPath) {
        JFileChooser chooser = new JFileChooser(folderPath);
        chooser.setDialogTitle("save as file");
        chooser.setSize(new java.awt.Dimension(45, 37)); // Generated
//        chooser.setFileFilter(new FileNameExtensionFilter("png", "png"));
        File file = null;
        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
            return file;
        }
        return file;
    }

    public static File getFileFromChooserLoad() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("select file");
        chooser.setSize(new java.awt.Dimension(45, 37)); // Generated
//        chooser.setFileFilter(new FileNameExtensionFilter("png", "png"));
        File file = null;
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
            return file;
        }
        return file;
    }

    public static File getFileFromChooserLoad(String folderPath) {
        JFileChooser chooser = new JFileChooser(folderPath);
        chooser.setDialogTitle("select file");
        chooser.setSize(new java.awt.Dimension(45, 37)); // Generated
//        chooser.setFileFilter(new FileNameExtensionFilter("png", "png"));
        File file = null;
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
            return file;
        }
        return file;
    }

    public static File browseDirectory() {
        return browseDirectory(getDefaultDirectory());
    }

    public static File browseDirectory(String path) {
        JFileChooser chooser = new JFileChooser(path);
        chooser.setDialogTitle("Browse Directory");
        chooser.setSize(new java.awt.Dimension(45, 37)); // Generated
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        File file = null;
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
            System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
            System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
        } else {
            System.out.println("No Selection ");
        }
        return file;
    }

    public static File browseFile() {
        return getFileFromChooserLoad();
    }

    public static File browseFile(String path) {
        return getFileFromChooserLoad(path);
    }

    public static void writeOnFile(String file_name, String row) {
        File outFile = new File(file_name);
        FileWriter out;
        try {
            out = new FileWriter(outFile, true);
            out.write(row);
            out.close();
        } catch (IOException e) {
        }
    }

    public static void writeOnFile(String row) {
        writeOnFile(getFileFromChooser().getAbsolutePath(), row);
    }

    public static void writeToFile(String row) {
        writeToFile(getFileFromChooserSave(getDefaultDirectory()).getAbsolutePath(), row);
    }

    public static void writeToFile(String row, boolean currentDir) {
        if (currentDir) {
            writeToFile(getFileFromChooser(getDefaultDirectory()).getAbsolutePath(), row);
        } else {
            writeToFile(getFileFromChooser().getAbsolutePath(), row);
        }

    }

    public static void writeToFile(String path, String row) {
        Writer out;
        try {
            try {
                out = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(path), "UTF-8"));
                try {
                    try {
                        out.write(row);
                    } catch (IOException ex) {
                        Logger.getLogger(FactoryUtils.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } finally {
                    try {
                        out.close();
                    } catch (IOException ex) {
                        Logger.getLogger(FactoryUtils.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FactoryUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(FactoryUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    public static void writeToFile(String file_name, String row) {
//        File outFile = new File(file_name);
//        FileWriter out;
//        if (outFile.exists()) {
////            showMessage(file_name + " isminde bir dosya zaten var üzerine yazılacak");
//        }
//        try {
//            out = new FileWriter(outFile, false);
//            out.write(row);
//            out.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    public static void writeToFile(String file_name, double[][] d) {
        File outFile = new File(file_name);
        FileWriter out;
        if (outFile.exists()) {
            showMessage(file_name + " isminde bir dosya zaten var üzerine yazılacak");
        }
        try {
            out = new FileWriter(outFile, false);
            String row = "";
            for (int i = 0; i < d.length; i++) {
                String r = "";
                for (int j = 0; j < d[0].length; j++) {
                    r += d[i][j] + ",";
                }
                r = r.substring(0, r.length() - 1);
                row += r + "\n";

            }
            out.write(row);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeToFile(String file_name, String[][] d) {
        File outFile = new File(file_name);
        FileWriter out;
        if (outFile.exists()) {
            showMessage(file_name + " isminde bir dosya zaten var üzerine yazılacak");
        }
        try {
            out = new FileWriter(outFile, false);
            String row = "";
            for (int i = 0; i < d.length; i++) {
                String r = "";
                for (int j = 0; j < d[0].length; j++) {
                    r += d[i][j] + ",";
                }
                r = r.substring(0, r.length() - 1);
                row += r + "\n";

            }
            out.write(row);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeToFile(String file_name, int[][] d) {
        File outFile = new File(file_name);
        FileWriter out;
        if (outFile.exists()) {
            showMessage(file_name + " isminde bir dosya zaten var üzerine yazılacak");
        }
        try {
            out = new FileWriter(outFile, false);
            String row = "";
            for (int i = 0; i < d.length; i++) {
                String r = "";
                for (int j = 0; j < d[0].length; j++) {
                    r += d[i][j] + ",";
                }
                r = r.substring(0, r.length() - 1);
                row += r + "\n";

            }
            out.write(row);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * write data matrix as Weka ARFF file format note that feature names are
     * given f_1..f_n format
     *
     * @param file_path : path with desired file_name
     * @param d : data matrix
     * @param learning_type : TLearningType.REGRESSION or
     * TLearningType.CLASSIFICATION
     */
    public static void writeToArffFile(String file_path, double[][] d, int learning_type) {
        String s = "";
        String relationName = getFileName(file_path);
        s += "@relation '" + relationName + "'\n";
        int n = d[0].length;
        for (int i = 1; i < n; i++) {
            s += "@attribute 'f_" + i + "' real\n";
        }
        if (learning_type == TLearningType.REGRESSION) {
            s += "@attribute 'output' real\n";
        }
        if (learning_type == TLearningType.CLASSIFICATION) {
            double[][] tr = transpose(d);
            double[] distinct = getDistinctValues(tr[tr.length - 1]);
            s += "@attribute 'output' {";
            for (int i = 0; i < distinct.length; i++) {
                s += distinct[i] + ",";
            }
            s = s.substring(0, s.length() - 1);
            s += "}\n";
        }
        s += "@data\n";
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                s += d[i][j] + ",";
            }
            s = s.substring(0, s.length() - 1);
            s += "\n";
//            writeToFile(file_path, s);
//            s = "";
        }
        writeToFile(file_path, s);
    }

    /**
     * write data matrix as Weka ARFF file format note that feature names are
     * given f_1..f_n format
     *
     * @param file_path : path with desired file_name
     * @param learning_type : TLearningType.REGRESSION or
     * TLearningType.CLASSIFICATION
     */
    public static void writeToArffFile(String file_path, String lines, int learning_type) {
        String[] ss = lines.split("\n");
        String[] sss = ss[0].split(",");
        double[] dd = new double[ss.length];
        for (int i = 0; i < ss.length; i++) {
            sss = ss[i].split(",");
            dd[i] = Double.parseDouble(sss[sss.length - 1]);
        }
        String s = "";
        String relationName = getFileName(file_path);
        s += "@relation '" + relationName + "'\n";
        int n = sss.length;
        for (int i = 1; i < n; i++) {
            s += "@attribute 'f_" + i + "' real\n";
        }
        if (learning_type == TLearningType.REGRESSION) {
            s += "@attribute 'output' real\n";
        }
        if (learning_type == TLearningType.CLASSIFICATION) {
            double[] distinct = getDistinctValues(dd);
            s += "@attribute 'output' {";
            for (int i = 0; i < distinct.length; i++) {
                s += (int) distinct[i] + ",";
            }
            s = s.substring(0, s.length() - 1);
            s += "}\n";
        }
        s += "@data\n" + lines;
        writeToFile(file_path, s);
    }

    /**
     * write data matrix as Weka ARFF file format note that feature names are
     * given f_1..f_n format
     *
     * @param file_path : path with desired file_name
     * @param d : data matrix
     * @param learning_type : TLearningType.REGRESSION or
     * TLearningType.CLASSIFICATION
     */
    public static void writeOnArffFile(String file_path, double[][] d, int learning_type) {
        String s = "";
        String relationName = getFileName(file_path);
        s += "@relation '" + relationName + "'\n";
        int n = d[0].length;
        for (int i = 1; i < n; i++) {
            s += "@attribute 'f_" + i + "' real\n";
        }
        if (learning_type == TLearningType.REGRESSION) {
            s += "@attribute 'output' real\n";
        }
        if (learning_type == TLearningType.CLASSIFICATION) {
            double[][] tr = transpose(d);
            double[] distinct = getDistinctValues(tr[tr.length - 1]);
            s += "@attribute 'output' {";
            for (int i = 0; i < distinct.length; i++) {
                s += distinct[i] + ",";
            }
            s = s.substring(0, s.length() - 1);
            s += "}\n";
        }
        s += "@data\n";
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                s += d[i][j] + ",";
            }
            s = s.substring(0, s.length() - 1);
            s += "\n";
            writeOnFile(file_path, s);
            s = "";
        }
//        writeToFile(file_path, s);
    }

    /**
     * write data matrix as Weka ARFF file format note that feature names are
     * given f_1..f_n format
     *
     * @param file_path : path with desired file_name
     * @param features: names of the features
     * @param d : data matrix
     * @param learning_type : TLearningType.REGRESSION or
     * TLearningType.CLASSIFICATION
     */
    public static void writeToArffFile(String file_path, String[] features, double[][] d, int learning_type) {
        String s = "";
        String relationName = getFileName(file_path);
        s += "@relation '" + relationName + "'\n";
        int n = d[0].length;
        for (int i = 0; i < n - 1; i++) {
            s += "@attribute '" + features[i] + "' real\n";
        }
        if (learning_type == TLearningType.REGRESSION) {
            s += "@attribute 'output' real\n";
        }
        if (learning_type == TLearningType.CLASSIFICATION) {
            double[][] tr = transpose(d);
            double[] distinct = getDistinctValues(tr[tr.length - 1]);
            s += "@attribute 'output' {";
            for (int i = 0; i < distinct.length; i++) {
                s += distinct[i] + ",";
            }
            s = s.substring(0, s.length() - 1);
            s += "}\n";
        }
        s += "@data\n";
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                s += d[i][j] + ",";
            }
            s = s.substring(0, s.length() - 1);
            s += "\n";
        }
        writeToFile(file_path, s);
    }

    /**
     * get the distinct values from the array provided
     *
     * @param d searching array
     * @return distinct values array
     */
    public static double[] getDistinctValues(double[] d) {
        ArrayList<Double> lst = new ArrayList<>();
        for (int i = 0; i < d.length; i++) {
            if (!lst.contains(d[i])) {
                lst.add(d[i]);
            }
        }
        return toDoubleArray(lst);
    }

    /**
     * get the distinct values from the array provided
     *
     * @param d searching array
     * @return distinct values array
     */
    public static int[] getDistinctValues(int[] d) {
        ArrayList<Integer> lst = new ArrayList<>();
        for (int i = 0; i < d.length; i++) {
            if (!lst.contains(d[i])) {
                lst.add(d[i]);
            }
        }
        return toIntArray1D(lst);
    }

    public static String readFromFileUTF8(String file_name) {
        File file = new File(file_name);
        if (!file.exists()) {
            showMessage(file_name + " isminde bir dosya yok");
            return null;
        }
        String ret = "";
        Charset charset = StandardCharsets.UTF_8;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
            try {
                String s;
                while ((s = br.readLine()) != null) {
                    ret += s + "\n";
                }
                ret = ret.substring(0, ret.length() - 1);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FactoryUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    public static String readFile(String fileName) {
        String ret = "";
        File file = new File(fileName);
        if (!file.exists()) {
            showMessage(fileName + " isminde bir dosya yok");
            return ret;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String s;
            while ((s = br.readLine()) != null) {
                ret = ret + s + "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ret;
        }
        return ret;
    }

    /**
     * read matrix written as atext file with ; split token
     *
     * @return
     */
    public static double[][] readFromFile() {
        return readFromFile(getFileFromChooserLoad(getDefaultDirectory()).getAbsolutePath(), ";");
    }

    public static double[][] readFromFile(String token) {
        return readFromFile(getFileFromChooserLoad(getDefaultDirectory()).getAbsolutePath(), token);
    }

    public static double[][] readFromFile(String file_name, String token) {
        double[][] d = new double[1][1];
        ArrayList<double[]> lst = new ArrayList<>();
        File file = new File(file_name);
        if (!file.exists()) {
            showMessage(file_name + " isminde bir dosya yok");
            return d;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file_name))) {
            String s;
            while ((s = br.readLine()) != null) {
                if ((s.indexOf("@") != -1) || (s.indexOf("%") != -1)) {
                    continue;
                }
                double[] row = null;
                if (token.isEmpty()) {
                    row = new double[1];
                    row[0] = Double.parseDouble(s);
                } else {
                    String[] sd = s.split(token);
                    row = new double[sd.length];
                    for (int i = 0; i < sd.length; i++) {
//                        System.out.println("i = " + i+" val="+sd[i]);
                        row[i] = Double.parseDouble(sd[i]);
                    }
                }
                lst.add(row);

            }
        } catch (IOException e) {
            e.printStackTrace();
            return d;
        }
        return lst.toArray(d);
    }

    public static ReaderCSV readFromCSVFile(String file_name) {
        ReaderCSV csv = new ReaderCSV();
        List<String> columnNames=new ArrayList<>();
        List<String> classLabels=new ArrayList<>();
        double[][] d = new double[1][1];
        ArrayList<double[]> lst = new ArrayList<>();
        File file = new File(file_name);
        if (!file.exists()) {
            showMessage(file_name + " does not exist please check it");
            return null;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file_name))) {
            String s=br.readLine().replace("\"", "");
            columnNames=Arrays.asList(s.split(","));
            while ((s = br.readLine()) != null) {
                double[] row = null;
                String[] sd = s.split(",");
                row = new double[sd.length];
                for (int i = 0; i < sd.length-1; i++) {
                    row[i] = Double.parseDouble(sd[i]);
                }
                classLabels.add(sd[sd.length-1].replace("\"", ""));
                lst.add(row);

            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        csv.data=lst.toArray(d);
        csv.columnNames=columnNames;
        csv.classLabels=classLabels;
        return csv;
    }

    public static String[][] readFromFileAsString(String file_name, String token) {
        String[][] d = new String[1][1];
        ArrayList<String[]> lst = new ArrayList<>();
        File file = new File(file_name);
        if (!file.exists()) {
            showMessage(file_name + " isminde bir dosya yok");
            return d;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file_name))) {
            String s;
            while ((s = br.readLine()) != null) {
                if (s.indexOf("@") != -1) {
                    continue;
                }
                String[] row = null;
                if (token.isEmpty()) {
                    row = new String[1];
                    row[0] = s;
                } else {
                    String[] sd = s.split(token);
                    row = new String[sd.length];
                    for (int i = 0; i < sd.length; i++) {
                        row[i] = sd[i];
                    }
                }
                lst.add(row);

            }
        } catch (IOException e) {
            e.printStackTrace();
            return d;
        }
        return lst.toArray(d);
    }

    public static String[] readFromFileAsString1D(String file_name) {
        ArrayList<String> lst = new ArrayList<>();
        File file = new File(file_name);
        if (!file.exists()) {
            showMessage(file_name + " isminde bir dosya yok");
            return null;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file_name))) {
            String s;
            while ((s = br.readLine()) != null) {
                if (s.indexOf("@") != -1) {
                    continue;
                }
                lst.add(s);

            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        String[] d = {};
        return lst.toArray(d);
    }

    /**
     * Read sensor data from piezoelectric sensor file
     *
     * @param file_name
     * @param count
     * @return
     */
    public static double[][] readVibrationSensorDataFromFile(String file_name, int count) {
        double[][] d = new double[1][1];
        ArrayList<double[]> lst = new ArrayList<>();
        File file = new File(file_name);
        if (!file.exists()) {
            showMessage(file_name + " isminde bir dosya yok");
            return d;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file_name))) {
            String s;
            while ((s = br.readLine()) != null) {
                String[] sd = s.split("\\.");
                if (sd.length == 2) {
                    double[] q = new double[count];
                    for (int i = 0; i < count; i++) {
                        s = br.readLine();
                        try {
                            q[i] = Double.parseDouble(s);
                        } catch (Exception e) {
                            int a = 1;
                        }
                    }
                    lst.add(q);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return d;
        }
        d = lst.toArray(d);
        d = transpose(d);
        return d;
    }

    public static void showMessage(String str) {
        JOptionPane.showMessageDialog(null, str);
    }

    public static String inputMessage(String str1, String str2) {
        return JOptionPane.showInputDialog(str1, str2);
    }

    public static String inputMessage(String str1) {
        return JOptionPane.showInputDialog(str1);
    }

    public static void yazln(String str) {
        System.out.println(str);
    }

    public static void yaz(String str) {
        System.out.print(str);
    }

    public static void yaz(double[] m) {
        yaz("[");
        for (int i = 0; i < m.length; i++) {
            yaz(m[i] + " ");
        }
        yaz("]\n");
    }

    public static void yaz(int[][] m) {
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                yaz(m[i][j] + " ");
            }
            yazln("");
        }
        yazln("");
    }

    private static double[][] getDoubleMatrix(int[][] a) {
        double[][] d = new double[a.length][a[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                d[i][j] = (double) a[i][j];
            }
        }
        return d;
    }

    private static int[][] getIntMatrix(double[][] a) {
        int[][] d = new int[a.length][a[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                d[i][j] = (int) a[i][j];
            }
        }
        return d;
    }

    public static double[][] getSubMatrix(double[][] m, int p1x, int p1y, int p2x, int p2y) {
        int r = m.length;
        int c = m[0].length;
        if (p1x < 0 || p1y < 0 || p1x > p2x || p1y > p2y
                || p1x > r || p1y > c || p2x > r || p2y > c) {
            return m;
        } else {
            double[][] ret = new double[p2x - p1x + 1][p2y - p1y + 1];
            for (int i = p1x; i <= p2x; i++) {
                for (int j = p1y; j <= p2y; j++) {
                    ret[i - p1x][j - p1y] = m[i][j];
                }
            }
            return ret;
        }
    }

    public static int[][] getSubMatrix(int[][] m, CPoint p1, CPoint p2) {
        int r = m.length;
        int c = m[0].length;
        if (p1.row < 0 || p1.column < 0 || p1.row > p2.row || p1.column > p2.column
                || p1.row > r || p1.column > c || p2.row > r || p2.column > c) {
            return m;
        } else {
            int[][] ret = new int[p2.row - p1.row + 1][p2.column - p1.column + 1];
            for (int i = p1.row; i <= p2.row; i++) {
                for (int j = p1.column; j <= p2.column; j++) {
                    ret[i - p1.row][j - p1.column] = m[i][j];
                }
            }
            return ret;
        }
    }

    /**
     * Crop the matrix at the center point with the specified length from the
     * center as in the case of circle radious
     *
     * @param m:original matrix
     * @param cp:center point
     * @param r:window size
     * @return cropped matrix
     */
    public static double[][] crop(double[][] m, CPoint cp, int r) {
        int c1 = cp.column - r;
        int c2 = cp.column + r;
        int r1 = cp.row - r;
        int r2 = cp.row + r;
        if (c1 < 0 || r1 < 0 || c2 > m[0].length - 1 || r2 > m.length - 1) {
//            System.out.println("cropped matrix is outside from the matrix");
            return m;
        }
        return getSubMatrix(m, new CPoint(r1, c1), new CPoint(r2, c2));
    }

    /**
     * Crop the matrix from p1 to p2 position which are actually identical to
     * top-left and right-bottom positions
     *
     * @param m:original matrix
     * @param p1:top-left
     * @param p2:bottom-right
     * @return cropped matrix
     */
    public static double[][] crop(double[][] m, CPoint p1, CPoint p2) {
        return getSubMatrix(m, p1, p2);
    }

    /**
     * Crop the matrix from p1 to p2 position which are actually identical to
     * top-left and right-bottom positions
     *
     * @param m:original matrix
     * @param p1:top-left
     * @param p2:bottom-right
     * @return cropped matrix
     */
    public static double[][] getSubMatrix(double[][] m, CPoint p1, CPoint p2) {
        int r = m.length;
        int c = m[0].length;
        if (p1.row < 0 || p1.column < 0 || p1.row > p2.row || p1.column > p2.column
                || p1.row > r || p1.column > c || p2.row > r || p2.column > c) {
            return m;
        } else {
            double[][] ret = new double[p2.row - p1.row + 1][p2.column - p1.column + 1];
            for (int i = p1.row; i < p2.row; i++) {
                for (int j = p1.column; j < p2.column; j++) {
                    ret[i - p1.row][j - p1.column] = m[i][j];
                }
            }
            return ret;
        }
    }

    public static double getSum(double[] d) {
        double ret = 0;
        for (int i = 0; i < d.length; i++) {
            ret += d[i];
        }
        return ret;
    }

    public static int getSum(int[] d) {
        int ret = 0;
        for (int i = 0; i < d.length; i++) {
            ret += d[i];
        }
        return ret;
    }

    public static float getSum(float[] d) {
        float ret = 0;
        for (int i = 0; i < d.length; i++) {
            ret += d[i];
        }
        return ret;
    }

    public static double getSum(double[][] d) {
        double ret = 0;
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                ret += d[i][j];
            }
        }
        return ret;
    }

    public static int getSum(int[][] d) {
        int ret = 0;
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                ret += d[i][j];
            }
        }
        return ret;
    }

    public static float getSum(float[][] d) {
        float ret = 0;
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                ret += d[i][j];
            }
        }
        return ret;
    }

    public static double getPixelCount(int[][] m) {
        if (m == null) {
            return 0.0;
        }
        double ret = 0.0;
        double toplam = 0;
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                if (m[i][j] != 0) {
                    toplam++;
                }
            }
        }
        return toplam;
    }

    public static ArrayList<Point> getROIPos(ArrayList lst, int threshold) {
        ArrayList<Point> ret = new ArrayList<>();
        ArrayList posList = new ArrayList();
        for (Object obj : lst) {
            ArrayList<Point> ps = (ArrayList<Point>) obj;
            ArrayList<Point> pos = new ArrayList<>();
            int delta;
            ArrayList<Integer> v = new ArrayList<>();
            for (int i = 0; i < ps.size() - 1; i++) {
                delta = Math.abs(ps.get(i).y - ps.get(i + 1).y);
                if (delta != 0 && delta < threshold) {
                    v.add(ps.get(i).y);
                } else {
                    int tp = 0;
                    for (Integer nt : v) {
                        tp += nt;
                    }
                    Point p = new Point();
                    p.x = ps.get(i).x;
                    p.y = tp / v.size();
                    pos.add(p);
                    v.clear();
                }
            }
            posList.add(pos);
        }
        return ret;
    }

    /**
     * Project matrix on X axis or row axis
     *
     * @param m
     * @return
     */
    public static int[] getProjectedMatrixOnX(int[][] m) {
        int[] d = new int[m[0].length];
        for (int j = 0; j < d.length; j++) {
            for (int i = 0; i < m.length; i++) {
                d[j] += m[i][j];
            }
        }
        return d;
    }

    /**
     * Project matrix on X axis or row axis
     *
     * @param m
     * @return
     */
    public static int[] getProjectedMatrixOnX(double[][] m) {
        int[] d = new int[m[0].length];
        for (int j = 0; j < m[0].length; j++) {
            for (int i = 0; i < m.length; i++) {
                d[j] += m[i][j];
            }
        }
        return d;
    }

    /**
     * Project matrix on Y axis or column axis
     *
     * @param m
     * @return
     */
    public static int[] getProjectedMatrixOnY(int[][] m) {
        int[] d = new int[m.length];
        for (int j = 0; j < m.length; j++) {
            for (int i = 0; i < m[0].length; i++) {
                d[j] += m[j][i];
            }
        }
        return d;
    }

    /**
     * Project matrix on Y axis or Column axis
     *
     * @param m
     * @return
     */
    public static int[] getProjectedMatrixOnY(double[][] m) {
        int[] d = new int[m.length];
        for (int j = 0; j < m.length; j++) {
            for (int i = 0; i < m[0].length; i++) {
                d[j] += m[j][i];
            }
        }
        return d;
    }

    public static TWord[] getPoints(int[] d) {
        ArrayList<Integer> lst = new ArrayList<>();
        ArrayList<Integer> lstW = new ArrayList<>();
        //binarize image
        for (int i = 0; i < d.length; i++) {
            if (d[i] != 0) {
                d[i] = 255;
            }
        }

        //1D chain algorithm threshold is 10 which means pixel is assigned at most 10 aparture or gap
        @SuppressWarnings("UseOfObsoleteCollectionType")
        Vector<Integer> v = new Vector<>();
        int gap = 0;
        for (int i = 0; i < d.length; i++) {
            if (d[i] != 0) {
                v.add(i);
                gap = 0;
            } else {
                gap++;
                if (gap < 10 && i < d.length - 1) {
                    continue;
                } else {
                    if (!v.isEmpty() && v.size() > 10 && v.size() < 80) {
                        int mid = (v.firstElement() + v.lastElement()) / 2;
                        int w = Math.abs(v.lastElement() - v.firstElement());
                        lst.add(mid);
                        lstW.add(w);
                        v.removeAllElements();
                        gap = 0;
                    } else {
                        v.removeAllElements();
                        gap = 0;
                        continue;
                    }
                }
            }
        }
        TWord[] ret = new TWord[lst.size()];
        for (int i = 0; i < lst.size(); i++) {
            ret[i] = new TWord();
            ret[i].centerPos = lst.get(i);
            ret[i].width = lstW.get(i);
        }
        return ret;
    }

    public static String reverseString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            str += s.charAt(s.length() - 1 - i);
        }
        return str;
    }

    public static String getDefaultDirectory() {
        String workingDir = System.getProperty("user.dir");
        return workingDir;
    }

    public static String getWorkingDirectory() {
        return getDefaultDirectory();
    }

    public static double[] toDoubleArray1D(int[] m) {
        double[] ret = new double[m.length];
        for (int i = 0; i < m.length; i++) {
            ret[i] = m[i] * 1.0;
        }
        return ret;
    }

    public static double[] toDoubleArray1D(byte[] m) {
        double[] ret = new double[m.length];
        for (int i = 0; i < m.length; i++) {
            ret[i] = m[i] * 1.0;
        }
        return ret;
    }

    public static double[] toDoubleArray1D(float[] m) {
        double[] ret = new double[m.length];
        for (int i = 0; i < m.length; i++) {
            ret[i] = m[i];
        }
        return ret;
    }

    public static int[] toIntArray1D(byte[] m) {
        int[] ret = new int[m.length];
        for (int i = 0; i < m.length; i++) {
            ret[i] = (((int) m[i]) & 0xFF);  //m[i]&0xFF;
        }
        return ret;
    }

    public static int[] toIntArray1D(short[] m) {
        int[] ret = new int[m.length];
        for (int i = 0; i < m.length; i++) {
            ret[i] = (short) m[i];
        }
        return ret;
    }

    public static int[] toIntArray1D(char[] m) {
        int[] ret = new int[m.length];
        for (int i = 0; i < m.length; i++) {
            ret[i] = m[i];
        }
        return ret;
    }

    public static double[] toDoubleArray1D(double[] m) {
        double[] ret = new double[m.length];
        for (int i = 0; i < m.length; i++) {
            ret[i] = m[i];
        }
        return ret;
    }

    public static double[][] toDoubleArray2D(int[][] m) {
        double[][] ret = new double[m.length][m[0].length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                ret[i][j] = m[i][j] * 1.0;
            }
        }
        return ret;
    }

    public static double[] toDoubleArray1D(double[][] m) {
        double[] ret = new double[m.length * m[0].length];
        int k = 0;
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                ret[k++] = m[i][j];
            }
        }
        return ret;
    }

    public static Object[] toDoubleArray1D(Object[][] m) {
        Object[] ret = new Object[m.length * m[0].length];
        int k = 0;
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                ret[k++] = m[i][j];
            }
        }
        return ret;
    }

    public static double[][] toDoubleArray2D(byte[][] m) {
        double[][] ret = new double[m.length][m[0].length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                ret[i][j] = m[i][j] * 1.0;
            }
        }
        return ret;
    }

    public static double[][] toDoubleArray2D(float[][] m) {
        double[][] ret = new double[m.length][m[0].length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                ret[i][j] = m[i][j] * 1.0;
            }
        }
        return ret;
    }

    public static double[][][] toDoubleArray3D(int[][][] m) {
        double[][][] ret = new double[m.length][m[0].length][m[0][0].length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                for (int k = 0; k < m[0][0].length; k++) {
                    ret[i][j][k] = m[i][j][k] * 1.0;
                }
            }
        }
        return ret;
    }

    /**
     * encrypt some value with salt value by means of xor knowing that double
     * xor with salt yields original value
     *
     * @param original
     * @param salt
     * @return
     */
    public static int encrypt(int original, int salt) {
        return original ^ salt;
    }

    /**
     * decrypt encrypted value with salt value by means of xor knowing that
     * double xor with salt yields original value
     *
     * @param encrypted
     * @param salt
     * @return
     */
    public static int decrypt(int encrypted, int salt) {
        return encrypted ^ salt;
    }

    /**
     *
     * @param m
     * @return
     */
    public static double[] toDoubleArray(Vector m) {
        double[] ret = new double[m.size()];
        for (int i = 0; i < m.size(); i++) {
            ret[i] = (double) m.get(i);
        }
        return ret;
    }

    public static double[] toDoubleArray(ArrayList m) {
        double[] ret = new double[m.size()];
        for (int i = 0; i < m.size(); i++) {
            ret[i] = (double) m.get(i);
        }
        return ret;
    }

    public static int[] toIntArray1D(double[] m) {
        int[] ret = new int[m.length];
        for (int i = 0; i < m.length; i++) {
            ret[i] = (int) Math.round(m[i]);
        }
        return ret;
    }

    public static int[][] toIntArray2D(double[][] m) {
        int[][] ret = new int[m.length][m[0].length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                ret[i][j] = (int) Math.round(m[i][j]);
            }
        }
        return ret;
    }

    public static short[][] toShortArray2D(double[][] m) {
        short[][] ret = new short[m.length][m[0].length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                ret[i][j] = (short) Math.round(m[i][j]);
            }
        }
        return ret;
    }

    public static byte[][] toByteArray2D(double[][] m) {
        byte[][] ret = new byte[m.length][m[0].length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                ret[i][j] = (byte) Math.round(m[i][j]);
            }
        }
        return ret;
    }

    public static float[][] toFloatArray2D(double[][] m) {
        float[][] ret = new float[m.length][m[0].length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                ret[i][j] = (float) Math.round(m[i][j]);
            }
        }
        return ret;
    }

    public static long[][] toLongArray2D(double[][] m) {
        long[][] ret = new long[m.length][m[0].length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                ret[i][j] = (long) Math.round(m[i][j]);
            }
        }
        return ret;
    }

    public static String[][] toStringArray2D(double[][] m) {
        String[][] ret = new String[m.length][m[0].length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
//                ret[i][j] = String.valueOf(Math.round(m[i][j]));
                ret[i][j] = String.valueOf(formatDouble(m[i][j], 3));
            }
        }
        return ret;
    }

    public static int[] toIntArray1D(float[] m) {
        int[] ret = new int[m.length];
        for (int i = 0; i < m.length; i++) {
            ret[i] = (int) Math.round(m[i]);
        }
        return ret;
    }

    public static int[][] toIntArray2D(float[][] m) {
        int[][] ret = new int[m.length][m[0].length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                ret[i][j] = (int) Math.round(m[i][j]);
            }
        }
        return ret;
    }

    public static int[][] toIntArray2D(byte[][] m) {
        int[][] ret = new int[m.length][m[0].length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                ret[i][j] = (int) m[i][j];
            }
        }
        return ret;
    }

    public static int[] toIntArray1D(Vector m) {
        int[] ret = new int[m.size()];
        for (int i = 0; i < m.size(); i++) {
            ret[i] = (int) m.get(i);
        }
        return ret;
    }

    public static int[] toIntArray1D(ArrayList m) {
        int[] ret = new int[m.size()];
        for (int i = 0; i < m.size(); i++) {
            ret[i] = (int) m.get(i);
        }
        return ret;
    }

    public static float formatFloat(float num) {
        float q = 0f;
        try {
            DecimalFormat df = new DecimalFormat("#.###");
            q = Float.parseFloat(df.format(num).replace(",", "."));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return q;
    }

    public static String getFileExtension(File file) {
        String extension = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf('.') + 1);
        return extension;
    }

    public static String getFileExtension(String str) {
        String extension = str.substring(str.lastIndexOf('.') + 1);
        return extension;
    }

    /**
     * parse the file name from file path string
     *
     * @param str:file path contains filename
     * @return file name
     */
    public static String getFileNameFromPath(String str) {
        if (str == null || str.equals("") || str.isEmpty()) {
            return null;
        }
        Path p = Paths.get(str);
        String fileName = p.getFileName().toString();
        return fileName;
    }

    /**
     * parse the file name from composite file structure/name
     *
     * @param str:file path contains filename
     * @return file name
     */
    public static String getFileName(String str) {
        String extension = str.substring(str.lastIndexOf('.') + 1);
        String fileName = getFileNameFromPath(str);
        if (extension.equals(fileName)) {
            return fileName;
        } else {
            String ret = fileName.substring(0, fileName.lastIndexOf(extension) - 1);
            return ret;
        }
    }

    /**
     * b.try to format double number with 3 digit precision by default
     *
     * @param num
     * @return
     */
    public static double[] formatDouble(double[] num) {
        double[] q = new double[num.length];
        for (int i = 0; i < num.length; i++) {
            q[i] = formatDouble(num[i]);
        }
        return q;
    }

    /**
     * b.try to format double number with 3 digit precision by default
     *
     * @param num
     * @return
     */
    public static double[][] formatDouble(double[][] num) {
        double[][] q = new double[num.length][num[0].length];
        for (int i = 0; i < num.length; i++) {
            for (int j = 0; j < num[0].length; j++) {
                q[i][j] = formatDouble(num[i][j]);
            }
        }
        return q;
    }
    
    /**
     * b.try to format double number with precision number
     *
     * @param num
     * @return
     */
    public static double[][] formatDouble(double[][] num,int precision) {
        double[][] q = new double[num.length][num[0].length];
        for (int i = 0; i < num.length; i++) {
            for (int j = 0; j < num[0].length; j++) {
                q[i][j] = formatDouble(num[i][j],precision);
            }
        }
        return q;
    }

    /**
     * b.try to format double number with n digit precision by default
     *
     * @param num
     * @return
     */
    public static double[] formatDouble(double[] num, int n) {
        double[] q = new double[num.length];
        for (int i = 0; i < num.length; i++) {
            q[i] = formatDouble(num[i], n);
        }
        return q;
    }

    /**
     * b.try to format double number with 3 digit precision by default
     *
     * @param num
     * @return
     */
    public static double formatDouble(double num) {
        double q = 0;
        try {
            DecimalFormat df = new DecimalFormat("#.000");
            q = Double.parseDouble(df.format(num).replace(",", "."));
        } catch (Exception e) {
//            e.printStackTrace();
            return -10000000000000.0;
        }
        return q;
    }

    public static String formatDoubleAsString(double num, int n) {
        String ret = String.format("%." + n + "f", num).replace(',', '.');
        if (ret.length() > 5) {
            ret = String.format("%5.2e", Double.parseDouble(ret));
        }
        return ret;
    }

    public static double formatDouble(double num, int n) {
        double q = 0;
        try {
            DecimalFormat df = null;
            switch (n) {
                case 0:
                    return Math.floor(num);
                case 1:
                    df = new DecimalFormat("#.0");
                    break;
                case 2:
                    df = new DecimalFormat("#.00");
                    break;
                case 3:
                    df = new DecimalFormat("#.000");
                    break;
                case 4:
                    df = new DecimalFormat("#.0000");
                    break;
                case 5:
                    df = new DecimalFormat("#.00000");
                    break;
                default:
                    df = new DecimalFormat("#.000");
                    break;

            }
//            DecimalFormat df = new DecimalFormat("#.###");
            q = Double.parseDouble(df.format(num).replace(",", "."));
        } catch (Exception e) {
//            e.printStackTrace();
            return 0;
        }
        return q;
    }

    /*
    public static float[] clone(float[] d) {
        float[] ret = new float[d.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = d[i];
        }
        return ret;
    }

    public static float[][] clone(float[][] d) {
        float[][] ret = new float[d.length][d[0].length];
        for (int i = 0; i < ret.length; i++) {
            for (int j = 0; j < ret[0].length; j++) {
                ret[i][j] = d[i][j];
            }
        }
        return ret;
    }

    public static double[] clone(double[] d) {
        double[] ret = new double[d.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = d[i];
        }
        return ret;
    }

    public static double[][] clone(double[][] d) {
        double[][] ret = new double[d.length][d[0].length];
        for (int i = 0; i < ret.length; i++) {
            for (int j = 0; j < d[i].length; j++) {
                ret[i][j] = d[i][j];
            }
        }
        return ret;
    }

    public static double[][][] clone(double[][][] d) {
        if (d == null) {
            return null;
        }
        double[][][] ret = new double[d.length][d[0].length][d[0][0].length];
        for (int i = 0; i < ret.length; i++) {
            for (int j = 0; j < ret[0].length; j++) {
                for (int k = 0; k < ret[0][0].length; k++) {
                    ret[i][j][k] = d[i][j][k];
                }
            }
        }
        return ret;
    }

    public static int[] clone(int[] d) {
        int[] ret = new int[d.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = d[i];
        }
        return ret;
    }

    public static String[] clone(String[] d) {
        String[] ret = new String[d.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = d[i];
        }
        return ret;
    }

    public static int[][] clone(int[][] d) {
        int[][] ret = new int[d.length][d[0].length];
        for (int i = 0; i < ret.length; i++) {
            for (int j = 0; j < ret[0].length; j++) {
                ret[i][j] = d[i][j];
            }
        }
        return ret;
    }

    public static byte[][] clone(byte[][] d) {
        byte[][] ret = new byte[d.length][d[0].length];
        for (int i = 0; i < ret.length; i++) {
            for (int j = 0; j < ret[0].length; j++) {
                ret[i][j] = d[i][j];
            }
        }
        return ret;
    }
     */
    public static int[] sortArrayAndReturnIndex(double[] p, String t) {
        int[] ret = new int[p.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = i;
        }
        if (t.equals("desc")) {
            //buyukten kucuge sirala
            double temp = p[0];
            int q = 0;
            for (int i = 0; i < p.length; i++) {
                for (int j = i; j < p.length; j++) {
                    if (p[j] > p[i]) {
                        temp = p[i];
                        p[i] = p[j];
                        p[j] = temp;

                        q = ret[i];
                        ret[i] = ret[j];
                        ret[j] = q;
                    }
                }
            }
        }
        if (t.equals("asc")) {
            //kucukten buyuge sirala
            double temp = 0;
            int q = 0;
            for (int i = 0; i < p.length; i++) {
                for (int j = i; j < p.length; j++) {
                    if (p[j] < p[i]) {
                        temp = p[i];
                        p[i] = p[j];
                        p[j] = temp;

                        q = ret[i];
                        ret[i] = ret[j];
                        ret[j] = q;
                    }
                }
            }
        }
        return ret;
    }

    public static String convertArrayToString(double[] p) {
        String s = "";
        for (int i = 0; i < p.length; i++) {
            s += p[i] + ";";
        }
        return s;
    }

    public static String toString(double[] p, String token) {
        String s = "";
        for (int i = 0; i < p.length; i++) {
            s += p[i] + token;
        }
        return s;
    }

    public static String toString(float[] p, String token) {
        String s = "";
        for (int i = 0; i < p.length; i++) {
            s += p[i] + token;
        }
        return s;
    }

    public static String toString(long[] p, String token) {
        String s = "";
        for (int i = 0; i < p.length; i++) {
            s += p[i] + token;
        }
        return s;
    }

    public static String toString(int[] p, String token) {
        String s = "";
        for (int i = 0; i < p.length; i++) {
            s += p[i] + token;
        }
        return s;
    }

    public static String toString(short[] p, String token) {
        String s = "";
        for (int i = 0; i < p.length; i++) {
            s += p[i] + token;
        }
        return s;
    }

    public static String toString(byte[] p, String token) {
        String s = "";
        for (int i = 0; i < p.length; i++) {
            s += p[i] + token;
        }
        return s;
    }

    public static String toString(char[] p, String token) {
        String s = "";
        for (int i = 0; i < p.length; i++) {
            s += p[i] + token;
        }
        return s;
    }

    public static String toString(boolean[] p, String token) {
        String s = "";
        for (int i = 0; i < p.length; i++) {
            s += p[i] + token;
        }
        return s;
    }

    public static String toString(String[] p, String token) {
        String s = "";
        for (int i = 0; i < p.length; i++) {
            s += p[i] + token;
        }
        return s;
    }

    public static String convertArrayToString(int[] p) {
        String s = "";
        for (int i = 0; i < p.length; i++) {
            s += p[i] + ";";
        }
        return s;
    }

    public static boolean isExistInTheList(int[] d, Vector<int[]> list) {
        int[] temp = null;
        boolean flag = false;
        for (int i = 0; i < list.size(); i++) {
            temp = list.get(i);
            flag = true;
            for (int j = 0; j < d.length; j++) {
                if (!containsArray(d[j], temp)) {
                    flag = false;
                }
            }
            if (flag) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsArray(int i, int[] temp) {
        for (int j = 0; j < temp.length; j++) {
            if (i == temp[j]) {
                return true;
            }
        }
        return false;
    }

    public static String getFolderPath(String path) {
        String[] s = path.split("\\\\");
        String ret = "";
        if (s.length == 1) {
            return s[0];
        }
        for (int i = 0; i < s.length - 1; i++) {
            ret += s[i] + "\\";
        }
        return ret;
    }

    public static int[] sortArray(int[] a) {
        int[] b = a.clone();
        int t = 0;
        for (int i = 0; i < a.length; i++) {
            for (int j = i; j < a.length; j++) {
                if (b[i] > b[j]) {
                    t = b[i];
                    b[i] = b[j];
                    b[j] = t;
                }
            }
        }
        return b;
    }

    public static double[] sortArrayAscend(double[] a) {
        Arrays.sort(a);
        return a;
    }

    public static double[] sortArrayDescend(double[] a) {
        Double[] d = new Double[1];
        d = Arrays.asList(a).toArray(d);
        Arrays.sort(d, Collections.reverseOrder());
        double[] unboxed = Stream.of(d).mapToDouble(Double::doubleValue).toArray();
        return unboxed;
    }

    public static double[] sortArray(double[] a, int[] index) {
        double[] b = a.clone();
        for (int i = 0; i < a.length; i++) {
            b[i] = a[index[i]];
        }
        return b;
    }

    public static String getRandomSubset(String[] set) {
        Random rnd = new Random();
        int n = rnd.nextInt(set.length - 1);
        return set[n];
    }

    /**
     * Önce seçilen bir combinasyonun sağ ve solunda gaussian miktar kadar ötede
     * bir komşu kombinasyon araştırır. Eğer ötelenen yer pozitif yönde ise ve
     * shift miktarı ile konumlanan index set in uzunluğundan küçük ise shift
     * ekelenip ilgili kombinasyon split edilerek döndürülür değilse circle
     * mantığı kullanılarak dizinin başına taşar. Negatif için de durum
     * böyledir.
     *
     * @param prevComb
     * @param set
     * @return
     */
    public static String getNeighborSubset(String prevComb, String[] set) {
        int n = find(prevComb, set);
        String ret = set[0];
        if (n != -1) {
            int k = (int) (new Random().nextGaussian() * 5);
            if (k >= 0) {
                if (n + k > set.length - 1) {
                    ret = set[Math.abs((n + k) - (set.length - 1))];
                } else {
                    ret = set[n + k];
                }
            } else {
                if (n + k < 0) {
                    ret = set[Math.abs((set.length - 1) - (n + k))];
                } else {
                    ret = set[n + k];
                }

            }
        }
        return ret;
    }

    private static int find(String prev, String[] set) {
        for (int i = 0; i < set.length; i++) {
            if (prev.equals(set[i])) {
                return i;
            }
        }
        return -1;
    }

    public static void convertImageToDifferentDPI(BufferedImage sourceImage, String destFile, int dpi, double... maxValue) {
        try {
            File destinationFile = new File(destFile);
            ImageWriter imageWriter = ImageIO.getImageWritersBySuffix("jpeg").next();
            ImageOutputStream ios = ImageIO.createImageOutputStream(destinationFile);
            imageWriter.setOutput(ios);
            ImageWriteParam jpegParams = imageWriter.getDefaultWriteParam();

            IIOMetadata data = imageWriter.getDefaultImageMetadata(new ImageTypeSpecifier(sourceImage), jpegParams);
            Element tree = (Element) data.getAsTree("javax_imageio_jpeg_image_1.0");
            Element jfif = (Element) tree.getElementsByTagName("app0JFIF").item(0);
            jfif.setAttribute("Xdensity", Integer.toString(dpi));
            jfif.setAttribute("Ydensity", Integer.toString(dpi));
            jfif.setAttribute("resUnits", "1"); // density is dots per inch
            data.mergeTree("javax_imageio_jpeg_image_1.0", tree); // Write and clean up

            imageWriter.write(data, new IIOImage(sourceImage, null, data), jpegParams);
            ios.close();
            imageWriter.dispose();

        } catch (IOException ex) {
            Logger.getLogger(FactoryUtils.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * bir matrisin bütün rowlarını index dizisine göre sıralar
     *
     * @param m
     * @param index
     * @return
     */
    public static CMatrix sortRows(CMatrix m, int[] index) {
        double[][] d = m.transpose().toDoubleArray2D();
        for (int i = 0; i < d.length; i++) {
            d[i] = sortArray(d[i], index);
        }
        m.setArray(d);
        return m.transpose();
    }

    public static double[][] transpose(double[][] d) {
        double[][] ret = new double[d[0].length][d.length];
        for (int i = 0; i < d[0].length; i++) {
            for (int j = 0; j < d.length; j++) {
                ret[i][j] = d[j][i];
            }
        }
        return ret;
    }

    public static int[][] transpose(int[][] d) {
        int[][] ret = new int[d[0].length][d.length];
        for (int i = 0; i < d[0].length; i++) {
            for (int j = 0; j < d.length; j++) {
                ret[i][j] = d[j][i];
            }
        }
        return ret;
    }

    public static float[][] transpose(float[][] d) {
        float[][] ret = new float[d[0].length][d.length];
        for (int i = 0; i < d[0].length; i++) {
            for (int j = 0; j < d.length; j++) {
                ret[i][j] = d[j][i];
            }
        }
        return ret;
    }

    public static byte[][] transpose(byte[][] d) {
        byte[][] ret = new byte[d[0].length][d.length];
        for (int i = 0; i < d[0].length; i++) {
            for (int j = 0; j < d.length; j++) {
                ret[i][j] = d[j][i];
            }
        }
        return ret;
    }

    public static String[][] transpose(String[][] d) {
        String[][] ret = new String[d[0].length][d.length];
        for (int i = 0; i < d[0].length; i++) {
            for (int j = 0; j < d.length; j++) {
                ret[i][j] = d[j][i];
            }
        }
        return ret;
    }

    public static Object[][] transpose(Object[][] d) {
        Object[][] ret = new Object[d[0].length][d.length];
        for (int i = 0; i < d[0].length; i++) {
            for (int j = 0; j < d.length; j++) {
                ret[i][j] = d[j][i];
            }
        }
        return ret;
    }

//    public static double[] to1D(double[][] d) {
//        int r = d.length;
//        int c = d[0].length;
//        double[] ret = new double[r * c];
//        int k = 0;
//        for (int j = 0; j < c; j++) {
//            for (int i = 0; i < r; i++) {
//                ret[k++] = d[i][j];
//            }
//        }
//        return ret;
//    }
//
    public static double[][] reshape(double[][] d, int r, int c) {
        if (d.length * d[0].length != r * c) {
            System.err.println("size mismatch");
//            showMessage("size mismatch");
            return d;
        }
        double[][] ret = new double[r][c];
        double[] a = toDoubleArray1D(d);
        int k = 0;
        for (int j = 0; j < c; j++) {
            for (int i = 0; i < r; i++) {
                ret[i][j] = a[k++];
            }
        }
        return ret;
    }

    public static double[][] reshape(double[] d, int r, int c) {
        double[][] ret = new double[r][c];
        if (d.length != r * c) {
            showMessage("size mismatch");
            return ret;
        }
        int k = 0;
        for (int j = 0; j < c; j++) {
            for (int i = 0; i < r; i++) {
                ret[i][j] = d[k++];
            }
        }
        return ret;
    }

    /**
     * if we want to convert 1D column vector index to 2D coordinates i.e after
     * applying find command we get 1D column vector in which we want to find
     * the exact coordinate of the specified item of the column vector
     *
     * @param index:index of the column vector
     * @param r:original row number of 2D matrix
     * @param c:original column number of 2D matrix
     * @return CPoint(row,column)
     */
    public static CPoint to2D(int index, int r, int c) {
        CPoint ret = new CPoint(0, 0);
        int col = index / r;
        int row = index - col * r;
        ret.row = row;
        ret.column = col;
        return ret;
    }

    /**
     * return the row and column number of the input 2D array
     *
     * @param d:2D double array
     * @return
     */
    public static CPoint getSize(double[][] d) {
        CPoint ret = new CPoint(d.length, d[0].length);
        return ret;
    }

    /**
     * return the row and column number of the input 2D array
     *
     * @param d:2D int array
     * @return
     */
    public static CPoint getSize(int[][] d) {
        CPoint ret = new CPoint(d.length, d[0].length);
        return ret;
    }

    public static int[] toIntArray1D(double[][] d) {
        int[] ret = new int[d.length * d[0].length];
        int k = 0;
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                ret[k++] = (int) d[i][j];
            }
        }
        return ret;
    }

    public static int[] toIntArray1D(float[][] d) {
        int[] ret = new int[d.length * d[0].length];
        int k = 0;
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                ret[k++] = (int) d[i][j];
            }
        }
        return ret;
    }

    public static short[] toShortArray1D(double[][] d) {
        short[] ret = new short[d.length * d[0].length];
        int k = 0;
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                ret[k++] = (short) d[i][j];
            }
        }
        return ret;
    }

    public static float[] toFloatArray1D(double[][] d) {
        float[] ret = new float[d.length * d[0].length];
        int k = 0;
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                ret[k++] = (float) d[i][j];
            }
        }
        return ret;
    }

    public static long[] toLongArray1D(double[][] d) {
        long[] ret = new long[d.length * d[0].length];
        int k = 0;
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                ret[k++] = (long) d[i][j];
            }
        }
        return ret;
    }

    public static String[] toStringArray1D(double[][] d) {
        String[] ret = new String[d.length * d[0].length];
        int k = 0;
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                ret[k++] = String.valueOf(d[i][j]);
            }
        }
        return ret;
    }

    public static short[] toShortArray1D(float[][] d) {
        short[] ret = new short[d.length * d[0].length];
        int k = 0;
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                ret[k++] = (short) d[i][j];
            }
        }
        return ret;
    }

    public static byte[] toByteArray1D(double[][] d) {
        byte[] ret = new byte[d.length * d[0].length];
        int k = 0;
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                ret[k++] = (byte) d[i][j];
            }
        }
        return ret;
    }

    public static byte[] toByteArray1D(float[][] d) {
        byte[] ret = new byte[d.length * d[0].length];
        int k = 0;
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                ret[k++] = (byte) d[i][j];
            }
        }
        return ret;
    }

    public static byte[] toByteArray1D(int[][] d) {
        byte[] ret = new byte[d.length * d[0].length];
        int k = 0;
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                ret[k++] = (byte) d[i][j];
            }
        }
        return ret;
    }

    public static float[] toFloatArray1D(float[][] d) {
        float[] ret = new float[d.length * d[0].length];
        int k = 0;
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                ret[k++] = d[i][j];
            }
        }
        return ret;
    }

    public static int[] toIntArray1D(int[][] d) {
        int[] ret = new int[d.length * d[0].length];
        int k = 0;
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                ret[k++] = d[i][j];
            }
        }
        return ret;
    }

    public static byte[] toByteArray1D(byte[][] d) {
        byte[] ret = new byte[d.length * d[0].length];
        int k = 0;
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                ret[k++] = d[i][j];
            }
        }
        return ret;
    }

    public static short[] toShortArray1D(short[][] d) {
        short[] ret = new short[d.length * d[0].length];
        int k = 0;
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                ret[k++] = d[i][j];
            }
        }
        return ret;
    }

    public static int getMinimum(int[] p) {
        int m = p[0];
        for (int i = 0; i < p.length; i++) {
            if (p[i] < m) {
                m = p[i];
            }
        }
        return m;
    }

    public static int getMaximum(int[] p) {
        int m = p[0];
        for (int i = 0; i < p.length; i++) {
            if (p[i] > m) {
                m = p[i];
            }
        }
        return m;
    }

    public static double getMaximum(double[][] p) {
        double m = p[0][0];
        for (int i = 0; i < p.length; i++) {
            for (int j = 0; j < p[0].length; j++) {
                if (p[i][j] > m) {
                    m = p[i][j];
                }
            }
        }
        return m;
    }

    public static double getMinimum(double[][] p) {
        double m = p[0][0];
        for (int i = 0; i < p.length; i++) {
            for (int j = 0; j < p[0].length; j++) {
                if (p[i][j] < m) {
                    m = p[i][j];
                }
            }
        }
        return m;
    }

    public static float getMaximum(float[][] p) {
        float m = p[0][0];
        for (int i = 0; i < p.length; i++) {
            for (int j = 0; j < p[0].length; j++) {
                if (p[i][j] > m) {
                    m = p[i][j];
                }
            }
        }
        return m;
    }

    public static float getMinimum(float[][] p) {
        float m = p[0][0];
        for (int i = 0; i < p.length; i++) {
            for (int j = 0; j < p[0].length; j++) {
                if (p[i][j] < m) {
                    m = p[i][j];
                }
            }
        }
        return m;
    }

    public static int getMaximum(int[][] p) {
        int m = p[0][0];
        for (int i = 0; i < p.length; i++) {
            for (int j = 0; j < p[0].length; j++) {
                if (p[i][j] > m) {
                    m = p[i][j];
                }
            }
        }
        return m;
    }

    public static int getMinimum(int[][] p) {
        int m = p[0][0];
        for (int i = 0; i < p.length; i++) {
            for (int j = 0; j < p[0].length; j++) {
                if (p[i][j] < m) {
                    m = p[i][j];
                }
            }
        }
        return m;
    }

    public static byte getMaximum(byte[][] p) {
        byte m = p[0][0];
        for (int i = 0; i < p.length; i++) {
            for (int j = 0; j < p[0].length; j++) {
                if (p[i][j] > m) {
                    m = p[i][j];
                }
            }
        }
        return m;
    }

    public static byte getMinimum(byte[][] p) {
        byte m = p[0][0];
        for (int i = 0; i < p.length; i++) {
            for (int j = 0; j < p[0].length; j++) {
                if (p[i][j] < m) {
                    m = p[i][j];
                }
            }
        }
        return m;
    }

    public static double getMinimum(double[] p) {
        double m = p[0];
        for (int i = 0; i < p.length; i++) {
            if (p[i] < m) {
                m = p[i];
            }
        }
        return m;
    }

    public static int getMinimumIndex(double[] p) {
        double m = p[0];
        int min = 0;
        for (int i = 0; i < p.length; i++) {
            if (p[i] < m) {
                m = p[i];
                min = i;
            }
        }
        return min;
    }

    public static double getMaximum(double[] p) {
        double m = p[0];
        for (int i = 0; i < p.length; i++) {
            if (p[i] > m) {
                m = p[i];
            }
        }
        return m;
    }

    public static int getMaximumIndex(double[] p) {
        double m = p[0];
        int max = 0;
        for (int i = 0; i < p.length; i++) {
            if (p[i] > m) {
                m = p[i];
                max = i;
            }
        }
        return max;
    }

    public static float getMinimum(float[] p) {
        float m = p[0];
        for (int i = 0; i < p.length; i++) {
            if (p[i] < m) {
                m = p[i];
            }
        }
        return m;
    }

    public static float getMaximum(float[] p) {
        float m = p[0];
        for (int i = 0; i < p.length; i++) {
            if (p[i] > m) {
                m = p[i];
            }
        }
        return m;
    }

    public static byte getMinimum(byte[] p) {
        byte m = p[0];
        for (int i = 0; i < p.length; i++) {
            if (p[i] < m) {
                m = p[i];
            }
        }
        return m;
    }

    public static byte getMaximum(byte[] p) {
        byte m = p[0];
        for (int i = 0; i < p.length; i++) {
            if (p[i] > m) {
                m = p[i];
            }
        }
        return m;
    }

    public static double getMean(double[][] m) {
        double ret;
        double toplam = 0;
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                toplam += m[i][j];
            }
        }
        int pix = (m.length * m[0].length);
        ret = toplam / pix;
        return ret;
    }

    public static double getMean(int[][] m) {
        double ret;
        double toplam = 0;
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                toplam += m[i][j];
            }
        }
        int pix = (m.length * m[0].length);
        ret = toplam / pix;
        return ret;
    }

    public static double getMean(float[][] m) {
        double ret;
        double toplam = 0;
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                toplam += m[i][j];
            }
        }
        int pix = (m.length * m[0].length);
        ret = toplam / pix;
        return ret;
    }

    public static double getMean(double[] d) {
        double t = getSum(d);
        return t / d.length;
    }

    public static double getMean(float[] d) {
        double t = getSum(d);
        return t / d.length;
    }

    public static double getMean(int[] d) {
        double t = getSum(d);
        return t / d.length;
    }

    public static double getMagnitude(double[] d) {
        double t = 0;
        for (int i = 0; i < d.length; i++) {
            t += d[i] * d[i];
        }
        return Math.sqrt(t);
    }

    public static double getEnergy(double[] d) {
        double t = 0;
        for (int i = 0; i < d.length; i++) {
            t += d[i] * d[i];
        }
        return t;
    }

    public static double getEntropy(double[] d) {
        double entropy = 0.0d;
        double[] pdfArray = getPDFData(d);
        for (int i = 0; i < pdfArray.length; i++) {
            if (pdfArray[i] >= 0 && pdfArray[i] <= 0.000001) {
                continue;
            }
            entropy += pdfArray[i] * Math.log(pdfArray[i]);
        }
        entropy = -entropy;
        return entropy;
    }

    public static double[] getPDFData(double[] d) {
        double[] hst = getHistogram(d, 255);
        double sum = sum(hst);
        double[] ret = new double[d.length];
        for (int i = 0; i < d.length; i++) {
            ret[i] = d[i] / sum;
        }
        return ret;
    }

    public static double getMagnitude(int[] d) {
        double t = 0;
        for (int i = 0; i < d.length; i++) {
            t += d[i] * d[i];
        }
        return Math.sqrt(t);
    }

    public static double getMagnitude(float[] d) {
        double t = 0;
        for (int i = 0; i < d.length; i++) {
            t += d[i] * d[i];
        }
        return Math.sqrt(t);
    }

    public static double getMagnitude(byte[] d) {
        double t = 0;
        for (int i = 0; i < d.length; i++) {
            t += d[i] * d[i];
        }
        return Math.sqrt(t);
    }

    public static ArrayList<Integer> toArrayList(int[] p) {
        ArrayList<Integer> ret = new ArrayList<>();
        for (int i = 0; i < p.length; i++) {
            ret.add(i);
        }
        return ret;
    }

    public static double sum(double[] d) {
        double ret = 0;
        for (int i = 0; i < d.length; i++) {
            ret += d[i];
        }
        return ret;
    }

    public static double prod(double[] d) {
        double ret = 1;
        for (int i = 0; i < d.length; i++) {
            ret *= d[i];
        }
        return ret;
    }

    public static double sum(double[][] d) {
        double ret = 0;
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                ret += d[i][j];
            }
        }
        return ret;
    }

    public static double prod(double[][] d) {
        double ret = 1;
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                ret *= d[i][j];
            }
        }
        return ret;
    }

    public static double sum(double[] d, int n) {
        double ret = 0;
        for (int i = 0; i < n; i++) {
            ret += d[i];
        }
        return ret;
    }

    public static int sum(int[] d) {
        int ret = 0;
        for (int i = 0; i < d.length; i++) {
            ret += d[i];
        }
        return ret;
    }

    public static float sum(float[] d) {
        float ret = 0;
        for (int i = 0; i < d.length; i++) {
            ret += d[i];
        }
        return ret;
    }

    public static long sum(long[] d) {
        long ret = 0;
        for (int i = 0; i < d.length; i++) {
            ret += d[i];
        }
        return ret;
    }

    /**
     * average two identical double array
     *
     * @param a
     * @param b
     * @return
     */
    public static double[] mean(double[] a, double[] b) {
        int n = Math.min(a.length, b.length);
        double[] ret = new double[a.length];
        for (int i = 0; i < n; i++) {
            ret[i] = (a[i] + b[i]) / 2.0;
        }
        return ret;
    }

    public static double mean(double[] d) {
        return sum(d) / d.length;
    }

    public static int mean(int[] d) {
        return sum(d) / d.length;
    }

    public static float mean(float[] d) {
        return sum(d) / d.length;
    }

    public static long mean(long[] d) {
        return sum(d) / d.length;
    }

    public static double std(double[] d) {
        return FactoryStatistic.getStandardDeviation(d);
    }

    public static double var(double[] d) {
        return FactoryStatistic.getVariance(d);
    }

    public static int[][] to2DInt(Integer[] bDizi, int m, int n) {
        int[][] ret = new int[m][n];
        int k = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                ret[i][j] = bDizi[k++];
//                if (bDizi[k]!=0) {
//                    System.out.println(k+".pixel:"+bDizi[k]);
//                }
            }
        }
        return ret;
    }

    public static int[][] to2DInt(int[] bDizi, int m, int n) {
        int[][] ret = new int[m][n];
        int k = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                ret[i][j] = bDizi[k++];
            }
        }
        return ret;
    }

    public static double[][] to2Ddouble(double[] bDizi, int m, int n) {
        double[][] ret = new double[m][n];
        int k = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                ret[i][j] = bDizi[k++];
            }
        }
        return ret;
    }

    public static String getSubstringValueWithKeyFromBegin(String str, String key, TDeviceState ds) {
        String atla = "name=\"Value\">\n"
                + "                    <itf guid=\"{99B44940-BFE1-4083-ADA1-BE703F4B8E08}\" value=\"";
        int i = str.indexOf(key) + key.length();
        int j = str.indexOf("name=\"Value\"", i) + atla.length();
        ds.pos = j;
        ds.str = str.substring(0, j);
        return str.substring(0, j);
    }

    public static String getSubstringValueWithKeyToEnd(String str, TDeviceState ds) {
        int j = str.indexOf("\"", ds.pos);
        return str.substring(j, str.length());
    }

    public static String getSubstringValueWithKey(String str, String key) {
        String atla = "name=\"Value\">\n"
                + "                    <itf guid=\"{99B44940-BFE1-4083-ADA1-BE703F4B8E08}\" value=\"";
        int i = str.indexOf(key) + key.length();

        int j = str.indexOf("name=\"Value\"", i) + atla.length();
        int k = str.indexOf("\"", j);

        return str.substring(j, k);
    }

    public static double[] normalizeToCanvas(double[] votes, Rectangle rect) {
        double[] ret = new double[votes.length];
        int py = 50;
        int dh = rect.height - 2 * py;
        double maxH = FactoryUtils.getMaximum(votes);
        double scale = dh / maxH;
        for (int i = 0; i < ret.length; i++) {
            ret[i] = votes[i] * scale;
        }
        return ret;
    }

    public static int getDigitNumber(double p) {
        NumberFormat formatter = new DecimalFormat();
        formatter = new DecimalFormat("0.######E0");
        String s = formatter.format(p);
//        String s = p + "";
//        return s.length();
        String ss = s.substring(s.indexOf("E") + 1);
        return Integer.parseInt(ss);
    }

    public static File[] getFileListInFolder(String path) {
        ArrayList<File> results = new ArrayList<>();
        File[] files = new File(path).listFiles();
        for (File file : files) {
            if (file.isFile()) {
                results.add(file);
            }
        }
        return results.toArray(new File[0]);
    }

    public static File[] getFileListInFolderForImages(String imageFolder) {
        File dir = new File(imageFolder);
        final String[] EXTENSIONS = new String[]{
            "gif", "png", "bmp", "jpg", "PNG", "JPG", "BMP", "GIF" // and other formats you need
        };
        // filter to identify images based on their extensions
        FilenameFilter IMAGE_FILTER = new FilenameFilter() {

            @Override
            public boolean accept(final File dir, final String name) {
                for (final String ext : EXTENSIONS) {
                    if (name.endsWith("." + ext)) {
                        return (true);
                    }
                }
                return (false);
            }
        };
        File[] list = dir.listFiles(IMAGE_FILTER);
        return list;
    }

    public static long fact(int n) {
        int t = 1;
        for (int i = 1; i < n; i++) {
            t *= i;
        }
        return t;
    }

    public static long fact(int m, int n) {
        int t = 1;
        for (int i = n + 1; i <= m; i++) {
            t *= i;
        }
        return t;
    }

    public static double[] ekle(double[] p1, double[] p2) {
        return concatenate(p1, p2);
    }

    public static int[] ekle(int[] p1, int[] p2) {
        return concatenate(p1, p2);
    }

    public static double[] concatenate(double[] p1, double[] p2) {
        double[] ret = new double[p1.length + p2.length];
        ArrayList<Double> lst = new ArrayList<>();
        for (int i = 0; i < p1.length; i++) {
            lst.add(p1[i]);
        }
        for (int i = 0; i < p2.length; i++) {
            lst.add(p2[i]);
        }
        Double[] d = new Double[p1.length + p2.length];
        d = lst.toArray(d);
        for (int i = 0; i < d.length; i++) {
            ret[i] = d[i];
        }
        return ret;
    }

    public static int[] concatenate(int[] p1, int[] p2) {
        int[] ret = new int[p1.length + p2.length];
        ArrayList<Integer> lst = new ArrayList<>();
        for (int i = 0; i < p1.length; i++) {
            lst.add(p1[i]);
        }
        for (int i = 0; i < p2.length; i++) {
            lst.add(p2[i]);
        }
        Integer[] d = new Integer[p1.length + p2.length];
        d = lst.toArray(d);
        for (int i = 0; i < d.length; i++) {
            ret[i] = d[i];
        }
        return ret;
    }

    public static int getGraphicsTextWidth(Graphics gr, String t) {
        Rectangle2D r1 = gr.getFont().getStringBounds(t, 0, t.length(), gr.getFontMetrics().getFontRenderContext());
        return (int) r1.getWidth();
    }

    public static int getGraphicsTextHeight(Graphics gr, String t) {
        Rectangle2D r1 = gr.getFont().getStringBounds(t, 0, t.length(), gr.getFontMetrics().getFontRenderContext());
        return (int) r1.getHeight();
    }

    public static double[] gaussian(double[] d, double sigma, double mean) {
        double[] ret = new double[d.length];
        for (int i = 0; i < d.length; i++) {
            ret[i] = gaussian(d[i], sigma, mean);
        }
        return ret;
    }

    public static double gaussian(double x, double sigma, double mean) {
        double ret = Math.exp(-(Math.pow(x - mean, 2) / (2 * sigma * sigma)));
        return ret;
    }

    public static long tic() {
        long currentTime = System.nanoTime();
        return currentTime;
    }

    public static long toc(String msg, long tic) {
        long toc = System.nanoTime();
        double elapsed = (toc - tic) / (1000000.0d);
        System.out.println(msg + elapsed + " miliSecond");
        return toc;
    }

    public static long toc(long tic) {
        long toc = System.nanoTime();
        double elapsed = (toc - tic) / (1000000.0d);
        System.out.println("Elapsed Time:" + elapsed + " miliSecond");
        return toc;
    }

    public static int fps(long t) {
        int ret = (int) Math.round(1E9 / (System.nanoTime() - t));
        return ret;
    }

    /**
     * it is used for detecting the single any object in the image matrix and
     * crop the roi of the object from the matrix it should be noted that
     * internally it is used nf=nearFactor which you should give as a parameter
     *
     * @param d:input image matrix
     * @param t:threshould value for background subtraction
     * @param nf:near factor how far object from the edge of the image matrix
     * @return cropped matrix related to the object itself
     */
    public static double[][] getWeightCenteredROI(double[][] d, int t, int nf) {
        double[][] ret = null;
//        CMatrix.getInstance(d).imshow();
        int[] px = getProjectedMatrixOnX(d);
        int[] py = getProjectedMatrixOnY(d);
//        CMatrix.getInstance(px).plot();
//        CMatrix.getInstance(py).plot();
        int thr = 10000;
        CPoint[] p_x = getPotentialObjects(px, thr);
        CPoint[] p_y = getPotentialObjects(py, thr);
        try {
            if (p_x.length == 0 || p_y.length == 0) {
                return null;
            }
            ret = getSubMatrix(d, new CPoint(p_y[0].row, p_x[0].row), new CPoint(p_y[0].column, p_x[0].column));
//            Thread.sleep(10);
        } catch (Exception ex) {
            Logger.getLogger(FactoryUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

//        int[] p = getMinMaxPosition(d, t, 5);
//        int w = d[0].length;
//        int h = d.length;
////        Point center = new Point(Math.abs(p[0] + p[2]) / 2, Math.abs(p[1] + p[3]) / 2);
//        if (p[0] <= 1 || p[0] >= w - 1 || p[2] <= 1 || p[2] >= w - 1
//                || p[1] <= 1 || p[1] >= h - 1 || p[3] <= 1 || p[3] >= h - 1) {
//            ret = new double[1][1];
//        } else {
//            ret = getSubMatrix(d, new CPoint(p[1], p[0]), new CPoint(p[3], p[2]));
//        }
        return ret;
    }

    public static CPoint getWeightCenterPos(double[][] d) {
        //long t1=tic();
        double[][] ret = null;
        CPoint cp = new CPoint();
        int[] px = getProjectedMatrixOnX(d);
        int pxFrom = getFirstOccuranceOfData(px);
        int pxTo = getLastOccuranceOfData(px);
        cp.column = (pxFrom + pxTo) / 2;
        int[] py = getProjectedMatrixOnY(d);
        int pyFrom = getFirstOccuranceOfData(py);
        int pyTo = getLastOccuranceOfData(py);
        cp.row = (pyFrom + pyTo) / 2;
        //t1=toc("extra cost:",t1);
        return cp;
    }

    public static int getFirstOccuranceOfData(int[] p) {
        int ret = 0;
        for (int i = 0; i < p.length; i++) {
            if (p[i] != 0) {
                ret = i;
                break;
            }
        }
        return ret;
    }

    public static int getLastOccuranceOfData(int[] p) {
        int ret = 0;
        for (int i = p.length - 1; i > 0; i--) {
            if (p[i] != 0) {
                ret = i;
                break;
            }
        }
        return ret;
    }

    public static double[][] getWeightCenteredROI(double[][] d) {
        double[][] ret = null;
        int[] px = getProjectedMatrixOnX(d);
        int[] py = getProjectedMatrixOnY(d);
        int[] p_x = getPotentialObjects(px);
        int[] p_y = getPotentialObjects(py);
        ret = getSubMatrix(d, p_y[0], p_x[0], p_y[1], p_x[1]);
        return ret;
    }

    public static double[][] getWeightCenteredROI(double[][] d, CPoint[] cp) {
        double[][] ret = null;
        int[] px = getProjectedMatrixOnX(d);
        int[] py = getProjectedMatrixOnY(d);
        int[] p_x = getPotentialObjects(px);
        int[] p_y = getPotentialObjects(py);
        cp = new CPoint[2];
        cp[0] = new CPoint();
        cp[1] = new CPoint();
        cp[0].row = p_y[0];
        cp[0].column = p_x[0];
        cp[1].row = p_y[1];
        cp[1].column = p_x[1];

        ret = getSubMatrix(d, p_y[0], p_x[0], p_y[1], p_x[1]);
        return ret;
    }

    public static CPoint[] getWeightCenteredROI(double[][] d, boolean flag) {
        double[][] ret = null;
        int[] px = getProjectedMatrixOnX(d);
        int[] py = getProjectedMatrixOnY(d);
        int[] p_x = getPotentialObjects(px);
        int[] p_y = getPotentialObjects(py);
        CPoint[] cp = new CPoint[2];
        cp[0] = new CPoint();
        cp[1] = new CPoint();
        cp[0].row = p_y[0];
        cp[0].column = p_x[0];
        cp[1].row = p_y[1];
        cp[1].column = p_x[1];

        ret = getSubMatrix(d, p_y[0], p_x[0], p_y[1], p_x[1]);
        return cp;
    }

    private static int[] getPotentialObjects(int[] pr) {
        ArrayList<CPoint> lst = new ArrayList<>();
        for (int i = 0; i < pr.length; i++) {
            if (pr[i] > 0) {
                int p1 = i;
                int p2 = 0;
                int t = 0;
                while (i < pr.length) {
                    if (pr[i] == 0) {
                        p2 = i;
                        break;
                    }
                    p2 = i;
                    t += pr[i];
                    i++;
                }
                CPoint p = new CPoint(p1, p2);
                p.weight = t;
                if (t > 0) {
                    lst.add(p);
                }
            }
        }
        Collections.sort(lst, new CustomComparatorForCPoint());
        int[] ret = new int[2];
        ret[0] = lst.get(0).row;
        ret[1] = lst.get(0).column;
        return ret;
    }

    private static CPoint[] getPotentialObjects(int[] pr, int thr) {
        ArrayList<CPoint> lst = new ArrayList<>();
        for (int i = 0; i < pr.length; i++) {
            if (pr[i] > 0) {
                int p1 = i;
                int p2 = 0;
                int t = 0;
                while (i < pr.length) {
                    if (pr[i] == 0) {
                        p2 = i;
                        break;
                    }
                    p2 = i;
                    t += pr[i];
                    i++;
                }
                CPoint p = new CPoint(p1, p2);
                p.weight = t;
                if (t > thr) {
                    lst.add(p);
                }
            }
        }
        Collections.sort(lst, new CustomComparatorForCPoint());
        CPoint[] ret = new CPoint[lst.size()];
        ret = lst.toArray(ret);
        return ret;
    }

    private static int[] getMinMaxPosition(double[][] d, int t, int a) {
        int maxX = 0;
        int maxY = 0;
        int minX = d.length * 2;
        int minY = d[0].length * 2;
        int[] ret = new int[4];
        for (int i = 0; i < d[0].length; i++) {
            for (int j = 0; j < d.length; j++) {
                if (d[j][i] > t) {
                    if (minX > i) {
                        minX = i;
                    }
                    if (maxX < i) {
                        maxX = i;
                    }
                    if (minY > j) {
                        minY = j;
                    }
                    if (maxY < j) {
                        maxY = j;
                    }
                }
            }
        }
        ret[0] = ((minX - a) < 0) ? minX : (minX - a);
        ret[1] = ((minY - a) < 0) ? minY : (minY - a);
        ret[2] = ((maxX + a) > d[0].length) ? maxX : (maxX + a);
        ret[3] = ((maxY + a) > d.length) ? maxY : (maxY + a);
        return ret;
    }

    /**
     * herhangi bir matrisin içerisindeki geometrik şeklin en uzun boyutu ve
     * buna dik olan ikinci boyutun pixel olarak uzunluğunu hesaplar
     *
     * @param m=matrix
     * @param thr=threshold
     * @param isShow
     * @return
     */
    public static double[] getObjectDimensions(double[][] m, int thr, boolean isShow) {
        int cRow = m.length / 2;
        int cColumn = m[0].length / 2;
        double d1 = 0;
        double d2 = 0;
        int lr = 0;
        int lc = 0;
        int dim1R1 = cRow;
        int dim1C1 = cColumn;
        int dim1R2 = 0;
        int dim1C2 = 0;
        int dim2R2 = 0;
        int dim2C2 = 0;
        //en uzun dimension thresholddan büyük olan pixeller
        //arasında merkeze eucledian uzaklığı en büyük olan pixelin koordinattır.
        //en sonda bulunan lr ve lc normal grafikteki y,x değerlerini verirler.
        //bulunan koordinatın merkeze uzaklığının 2 katı principal dimensiondır.
        for (int c = 0; c < m[0].length; c++) {
            for (int r = 0; r < m.length; r++) {
                if (m[r][c] > thr) {
                    double ed = getEucledianDistance(cRow, cColumn, r, c);
                    if (ed > d1) {
                        lr = r;
                        lc = c;
                        d1 = ed;
                        dim1R2 = r;
                        dim1C2 = c;
                    }
                }
            }
        }
        d1 = FactoryUtils.formatDouble(2 * d1);
        double m1 = FactoryUtils.formatDouble(getSlope(lr, lc, cRow, cColumn));
        double m2 = FactoryUtils.formatDouble(-1.0 / m1);
        double dt = 0.1;
        //en uzun dim bulunduktan sonra buna dik dim2 nin eğimini biliyoruz
        //bir de merkezi biliyoruz merkezden dışarıya çekilen doğrulardan en büyük ve 
        //eğimi m2 olan dim2 dir.
        for (int c = 0; c < m[0].length; c++) {
            for (int r = 0; r < m.length; r++) {
                if (m[r][c] > thr) {
                    double ed = getEucledianDistance(cRow, cColumn, r, c);
                    double slope = getSlope(r, c, cRow, cColumn);
                    double mm2 = Math.abs(m2 - slope);
                    if (ed > d2 && mm2 < dt) {
                        lr = r;
                        lc = c;
                        d2 = ed;
                        dim2R2 = r;
                        dim2C2 = c;
                    }
                }
            }
        }
        d2 = FactoryUtils.formatDouble(d2 * 2);
        double[] ret = new double[4];
        ret[0] = m1;
        ret[1] = d1;
        ret[2] = m2;
        ret[3] = d2;
        if (isShow) {
            CMatrix cm = CMatrix.getInstance(m).
                    drawLine(dim1R1, dim1C1, dim1R2, dim1C2, 2, Color.yellow).
                    drawLine(dim1R1, dim1C1, dim2R2, dim2C2, 2, Color.yellow).imshow(".fıstık");
        }
        return ret;
    }

    /**
     * herhangi bir matrisin içerisindeki geometrik şeklin en uzun boyutu ve
     * buna dik olan ikinci boyutun pixel olarak uzunluğunu hesaplar
     *
     * @param m=matrix
     * @param thr=threshold
     * @param isShow
     * @return
     */
    public static double[] getObjectDimensionsV2(double[][] m, int thr, boolean isShow) {
        int cRow = m.length / 2;
        int cColumn = m[0].length / 2;
        double d1 = 0;
        double d2 = 0;
        int lr = 0;
        int lc = 0;
        int dim1R1 = cRow;
        int dim1C1 = cColumn;
        int dim1R2 = 0;
        int dim1C2 = 0;
        int dim2R2 = 0;
        int dim2C2 = 0;
        //en uzun dimension thresholddan büyük olan pixeller
        //arasında merkeze eucledian uzaklığı en büyük olan pixelin koordinattır.
        //en sonda bulunan lr ve lc normal grafikteki y,x değerlerini verirler.
        //bulunan koordinatın merkeze uzaklığının 2 katı principal dimensiondır.
        for (int c = 0; c < m[0].length; c++) {
            for (int r = 0; r < m.length; r++) {
                if (m[r][c] > thr) {
                    double ed = getEucledianDistance(cRow, cColumn, r, c);
                    if (ed > d1) {
                        lr = r;
                        lc = c;
                        d1 = ed;
                        dim1R2 = r;
                        dim1C2 = c;
                    }
                }
            }
        }
        d1 = FactoryUtils.formatDouble(2 * d1);
        double m1 = FactoryUtils.formatDouble(getSlope(lr, lc, cRow, cColumn));
        double m2 = FactoryUtils.formatDouble(-1.0 / m1);
        double dt = 0.1;
        //en uzun dim bulunduktan sonra buna dik dim2 nin eğimini biliyoruz
        //bir de merkezi biliyoruz merkezden dışarıya çekilen doğrulardan en büyük ve 
        //eğimi m2 olan dim2 dir.
        for (int c = 0; c < m[0].length; c++) {
            for (int r = 0; r < m.length; r++) {
                if (m[r][c] > thr) {
                    double ed = getEucledianDistance(cRow, cColumn, r, c);
                    double slope = getSlope(r, c, cRow, cColumn);
                    double mm2 = Math.abs(m2 - slope);
                    if (ed > d2 && mm2 < dt) {
                        lr = r;
                        lc = c;
                        d2 = ed;
                        dim2R2 = r;
                        dim2C2 = c;
                    }
                }
            }
        }
        d2 = FactoryUtils.formatDouble(d2 * 2);
        double[] ret = new double[4];
        ret[0] = m1;
        ret[1] = d1;
        ret[2] = m2;
        ret[3] = d2;
        if (isShow) {
            CMatrix cm = CMatrix.getInstance(m).
                    drawLine(dim1R1, dim1C1, dim1R2, dim1C2, 2, Color.yellow).
                    drawLine(dim1R1, dim1C1, dim2R2, dim2C2, 2, Color.yellow).imshow(".fıstık");
        }
        return ret;
    }

    public static double getSlope(int r1, int c1, int r2, int c2) {
        double ret = -(r1 - r2) * 1.0 / (c1 - c2);
        return ret;
    }

    private static double getEucledianDistance(int x1, int y1, int x2, int y2) {
        double d = Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
        return d;
    }

    /**
     * return current time as String for file name or other issues
     *
     * @return
     */
    public static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        Calendar cal = Calendar.getInstance();
        String ret = dateFormat.format(cal.getTime());
        return ret;
    }

    /**
     * calculate number of peaks of each column
     *
     * @param d:two dimensional matrix
     * @return array of peaks numbers of columns
     */
    public static double[] getNumberOfPeaks(double[][] d) {
        double[] ret = new double[d[0].length];
        double[][] dt = transpose(d);
        for (int i = 0; i < ret.length; i++) {
            ret[i] = getNumberOfPeaks(dt[i]);
        }
        return ret;
    }

    public static double getNumberOfPeaks(double[] d) {
        double ret = 0;
        double y1, y2, y3;
        for (int i = 1; i < d.length - 1; i++) {
            y1 = d[i - 1];
            y2 = d[i];
            y3 = d[i + 1];
            if (y1 < y2 && y3 < y2) {
                ret++;
            }
        }
        return ret;
    }

    public static double getNumberOfWalleys(double[] d) {
        double ret = 0;
        double y1, y2, y3;
        for (int i = 1; i < d.length - 1; i++) {
            y1 = d[i - 1];
            y2 = d[i];
            y3 = d[i + 1];
            if (y1 > y2 && y3 > y2) {
                ret++;
            }
        }
        return ret;
    }

    public static double getNumberOfPeaks(double[] d, double sensitivity) {
        double ret = 0;
        double y1, y2, y3;
        for (int i = 1; i < d.length - 1; i++) {
            y1 = d[i - 1];
            y2 = d[i];
            y3 = d[i + 1];
            if ((y2 - y1) > sensitivity && (y2 - y1) > sensitivity) {
                ret++;
            }
        }
        return ret;
    }

    public static double getNumberOfWalleys(double[] d, double sensitivity) {
        double ret = 0;
        double y1, y2, y3;
        for (int i = 1; i < d.length - 1; i++) {
            y1 = d[i - 1];
            y2 = d[i];
            y3 = d[i + 1];
            if ((y1 - y2) > sensitivity && (y3 - y2) > sensitivity) {
                ret++;
            }
        }
        return ret;
    }

    public static void makeDirectory(String fn) {
        new File(fn).mkdir();
    }

    public static double[] getTotalMovement(double[][] d) {
        double[] ret = new double[d[0].length];
        double[][] dt = transpose(d);
        for (int i = 0; i < ret.length; i++) {
            ret[i] = getTotalMovement(dt[i]);
        }
        return ret;
    }

    public static double getTotalMovement(double[] d) {
        double ret = 0;
        for (int i = 0; i < d.length; i++) {
            ret += Math.abs(d[i]);
        }
        return ret;
    }

    public static float[] scale(float[] dizi, float d) {
        float[] ret = new float[dizi.length];
        for (int i = 0; i < dizi.length; i++) {
            ret[i] = dizi[i] * d;
        }
        return ret;
    }

    public static double[] scale(double[] dizi, double d) {
        double[] ret = new double[dizi.length];
        for (int i = 0; i < dizi.length; i++) {
            ret[i] = dizi[i] * d;
        }
        return ret;
    }

    public static String toWekaString(double[] d) {
        String ret = "";
        for (int i = 0; i < d.length; i++) {
            ret += d[i] + ",";
        }
        ret = ret.substring(0, ret.length() - 1);
        return ret;
    }

    public static String toCSVString(double[] d) {
        String ret = "";
        for (int i = 0; i < d.length; i++) {
            ret += d[i] + ";";
        }
        ret = ret.substring(0, ret.length() - 1);
        return ret;
    }

    /**
     * add binary edge image on the original image
     *
     * @param d : original image
     * @param dc: binary edge image
     * @return original image with edge information
     */
    public static double[][] overlayIdenticalMatrix(double[][] d, double[][] dc) {
        if (d.length != dc.length || d[0].length != dc[0].length) {
            return d;
        }
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                if (dc[i][j] == 255) {
                    d[i][j] = 255;
                }
            }
        }
        return d;
    }

    /**
     * add any matrix on the original matrix at specified point
     *
     * @param d : original image
     * @param dc: binary edge image
     * @return original image with edge information
     */
    public static double[][] overlayMatrix(double[][] d, double[][] dc, CPoint cp) {
        if (cp.row - dc.length < 0 || cp.row + dc.length > d.length - 1 || cp.column - dc[0].length < 0 || cp.column + dc[0].length > d[0].length - 1) {
            return d;
        }
        int pr = dc.length / 2;
        int pc = dc[0].length / 2;

        for (int i = 0; i < dc.length; i++) {
            for (int j = 0; j < dc[0].length; j++) {
                d[cp.row - pr + i][cp.column - pc + j] = dc[i][j];
            }
        }
        return d;
    }

    public static boolean renameFile(String oldname, String newname) {
        // File (or directory) with old name
        File file = new File(oldname);

        // File (or directory) with new name
        File file2 = new File(newname);
        if (file2.exists()) {
            System.out.println("can not rename since target name is same as source name");
            return false;
        }

        // Rename file (or directory)
        boolean success = file.renameTo(file2);
        if (!success) {
            System.out.println("can not rename");
        }
        return true;
    }

    public static boolean renameFile(File oldFile, File newFile) {
        if (newFile.exists()) {
            System.out.println("can not rename since target name is same as source name");
            return false;
        }
        boolean success = oldFile.renameTo(newFile);
        if (!success) {
            System.out.println("can not rename");
        }
        return true;
    }

    public static double multiplyAndSum(double[] r1, double[] r2) {
        double ret = 0;
        for (int i = 0; i < r1.length; i++) {
            ret += r1[i] * r2[i];
        }
        return ret;
    }

    public static boolean newFolder(String dirName) {
        File theDir = new File(dirName);
        if (!theDir.exists()) {
            try {
                theDir.mkdir();
                return true;
            } catch (SecurityException se) {
                System.out.println("exception was thrown:" + se.getMessage());
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean isFolderExist(String dirName) {
        File theDir = new File(dirName);
        if (theDir.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isFileExist(String path) {
        File fl = new File(path);
        if (fl.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isPointInROI(CPoint p, ArrayList<CPoint> lst) {
        CPoint[] plst = new CPoint[lst.size()];
        plst = lst.toArray(plst);
        return isPointInPolygon(p, plst);
    }

    public static boolean isPointInROI(CPoint p, CPoint[] roi) {
        return isPointInPolygon(p, roi);
    }

    public static CPoint[] getPointsInROI(CPoint[] roi) {
        ArrayList<CPoint> lst = new ArrayList<>();
        CPoint[] cp = extractOuterBoundryFromROI(roi);
        for (int i = cp[0].row; i < cp[1].row; i++) {
            for (int j = cp[0].column; j < cp[1].column; j++) {
                CPoint p = new CPoint(i, j);
                if (isPointInPolygon(p, roi)) {
                    lst.add(p);
                }
            }
        }
        CPoint[] cp_lst = new CPoint[lst.size()];
        return lst.toArray(cp_lst);
    }

    public static void savePointsInROI(CPoint[] roiPixels) {
        String s = "";
        for (CPoint rp : roiPixels) {
            s += rp.row + "," + rp.column + "\n";
        }
        writeToFile(s, true);
    }

    public static boolean isPointInROI(CPoint p, double[][] d) {
        CPoint[] lst = new CPoint[d.length];
        for (int i = 0; i < d.length; i++) {
            CPoint cp = new CPoint((int) d[i][0], (int) d[i][1]);
            lst[i] = cp;
        }
        return isPointInPolygon(p, lst);
    }

    public static CPoint[] getRoiBoundary(double[][] d) {
        CPoint[] polygon = new CPoint[d.length];
        for (int i = 0; i < d.length; i++) {
            polygon[i] = new CPoint();
            polygon[i].row = (int) d[i][0];
            polygon[i].column = (int) d[i][1];
        }
        return getRoiBoundary(polygon);
    }

    public static CPoint[] getRoiBoundary(CPoint[] polygon) {
        int minC = polygon[0].column;
        int maxC = polygon[0].column;
        int minR = polygon[0].row;
        int maxR = polygon[0].row;
        for (int i = 1; i < polygon.length; i++) {
            CPoint q = polygon[i];
            minC = Math.min(q.column, minC);
            maxC = Math.max(q.column, maxC);
            minR = Math.min(q.row, minR);
            maxR = Math.max(q.row, maxR);
        }
        CPoint[] ret = new CPoint[2];
        ret[0] = new CPoint(minR, minC);
        ret[1] = new CPoint(maxR, maxC);
        return ret;
    }

    private static CPoint[] extractOuterBoundryFromROI(CPoint[] polygon) {
        int minC = polygon[0].column;
        int maxC = polygon[0].column;
        int minR = polygon[0].row;
        int maxR = polygon[0].row;
        for (int i = 1; i < polygon.length; i++) {
            CPoint q = polygon[i];
            minC = Math.min(q.column, minC);
            maxC = Math.max(q.column, maxC);
            minR = Math.min(q.row, minR);
            maxR = Math.max(q.row, maxR);
        }
        CPoint[] ret = new CPoint[2];
        ret[0] = new CPoint(minR, minC);
        ret[1] = new CPoint(maxR, maxC);
        return ret;
    }

    private static boolean isPointInPolygon(CPoint p, CPoint[] polygon) {
        CPoint[] cp = extractOuterBoundryFromROI(polygon);
        int minC = cp[0].column;
        int maxC = cp[1].column;
        int minR = cp[0].row;
        int maxR = cp[1].row;

        if (p.column < minC || p.column > maxC || p.row < minR || p.row > maxR) {
            return false;
        }

        // http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
        boolean inside = false;
        for (int i = 0, j = polygon.length - 1; i < polygon.length; j = i++) {
            if ((polygon[i].row > p.row) != (polygon[j].row > p.row)
                    && p.column < (polygon[j].column - polygon[i].column) * (p.row - polygon[i].row) / (polygon[j].row - polygon[i].row) + polygon[i].column) {
                inside = !inside;
            }
        }

        return inside;
    }

    public static double[] getHistogram(double[] d, int nBins) {
        double[] ret = new double[nBins];
        double min = getMinimum(d);
        boolean eksili = false;
        if (min < 0) {
            eksili = true;
        }
        double max = getMaximum(d);
        double delta = 0;
        if (min < 0 && max > 0) {
            delta = Math.abs(Math.abs(max) + Math.abs(min)) / (nBins - 1);
        }
        if ((min <= 0 && max <= 0) || (min >= 0 && max >= 0)) {
            delta = Math.abs(Math.abs(max) - Math.abs(min)) / (nBins - 1);
        }
        int index = 0;
        for (int i = 0; i < d.length; i++) {
            index = (int) Math.round((d[i] - min) / delta);
            if (index >= ret.length) {
                index = ret.length - 1;
            }
            if (index < 0) {
                index = 0;
            }
            if (d[i] == min) {
                int a = 1;
            }
            ret[index]++;
        }
        return ret;
    }

    /**
     * generate n different colors
     *
     * @param n
     * @return
     */
    public static Color[] generateColor(int n) {
        Color[] cl = new Color[n];
        if (n == 1) {
            MersenneTwister r = new MersenneTwister();
            int red = new MersenneTwister(r.nextInt(1000)).nextInt(255);
            int green = new MersenneTwister(r.nextInt(10)).nextInt(255);
            int blue = new MersenneTwister(r.nextInt(100)).nextInt(255);
            cl[0] = new Color(red, green, blue);
        } else if (n == 2) {
            cl[0] = new Color(255, 0, 0);
            cl[1] = new Color(0, 0, 255);
        } else if (n == 3) {
            cl[0] = new Color(255, 0, 0);
            cl[1] = new Color(0, 255, 0);
            cl[2] = new Color(0, 0, 255);
        } else {
            Random r = new Random();
            for (int i = 0; i < n; i++) {
                int red = UniqueRandomNumbers.getUniqueNumber(0, 255);
                int green = UniqueRandomNumbers.getUniqueNumber(0, 255);
                int blue = UniqueRandomNumbers.getUniqueNumber(0, 255);
                cl[i] = new Color(red, green, blue);
            }
        }
        return cl;
    }

    public static double[][] getHistogram(double[][] array) {
        double[][] d = FactoryMatrix.clone(array);
        d = transpose(d);
        int nBins = (int) (getMaximum(array) - getMinimum(array)) + 1;
        double[][] ret = new double[d.length][nBins];
        for (int i = 0; i < d.length; i++) {
            ret[i] = getHistogram(d[i], nBins);
        }
        return transpose(ret);
    }

    public static double[][] getHistogram(double[][] array, int nBins) {
        double[][] d = FactoryMatrix.clone(array);
        d = transpose(d);
        double[][] ret = new double[d.length][nBins];
        for (int i = 0; i < d.length; i++) {
            ret[i] = getHistogram(d[i], nBins);
        }
//        return transpose(ret);
        return ret;
    }

    public static double[][] hist(double[][] array) {
        return getHistogram(array);
    }

    public static double[][] hist(double[][] array, int nBins) {
        return getHistogram(array, nBins);
    }

    public static double[] hist(double[] array, int nBins) {
        return getHistogram(array, nBins);
    }

    public static int[] hist(int[] array, int nBins) {
        return FactoryUtils.toIntArray1D(getHistogram(FactoryUtils.toDoubleArray1D(array), nBins));
    }

    public static double[][] shiftOnRow(double[][] d, int q) {
        double[][] ret = new double[d.length][d[0].length];
        if (q >= 0) {
            for (int i = 0; i < d.length; i++) {
                for (int j = q; j < d[0].length; j++) {
                    ret[i][j] = d[i][j - q];
                }
            }
        } else {
            for (int i = 0; i < d.length; i++) {
                for (int j = 0; j < d[0].length + q; j++) {
                    ret[i][j] = d[i][j - q];
                }
            }
        }
        return ret;
    }

    public static double[][] shiftOnColumn(double[][] d, int q) {

        double[][] ret = new double[d.length][d[0].length];
        if (q >= 0) {
            for (int row = 0; row < d.length - q; row++) {
                for (int column = 0; column < d[0].length; column++) {
                    ret[row + q][column] = d[row][column];
                }
            }
        } else {
            for (int row = 0; row < d.length + q; row++) {
                for (int column = 0; column < d[0].length; column++) {
                    ret[row][column] = d[row - q][column];
                }
            }
        }
        return ret;
    }

    public static double[][] subtract(double[][] a, double[][] b) {
        double[][] d = new double[a.length][a[0].length];
        if (isIdenticalMatrix(a, b)) {
            for (int i = 0; i < a.length; i++) {
                for (int j = 0; j < a[0].length; j++) {
                    d[i][j] = a[i][j] - b[i][j];
                }
            }
        } else {

        }
        return d;
    }

    public static double[][] subtractWithThreshold(double[][] a, double[][] b, double thr) {
        double[][] d = new double[a.length][a[0].length];
        if (isIdenticalMatrix(a, b)) {
            for (int i = 0; i < a.length; i++) {
                for (int j = 0; j < a[0].length; j++) {
                    if (a[i][j] - b[i][j] < thr) {
                        d[i][j] = 0;
                    } else {
                        d[i][j] = 255;
                    }

                }
            }
        } else {

        }
        return d;
    }

    private static boolean isIdenticalMatrix(double[][] a, double[][] b) {
        if (a.length == b.length && a[0].length == b[0].length) {
            return true;
        } else {
            return false;
        }
    }

    public static double[][] add(double[][] a, double[][] b) {
        double[][] d = new double[a.length][a[0].length];
        if (isIdenticalMatrix(a, b)) {
            for (int i = 0; i < a.length; i++) {
                for (int j = 0; j < a[0].length; j++) {
                    d[i][j] = a[i][j] + b[i][j];
                }
            }
        } else {

        }
        return d;
    }

    /**
     * compute eucledian distance between two points in n dimensioanal space
     *
     * @param tr
     * @param test
     * @return distance
     */
    public static double getEucledianDistance(double[] tr, double[] test) {
        double ret = 0;
        for (int i = 0; i < tr.length; i++) {
            ret += Math.pow((tr[i] - test[i]), 2);
        }
        ret = Math.sqrt(ret);
        return ret;
    }

    /**
     * compute eucledian distance between two points in n dimensioanal space
     * except last element since last element is used as class label or target
     * function
     *
     * @param tr
     * @param test
     * @return distance
     */
    public static double getEucledianDistanceExceptLastElement(double[] tr, double[] test) {
        double ret = 0;
        for (int i = 0; i < tr.length - 1; i++) {
            ret += Math.pow((tr[i] - test[i]), 2);
        }
        ret = Math.sqrt(ret);
        return ret;
    }

    private static BigInteger getFactorial(int n) {
        BigInteger fact = BigInteger.ONE;
        for (int i = n; i > 1; i--) {
            fact = fact.multiply(new BigInteger(Integer.toString(i)));
        }
        return fact;
    }

    public static BigInteger combination(int m, int n) {
        BigInteger mFact = getFactorial(m);
        BigInteger nFact = getFactorial(n);
        BigInteger mminusnFact = getFactorial(m - n);
        BigInteger total = mFact.divide(nFact.multiply(mminusnFact));
        return total;
    }

    public static void readln() {
        try {
            System.out.println("press enter to proceed");
            System.in.read();
        } catch (IOException ex) {
            Logger.getLogger(FactoryUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static double[][] threshold(double[][] d, int t) {
        double[][] ret = new double[d.length][d[0].length];
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                if (d[i][j] > t) {
                    ret[i][j] = 255;
                } else {
                    ret[i][j] = 0;
                }
            }
        }
        return ret;
    }

    /**
     * Matlab compatible see code of imnoise in Matlab
     *
     * @param d
     * @param m
     * @param v
     * @return
     */
    public static double[][] addGaussianNoise(double[][] d, double m, double v) {
        double[][] r = new double[d.length][d[0].length];
        Random rnd = new Random();
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                r[i][j] = Math.round(m + d[i][j] + rnd.nextGaussian() * Math.sqrt(v));
            }
        }
        return r;
    }

    /**
     *
     * @param d
     * @param freq : 0..1
     * @return
     */
    public static double[][] addSaltAndPepperNoise(double[][] d, double freq) {
        double[][] r = clone(d);
        Random rnd = new Random();
        int nr = d.length;
        int nc = d[0].length;
        int max = (int) (nr * nc * freq);
        for (int i = 0; i < max; i++) {
            int pr = rnd.nextInt(nr);
            int pc = rnd.nextInt(nc);
            if (rnd.nextBoolean()) {
                r[pr][pc] = 255;
            } else {
                r[pr][pc] = 0;
            }

        }
        return r;
    }

    /**
     * Reads a CSV-file from disk into a 2D double array.
     *
     * @param filename
     * @param separator Separator character between values.
     * @param headerLines Number of header lines to skip before reading data.
     * @return 2D double array
     */
    public static double[][] readCSV(String filename, char separator, int headerLines) {
        BufferedReader br = null;
        java.util.List<String[]> values = null;
        try {
            br = new BufferedReader(new FileReader(filename));
            CSVReader cr = new CSVReader(br, separator, '\"', '\\', headerLines);
            values = cr.readAll();
            cr.close();
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FactoryUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FactoryUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        int numRows = values.size();
        int numCols = values.get(0).length;
        double[][] ret = new double[numRows][numCols];
        for (int row = 0; row < numRows; row++) {
            String[] rowValues = values.get(row);
            for (int col = 0; col < numCols; col++) {
                ret[row][col] = Double.parseDouble(rowValues[col]);
            }
        }
        return ret;
    }

    public static float[][] toFloatArray2D(int[][] array) {
        int nr = array.length;
        int nc = array[0].length;
        float[][] ret = new float[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = (float) array[i][j];
            }
        }
        return ret;
    }

    public static float[][] toFloatArray2D(long[][] array) {
        int nr = array.length;
        int nc = array[0].length;
        float[][] ret = new float[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = (float) array[i][j];
            }
        }
        return ret;
    }

    public static float[][] toFloatArray2D(short[][] array) {
        int nr = array.length;
        int nc = array[0].length;
        float[][] ret = new float[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = (float) array[i][j];
            }
        }
        return ret;
    }

    public static float[][] toFloatArray2D(byte[][] array) {
        int nr = array.length;
        int nc = array[0].length;
        float[][] ret = new float[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = (float) array[i][j];
            }
        }
        return ret;
    }

    public static float[][] toFloatArray2D(String[][] array) {
        int nr = array.length;
        int nc = array[0].length;
        float[][] ret = new float[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = Float.parseFloat(array[i][j]);
            }
        }
        return ret;
    }

    public static short[][] toShortArray2D(float[][] array) {
        int nr = array.length;
        int nc = array[0].length;
        short[][] ret = new short[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = (short) array[i][j];
            }
        }
        return ret;
    }

    public static short[][] toShortArray2D(int[][] array) {
        int nr = array.length;
        int nc = array[0].length;
        short[][] ret = new short[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = (short) array[i][j];
            }
        }
        return ret;
    }

    public static short[][] toShortArray2D(long[][] array) {
        int nr = array.length;
        int nc = array[0].length;
        short[][] ret = new short[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = (short) array[i][j];
            }
        }
        return ret;
    }

    public static short[][] toShortArray2D(byte[][] array) {
        int nr = array.length;
        int nc = array[0].length;
        short[][] ret = new short[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = (short) array[i][j];
            }
        }
        return ret;
    }

    public static short[][] toShortArray2D(String[][] array) {
        int nr = array.length;
        int nc = array[0].length;
        short[][] ret = new short[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = Short.parseShort(array[i][j]);
            }
        }
        return ret;
    }

    public static byte[][] toByteArray2D(float[][] array) {
        int nr = array.length;
        int nc = array[0].length;
        byte[][] ret = new byte[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = (byte) array[i][j];
            }
        }
        return ret;
    }

    public static byte[][] toByteArray2D(int[][] array) {
        int nr = array.length;
        int nc = array[0].length;
        byte[][] ret = new byte[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = (byte) array[i][j];
            }
        }
        return ret;
    }

    public static byte[][] toByteArray2D(long[][] array) {
        int nr = array.length;
        int nc = array[0].length;
        byte[][] ret = new byte[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = (byte) array[i][j];
            }
        }
        return ret;
    }

    public static byte[][] toByteArray2D(short[][] array) {
        int nr = array.length;
        int nc = array[0].length;
        byte[][] ret = new byte[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = (byte) array[i][j];
            }
        }
        return ret;
    }

    public static byte[][] toByteArray2D(String[][] array) {
        int nr = array.length;
        int nc = array[0].length;
        byte[][] ret = new byte[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = Byte.parseByte(array[i][j]);
            }
        }
        return ret;
    }

    public static char[][] toCharArray2D(double[][] array) {
        int nr = array.length;
        int nc = array[0].length;
        char[][] ret = new char[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = (char) array[i][j];
            }
        }
        return ret;
    }

    public static char[][] toCharArray2D(float[][] array) {
        int nr = array.length;
        int nc = array[0].length;
        char[][] ret = new char[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = (char) array[i][j];
            }
        }
        return ret;
    }

    public static char[][] toCharArray2D(int[][] array) {
        int nr = array.length;
        int nc = array[0].length;
        char[][] ret = new char[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = (char) array[i][j];
            }
        }
        return ret;
    }

    public static char[][] toCharArray2D(long[][] array) {
        int nr = array.length;
        int nc = array[0].length;
        char[][] ret = new char[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = (char) array[i][j];
            }
        }
        return ret;
    }

    public static char[][] toCharArray2D(short[][] array) {
        int nr = array.length;
        int nc = array[0].length;
        char[][] ret = new char[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = (char) array[i][j];
            }
        }
        return ret;
    }

    public static char[][] toCharArray2D(String[][] array) {
        int nr = array.length;
        int nc = array[0].length;
        char[][] ret = new char[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = (char) Byte.parseByte(array[i][j]);
            }
        }
        return ret;
    }

    public static long[][] toLongArray2D(float[][] array) {
        int nr = array.length;
        int nc = array[0].length;
        long[][] ret = new long[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = (long) array[i][j];
            }
        }
        return ret;
    }

    public static long[][] toLongArray2D(int[][] array) {
        int nr = array.length;
        int nc = array[0].length;
        long[][] ret = new long[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = (long) array[i][j];
            }
        }
        return ret;
    }

    public static long[][] toLongArray2D(byte[][] array) {
        int nr = array.length;
        int nc = array[0].length;
        long[][] ret = new long[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = (long) array[i][j];
            }
        }
        return ret;
    }

    public static long[][] toLongArray2D(char[][] array) {
        int nr = array.length;
        int nc = array[0].length;
        long[][] ret = new long[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = (long) array[i][j];
            }
        }
        return ret;
    }

    public static long[][] toLongArray2D(String[][] array) {
        int nr = array.length;
        int nc = array[0].length;
        long[][] ret = new long[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = Long.parseLong(array[i][j]);
            }
        }
        return ret;
    }

    public static String[][] toStringArray2D(float[][] array) {
        int nr = array.length;
        int nc = array[0].length;
        String[][] ret = new String[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = "" + array[i][j];
            }
        }
        return ret;
    }

    public static String[][] toStringArray2D(long[][] array) {
        int nr = array.length;
        int nc = array[0].length;
        String[][] ret = new String[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = "" + array[i][j];
            }
        }
        return ret;
    }

    public static String[][] toStringArray2D(int[][] array) {
        int nr = array.length;
        int nc = array[0].length;
        String[][] ret = new String[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = "" + array[i][j];
            }
        }
        return ret;
    }

    public static String[][] toStringArray2D(byte[][] array) {
        int nr = array.length;
        int nc = array[0].length;
        String[][] ret = new String[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = "" + array[i][j];
            }
        }
        return ret;
    }

    public static String[][] toStringArray2D(char[][] array) {
        int nr = array.length;
        int nc = array[0].length;
        String[][] ret = new String[nr][nc];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                ret[i][j] = "" + array[i][j];
            }
        }
        return ret;
    }

    public static List clone(List lst) {
        if (lst==null) {
            return null;
        }
        return Arrays.asList(lst.toArray());
    }
    

    public static double[] clone(double[] p) {
        double[] ret = new double[p.length];
        System.arraycopy(p, 0, ret, 0, p.length);
        return ret;
    }

    public static float[] clone(float[] p) {
        float[] ret = new float[p.length];
        System.arraycopy(p, 0, ret, 0, p.length);
        return ret;
    }

    public static int[] clone(int[] p) {
        int[] ret = new int[p.length];
        System.arraycopy(p, 0, ret, 0, p.length);
        return ret;
    }

    public static byte[] clone(byte[] p) {
        byte[] ret = new byte[p.length];
        System.arraycopy(p, 0, ret, 0, p.length);
        return ret;
    }

    public static char[] clone(char[] p) {
        char[] ret = new char[p.length];
        System.arraycopy(p, 0, ret, 0, p.length);
        return ret;
    }

    public static long[] clone(long[] p) {
        long[] ret = new long[p.length];
        System.arraycopy(p, 0, ret, 0, p.length);
        return ret;
    }

    public static short[] clone(short[] p) {
        short[] ret = new short[p.length];
        System.arraycopy(p, 0, ret, 0, p.length);
        return ret;
    }

    public static String[] clone(String[] p) {
        String[] ret = new String[p.length];
        System.arraycopy(p, 0, ret, 0, p.length);
        return ret;
    }

    public static boolean[] clone(boolean[] p) {
        boolean[] ret = new boolean[p.length];
        System.arraycopy(p, 0, ret, 0, p.length);
        return ret;
    }

    public static double[][] clone(double[][] p) {
        double[][] ret = new double[p.length][p[0].length];
        int nr = p.length;
        for (int i = 0; i < nr; i++) {
            System.arraycopy(p[i], 0, ret[i], 0, p[0].length);
        }
        return ret;
    }

    public static float[][] clone(float[][] p) {
        float[][] ret = new float[p.length][p[0].length];
        int nr = p.length;
        for (int i = 0; i < nr; i++) {
            System.arraycopy(p[i], 0, ret[i], 0, p[0].length);
        }
        return ret;
    }

    public static int[][] clone(int[][] p) {
        int[][] ret = new int[p.length][p[0].length];
        int nr = p.length;
        for (int i = 0; i < nr; i++) {
            System.arraycopy(p[i], 0, ret[i], 0, p[0].length);
        }
        return ret;
    }

    public static long[][] clone(long[][] p) {
        long[][] ret = new long[p.length][p[0].length];
        int nr = p.length;
        for (int i = 0; i < nr; i++) {
            System.arraycopy(p[i], 0, ret[i], 0, p[0].length);
        }
        return ret;
    }

    public static short[][] clone(short[][] p) {
        short[][] ret = new short[p.length][p[0].length];
        int nr = p.length;
        for (int i = 0; i < nr; i++) {
            System.arraycopy(p[i], 0, ret[i], 0, p[0].length);
        }
        return ret;
    }

    public static byte[][] clone(byte[][] p) {
        byte[][] ret = new byte[p.length][p[0].length];
        int nr = p.length;
        for (int i = 0; i < nr; i++) {
            System.arraycopy(p[i], 0, ret[i], 0, p[0].length);
        }
        return ret;
    }

    public static boolean[][] clone(boolean[][] p) {
        boolean[][] ret = new boolean[p.length][p[0].length];
        int nr = p.length;
        for (int i = 0; i < nr; i++) {
            System.arraycopy(p[i], 0, ret[i], 0, p[0].length);
        }
        return ret;
    }

    public static String[][] clone(String[][] p) {
        String[][] ret = new String[p.length][p[0].length];
        int nr = p.length;
        for (int i = 0; i < nr; i++) {
            System.arraycopy(p[i], 0, ret[i], 0, p[0].length);
        }
        return ret;
    }

    public static long getTotalMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    public static long getMaxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

    public static long getFreeMemory() {
        return Runtime.getRuntime().freeMemory();
    }

    public static int getAvailableProcessors() {
        return Runtime.getRuntime().availableProcessors();
    }

    public static double[] exp(double[] d) {
        int n = d.length;
        double[] ret = new double[n];
        for (int i = 0; i < n; i++) {
            ret[i] = Math.exp(d[i]);
        }
        return ret;
    }

    public static double[] divide(double[] d, double sum) {
        int n = d.length;
        double[] ret = new double[n];
        for (int i = 0; i < n; i++) {
            ret[i] = d[i] / sum;
        }
        return ret;
    }

    public static double[] multiply(double[] d, double sum) {
        int n = d.length;
        double[] ret = new double[n];
        for (int i = 0; i < n; i++) {
            ret[i] = d[i] * sum;
        }
        return ret;
    }

    public static double[] add(double[] d, double sum) {
        int n = d.length;
        double[] ret = new double[n];
        for (int i = 0; i < n; i++) {
            ret[i] = d[i] + sum;
        }
        return ret;
    }

    public static double[] subtract(double[] a, double val) {
        int n = a.length;
        double[] d = new double[n];
        for (int i = 0; i < n; i++) {
            d[i] = a[i] - val;
        }
        return d;
    }

    public static String formatBinary(int p) {
        return formatBinary(8, p);
    }

    public static String formatBinary(int n, int p) {
        char[] chars = new char[n];
        for (int j = 0; j < n; j++) {
            chars[j] = (char) (((p >>> (n - j - 1)) & 1) + '0');
        }
        return String.valueOf(chars);
    }

    public static void copyFile(File sourceFile, File destFile) {
        if (!destFile.exists()) {
            try {
                destFile.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(FactoryUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        FileChannel source = null;
        FileChannel destination = null;
        try {
            try {
                source = new RandomAccessFile(sourceFile, "rw").getChannel();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FactoryUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                destination = new RandomAccessFile(destFile, "rw").getChannel();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FactoryUtils.class.getName()).log(Level.SEVERE, null, ex);
            }

            long position = 0;
            long count = 0;
            try {
                count = source.size();
            } catch (IOException ex) {
                Logger.getLogger(FactoryUtils.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                source.transferTo(position, count, destination);
            } catch (IOException ex) {
                Logger.getLogger(FactoryUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        } finally {
            if (source != null) {
                try {
                    source.close();
                } catch (IOException ex) {
                    Logger.getLogger(FactoryUtils.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (destination != null) {
                try {
                    destination.close();
                } catch (IOException ex) {
                    Logger.getLogger(FactoryUtils.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static String[][] readArffString(String path, int classIndex) {
        String[][] str = null;
        try {
            Instances wekaInstance = ConverterUtils.DataSource.read(path);
            wekaInstance.setClassIndex(classIndex);
            Instance ins = wekaInstance.instance(0);
            str = new String[wekaInstance.numInstances()][ins.numAttributes()];
            for (int i = 0; i < wekaInstance.numInstances(); i++) {
                ins = wekaInstance.instance(i);
                for (int j = 0; j < ins.numAttributes(); j++) {
                    str[i][j] = ins.stringValue(j);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(CMatrix.class.getName()).log(Level.SEVERE, null, ex);
        }
        return str;
    }

    public static double getStd(double[] d) {
        return std(d);
    }

    public static double[][] deleteRowsFrom(double[][] dimg, int index) {
        double[][] ret = new double[index][dimg[0].length];
        for (int i = 0; i < index; i++) {
            ret[i] = dimg[i];
        }
        return ret;
    }

    public static int[] convertInt(List<Integer> lst) {
        int[] ret = lst.stream().filter(i -> i != null).mapToInt(i -> i).toArray();
        return ret;
    }

    public static double[] convertDouble(List<Double> lst) {
        double[] ret = lst.stream().filter(i -> i != null).mapToDouble(i -> i).toArray();
        return ret;
    }

    public static long[] convertLong(List<Long> lst) {
        long[] ret = lst.stream().filter(i -> i != null).mapToLong(i -> i).toArray();
        return ret;
    }

    public static boolean isPointInsidePolygon(Point[] polygon, Point point) {
        int i;
        int j;
        boolean result = false;
        for (i = 0, j = polygon.length - 1; i < polygon.length; j = i++) {
            if ((polygon[i].y > point.y) != (polygon[j].y > point.y)
                    && (point.x < (polygon[j].x - polygon[i].x) * (point.y - polygon[i].y) / (polygon[j].y - polygon[i].y) + polygon[i].x)) {
                result = !result;
            }
        }
        return result;
    }

    public static double getDistanceFromLatLonInKm(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371; // Radius of the earth in km
        double dLat = deg2rad(lat2 - lat1);  // deg2rad below
        double dLon = deg2rad(lon2 - lon1);
        double a
                = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c; // Distance in km
        return d;
    }

    public static double deg2rad(double deg) {
        return deg * (Math.PI / 180);
    }

    public static double[] getCosineSimilarity(double[][] d1, double[][] d2) {
        int n = d1.length;
        double[] ret = new double[n];
        for (int i = 0; i < n; i++) {
            ret[i] = getCosineSimilarity(d1[i], d2[i]);
        }
        return ret;
    }

    public static double getCosineSimilarity(double[] vectorA, double[] vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

}
