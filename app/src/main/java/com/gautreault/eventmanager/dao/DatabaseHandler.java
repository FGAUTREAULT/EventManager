package com.gautreault.eventmanager.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gautreault.eventmanager.dao.group.GroupDAO;

/**
 * Database singleton, a better approach would be to implement ContentProvider
 * and let the system take care of everything.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "eventManager.db";
    // Nous sommes à la première version de la base
    // Si je décide de la mettre à jour, il faudra changer cet attribut
    private static final int DATABASE_VERSION = 1;

    //Singleton
    private static DatabaseHandler instance;

    /**
     * Singleton, private constructor prevent form direct instantiation. Call static getInstance() instead.
     *
     * @param context : the application context
     */
    private DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Singleton, guarentees that only one DatabaseHandler will exist in the whole application.
     *
     * @param context : the context
     * @return the application DatabaseHandler
     */
    public static synchronized DatabaseHandler getInstance(Context context) {
        // Use the get application context and singleton to prevent leak
        if (instance == null) {
            instance = new DatabaseHandler(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        GroupDAO.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        GroupDAO.onUpgrade(db,oldVersion,newVersion);
    }
}
