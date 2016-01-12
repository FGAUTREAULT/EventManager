package com.gautreault.eventmanager.dao.group;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gautreault.eventmanager.dao.DAOBase;

public class GroupDAO extends DAOBase {

    public static final String TABLE_NAME = "groups";
    public static final String KEY = "_id";
    public static final String NAME = "name";

    public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" + KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT);";

    public static final String TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    public GroupDAO(Context context) {
        super(context);
    }

    /**
     * @param group : the group to add
     */
    public void add(Group group) {
        ContentValues value = new ContentValues();
        value.put(GroupDAO.NAME, group.getName());
        long result = mDb.insert(GroupDAO.TABLE_NAME, null, value);
    }

    /**
     * @param id : the id of the group to delete
     */
    public void delete(long id) {
        mDb.delete(TABLE_NAME, KEY + " = ? ", new String[]{String.valueOf(id)});
    }

    /**
     * @param group : the modified group
     */
    public void modify(Group group) {
        ContentValues value = new ContentValues();
        value.put(GroupDAO.NAME, group.getName());
        mDb.update(TABLE_NAME, value, KEY + " = ? ", new String[]{String.valueOf(group.getId())});
    }

    /**
     * @param id :the id of the group to get
     */
    public Group select(long id) {
        Cursor cursor = mDb.rawQuery("SELECT * FROM" + TABLE_NAME + "WHERE KEY = ?", new String[]{String.valueOf(id)});
        if (cursor != null) {
            long theid = cursor.getLong(0);
            String name = cursor.getString(1);
            Group group = new Group(theid, name);
            return group;
        }
        return null;
    }

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Attention ici la bDD est recreee a l upgrade...
        db.execSQL(TABLE_DROP);
        onCreate(db);
    }
}
