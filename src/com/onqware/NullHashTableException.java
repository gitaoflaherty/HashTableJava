package com.onqware;

public class NullHashTableException extends HashTableException {

    public NullHashTableException( )
    {
        super( "Uninitialized hash table." );
    }
}
