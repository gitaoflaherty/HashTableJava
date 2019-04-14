package unittest;

public class Support {

    public static int MAC_ADDRESS_FIELD_COUNT = 6;

    public static String GetMACAddressAsString( byte[] macAddress )
    {
        String result = "";
        for (int i=0; i < macAddress.length; i++ )
        {
            result += String.format( "%2X", macAddress[i]);
            if ( i < macAddress.length - 1 )
            {
                result += ",";
            }
        }
        return result;
    }

    public static byte[] GetMACAddress( byte byte0, byte byte1, byte byte2, byte byte3, byte byte4, byte byte5 )
    {
        byte[] macAddress = new byte[6];
        macAddress[0] = byte0;
        macAddress[1] = byte1;
        macAddress[2] = byte2;
        macAddress[3] = byte3;
        macAddress[4] = byte4;
        macAddress[5] = byte5;

        return macAddress;
    }

    public static int GetHash( byte[]macAddress )
    {
        long hash = macAddress[3] << 16 | macAddress[4] << 8 | macAddress[5];

        return (int) hash & 0xffffff;
    }


    public static boolean AreEqual( byte[] macAddress1, byte[] macAddress2 )
    {
        boolean areEqual = true;
        for (int i = 0; i < MAC_ADDRESS_FIELD_COUNT && areEqual; i++ )
        {
            if ( macAddress1[i] != macAddress2[i])
            {
                areEqual = false;
            }
        }
        return areEqual;
    }

}
