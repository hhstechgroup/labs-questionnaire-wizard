package com.engagepoint.labs.wizard.upload;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by igor.guzenko on 3/12/14.
 */
public class ArchiverZip {
    private static final Logger LOGGER = Logger.getLogger(ArchiverZip.class);
    public static File ZIP_FILE;
    public static final int BUFER_SIZE = 1024 * 1024;

    private ArchiverZip() {
    }

    public static void addFilesToZip(List<File> files) {
        FileOutputStream zipFileOUT = null;
        ZipOutputStream zipOutStream = null;
        try {
            ZIP_FILE = File.createTempFile("zip"+Math.random(),".zip");
            zipFileOUT = new FileOutputStream(ZIP_FILE);
            zipOutStream = new ZipOutputStream(zipFileOUT);
            for (File file : files) {
                addToZipFile(file, zipOutStream);
            }

        } catch (FileNotFoundException e) {
            LOGGER.warn("ZIP IO EXCEPTION!!! Error when adding files to zip!", e);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != zipOutStream) {
                    zipOutStream.close();
                }
                if (null != zipFileOUT) {
                    zipFileOUT.close();
                }
            } catch (IOException e) {
                LOGGER.warn("Exception close streams", e);
            }
        }
    }

    private static void addToZipFile(File fileForZip, ZipOutputStream zipOutputStream) {
        FileInputStream fileInputStream = null;
        ZipEntry zipEntry = new ZipEntry(fileForZip.getName());

        byte[] bytes = new byte[BUFER_SIZE];
        int length;
        try {
            zipOutputStream.putNextEntry(zipEntry);
            fileInputStream = new FileInputStream(fileForZip.getPath());
            while (true) {
                length = fileInputStream.read(bytes);
                if (length > 0) {
                    zipOutputStream.write(bytes, 0, length);
                } else {
                    break;
                }
            }

        } catch (IOException e) {
            LOGGER.warn("ZIP IO Exception  ", e);
        } finally {
            if (null != fileInputStream) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    LOGGER.warn("ZIP IO Exception when trying close fileInputStream ", e);
                }
            }
        }
    }
}
