package cn.hjf.tmcrm.attachment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import cn.hjf.tmcrm.AppSQLiteOpenHelper;

public class AttachmentDao {

	private Context mContext;

	private AppSQLiteOpenHelper mAppSQLiteOpenHelper;

	public AttachmentDao(Context context) {
		mContext = context;
		mAppSQLiteOpenHelper = new AppSQLiteOpenHelper(context);
	}

	public boolean insertAll(Attachment attachment) {
// Gets the data repository in write mode
		SQLiteDatabase db = mAppSQLiteOpenHelper.getWritableDatabase();

// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put("uuid", attachment.getUuid());
		values.put("file_path", attachment.getFilePath());
		values.put("mime_type", attachment.getMimeType());

// Insert the new row, returning the primary key value of the new row
		long newRowId = db.insert("attachment", null, values);

		return newRowId != -1;
	}

	public Attachment queryByUuid(String uuid) {
		SQLiteDatabase db = mAppSQLiteOpenHelper.getReadableDatabase();

// Define a projection that specifies which columns from the database
// you will actually use after this query.
		String[] projection = {
				"uuid",
				"file_path",
				"mime_type"
		};

// Filter results WHERE "title" = 'My Title'
		String selection = "uuid" + " = ?";
		String[] selectionArgs = {uuid};

// How you want the results sorted in the resulting Cursor
//		String sortOrder =
//				FeedEntry.COLUMN_NAME_SUBTITLE + " DESC";

		Cursor cursor = db.query(
				"attachment",   // The table to query
				projection,             // The array of columns to return (pass null to get all)
				selection,              // The columns for the WHERE clause
				selectionArgs,          // The values for the WHERE clause
				null,                   // don't group the rows
				null,                   // don't filter by row groups
				null               // The sort order
		);

		Attachment attachment = null;
		if (cursor.moveToNext()) {
			attachment.setUuid(cursor.getString(cursor.getColumnIndex("uuid")));
			attachment.setFilePath(cursor.getString(cursor.getColumnIndex("file_path")));
			attachment.setMimeType(cursor.getString(cursor.getColumnIndex("mime_type")));
		}
		cursor.close();

		return attachment;
	}

	public List<Attachment> queryAll() {
		SQLiteDatabase db = mAppSQLiteOpenHelper.getReadableDatabase();

// Define a projection that specifies which columns from the database
// you will actually use after this query.
		String[] projection = {
				"uuid",
				"file_path",
				"mime_type"
		};

// Filter results WHERE "title" = 'My Title'
//		String selection = "uuid" + " = ?";
//		String[] selectionArgs = { uuid };

// How you want the results sorted in the resulting Cursor
//		String sortOrder =
//				FeedEntry.COLUMN_NAME_SUBTITLE + " DESC";

		Cursor cursor = db.query(
				"attachment",   // The table to query
				projection,             // The array of columns to return (pass null to get all)
				null,              // The columns for the WHERE clause
				null,          // The values for the WHERE clause
				null,                   // don't group the rows
				null,                   // don't filter by row groups
				null               // The sort order
		);

		List<Attachment> attachmentList = new ArrayList<>();
		while (cursor.moveToNext()) {
			Attachment attachment = new Attachment();
			attachment.setUuid(cursor.getString(cursor.getColumnIndex("uuid")));
			attachment.setFilePath(cursor.getString(cursor.getColumnIndex("file_path")));
			attachment.setMimeType(cursor.getString(cursor.getColumnIndex("mime_type")));
			attachmentList.add(attachment);
		}
		cursor.close();

		return attachmentList;
	}

	public boolean deleteByUuid(String uuid) {
		SQLiteDatabase db = mAppSQLiteOpenHelper.getWritableDatabase();
		// Define 'where' part of query.
		String selection = "uuid" + " = ?";
// Specify arguments in placeholder order.
		String[] selectionArgs = {uuid};
// Issue SQL statement.
		int deletedRows = db.delete("attachment", selection, selectionArgs);

		return deletedRows != 0;
	}
}
