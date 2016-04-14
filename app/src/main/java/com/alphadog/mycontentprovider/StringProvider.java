package com.alphadog.mycontentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by Alpha Dog on 2016/4/8.
 */
public class StringProvider extends ContentProvider {

    private static final String PROVIDER_NAME = "com.alphadog.mycontentprovider";
    private static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/string");
    private static final int STR = 1;
    private static final int STRS = 2;
    private StringDataBase sdb = null;

    private static final UriMatcher um = getUriMatcher();

    private static UriMatcher getUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "string/#", STR);
        uriMatcher.addURI(PROVIDER_NAME, "string", STRS);
        return uriMatcher;
    }

    //初始化StringDatabase
    @Override
    public boolean onCreate() {
        Context context = getContext();
        sdb = new StringDataBase(context);
        return true;
    }

    @Override
    public String getType(Uri uri) {
        switch (um.match(uri)) {
            case STR:
                return "vnd.android.cursor.item/vnd.com.alphadog.mycontentprovider.string/#";
            case STRS:
                return "vnd.android.cursor.dir/vnd.com.alphadog.mycontentprovider.string";
        }

        return "";
    }

    //增
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        try {
            long id = sdb.addString(values);
            Uri returnUri = ContentUris.withAppendedId(CONTENT_URI, id);
            return returnUri;
        } catch (Exception e) {
            return null;
        }
    }

    //删
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
//        使用URI的方式取段删除
//        String id = null;
//        if (um.match(uri) == STR) {
//            id = uri.getPathSegments().get(0);
//        }

        return sdb.deleteString(selection);
    }

    //改
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

//        String id = null;
//        if (um.match(uri) == STR) {
//            id = uri.getPathSegments().get(0);
//        }
        return sdb.updateString( values,selection);
    }

    //查

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

//        String id=null;
//        if(um.match(uri)==STR){
//            id=uri.getPathSegments().get(0);
//        }

        return sdb.getString(projection,selection,selectionArgs,sortOrder);
    }
}
