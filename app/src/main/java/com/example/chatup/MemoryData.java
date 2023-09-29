package com.example.chatup;

import android.content.Context;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public final class MemoryData {
    public static void saveData(String data, Context context) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput("datata.txt", 0);
            fileOutputStream.write(data.getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void saveLastMsgTS(String data, String chatId, Context context) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(chatId + ".txt", 0);
            fileOutputStream.write(data.getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveName(String data, Context context) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput("Nameee.txt", 0);
            fileOutputStream.write(data.getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getData(Context context) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.openFileInput("datata.txt")));
            StringBuilder stringBuilder = new StringBuilder();
            while (true) {
                String readLine = bufferedReader.readLine();
                String line = readLine;
                if (readLine == null) {
                    return stringBuilder.toString();
                }
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getName(Context context) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.openFileInput("Nameee.txt")));
            StringBuilder stringBuilder = new StringBuilder();
            while (true) {
                String readLine = bufferedReader.readLine();
                String line = readLine;
                if (readLine == null) {
                    return stringBuilder.toString();
                }
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String cleardata(Context context,String chatId) {
        try {
            context.deleteFile("datata.txt");
            context.deleteFile(chatId + ".txt");
            context.deleteFile("Nameee.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getLastMsgTs(Context context, String chatId) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.openFileInput(chatId + ".txt")));
            StringBuilder stringBuilder = new StringBuilder();
            while (true) {
                String readLine = bufferedReader.readLine();
                String line = readLine;
                if (readLine == null) {
                    return stringBuilder.toString();
                }
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "0";
        }
    }
}
