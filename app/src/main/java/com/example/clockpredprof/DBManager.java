package com.example.clockpredprof;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
/**
 * Класс описания менеджера, для работы с базами данных
 * @autor Попов Кирилл
 */
public class DBManager {
    /** Поле контекста */
    private Context context;
    /** Поле название базы данных */
    private String DB_NAME = "game.db";
    /** Поле базыданных */
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
     * @param context - контекст активити или фрагмента
     */
    private DBManager(Context context) {
        this.context = context;
        db = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        createTablesIfNeedBe();
    }
    /**
     * Функция вставления значений в базу данных
     * @return void
     */
    void addResult(String username, String mail) {
        db.execSQL("INSERT INTO RESULTS VALUES ('" + username + "', " + mail
                + ");");
    }
    /**
     * Функция получения всех значений базы данных
     * @return ArrayList со значениями
     */
    ArrayList<User> getAllResults() {
        ArrayList<User> data = new ArrayList<User>();
        Cursor cursor = db.rawQuery("SELECT * FROM RESULTS ;", null);
        boolean hasMoreData = cursor.moveToFirst();
        while (hasMoreData) {
            String name = cursor.getString(cursor.getColumnIndex("USERNAME"));
            String mail = cursor.getString(cursor.getColumnIndex("MAIL"));
            data.add(new User(name, mail));
            hasMoreData = cursor.moveToNext();
        }
        return data;
    }
    /**
     * Функция создания базы данных
     * @return void
     */
    private void createTablesIfNeedBe() {
        db.execSQL("CREATE TABLE IF NOT EXISTS RESULTS (USERNAME TEXT, MAIL TEXT);");
    }
}
