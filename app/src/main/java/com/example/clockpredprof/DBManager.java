package com.example.clockpredprof;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Класс описания менеджера, для работы с базами данных
 * @autor Пустовалов Данил
 */
public class DBManager {
    /** Поле контекста */
    private Context context;
    /** Поле название базы данных */
    private String DB_NAME = "users.db";
    /** Поле базы данных */
    private SQLiteDatabase db;
    private static DBManager dbManager;

    public static DBManager getInstance(Context context) {
        if (dbManager == null) {
            dbManager = new DBManager(context);
        }
        return dbManager;
    }
    /**
     * Конструктор - создание нового объекта и базы данных
     * @param context - контекст активности или фрагмента
     */
    private DBManager(Context context) {
        this.context = context;
        db = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        createTablesIfNeedBe();
    }
    /**
     * Метод вставления значений в базу данных
     * @return void
     */
    void addResult(String username, String city) {
        db.execSQL("INSERT INTO RESULTS VALUES ('" + username  +"','"+city+"');");
    }
    /**
     * Метод получения всех значений базы данных
     * @return ArrayList со значениями
     */
    ArrayList<User> getAllResults() {
        ArrayList<User> data = new ArrayList<User>();
        Cursor cursor = db.rawQuery("SELECT * FROM RESULTS ;", null);
        boolean hasMoreData = cursor.moveToFirst();
        while (hasMoreData) {
            String name = cursor.getString(cursor.getColumnIndex("USERNAME"));
            String mail = cursor.getString(cursor.getColumnIndex("CITY"));
            data.add(new User(name, mail));
            hasMoreData = cursor.moveToNext();
        }
        return data;
    }
    /**
     * Метод, возвращающий город по имени пользователя
     * @return String
     */
    String getCity(String name){

        String query = "SELECT CITY FROM RESULTS WHERE USERNAME=?";
        Cursor cursor= db.rawQuery(query, new String[]{ name });
        String result="";
        if (cursor.moveToFirst()) {
            result = cursor.getString(cursor.getColumnIndex("CITY"));
        }
        cursor.close();
        return result;
    }
    /**
     * Метод удаления данных из базы
     * @return void
     */
    public void removeAll()
    {
        db.delete("RESULTS", null, null);
    }
    /**
     * Метод создания базы данных
     * @return void
     */
    private void createTablesIfNeedBe() {
        db.execSQL("CREATE TABLE IF NOT EXISTS RESULTS (USERNAME TEXT, CITY TEXT);");
    }

}
