package com.aceventura.voicerecoder.Imp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AudioFileToMD5 {


    public static String calculateMD5(String filePath) {
        try {
            File audioFile = new File(filePath);
            FileInputStream fis = new FileInputStream(audioFile);

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                md.update(buffer, 0, bytesRead);
            }
            byte[] md5sum = md.digest();

            StringBuilder hexString = new StringBuilder();
            for (byte b : md5sum) {
                hexString.append(String.format("%02x", b));
            }

            fis.close();
            return hexString.toString();
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String filePath = "path_to_your_audio_file";
        String md5Hash = calculateMD5(filePath);
        System.out.println("MD5 Hash: " + md5Hash);
    }
}


