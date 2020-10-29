/*
 * Copyright (c) 2020. Yurii Bezliudnyi.
 * Copying without author notice is prohibited.
 *
 */

package com.yube;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private final Scanner scanner = new Scanner(System.in);
    private final String doubleNumberPatternString = "-?\\d+(\\.\\d+)?";
    private final String vectorPatternString = "\\[((" + doubleNumberPatternString + ",\\s+)+" + doubleNumberPatternString + ")?\\]";
    Pattern vectorPattern = Pattern.compile(vectorPatternString);
    Pattern doubleNumberPattern = Pattern.compile(doubleNumberPatternString);

    public static void main(String[] args) {
        Main main = new Main();
        main.process();
    }

    public void process() {
        TransportProblem problem = readTransportProblem();
        TransportProblemSolver transportProblemSolver = new TransportProblemSolver();
        transportProblemSolver.solve(problem);
    }

    private TransportProblem readTransportProblem() {
        int m = Integer.parseInt(read("Введіть кількість постачальників:", "\\d{1,2}"));
        int n = Integer.parseInt(read("Введіть кількість споживачів:", "\\d{1,2}"));
        if (m < 1 || n < 1) {
            throw new IllegalArgumentException("Кількість споживачів чи постачальників не може бути меншою за 1");
        }
        double[][] c;
        while (true) {
            try {
                c = readMatrix("Траспортні витрати", m, n);
                validateTransportationCostsMatrix(c);
                break;
            } catch (Exception e) {
                System.out.printf("Матриця введена неправильно, причина: '%s'. Спробуйте ввести матрицю знову.\n", e.getMessage());
            }
        }
        System.out.println("Введіть значення вектору ресурсів:");
        double[] a = readVector(m);
        System.out.println("Введіть значення вектору потреб:");
        double[] b = readVector(n);
        TransportProblem transportProblem = new TransportProblem();
        transportProblem.setM(m);
        transportProblem.setN(n);
        transportProblem.setA(a);
        transportProblem.setB(b);
        transportProblem.setC(c);
        return transportProblem;
    }

    private String read(String question, String pattern) {
        while (true) {
            System.out.println(question);
            String line = scanner.nextLine().trim();
            if (line.matches(pattern)) return line;
            System.out.println("Неправильно введене значення, спробуйте знову.");
        }
    }

    private double[][] readMatrix(String label, int columns, int rows) {
        double[][] result = new double[columns][rows];
        System.out.printf("Введіть значення матриці '%s'\n", label);
        for (int i = 0; i < columns; i++) {
            System.out.printf("Введіть значення %d-го рядка матриці:\n", i);
            result[i] = readVector(rows);
        }
        return result;
    }

    private double[] readVector(int size) {
        double[] result = new double[size];
        boolean flag = false;
        String vector = null;
        while (!flag) {
            String line = scanner.nextLine();
            if (!line.trim().isEmpty()) {
                if (vectorPattern.matcher(line).matches()) {
                    vector = line;
                    flag = true;
                } else {
                    System.out.println("Ви неправильно ввели значення вектору, спробуйте знову:");
                    flag = false;
                }
            }
        }

        int i = 0;
        Matcher numberMatcher = doubleNumberPattern.matcher(vector);
        while (numberMatcher.find()) {
            String number = numberMatcher.group();
            result[i++] = Double.parseDouble(number);
        }
        return result;
    }

    private void validateTransportationCostsMatrix(double[][] matrix) {
        int columns = matrix.length;
        int rows = matrix[0].length;
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                if (matrix[i][j] < 0) {
                    throw new IllegalArgumentException("Матриця траспортних витрат не може містити від'ємні значення");
                }
            }
        }
    }
}
