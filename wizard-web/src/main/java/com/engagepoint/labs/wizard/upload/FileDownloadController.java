package com.engagepoint.labs.wizard.upload;

import org.primefaces.model.StreamedContent;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by igor.guzenko on 3/3/14.
 */
@Named
@SessionScoped
public class FileDownloadController implements Serializable{
    private StreamedContent file;

    public void setFile(StreamedContent file) {
        this.file = file;
    }




    public StreamedContent getFile() {
        return file;
    }


}
