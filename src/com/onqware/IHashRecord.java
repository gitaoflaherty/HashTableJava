package com.onqware;

// ***** NOT USED *****
// A linked list node where
// - value: BridgeEntry
// - next: Pointer to another linked list node
public interface IHashRecord {

    // Value getter and setter
    BridgeEntry getValue();
    void setValue(BridgeEntry value);

    // Next getter and setter
    IHashRecord getNext();
    void setNext(IHashRecord next);
}
