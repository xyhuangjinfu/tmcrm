package cn.hjf.tmcrm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppSQLiteOpenHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "tmcrm.db";
	public static final int DATABASE_VERSION = 1;

	private static final String SQL_CREATE_ATTACHMENT =
			"CREATE TABLE " + "attachment" + " (" +
					"uuid" + " TEXT PRIMARY KEY," +
					"file_path" + " TEXT," +
					"mime_type" + " TEXT)";

	public AppSQLiteOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		sqLiteDatabase.execSQL(SQL_CREATE_ATTACHMENT);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

	}
}
