package me.arjenlee.shirolearn.util;

public enum Status {

    DELETED(0), ENABLED(1), DISABLED(2), DAISHANGXIAN(3), VERIFY(4), ALL(9);

    private int value;

    Status(int num) {
        this.value = num;
    }

    public int getValue() {
        return value;
    }
}
