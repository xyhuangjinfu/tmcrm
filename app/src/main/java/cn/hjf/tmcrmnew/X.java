package cn.hjf.tmcrmnew;

import android.content.Context;
import androidx.room.Room;

public class X {

	public void f(Context context) {
		AppDatabase db = Room.databaseBuilder(context.getApplicationContext(),
				AppDatabase.class, "database-name").build();

		db.customerDao().insert();
	}
}
