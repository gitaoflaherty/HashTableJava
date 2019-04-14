package com.onqware;

// MAC address and interface value pair
public class BridgeEntry {

    // Constants
    public static final int INVALID_INTERFACE_VALUE = -1;
    public static final int NUM_FIELDS = 6;

    // Fields
    public byte[] macAddress;
    public int interfaceValue;

    public BridgeEntry() {
        macAddress =    new byte[NUM_FIELDS];
        interfaceValue = INVALID_INTERFACE_VALUE;
    }
}
