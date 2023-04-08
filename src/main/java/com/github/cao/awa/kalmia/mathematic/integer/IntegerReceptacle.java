package com.github.cao.awa.kalmia.mathematic.integer;

public class IntegerReceptacle {
    private int value;

    public IntegerReceptacle(int value) {
        this.value = value;
    }

    public static IntegerReceptacle of(int value) {
        return new IntegerReceptacle(value);
    }

    public IntegerReceptacle subtract(int value) {
        this.value -= value;
        return this;
    }

    public IntegerReceptacle subtract() {
        this.value--;
        return this;
    }

    public IntegerReceptacle add(int value) {
        this.value -= value;
        return this;
    }

    public IntegerReceptacle add() {
        this.value++;
        return this;
    }

    public int get() {
        return this.value;
    }

    public IntegerReceptacle set(int value) {
        this.value = value;
        return this;
    }

    public IntegerReceptacle multiply(int value) {
        this.value *= value;
        return this;
    }

    public IntegerReceptacle divide(int value) {
        this.value /= value;
        return this;
    }
}
