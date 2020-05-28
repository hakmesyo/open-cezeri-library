package cezeri.search.meta_heuristic.ant_colony_optimization;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Thanks: https://github.com/phil8192/tsp-java
 * Class directory: https://github.com/phil8192/tsp-java/blob/master/src/main/java/net/parasec/tsp/TSPReader.java
 */
public class TspReader {


    static double[] coordinateArray;

    static int counter = 0;

    static double[][] data;
    static double[][] distances;
    static String EdgeWeightType;

    static boolean isData = false;
    static double xd, yd, rij, tij;


    public static double[][] getDistances(String path, String file) {
        return TspReader.getDistances(new File(path, file).toString());
    }

    public static double[][] getCoordinates(String path, String file) {
        return TspReader.getCoordinates(new File(path, file).toString());
    }

    public static double[][] getCoordinates(String file) {
        isData = false;
        counter = 0;
        stripCoordinates(file);
        System.out.println();
        return data;
    }

    public static double[][] getDistances(String file) {
        isData = false;
        counter = 0;
        stripCoordinates(file);
        calculateDistances();
        return distances;
    }

    public static String getEdgeWeightType(String file) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String input = null;
            try {
                while ((input = in.readLine()) != null) {
                    if (input.contains("EDGE_WEIGHT_TYPE")) {
                        Pattern p = Pattern.compile("EDGE_WEIGHT_TYPE : (.+)");
                        Matcher m = p.matcher(input);
                        if (m.matches()) {
                            return m.group(1);
                        } else {
                            return null;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void read(String file) {
        isData = false;
        counter = 0;
        if (file.length() > 0) {
            stripCoordinates(file);
            calculateDistances();
            print();
        } else {
            System.out.print("Enter the file as your argument: ");
            Scanner scan = new Scanner(System.in);
            stripCoordinates(scan.nextLine());
            calculateDistances();
            print();
        }
        System.exit(0);
    }

    public static void print() {
        System.out.println("\nCorresponding distance matrix");
        StringBuilder dataString = new StringBuilder();
        dataString.append("\t");
        for (int i = 0; i < distances.length; i++) {
            dataString.append((i) + ":\t");
            for (int j = 0; j < distances.length; j++) {
                dataString.append(distances[i][j] + "\t");
                if (j == distances.length - 1) {
                    dataString.append("\n\t");
                }
            }
        }
        System.out.println("\n" + dataString.toString());
    }


    private static void calculateDistances() {
        if (EdgeWeightType.equals("ATT")) {
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data.length; j++) {
                    if (i == j) {
                        distances[i][j] = 0;
                    } else {
                        xd = data[i][0] - data[j][0];
                        yd = data[i][1] - data[j][1];
                        rij = (float) Math.sqrt(((xd * xd) + (yd * yd)) / 10.0);
                        tij = Math.round(rij);
                        if (tij < rij) {
                            distances[i][j] = tij + 1;
                        } else {
                            distances[i][j] = tij;
                        }
                    }
                }
            }
        } else if (EdgeWeightType.equals("EUC_2D")) {
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data.length; j++) {
                    if (i == j) {
                        distances[i][j] = 0;
                    } else {
                        xd = data[i][0] - data[j][0];
                        yd = data[i][1] - data[j][1];
                        distances[i][j] = Math.round(Math.sqrt((xd * xd) + (yd * yd)));
                    }
                }
            }
        }
    }

    private static void stripCoordinates(String file) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String input = null;
            try {
                while ((input = in.readLine()) != null) {
                    input = input.trim();
                    input = input.replace("   ", " ");
                    input = input.replace("  ", " ");
                    if (input.contains("DIMENSION") && !isData) {
                        Pattern p = Pattern.compile("(\\d+)");
                        Matcher m = p.matcher(input);
                        if (m.find()) {
                            distances = new double[Integer.parseInt(m.group(0))][Integer.parseInt(m.group(0))];
                            data = new double[Integer.parseInt(m.group(0))][2];
                        }
                    }
                    if (input.contains("EDGE_WEIGHT_TYPE") && !isData) {
                        Pattern p = Pattern.compile("EDGE_WEIGHT_TYPE : (.+)");
                        Matcher m = p.matcher(input);
                        if (m.matches()) {
                            EdgeWeightType = m.group(1);
                        }
                    }
                    if (input.contains("NODE_COORD_SECTION") && !isData) {
                        isData = true;
                    } else if (isData) {
                        if (!input.equals("EOF")) {
                            String[] coordinates = input.split(" ");
                            coordinateArray = new double[2];
                            coordinateArray[0] = Double.parseDouble(coordinates[1]);
                            coordinateArray[1] = Double.parseDouble(coordinates[2]);
                            data[counter++] = coordinateArray;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
