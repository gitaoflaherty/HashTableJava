package com.onqware;

public class IndexOutOfBoundHashTableException extends HashTableException {

    public IndexOutOfBoundHashTableException( )
    {
        super( "Index is out of bounds." );
    }
}
