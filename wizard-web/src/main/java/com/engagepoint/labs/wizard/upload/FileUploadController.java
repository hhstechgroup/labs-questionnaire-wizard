package com.engagepoint.labs.wizard.upload;

import javax.enterprise.context.SessionScoped;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.inject.Named;
import javax.servlet.http.Part;
import java.io.*;

@Named("fileUploadController")
@SessionScoped
public class FileUploadController implements Serializable, ActionListener {
    private Part file;
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public void upload() throws IOException {
        String sourcePath = getClass().getClassLoader().getResource(".").getPath();
        path = new String(sourcePath + Math.random() + ".xml");
        InputStream inStream = null;
        OutputStream outStream = null;
        try {
            inStream = file.getInputStream();
            outStream = new FileOutputStream(new File(path));
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }
            System.err.println("File is copied successful!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inStream != null) {
                inStream.close();
            }
            if (outStream != null) {
                outStream.close();
            }
        }
    }

    @Override
    public void processAction(ActionEvent event) throws AbortProcessingException {
        try {
            //   upload();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }


}