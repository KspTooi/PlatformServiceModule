package com.ksptooi.psm.utils;

public class FixedSizeStringBuffer {

    private final StringBuffer buffer;
    private final int maxSize;

    public FixedSizeStringBuffer(int maxSize) {
        this.maxSize = maxSize;
        this.buffer = new StringBuffer();
    }

    public FixedSizeStringBuffer append(char c) {
        if (buffer.length() >= maxSize) {
            buffer.deleteCharAt(0);
        }
        buffer.append(c);
        return this;
    }

    public FixedSizeStringBuffer append(String str) {
        for (char c : str.toCharArray()) {
            append(c);
        }
        return this;
    }

    public String toString() {
        return buffer.toString();
    }

    public int length() {
        return buffer.length();
    }

    public char charAt(int index) {
        return buffer.charAt(index);
    }

    public void delete(int start, int end) {
        buffer.delete(start, end);
    }

    public FixedSizeStringBuffer clear(){
        buffer.setLength(0);
        return this;
    }

    public FixedSizeStringBuffer setLength(int i){
        buffer.setLength(i);
        return this;
    }

}
