package com.yube;

public class TransportProblem {

    private int m, n;
    private double[][] c;
    private double[] a, b;

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public double[][] getC() {
        return c;
    }

    public void setC(double[][] c) {
        this.c = c;
    }

    public double[] getA() {
        return a;
    }

    public void setA(double[] a) {
        this.a = a;
    }

    public double[] getB() {
        return b;
    }

    public void setB(double[] b) {
        this.b = b;
    }

    @Override
    public String toString() {
        return String.format("Кількість постачальників: %d\n", m) +
                String.format("Кількість споживачів: %d\n", n) +
                String.format("Матриця транспортних витрат: \n%s", Utils.matrixToString(c)) +
                String.format("Запаси: %s\n", Utils.arrayToString(a)) +
                String.format("Витрати: %s\n", Utils.arrayToString(b));
    }
}
