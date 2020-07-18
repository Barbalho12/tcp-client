package com.barbalho.rocha;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogFile {

    private static final String OUTPUT_LOG = "CLIENT_LOG.txt";
    private static SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");

    synchronized public static void log(String row) {

        String text = format.format(new Date())+"\t"+row +"\n";
        try (FileOutputStream outputStream = new FileOutputStream(OUTPUT_LOG, true)){
            byte[] strToBytes = text.getBytes();
            outputStream.write(strToBytes);
            // outputStream.close();
        } catch (Exception e) {
           e.printStackTrace();
        }
    }
    
}