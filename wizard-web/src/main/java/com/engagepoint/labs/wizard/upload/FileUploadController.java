package com.engagepoint.labs.wizard.upload;

import org.apache.log4j.Logger;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.servlet.http.Part;
import java.io.*;

@Named("fileUploadController")
@SessionScoped
public class FileUploadController implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(FileUploadController.class);
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
        path = new String(Math.random() + ".xml");
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
        } catch (IOException e) {
            LOGGER.warn("Can't copy file", e);
        } finally {
            if (inStream != null) {
                inStream.close();
            }
            if (outStream != null) {
                outStream.close();
            }
        }
    }

}