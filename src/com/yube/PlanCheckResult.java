package com.yube;

import java.util.Arrays;

public class PlanCheckResult {

    private boolean optimal;
    private Double[] u, v;
    private Double[][] d;

    public PlanCheckResult(boolean optimal) {
        this.optimal = optimal;
    }

    public boolean isOptimal() {
        return optimal;
    }

    public void setOptimal(boolean optimal) {
        this.optimal = optimal;
    }

    public Double[] getU() {
        return u;
    }

    public void setU(Double[] u) {
        this.u = u;
    }

    public Double[] getV() {
        return v;
    }

    public void setV(Double[] v) {
        this.v = v;
    }

    public Double[][] getD() {
        return d;
    }

    public void setD(Double[][] d) {
        this.d = d;
    }

    @Override
    public String toString() {
        String result = String.format("u: %s\n", Utils.arrayToString(u)) +
                String.format("v: %s\n", Arrays.toString(v)) +
                String.format("D: \n%s", Utils.matrixToString(d));
        if (!optimal) {
            result += "Результат: план не оптимальний\n";
        } else {
            result += "Результат: план оптимальний\n";
        }
        return result;
    }
}
