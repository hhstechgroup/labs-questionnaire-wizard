package com.engagepoint.labs.wizard.upload;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.servlet.http.Part;
import java.io.Serializable;

@ManagedBean
@SessionScoped
public class FileUploadController implements Serializable {
    private Part file;


    public void upload() {
        System.err.print("UPLOAD OBJECT"+file);


    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }
}