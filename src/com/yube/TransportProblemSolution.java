package com.yube;

public class TransportProblemSolution {

    private int iterations;
    private Double[][] x;
    private double function;

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public Double[][] getX() {
        return x;
    }

    public void setX(Double[][] x) {
        this.x = x;
    }

    public double getFunction() {
        return function;
    }

    public void setFunction(double function) {
        this.function = function;
    }

    @Override
    public String toString() {
        return String.format("Кількість ітерації: %d\n", iterations) +
                String.format("План: \n%s", Utils.matrixToString(x)) +
                String.format("Витрати: %s\n", function);
    }
}
