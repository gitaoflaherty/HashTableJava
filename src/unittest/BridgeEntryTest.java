package unittest;

import com.onqware.BridgeEntry;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BridgeEntryTest {

    @Test
    public void testProperties() throws Exception {

        byte[] expectedMacAddress = Support.GetMACAddress( (byte) 0, (byte) 0, (byte) 0, (byte) 0 , (byte) 0, (byte) 0);
        int expectedInterfaceValue = 150;
        BridgeEntry entry = new BridgeEntry();

        entry.macAddress = expectedMacAddress ;
        entry.interfaceValue =  expectedInterfaceValue;

        assertEquals(true, Support.AreEqual(expectedMacAddress, entry.macAddress));
        assertEquals(expectedInterfaceValue, entry.interfaceValue);

    }

}
