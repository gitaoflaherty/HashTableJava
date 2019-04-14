package com.onqware;

// Hash table implementation specific to storing BridgeEntry elements where
// BridgeEntry element is a MACAddress (6 byte) and Interface pair.
// Mac address [0],[1],[2] = OUI Indexes
// Mac address [3],[4],[5] = NIC Indexes
public class HashTable {

    // Byte size in bits
    final private int BYTE_SIZE = 8;
    // Byte mask to clear all bits except for the byte bits
    final private int BYTE_MASK = 0xff;
    // Number of bytes (OIU and NIC)
    final private int COUNT_NIC_BYTES = 3;
    final private int COUNT_OUI_BYTES = 3;
    // MAC address index to get first byte (OIU,NIC)
    final private int FIRST_OUI_BYTE = 0;
    final private int FIRST_NIC_BYTE = 3;
    // MAC address index to get last byte (OIU, NIC)
    final private int LAST_OUI_BYTE = FIRST_OUI_BYTE + COUNT_OUI_BYTES - 1; // zero based index
    final private int LAST_NIC_BYTE = FIRST_NIC_BYTE + COUNT_NIC_BYTES - 1; // zero based index

    // A fixed length array that will hold all possible NIC values
    final public int LENGTH = (int) Math.pow( 2, (BYTE_SIZE * COUNT_NIC_BYTES) );
    HashRecord[] data = new HashRecord[LENGTH];

    // initialize the table by clearing all entries
    public void initialize()
    {
        for ( int i=0; i < LENGTH; i++ )
        {
            data[i] = null;
        }
    }

    // Use the NIC bytes to calculate a unique hash key
    public int getHash(byte[] macAddress)
    {
        return (int) getNICNumericValue(macAddress);
    }

    // Return NIC bytes as an integer value
    private long getNICNumericValue(byte[] macAddress)
    {
        return getNumericValue(macAddress, FIRST_NIC_BYTE, LAST_NIC_BYTE);
    }

    // Return OUI bytes as an integer value
    private long getOUINumericValue(byte[] macAddress)
    {
        return getNumericValue(macAddress, FIRST_OUI_BYTE, LAST_OUI_BYTE);
    }

    // Return MAC address bytes (0-5) as an integer value
    private long getMACAddressNumericValue(byte[] macAddress)
    {
        return getNumericValue(macAddress, 0, BridgeEntry.NUM_FIELDS - 1);
    }

    // Shift the bytes by multiple of byte size where
    // the multiple depends on the byte index and array length.
    // (Similar to a register with three byte fields.)
    // Shift function example for a 6 element array:
    // Input(index)    Output(multiple of 8 bits to shift the MAC address field)
    //  5               0
    //  4               1
    //  3               2
    //  ..              ..
    //  0               5
    private long getNumericValue(byte[] macAddress, int firstByte, int lastByte)
    {
        long value = 0;

        for ( int i = firstByte; i <= lastByte; i++ )
        {
            int shiftBy = (lastByte - i) * BYTE_SIZE;
            long macAddressField = (( (long) ( macAddress[i] & BYTE_MASK) ) << shiftBy);
            value |= macAddressField ;
        }
        return value;
    }

    // Determine the index by getting the hash key
    // and insert into linked list (binary search tree).
    public void add(BridgeEntry newEntry) throws HashTableException{
        if ( data != null )
        {
            int index = getHash(newEntry.macAddress);
            boolean addRightNode = false;

            if ( index < LENGTH)
            {
                // Previous header = current first node
                HashRecord prevNode = null;
                HashRecord currentNode = data[index];
                long valueMacAddress = getMACAddressNumericValue(newEntry.macAddress);

                while ( currentNode != null )
                {
                    prevNode = currentNode;
                    long currentMacAddress = getMACAddressNumericValue(currentNode.value.macAddress);
                    if ( valueMacAddress == currentMacAddress )
                    {
                        // UNSUPPORTED USE CASE
                        throw new UnsupportedFeatureHashTableException("Mac addresses must be unique." );
                        // Alternative: Set interface value to the new value
                    }
                    else if ( valueMacAddress > currentMacAddress )
                    {
                        currentNode = currentNode.right;
                        addRightNode = true;
                    }
                    else // valueMacAddress < currentMacAddress
                    {
                        currentNode = currentNode.left;
                        addRightNode = false;
                    }
                }

                if ( prevNode == null ) // first element in the tree
                {
                    data[index] = new HashRecord();
                    data[index].value = newEntry;
                }
                else
                {
                    if ( addRightNode )
                    {
                        prevNode.right = new HashRecord();
                        prevNode.right.value = newEntry;
                    }
                    else
                    {
                        prevNode.left = new HashRecord();
                        prevNode.left.value = newEntry;
                    }
                }
            }
        }
        else
        {
            throw new NullHashTableException();
        }
    }

    // Return the BridgeEntry at the specified macAddress
    // Determine the index to hash into in O(1) time.
    // Then traverse the linear linked list to search the MAC address in worst case 0(n) time.
    public BridgeEntry find(byte[] macAddress) throws HashTableException {

        BridgeEntry result = null;

        if ( data == null )
        {
            throw new NullHashTableException();
        }

        // Hash into array
        int index = getHash(macAddress);
        if ( index >= LENGTH)
        {
            throw new IndexOutOfBoundHashTableException();
        }

        // Traverse linked list
        HashRecord currentRecord = data[index];
        while ( currentRecord != null )
        {
            if ( currentRecord.value != null )
            {
                long compare = (long) (getMACAddressNumericValue(macAddress) - getMACAddressNumericValue(currentRecord.value.macAddress));

                if ( compare == 0 )// equal
                {
                    result = currentRecord.value;
                    currentRecord = null; // exit condition
                }
                else if ( compare > 0 ) // greater than
                {
                    currentRecord = currentRecord.right;
                }
                else
                {
                    currentRecord = currentRecord.left;
                }
            }
        }

        return result;
    }

    // Return the interfaceValue for the specified macAddress
    public int findInterface(byte[] macAddress) throws HashTableException {

        int result = BridgeEntry.INVALID_INTERFACE_VALUE;

        BridgeEntry record = find(macAddress);
        if ( record != null )
        {
            result = record.interfaceValue;
        }

        return result;
    }

    // Compare the two MAC addresses and return true if equal, false if not equal.
    private boolean areEqual(byte[] macAddress1, byte[] macAddress2, int expectedLength)
    {
        boolean areEqual = false;

        if ( macAddress1 != null && macAddress2 != null )
        {
            if ( getMACAddressNumericValue(macAddress1) == getMACAddressNumericValue(macAddress2))
            {
                areEqual =true;
            }
        }

        return areEqual;
    }
}
