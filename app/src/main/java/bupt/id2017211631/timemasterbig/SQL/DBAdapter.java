package bupt.id2017211631.timemasterbig.SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;

import java.sql.Date;

public class DBAdapter {

    private static final String DB_NAME = "activity.db";
    private static final String DB_TABLE_Activity = "activityinfo";
    private static final String DB_TABLE_TAG = "taginfo";

    private static final int DB_VERSION = 1;

    public static final String KEY_ID = "_id";
    public static final String KEY_TAG = "tag";
    public static final String KEY_DATE = "date";
    public static final String KEY_START_TIME = "starttime";
    public static final String KEY_END_TIME ="endtime";
    public static final String KEY_NOTE ="note";

    public static final String KEY_COLOR ="color";
    public static final String KEY_ISSHOW ="isshow";


    private SQLiteDatabase db;
    private final Context context;
    private DBOpenHelper dbOpenHelper;

    public DBAdapter(Context _context) {
        context = _context;
    }

    /** Close the database */
    public void close() {
        if (db != null){
            db.close();
            db = null;
        }
    }

    /** Open the database */
    public void open() throws SQLiteException {
        dbOpenHelper = new DBOpenHelper(context, DB_NAME, null, DB_VERSION);
        try {
            db = dbOpenHelper.getWritableDatabase();
        }
        catch (SQLiteException ex) {
            db = dbOpenHelper.getReadableDatabase();
        }
    }


    public long insertActivity(Activity activity) {
        ContentValues newValues = new ContentValues();

        newValues.put(KEY_TAG, activity.tag);
        newValues.put(KEY_DATE, activity.date.toString());
        newValues.put(KEY_START_TIME, activity.startTime.toString());
        newValues.put(KEY_END_TIME,activity.endTime.toString());
        newValues.put(KEY_NOTE,activity.note);

        return db.insert(DB_TABLE_Activity, null, newValues);
    }

    public long insertTag(Tag tag) {
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_TAG, tag.name);
        newValues.put(KEY_COLOR, tag.color);
        newValues.put(KEY_ISSHOW,tag.isShow);
        return db.insert(DB_TABLE_TAG, null, newValues);
    }


    public Activity[] queryAllActivity() {
        Cursor results =  db.query(DB_TABLE_Activity, new String[] { KEY_ID, KEY_TAG, KEY_DATE, KEY_START_TIME,KEY_END_TIME,KEY_NOTE},
                null, null, null, null, null);
        return ConvertToActivity(results);
    }

    public Tag[] queryAllTag() {
        Cursor results =  db.query(DB_TABLE_TAG, new String[] { KEY_TAG, KEY_COLOR,KEY_ISSHOW},
                null, null, null, null, null);
        return ConvertToTag(results);
    }

    public Tag[] queryAllShowTag() {
        Cursor results =  db.query(DB_TABLE_TAG, new String[] { KEY_TAG, KEY_COLOR,KEY_ISSHOW},
                KEY_ISSHOW + " = " + 1, null, null, null, null);
        return ConvertToTag(results);
    }

    public Tag[] queryTagByname(String name) {
        Cursor results =  db.query(DB_TABLE_TAG, new String[] { KEY_TAG, KEY_COLOR,KEY_ISSHOW},
                KEY_TAG + "= '" + name+"'", null, null, null, null);
        return ConvertToTag(results);
    }

    public Activity[] queryActivityByID(long id) {
        Cursor results =  db.query(DB_TABLE_Activity, new String[] { KEY_ID, KEY_TAG, KEY_DATE, KEY_START_TIME,KEY_END_TIME,KEY_NOTE},
                KEY_ID + "=" + id, null, null, null, null);
        return ConvertToActivity(results);
    }

    public Activity[] queryActivityByTime(Date date1, Date date2) {
        Cursor results =  db.query(DB_TABLE_Activity, new String[] { KEY_ID, KEY_TAG, KEY_DATE, KEY_START_TIME,KEY_END_TIME,KEY_NOTE},
                "date("+KEY_DATE+") >= "+" date(?)and date("+KEY_DATE+") <= "+
                        " date(?) ORDER BY "+KEY_END_TIME +" ASC"
                , new String[] {date1.toString(),date2.toString()}, null, null, null);
        return ConvertToActivity(results);
    }

    public Activity[] queryLastActivity(Date date) {
        Cursor results =  db.query(DB_TABLE_Activity, new String[] { KEY_ID, KEY_TAG, KEY_DATE, KEY_START_TIME,KEY_END_TIME,KEY_NOTE},
                "date("+KEY_DATE+") = "+" date(?) ORDER BY "+KEY_END_TIME +" DESC"
                , new String[] {date.toString()}, null, null, null);
        return ConvertToActivity(results);
    }


    private Activity[] ConvertToActivity(Cursor cursor){
        int resultCounts = cursor.getCount();
        if (resultCounts == 0 || !cursor.moveToFirst()){
            return null;
        }
        Activity[] activitys = new Activity[resultCounts];
        for (int i = 0 ; i<resultCounts; i++){
            activitys[i] = new Activity();
            activitys[i].ID = cursor.getInt(0);
            activitys[i].tag = cursor.getString(cursor.getColumnIndex(KEY_TAG));
            activitys[i].date = Activity.strToDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
            activitys[i].startTime = Activity.strToTime(cursor.getString(cursor.getColumnIndex(KEY_START_TIME)));
            activitys[i].endTime=Activity.strToTime(cursor.getString(cursor.getColumnIndex(KEY_END_TIME)));
            activitys[i].note=cursor.getString(cursor.getColumnIndex(KEY_NOTE));
            cursor.moveToNext();
        }
        return activitys;
    }

    private Tag[] ConvertToTag(Cursor cursor){
        int resultCounts = cursor.getCount();
        if (resultCounts == 0 || !cursor.moveToFirst()){
            return null;
        }
        Tag[] tags = new Tag[resultCounts];
        for (int i = 0 ; i<resultCounts; i++){
            tags[i] = new Tag();
            tags[i].name = cursor.getString(cursor.getColumnIndex(KEY_TAG));
            tags[i].color = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_COLOR)));
            tags[i].isShow = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ISSHOW)));
            cursor.moveToNext();
        }
        return tags;
    }

    public long deleteAllActivity() {
        return db.delete(DB_TABLE_Activity, null, null);
    }

    public long deleteOneActivity(long id) {
        return db.delete(DB_TABLE_Activity,  KEY_ID + "=" + id, null);
    }

    public long deleteOneTag(String name) {
        return db.delete(DB_TABLE_Activity,  KEY_TAG + "=" + name, null);
    }

    public long updateActivity(long id , Activity activity){
        ContentValues updateValues = new ContentValues();
        updateValues.put(KEY_TAG, activity.tag);
        updateValues.put(KEY_DATE, activity.date.toString());
        updateValues.put(KEY_START_TIME, activity.startTime.toString());
        updateValues.put(KEY_END_TIME,activity.endTime.toString());
        updateValues.put(KEY_NOTE,activity.note);

        return db.update(DB_TABLE_Activity, updateValues,  KEY_ID + "=" + id, null);
    }

    public long updateTag(Tag tag){
        ContentValues updateValues = new ContentValues();
        updateValues.put(KEY_TAG, tag.name);
        updateValues.put(KEY_COLOR, tag.color);
        updateValues.put(KEY_ISSHOW,tag.isShow);
        return db.update(DB_TABLE_TAG, updateValues,  KEY_TAG + "= ?", new String[]{tag.name});
    }


    private static class DBOpenHelper extends SQLiteOpenHelper {

        public DBOpenHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        private static final String DB_CREATE_Activity = "create table " +
                DB_TABLE_Activity + " (" + KEY_ID + " integer primary key autoincrement, " +
                KEY_TAG + " text not null, " + KEY_DATE + " text not null," +
                KEY_START_TIME + " text not null,"+
                KEY_END_TIME+" text not null,"+KEY_NOTE+" text,"+
                "FOREIGN KEY ("+KEY_TAG+") REFERENCES "+DB_TABLE_TAG+" ("+KEY_TAG+")" +
                ");";

        private static final String DB_CREATE_TAG = "create table " +
                DB_TABLE_TAG + " (" + KEY_TAG + " text primary key, " +
                KEY_COLOR + " text not null, "+ KEY_ISSHOW + " text not null);";

        @Override
        public void onOpen(SQLiteDatabase _db) {
            super.onOpen(_db);
            if(!_db.isReadOnly()) {
                _db.execSQL("PRAGMA foreign_keys = ON;");
            }
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(DB_CREATE_Activity);
            _db.execSQL(DB_CREATE_TAG);
            _db.execSQL("INSERT INTO "+DB_TABLE_TAG+" VALUES ('睡觉',"+ Color.rgb(243, 156, 18)+",1"+");");
            _db.execSQL("INSERT INTO "+DB_TABLE_TAG+" VALUES ('吃饭',"+ Color.rgb(26, 188, 156)+",1"+");");
            _db.execSQL("INSERT INTO "+DB_TABLE_TAG+" VALUES ('学习',"+ Color.rgb(52, 152, 219)+",1"+");");
            _db.execSQL("INSERT INTO "+DB_TABLE_TAG+" VALUES ('锻炼',"+ Color.rgb(254, 67, 101)+",1"+");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
            _db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_Activity);
            _db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_TAG);
            onCreate(_db);
        }
    }
}