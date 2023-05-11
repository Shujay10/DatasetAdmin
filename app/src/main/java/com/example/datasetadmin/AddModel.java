package com.example.datasetadmin;

public class AddModel {

    String Internal;
    String External;

    public AddModel(String internal, String external) {
        Internal = internal;
        External = external;
    }

    public AddModel() {
    }

    public String getInternal() {
        return Internal;
    }

    public void setInternal(String internal) {
        Internal = internal;
    }

    public String getExternal() {
        return External;
    }

    public void setExternal(String external) {
        External = external;
    }
}
