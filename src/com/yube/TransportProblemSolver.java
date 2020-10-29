package com.yube;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class TransportProblemSolver {

    private final static int maxIterations = 100;

    public TransportProblemSolution solve(TransportProblem transportProblem) {
        System.out.println(Utils.getEnvelope("Транспортна задача", transportProblem.toString()));
        TransportProblem closedProblem;
        if (isClosed(transportProblem)) {
            closedProblem = transportProblem;
        } else {
            closedProblem = convertToClosed(transportProblem);
        }
        TransportProblemSolution solution = solveClosed(closedProblem);
        System.out.println(Utils.getEnvelope("Розв'язок транспортної задачі", transportProblem.toString()));
        return solution;
    }

    private TransportProblemSolution solveClosed(TransportProblem transportProblem) {
        Double[][] x = findPlan(transportProblem);
        double[][] c = transportProblem.getC();
        String infoString = Utils.getSplitter() + "\n";
        infoString += "Опорний план:\n";
        infoString += Utils.matrixToString(x);
        infoString += String.format("Затрати: %.3f\n", computeFunction(x, c));
        infoString += Utils.getSplitter() + "\n";
        System.out.println(infoString);
        int nonNull = Utils.countNonNullElements(x);
        if (transportProblem.getM() + transportProblem.getN() - 1 > nonNull)
            throw new RuntimeException("Транспортна задача є виродженою");
        PlanCheckResult checkResult = checkPlan(x, c);
        Double[][] d = checkResult.getD();
        System.out.println(Utils.getEnvelope("Перевірка плану на оптимальність", checkResult.toString()));
        int iterations = 0;
        while (!checkResult.isOptimal()) {
            if (iterations++ > maxIterations) {
                throw new RuntimeException("Неможливо знайти оптимальний розв'язок, " +
                        "максимальна кількість ітерацій перевищена");
            }
            System.out.println("Виконання ітерації з покращення плану...");
            improvePlan(x, d);
            String iterationInfoString = Utils.getHeader(String.format("Ітерація #%d", iterations));
            iterationInfoString += "Покращений план:\n";
            iterationInfoString += Utils.matrixToString(x);
            iterationInfoString += String.format("Затрати: %.3f\n", computeFunction(x, c));
            iterationInfoString += Utils.getSplitter() + "\n";
            System.out.println(iterationInfoString);
            checkResult = checkPlan(x, c);
            d = checkResult.getD();
            System.out.println(Utils.getEnvelope("Перевірка плану на оптимальність", checkResult.toString()));
        }
        double function = computeFunction(x, c);
        TransportProblemSolution solution = new TransportProblemSolution();
        solution.setIterations(iterations);
        solution.setFunction(function);
        solution.setX(x);
        return solution;
    }

    private double computeFunction(Double[][] x, double[][] c) {
        double function = 0;
        int m = x.length;
        int n = x[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (x[i][j] != null) {
                    function += x[i][j] * c[i][j];
                }
            }
        }
        return function;
    }

    private void improvePlan(Double[][] x, Double[][] d) {
        Point point = Utils.findMatrixPoint(d, false, null);
        if (point == null) throw new IllegalArgumentException("Матриця D має містити хоча б один додатній елемент");
        int m = x.length;
        int n = x[0].length;
        int tx = point.getX();
        int ty = point.getY();
        x[ty][tx] = 0.0;
        List<Point> pathTail = new ArrayList<>();
        pathTail.add(point);
        List<Point> path = getPath(tx, ty, tx, ty, n, m, x, pathTail);
        if (path == null) {
            throw new RuntimeException("Не знайдено замкнутого шляху в матриці X");
        }

        String[][] signs = new String[m][n];
        String sign = "+";
        for (Point p : path) {
            if (sign.equals("+")) {
                signs[p.getY()][p.getX()] = "+";
                sign = "-";
            } else {
                signs[p.getY()][p.getX()] = "-";
                sign = "+";
            }
        }
        double theta = Double.MAX_VALUE;
        for (Point p : path) {
            if (signs[p.getY()][p.getX()].equals("-")) {
                double xVal = x[p.getY()][p.getX()];
                if (xVal > 0 && xVal < theta) {
                    theta = xVal;
                }
            }
        }
        for (Point p : path) {
            if (signs[p.getY()][p.getX()].equals("+")) {
                x[p.getY()][p.getX()] += theta;
            } else {
                x[p.getY()][p.getX()] -= theta;
            }
            if (x[p.getY()][p.getX()] == 0.0) {
                x[p.getY()][p.getX()] = null;
            }
        }
    }

    private List<Point> getPath(int xb, int yb, int xc, int yc, int n, int m, Double[][] x, List<Point> pathTail) {
        List<Point> possiblePathHeads = new ArrayList<>();
        //---------------------------Find boundaries---------------------------
        int left = 0;
        int up = 0;
        int right = n - 1;
        int down = m - 1;
        List<Integer> xList = new ArrayList<>();
        List<Integer> yList = new ArrayList<>();
        for (Point p : pathTail) {
            if (p.getY() == yc) {
                xList.add(p.getX());
            }
            if (p.getX() == xc) {
                yList.add(p.getY());
            }
        }
        xList.sort(Integer::compareTo);
        yList.sort(Integer::compareTo);
        int pivotXIndex = xList.indexOf(xc);
        int pivotYIndex = yList.indexOf(yc);

        if (xList.size() > 1) {
            if (pivotXIndex == xList.size() - 1) {
                left = xList.get(pivotXIndex - 1);
            } else if (pivotXIndex == 0) {
                right = xList.get(pivotXIndex + 1);
            } else {
                left = xList.get(pivotXIndex - 1);
                right = xList.get(pivotXIndex + 1);
            }
        }
        if (yList.size() > 1) {
            if (pivotYIndex == yList.size() - 1) {
                up = yList.get(pivotYIndex - 1);
            } else if (pivotYIndex == 0) {
                down = yList.get(pivotYIndex + 1);
            } else {
                up = yList.get(pivotYIndex - 1);
                down = yList.get(pivotYIndex + 1);
            }
        }
        //---------------------------------------------------------------------------
        for (int i = xc; i >= left; i--) {
            if (i == xb && yc == yb && pathTail.size() > 2) {
                return pathTail;
            }
            if (x[yc][i] != null && !pathTail.contains(new Point(i, yc))) {
                possiblePathHeads.add(new Point(i, yc));
            }
        }
        for (int i = xc + 1; i <= right; i++) {
            if (i == xb && yc == yb && pathTail.size() > 2) {
                return pathTail;
            }
            if (x[yc][i] != null && !pathTail.contains(new Point(i, yc))) {
                possiblePathHeads.add(new Point(i, yc));
            }
        }
        for (int j = yc; j >= up; j--) {
            if (xc == xb && j == yb && pathTail.size() > 2) {
                return pathTail;
            }
            if (x[j][xc] != null && !pathTail.contains(new Point(xc, j))) {
                possiblePathHeads.add(new Point(xc, j));
            }
        }
        for (int j = yc + 1; j <= down; j++) {
            if (xc == xb && j == yb && pathTail.size() > 2) {
                return pathTail;
            }
            if (x[j][xc] != null && !pathTail.contains(new Point(xc, j))) {
                possiblePathHeads.add(new Point(xc, j));
            }
        }
        if (possiblePathHeads.isEmpty()) return null;
        for (Point possiblePathHead : possiblePathHeads) {
            List<Point> newPathTail = new ArrayList<>(pathTail);
            newPathTail.add(possiblePathHead);
            List<Point> path = getPath(xb, yb, possiblePathHead.getX(), possiblePathHead.getY(), n, m, x, newPathTail);
            if (path != null && path.size() % 2 == 0) return path;
        }
        return null;
    }

    private PlanCheckResult checkPlan(Double[][] x, double[][] c) {
        int m = x.length;
        int n = x[0].length;
        Double[] u = new Double[m];
        Double[] v = new Double[n];
        u[0] = 0.0;
        while (Utils.countNonNullElements(u) != m
                || Utils.countNonNullElements(v) != n) {
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (x[i][j] != null) {
                        if (u[i] != null && v[j] == null) {
                            v[j] = c[i][j] - u[i];
                        } else if (u[i] == null && v[j] != null) {
                            u[i] = c[i][j] - v[j];
                        }
                    }
                }
            }
        }
        Double[][] d = new Double[m][n];
        boolean optimal = true;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (x[i][j] == null) {
                    d[i][j] = (u[i] + v[j]) - c[i][j];
                    if (d[i][j] > 0) optimal = false;
                }
            }
        }
        PlanCheckResult result = new PlanCheckResult(optimal);
        result.setU(u);
        result.setV(v);
        result.setD(d);
        return result;
    }

    private Double[][] findPlan(TransportProblem transportProblem) {
        int n = transportProblem.getN();
        int m = transportProblem.getM();
        double[] ta = new double[m];
        System.arraycopy(transportProblem.getA(), 0, ta, 0, m);
        double[] tb = new double[n];
        System.arraycopy(transportProblem.getB(), 0, tb, 0, n);
        double[][] tc = new double[m][n];
        for (int i = 0; i < m; i++) {
            System.arraycopy(transportProblem.getC()[i], 0, tc[i], 0, n);
        }
        Double[][] coefficients = new Double[m][n];
        Set<Point> points = new LinkedHashSet<>();
        while (true) {
            Point point = Utils.findMatrixPoint(tc, true, points);
            if (point == null) break;
            double min = Math.min(tb[point.getX()], ta[point.getY()]);
            coefficients[point.getY()][point.getX()] = min;
            if (min == tb[point.getX()]) {
                ta[point.getY()] -= tb[point.getX()];
                //exclude column points
                for (int y = 0; y < m; y++) {
                    points.add(new Point(point.getX(), y));
                }
            } else {
                tb[point.getX()] -= ta[point.getY()];
                //exclude row points
                for (int x = 0; x < n; x++) {
                    points.add(new Point(x, point.getY()));
                }
            }
        }
        return coefficients;
    }

    private TransportProblem convertToClosed(TransportProblem transportProblem) {
        int m, n;
        double[] a, b;
        double[][] c;
        double diff = Utils.arraySum(transportProblem.getA()) - Utils.arraySum(transportProblem.getB());
        if (diff > 0) {
            //add fictive consumer (b_n+1)
            n = transportProblem.getN() + 1;
            m = transportProblem.getM();
            a = new double[m];
            System.arraycopy(transportProblem.getA(), 0, a, 0, m);
            b = new double[n];
            System.arraycopy(transportProblem.getB(), 0, b, 0, n - 1);
            b[n - 1] = diff;
            c = new double[m][n];
            for (int i = 0; i < m; i++) {
                System.arraycopy(transportProblem.getC()[i], 0, c[i], 0, n - 1);
            }
        } else {
            //add fictive producer (a_m+1)
            n = transportProblem.getN();
            m = transportProblem.getM() + 1;
            a = new double[m];
            System.arraycopy(transportProblem.getA(), 0, a, 0, m - 1);
            a[m - 1] = -diff;
            b = new double[n];
            System.arraycopy(transportProblem.getB(), 0, b, 0, n);
            c = new double[m][n];
            for (int i = 0; i < m - 1; i++) {
                System.arraycopy(transportProblem.getC()[i], 0, c[i], 0, n);
            }
        }

        TransportProblem closedProblem = new TransportProblem();
        closedProblem.setM(m);
        closedProblem.setN(n);
        closedProblem.setA(a);
        closedProblem.setB(b);
        closedProblem.setC(c);
        return closedProblem;
    }

    private boolean isClosed(TransportProblem transportProblem) {
        return Utils.arraySum(transportProblem.getA()) == Utils.arraySum(transportProblem.getB());
    }
}
