package com.onqware;

public class UnsupportedFeatureHashTableException extends HashTableException {

    public UnsupportedFeatureHashTableException( String unsupportedFeature )
    {
        super( "Unsupported feature - " + unsupportedFeature );
    }
}

