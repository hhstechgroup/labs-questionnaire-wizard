package com.engagepoint.labs.wizard.upload;

import com.engagepoint.labs.wizard.model.NavigationData;
import com.engagepoint.labs.wizard.questions.WizardQuestion;
import com.engagepoint.labs.wizard.values.FileValue;

import javax.enterprise.context.SessionScoped;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import java.io.*;

@Named("fileUploadController")
@SessionScoped
public class FileUploadController implements Serializable, ActionListener {
    private Part file;
    private String path;
    private String name="AAA";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Inject
    NavigationData navigationData;

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
        path = new String("D:\\" + Math.random() + ".xml");
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

    public void getAnswerInputStream(String id) {
        System.out.println("in Input Strema");
        WizardQuestion question = navigationData.getWizardForm().getWizardQuestionById(id);
        FileValue value = new FileValue();
        try {
            value.setValue(file.getInputStream());
            question.setAnswer(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("in Input Strema2");
    }

    @Override
    public void processAction(ActionEvent event) throws AbortProcessingException {
        try {
            upload();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}