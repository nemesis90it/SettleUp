package com.nemesis.settelup;

import java.math.BigDecimal;

public class Amount implements Comparable<Amount> {

    private BigDecimal value;

    public Amount(BigDecimal value) {
        this.value = value;
    }

    public Amount(Amount value) {
        this.value = new BigDecimal(String.valueOf(value));
    }

    public Amount add(Amount amount) {
        value = value.add(amount.value);
        return this;
    }

    public Amount add(BigDecimal amaunt) {
        value = value.add(amaunt);
        return this;
    }

    public Amount subtract(BigDecimal subtrahend) {
        value = value.subtract(subtrahend);
        return this;
    }

    public Amount subtract(Amount amount) {
        value = value.subtract(amount.value);
        return this;
    }

    public boolean isPositive() {
        return value.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isNegative() {
        return value.compareTo(BigDecimal.ZERO) < 0;
    }

    @Override
    public int compareTo(Amount amount) {
        return value.compareTo(amount.value);
    }

    public static Amount getAbs(Amount amount) {
        return new Amount(amount.value.abs());
    }

    public static Amount getMin(Amount val1, Amount val2) {
        return new Amount(val1.value.min(val2.value));
    }

    public static boolean isZero(Amount value) {
        return value.value.equals(BigDecimal.ZERO);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
