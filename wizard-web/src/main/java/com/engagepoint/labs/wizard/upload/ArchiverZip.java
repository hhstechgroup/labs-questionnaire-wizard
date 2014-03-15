package com.engagepoint.labs.wizard.upload;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by igor.guzenko on 3/12/14.
 */
public class ArchiverZip {

    public static final String ZIP_FILE_NAME = "/Wizard_answer.zip";

    public static void addFilesToZip(List<File> files) {
        FileOutputStream zipFileOUT = null;
        ZipOutputStream zipOutStream = null;
        try {
            zipFileOUT = new FileOutputStream(ZIP_FILE_NAME);
            zipOutStream = new ZipOutputStream(zipFileOUT);
            for (File file : files) {
                addToZipFile(file, zipOutStream);
            }

        } catch (FileNotFoundException e) {
            System.err.print("ZIP IO EXCEPTION!!! Error when adding files to zip!");
        } finally {
            try {
                if (null != zipOutStream)
                    zipOutStream.close();
                if (null != zipFileOUT)
                    zipFileOUT.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void addToZipFile(File fileForZip, ZipOutputStream zipOutputStream) {
        FileInputStream fileInputStream = null;
        ZipEntry zipEntry = new ZipEntry(fileForZip.getName());

        byte[] bytes = new byte[100000000];
        int length;
        try {
            zipOutputStream.putNextEntry(zipEntry);
            fileInputStream = new FileInputStream(fileForZip.getPath());
            while ((length = fileInputStream.read(bytes)) != -1) {
                zipOutputStream.write(bytes, 0, length);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != fileInputStream) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    System.err.print("ZIP IO Exception when trying close fileInputStream " + Arrays.toString(e.getStackTrace()));
                }
            }
        }
    }
}
