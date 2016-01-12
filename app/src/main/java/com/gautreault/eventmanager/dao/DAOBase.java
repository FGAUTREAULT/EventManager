package com.gautreault.eventmanager.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.gautreault.eventmanager.dao.group.GroupDAO;

public abstract class DAOBase {

    protected SQLiteDatabase mDb = null;
    protected DatabaseHandler mHandler = null;

    public DAOBase(Context pContext) {
        this.mHandler = DatabaseHandler.getInstance(pContext);
    }

    public SQLiteDatabase open() {
        // Pas besoin de fermer la dernière base puisque getWritableDatabase s'en charge
        mDb = mHandler.getWritableDatabase();
        return mDb;
    }

    public void close() {
        mDb.close();
    }

    public SQLiteDatabase getDb() {
        if (mDb == null) {
            open();
        }
        return mDb;
    }
}
