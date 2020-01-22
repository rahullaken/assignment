package com.rhl.assignment.utils;

import android.graphics.Bitmap;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    public static String saveBitmap(String filname, Bitmap b) {
        try {
            File file = new File(filname);
            FileOutputStream fout = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            return filname;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
