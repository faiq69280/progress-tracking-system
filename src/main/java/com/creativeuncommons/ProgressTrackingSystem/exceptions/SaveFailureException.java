package com.creativeuncommons.ProgressTrackingSystem.exceptions;

public class SaveFailureException extends RuntimeException {
    Object metaData;

    public SaveFailureException(String msg, Object metaData){
        super(msg);
        this.metaData = metaData;
    }

    public Object getMetaData(){
        return metaData;
    }

}
