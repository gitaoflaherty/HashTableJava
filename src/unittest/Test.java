package unittest;


public class Test {

    public static void main(String[] args) throws Exception{

        System.out.println("START: HashTable tests.");

        HashTableTest test = new HashTableTest();

        test.setUp();
        test.testCustomerUseCase();
        test.tearDown();

        test.setUp();
        test.testHashTableLength();
        test.tearDown();

        test.setUp();
        test.testGetHash();
        test.tearDown();

        test.setUp();
        test.testAddAndFind();
        test.tearDown();

        test.setUp();
        test.testAddHashCollision();
        test.tearDown();

        try
        {
            test.setUp();
            test.testUnsupportedUseCase();
        }
        catch ( UnsupportedOperationException ex )
        {
            // expected
        }
        finally
        {
            test.tearDown();
        }

        System.out.println("END: HashTable tests.");
    }
}
