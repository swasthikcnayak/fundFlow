package com.ecommerce.authorization.utils.errors;

public class InternalException extends RuntimeException{
    
    public InternalException(String messString){
        super(messString);
    }
}
