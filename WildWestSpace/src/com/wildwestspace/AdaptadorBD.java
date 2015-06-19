package com.wildwestspace;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;

public class AdaptadorBD {

	public static final String KEY_ROWID = "_id";
	public static final String KEY_PUNTUACION = "puntuacion";

	private static final String TAG = "AdaptadorBD";
	private static final String DATABASE_NAME = "dbpunt";
	private static final String DATABASE_TABLE = "Info";
	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_CREATE = "create table "
			+ DATABASE_TABLE + "(" + KEY_ROWID
			+ " integer primary key autoincrement, " + KEY_PUNTUACION
			+ " text not null);";

	private final Context context;

	private BaseDatosHelper BDHelper;
	private SQLiteDatabase bsSql;
	private String[] todasColumnas = new String[] { KEY_ROWID, KEY_PUNTUACION};

	public AdaptadorBD(Context ctx) {
		this.context = ctx;
		BDHelper = new BaseDatosHelper(context);
	}

	public AdaptadorBD open() throws SQLException {
		bsSql = BDHelper.getWritableDatabase();
		return this;
	}

	// ---closes the database---
	public void close() {
		BDHelper.close();
	}

	public long insertar(String punt) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_PUNTUACION, punt);
		return bsSql.insert(DATABASE_TABLE, null, initialValues);
	}

	
	public boolean borrar(long id) {
		return bsSql.delete(DATABASE_TABLE, KEY_ROWID + "=" + id, null) > 0;
	}

	public void borrarTodos() {
		Cursor cursor = this.getTodos();
		Long Id ;
		
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			Id = cursor.getLong(0);
			cursor.moveToNext();
			borrar(Id);
	
		}
		

	}

	public Cursor getTodos() {

		return bsSql.query(DATABASE_TABLE, todasColumnas, null, null, null,
				null, null);
	}

	public Cursor get(long Id) throws SQLException {

		Cursor mCursor = bsSql.query(true, DATABASE_TABLE, todasColumnas,
				KEY_ROWID + "=" + Id, null, null, null, null, null);

		if (mCursor != null)
			mCursor.moveToFirst();

		return mCursor;
	}

	public boolean actualizar(int Id, String Puntuacion) {
		ContentValues args = new ContentValues();
		args.put(KEY_PUNTUACION, Puntuacion);
		return bsSql.update(DATABASE_TABLE, args, KEY_ROWID + "=" + Id,
				null) > 0;
	}


	public String Mostrar(long Id) {
		String cadena = null;

		Cursor c = get(Id);

		if (c.moveToFirst()) {

			cadena = "Id: " + c.getString(0) + "\n" + "Puntuacion: "
					+ c.getString(1);
		}

		return cadena;
	}




	// **** CLASE PRIVADA ***/

	private static class BaseDatosHelper extends SQLiteOpenHelper {
		BaseDatosHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Actualizando base de datos de la versi—n " + oldVersion
					+ " a " + newVersion + ", borraremos todos los datos");
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}

	}

}
