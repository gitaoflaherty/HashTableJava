package unittest;

import com.onqware.*;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HashTableTest {

    HashTable table;

    private void printMacAddressHash(byte[] macAddress, int actualHash)
    {
        System.out.println( String.format( "TEST: Hash value for MAC address [%s] = 0x%6X   (decimal: %d).", Support.GetMACAddressAsString (macAddress), actualHash, actualHash ));
    }

    private void printAddMacAddress(byte[] macAddress, int interfaceValue)
    {
        System.out.println( String.format( "...add (MAC address %s, Interface: %d)." , Support.GetMACAddressAsString(macAddress), interfaceValue));
    }

    private void printFindMacAddress(byte[] macAddress, int interfaceValue)
    {
        System.out.println( String.format( "...find (MAC address: %s, Interface: %d) complete and asserted.", Support.GetMACAddressAsString(macAddress), interfaceValue ));
    }


    // Helper method to add entry into the hash table using specified macAddress and interface value.
    private void addEntry(byte[] macAddress, int interfaceValue) throws HashTableException {

        BridgeEntry BridgeEntry = new BridgeEntry();
        BridgeEntry.macAddress = macAddress;
        BridgeEntry.interfaceValue = interfaceValue;
        table.add(BridgeEntry);
        printAddMacAddress(macAddress, interfaceValue);
    }

    private void find(byte[] macAddress) throws HashTableException
    {
        BridgeEntry findRecord = table.find(macAddress);
        assertEquals( macAddress, findRecord.macAddress );
        printFindMacAddress(macAddress, findRecord.interfaceValue);
    }

    private void findInterface(byte[] macAddress, int expectedValue) throws HashTableException
    {
        int actualValue = table.findInterface(macAddress);
        assertEquals( expectedValue, actualValue );
        printFindMacAddress(macAddress, actualValue);
    }

    @Before
    public void setUp() throws Exception {
        table = new HashTable();
        //System.out.println( "Setup");
    }

    @After
    public void tearDown() throws Exception {
        table = null;
        //System.out.println( "TearDown");
    }

    @Test
    public void testHashTableLength() throws Exception {
        int expectedLength =  0x1000000;
        assertEquals ( expectedLength, table.LENGTH);
        System.out.println( String.format( "TEST: Hash Table Array Length = 0x%X.", expectedLength ));
    }

    @Test
    public void testGetHash() throws Exception {

        byte[] macAddress =  Support.GetMACAddress((byte) 0,(byte) 0,(byte) 0,(byte) 1, (byte) 1,(byte) 1  );
        int actualHash = table.getHash(macAddress) ;
        int expectedHash = Support.GetHash( macAddress );
        assertEquals(expectedHash, actualHash);
        printMacAddressHash(macAddress, actualHash);

        macAddress = Support.GetMACAddress((byte) 0,(byte) 0,(byte) 1,(byte) 1, (byte) 1,(byte) 1  );
        actualHash = table.getHash(macAddress) ;
        expectedHash = Support.GetHash( macAddress );
        assertEquals(expectedHash, actualHash);
        printMacAddressHash(macAddress, actualHash);

        macAddress = Support.GetMACAddress((byte) 0,(byte) 1,(byte) 0,(byte) 1, (byte) 1,(byte) 1  );
        actualHash = table.getHash(macAddress) ;
        expectedHash = Support.GetHash( macAddress );
        assertEquals(expectedHash, actualHash);
        printMacAddressHash(macAddress, actualHash);

        macAddress = Support.GetMACAddress( (byte) 1,(byte) 0,(byte) 0,(byte) 1, (byte) 1,(byte) 1  );
        expectedHash = Support.GetHash( macAddress );
        actualHash = table.getHash(macAddress) ;
        assertEquals(expectedHash, actualHash);
        printMacAddressHash(macAddress, actualHash);

        macAddress = Support.GetMACAddress( (byte) 1,(byte) 1,(byte) 1,(byte) 1, (byte) 1,(byte) 1  );
        actualHash = table.getHash(macAddress) ;
        expectedHash = Support.GetHash( macAddress );
        assertEquals(expectedHash, actualHash);
        printMacAddressHash(macAddress, actualHash);

        macAddress = Support.GetMACAddress( (byte) 1,(byte) 1,(byte) 1,(byte) 0xa, (byte) 0x0, (byte) 0x0 );
        actualHash = table.getHash(macAddress) ;
        expectedHash = Support.GetHash( macAddress );
        assertEquals(expectedHash, actualHash);
        printMacAddressHash(macAddress, actualHash);

        macAddress = Support.GetMACAddress( (byte) 1,(byte) 1,(byte) 1,(byte) 0x0, (byte) 0xa, (byte) 0x0 );
        actualHash = table.getHash(macAddress) ;
        expectedHash = Support.GetHash( macAddress );
        assertEquals(expectedHash, actualHash);
        printMacAddressHash(macAddress, actualHash);

        macAddress = Support.GetMACAddress( (byte) 0,(byte) 0,(byte) 0,(byte) 0x0, (byte) 0x0, (byte) 0xa );
        actualHash = table.getHash(macAddress) ;
        expectedHash = Support.GetHash( macAddress );
        assertEquals(expectedHash, actualHash);
        printMacAddressHash(macAddress, actualHash);

        macAddress = Support.GetMACAddress( (byte) 1,(byte) 1,(byte) 1,(byte) 0, (byte) 0,(byte) 0  );
        actualHash = table.getHash(macAddress) ;
        expectedHash = Support.GetHash( macAddress );
        assertEquals(expectedHash, actualHash);
        printMacAddressHash(macAddress, actualHash);

        macAddress = Support.GetMACAddress( (byte) 1,(byte) 1,(byte) 1,(byte) 0xff, (byte) 0xff, (byte) 0xff );
        actualHash = table.getHash(macAddress) ;
        expectedHash = Support.GetHash( macAddress );
        assertEquals(expectedHash, actualHash);
        printMacAddressHash(macAddress, actualHash);
    }

    @Test
    // add and find test (simple)
    public void testAddAndFind() throws Exception {

        byte[] macAddress = Support.GetMACAddress((byte) 0,(byte) 0,(byte) 0,(byte) 1, (byte) 1,(byte) 1  );
        int interfaceValue = 100;

        System.out.println( String.format( "TEST: add and find ..." ));
        addEntry(macAddress, interfaceValue);
        find(macAddress);
    }

    @Test
    // add and find test where different mac addresses can hash to the same index.
    public void testAddHashCollision() throws Exception {

        System.out.println( String.format( "TEST: add Hash collisions ..." ));

        byte[] macAddress =  Support.GetMACAddress((byte) 0,(byte) 0,(byte) 0,(byte) 1, (byte) 1,(byte) 1  );
        int interfaceValue = 10;
        byte[] macAddressCollision1 = Support.GetMACAddress((byte) 0,(byte) 0,(byte) 1,(byte) 1, (byte) 1,(byte) 1  );
        int interfaceValue1 = 50;
        byte[] macAddressCollision2 = Support.GetMACAddress((byte) 0,(byte) 1,(byte) 0,(byte) 1, (byte) 1,(byte) 1  );
        int interfaceValue2 = 100;

        addEntry(macAddress, interfaceValue);
        addEntry(macAddressCollision1, interfaceValue1);
        addEntry(macAddressCollision2, interfaceValue2);

        find(macAddress);
        find(macAddressCollision1);
        find(macAddressCollision2);
    }

    @Test(expected = UnsupportedFeatureHashTableException.class)
    // add a duplicate mac address
    public void testUnsupportedUseCase() throws UnsupportedFeatureHashTableException {

        try {
            System.out.println( String.format( "TEST: Unsupported use case ..." ));

            byte[] macAddress = Support.GetMACAddress((byte) 0,(byte) 0,(byte) 0,(byte) 1, (byte) 1,(byte) 1  );
            int interfaceValue = 42;
            addEntry(macAddress, interfaceValue);

            System.out.println("...Attempt to add same record again throws an exception which is asserted.");
            addEntry(macAddress, interfaceValue);
        }
        catch( UnsupportedFeatureHashTableException e )
        {
            throw e;
        }
        catch ( Exception e )
        {
            Assert.fail( e.getMessage() );
        }

    }


    @Test
    // Customer use case example
    public void testCustomerUseCase() throws Exception  {

        System.out.println( String.format( "TEST: Customer use case ..." ));

        byte[] missingMacAddress = new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00, (byte) 0x0FF  };
        int invalidInterface = BridgeEntry.INVALID_INTERFACE_VALUE;

        byte[] macAddress0 = new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00, (byte) 0x01  };
        int interface0 = 1;
        addEntry(macAddress0, interface0);

        byte[] macAddress1 = new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x01, (byte) 0x01  };
        int interface1 = 9;
        addEntry(macAddress1, interface1);

        byte[] macAddress2 = new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01,(byte) 0x00, (byte) 0x01  };
        int interface2  = 4;
        addEntry(macAddress2, interface2);

        byte[] macAddress3 = new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00,(byte) 0x00, (byte) 0x01  };
        int interface3  = 4;
        addEntry(macAddress3, interface3);

        byte[] macAddress4 = new byte[] { (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00,(byte) 0x00, (byte) 0x01  };
        int interface4  = 7;
        addEntry(macAddress4, interface4);

        // Valid mac address
        findInterface(macAddress2, interface2);

        // Invalid mac address
        findInterface(missingMacAddress, invalidInterface);
    }
}

