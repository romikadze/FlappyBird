package com.mygdx.game.flappybird.dataBase;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.sql.Database;
import com.badlogic.gdx.sql.DatabaseCursor;
import com.badlogic.gdx.sql.DatabaseFactory;
import com.badlogic.gdx.sql.SQLiteGdxException;

public class DataBase {

    Database dbHandler;

    public static final String TABLE_NAME = "score_table";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_SCORE = "score";
    public static final String COLUMN_DATE = "date";

    private static final String DATABASE_NAME = "scoreDatabase.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table if not exists "
            + TABLE_NAME + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_SCORE
            + " text not null, " + COLUMN_DATE + " text not null);";

    public DataBase() {
        dbHandler = DatabaseFactory.getNewDatabase(DATABASE_NAME,
                DATABASE_VERSION, DATABASE_CREATE, null);

        dbHandler.setupDatabase();
        try {
            dbHandler.openOrCreateDatabase();
            dbHandler.execSQL(DATABASE_CREATE);
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }


        Gdx.app.log("TAG", "CREATED");
        boolean isRowsNeeded = true;
        DatabaseCursor cursor = null;


        try {
            cursor = dbHandler.rawQuery("SELECT " + COLUMN_ID + " FROM " + TABLE_NAME);
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
        while (cursor.next()) {
            if (Integer.parseInt(cursor.getString(0)) > 4) {
                isRowsNeeded = false;
                Gdx.app.log("TAG", "DON'T NEED TO ADD");
            }
        }

        if (isRowsNeeded) {
            try {
                dbHandler
                        .execSQL("INSERT INTO " + TABLE_NAME + " (" + COLUMN_SCORE + ", " + COLUMN_DATE + ") " +
                                "VALUES ('0', '--')");
            } catch (SQLiteGdxException e) {
                e.printStackTrace();
            }
        }

    }

    public String select() {
        DatabaseCursor cursor = null;

        try {
            cursor = dbHandler.rawQuery("SELECT * FROM " + TABLE_NAME);
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
        String score = "";
        while (cursor.next()) {
            score += "\n" + cursor.getString(1) + "  --  " + cursor.getString(2);
            }
        Gdx.app.log("TAG" , score);
        return score;
    }

    public void update(String score, String date) {
        DatabaseCursor cursor = null;
        try {
            cursor = dbHandler.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_SCORE + "= " +
                    "(SELECT MIN(" + COLUMN_SCORE + ") FROM " + TABLE_NAME + ")");
            cursor.next();
            if(Integer.parseInt(cursor.getString(1)) < Integer.parseInt(score)){
                dbHandler
                        .execSQL("UPDATE " + TABLE_NAME + " SET " + COLUMN_SCORE + " = '" + score + "', " +
                                COLUMN_DATE + " = '" + date + "' WHERE " + COLUMN_ID + " = " + cursor.getString(0) + " ");
            }

        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
    }

    public void clearTable() {
        try {
            dbHandler
                    .execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " <= 999");
            Gdx.app.log("TAG", "CLEAR//////1");
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
        Gdx.app.log("TAG", "CLEAR//////2");
    }

    public void drop(){
        try {
            dbHandler.execSQL("DROP TABLE " + TABLE_NAME);
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
    }

    public void dispose() {
        try {
            dbHandler.closeDatabase();
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
        dbHandler = null;
    }
}
