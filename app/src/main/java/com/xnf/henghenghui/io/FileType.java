package com.xnf.henghenghui.io;


public enum FileType{
    Image("Image"),Video("Video"),Audio("Audio"),VCard("VCard"),Other("Other"),TEMP("TEMP"),ZIP("ZIP"),LOG("Log");
    
    String subDirectory;
    
    FileType(String subDirectory)
    {
        this.subDirectory=subDirectory;
    }
    
    public String getSubDirectory(){
        return subDirectory;
    }
    
};