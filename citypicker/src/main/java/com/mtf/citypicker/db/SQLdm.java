package com.mtf.citypicker.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 创造一个临时数据
 * Created by mtf on 2017/10/18.
 */

public class SQLdm {
    String filePath = "data/data/com.mtf.picker/databases/address.db";
    String pathStr = "data/data/com.mtf.picker/databases";

    SQLiteDatabase database;

    public SQLiteDatabase openDatabase(Context context) {
        System.out.println("filePath:" + filePath);
        File jhPath = new File(filePath);
        if (jhPath.exists()) {
            return SQLiteDatabase.openOrCreateDatabase(jhPath, null);
        } else {
            File path = new File(pathStr);
            if (path.mkdir()) {
            } else {
            }
            try {
                InputStream is = context.getClass().getClassLoader().getResourceAsStream("assets/" + "address.db");

                FileOutputStream fos = new FileOutputStream(jhPath);
                byte[] buffer = new byte[10240];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.flush();
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return openDatabase(context);
        }
    }
}
