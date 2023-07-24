package com.kasper.commons.Network;

public class NitroByteBuf {
    private byte[] buffer;
    private int length;
    private int capacity;

    public NitroByteBuf() {
        capacity = 100;  // Initiate capacity to 100 as buffer's length is 100
        buffer = new byte[capacity];
        length = 0; // Initialize length to 0, as no data is present initially
    }

    public void add(byte b) {
        if (capacity == length) {
            capacity *= 2; // Increase the capacity
            byte[] newArray = new byte[capacity];
            System.arraycopy(buffer, 0, newArray, 0, length); // Copy the existing data to new array
            buffer = newArray;
        }
        buffer[length++] = b; // Add byte to buffer and then increment the length
    }

    public byte[] getBuf() {
        return buffer;
    }
}
