package com.example.lljsm.codeviewer;

public class CodeTypeBean {
    private int type;
    private String code;

    CodeTypeBean(int type, String code){
        this.type = type;
        this.code = code;
    }

    CodeTypeBean(){
        this(-1, null);
    }

    public void setType(int type){
        this.type = type;
    }

    public void setCode(String code){
        this.code = code;
    }

    public int getType(){
        return this.type;
    }

    public String getCode(){
        return this.code;
    }
}
