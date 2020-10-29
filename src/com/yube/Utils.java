package com.yube;

import java.util.Set;

public class Utils {

    public static int countNonNullElements(Double[] vector) {
        int count = 0;
        for (int i = 0; i < vector.length; i++) {
            if (vector[i] != null) count++;
        }
        return count;
    }

    public static int countNonNullElements(Double[][] matrix) {
        int count = 0;
        int rows = matrix.length;
        int columns = matrix[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (matrix[i][j] != null) count++;
            }
        }
        return count;
    }

    public static Point findMatrixPoint(Double[][] matrix, boolean min, Set<Point> pointsExclusion) {
        int rows = matrix.length;
        int columns = matrix[0].length;
        int x = -1;
        int y = -1;
        double val = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (matrix[i][j] != null &&
                        (pointsExclusion == null || !pointsExclusion.contains(new Point(j, i)))) {
                    y = i;
                    x = j;
                    val = matrix[i][j];
                }
            }
        }
        if (x == -1) return null;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (matrix[i][j] != null &&
                        (pointsExclusion == null || !pointsExclusion.contains(new Point(j, i))) &&
                        ((min && matrix[i][j] < val) || (!min && matrix[i][j] > val))) {
                    y = i;
                    x = j;
                    val = matrix[i][j];
                }
            }
        }
        return new Point(x, y);
    }

    public static Point findMatrixPoint(double[][] matrix, boolean min, Set<Point> pointsExclusion) {
        int rows = matrix.length;
        int columns = matrix[0].length;
        int x = -1;
        int y = -1;
        double val = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (pointsExclusion == null || !pointsExclusion.contains(new Point(j, i))) {
                    y = i;
                    x = j;
                    val = matrix[i][j];
                }
            }
        }
        if (x == -1) return null;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if ((pointsExclusion == null || !pointsExclusion.contains(new Point(j, i)))
                        && ((min && matrix[i][j] < val) || (!min && matrix[i][j] > val))) {
                    y = i;
                    x = j;
                    val = matrix[i][j];
                }
            }
        }
        return new Point(x, y);
    }

    public static double arraySum(double[] arr) {
        double res = 0;
        for (double v : arr) {
            res += v;
        }
        return res;
    }

    public static String arrayToString(double[] arr) {
        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < arr.length - 1; i++) {
            result.append(String.format("%.3f, ", arr[i]));
        }
        if (arr.length > 0) {
            result.append(String.format("%.3f", arr[arr.length - 1]));
        }
        result.append("]");
        return result.toString();
    }

    public static String arrayToString(Double[] arr) {
        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] == null) {
                result.append("*, ");
            } else {
                result.append(String.format("%.3f, ", arr[i]));
            }
        }
        if (arr.length > 0) {
            if (arr[arr.length - 1] == null) {
                result.append("*");
            } else {
                result.append(String.format("%.3f", arr[arr.length - 1]));
            }
        }
        result.append("]");
        return result.toString();
    }

    public static String matrixToString(double[][] matrix) {
        StringBuilder result = new StringBuilder();
        for (double[] row : matrix) {
            for (double element : row) {
                result.append(String.format("%9.3f", element));
            }
            result.append("\n");
        }
        return result.toString();
    }

    public static String matrixToString(Double[][] matrix) {
        StringBuilder result = new StringBuilder();
        for (Double[] row : matrix) {
            for (Double element : row) {
                if (element == null) {
                    result.append(center("*", 9));
                } else {
                    result.append(String.format("%9.3f", element));
                }
            }
            result.append("\n");
        }
        return result.toString();
    }

    public static String center(String text, int len) {
        String out = String.format("%" + len + "s%s%" + len + "s", "", text, "");
        float mid = (out.length() / 2);
        float start = mid - (len / 2);
        float end = start + len;
        return out.substring((int) start, (int) end);
    }

    public static String getEnvelope(String header, String content) {
        String envelope = getHeader(header);
        envelope += content + "\n";
        envelope += Utils.getSplitter() + "\n";
        return envelope;
    }

    public static String getHeader(String content) {
        String splitter = getSplitter();
        return splitter + "\n" +
                Utils.center(content, 100) + "\n" +
                splitter + "\n";
    }

    public static String getSplitter() {
        return "----------------------------------------------------------------------------------------------------";
    }
}
