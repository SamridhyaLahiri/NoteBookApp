package com.example.samri.notebookapp;

/**
 * Created by samri on 18-02-2018.
 */

public class FileBean {
    String fileName;
    String filePath;

    public FileBean(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
