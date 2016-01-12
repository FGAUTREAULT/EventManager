package com.gautreault.eventmanager.dao.group;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.gautreault.eventmanager.dao.DatabaseHandler;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Stockage, récupération et partage de données grace au content provider, ne pollue pas le thread graphique
 * <p/>
 * URI =
 * content://                                  : un prefixe standard +
 * com.gautreault.eventmanager.dao.group       : l'autorité identifiant le content provider +
 * /groups                                     : le type de donnée (optionnel)
 * /group_id                                   : l'id de la donnée (optionnel)
 */
public class GroupContentProvider extends ContentProvider {

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/groups";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/group";

    // utilisées pour UriMatcher
    private static final int GROUP = 10;
    private static final int GROUP_ID = 20;

    private static final String AUTHORITY = "com.gautreault.eventmanager.dao.group";

    private static final String BASE_PATH = "groups";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, GROUP);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", GROUP_ID);
    }

    // base de données
    private DatabaseHandler mHandler;
    private SQLiteDatabase mDb;

    @Override
    public boolean onCreate() {
        mHandler = DatabaseHandler.getInstance(getContext());
        return false;
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

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // Utiliser SQLiteQueryBuilder à la place de la méthode query()
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // Vérifier si l'appelant a demandé une colonne qui n'existe pas
        checkColumns(projection);

        // Préciser la table
        queryBuilder.setTables(GroupDAO.TABLE_NAME);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case GROUP:
                break;
            case GROUP_ID:
                // ajouter l'ID à la requête d'origine
                queryBuilder.appendWhere(GroupDAO.KEY + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        Cursor cursor = queryBuilder.query(getDb(), projection, selection,
                selectionArgs, null, null, sortOrder);

        // assurez-vous que les écouteurs potentiels seront notifiés
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        long id = 0;
        switch (uriType) {
            case GROUP:
                id = getDb().insert(GroupDAO.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);

        int rowsDeleted = 0;
        switch (uriType) {
            case GROUP:
                rowsDeleted = getDb().delete(GroupDAO.TABLE_NAME, selection,
                        selectionArgs);
                break;
            case GROUP_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = getDb().delete(GroupDAO.TABLE_NAME,
                            GroupDAO.KEY + "=" + id,
                            null);
                } else {
                    rowsDeleted = getDb().delete(GroupDAO.TABLE_NAME,
                            GroupDAO.KEY + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int uriType = sURIMatcher.match(uri);
        int rowsUpdated = 0;
        switch (uriType) {
            case GROUP:
                rowsUpdated = getDb().update(GroupDAO.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case GROUP_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = getDb().update(GroupDAO.TABLE_NAME,
                            values,
                            GroupDAO.KEY + "=" + id,
                            null);
                } else {
                    rowsUpdated = getDb().update(GroupDAO.TABLE_NAME,
                            values,
                            GroupDAO.KEY + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private void checkColumns(String[] projection) {
        String[] available = {GroupDAO.NAME,
                GroupDAO.KEY};
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
            // vérifier si toutes les colonnes demandées sont disponibles
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }
}