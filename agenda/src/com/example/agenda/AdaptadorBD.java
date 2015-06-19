package com.example.agenda;

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
	public static final String KEY_NOMBRE = "nombre";
	public static final String KEY_TELEFONO = "telefono";
	public static final String KEY_DIRECCION = "direccion";
	public static final String KEY_CORREO = "correo";
	
	private static final String TAG = "AdaptadorBD";
	
	private static final String DATABASE_NAME = "dbagenda";
	private static final String DATABASE_TABLE = "Info";
	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_CREATE =
			"create table "+DATABASE_TABLE+
			"("+KEY_ROWID+" integer primary key autoincrement, "
			+KEY_NOMBRE+" text not null, "
			+KEY_TELEFONO+" text not null, "
			+KEY_DIRECCION+" text not null, "
			+KEY_CORREO+" text not null);";
	
	private final Context context;
	
	
	private BaseDatosHelper BDHelper;
	private SQLiteDatabase bsSql;
	private String[] todasColumnas =new String[] {KEY_ROWID,KEY_NOMBRE,KEY_TELEFONO,KEY_DIRECCION,KEY_CORREO};

	public AdaptadorBD(Context ctx) {
		this.context = ctx;
		BDHelper = new BaseDatosHelper(context);
	}
	
	public AdaptadorBD open() throws SQLException{
		bsSql = BDHelper.getWritableDatabase();
		return this;
	}
	
	
	//---closes the database---
	public void close(){
		BDHelper.close();
	}
	
	public long insertar(String alumno, String telefono, String direccion, String correo){
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NOMBRE, alumno);
		initialValues.put(KEY_TELEFONO, telefono);
		initialValues.put(KEY_DIRECCION, direccion);
		initialValues.put(KEY_CORREO, correo);
		return bsSql.insert(DATABASE_TABLE, null, initialValues);
	}
	
	public long insertar(Contacto alumno){
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NOMBRE, alumno.getNombre());
		initialValues.put(KEY_TELEFONO, alumno.getTelefono());
		initialValues.put(KEY_DIRECCION, alumno.getDireccion());
		initialValues.put(KEY_CORREO, alumno.getCorreo());
		return bsSql.insert(DATABASE_TABLE, null, initialValues);
	}
	
	
	
	public boolean borrar(long expediente){
		return bsSql.delete(DATABASE_TABLE, KEY_ROWID + "=" + expediente, null) > 0;
	}
	public void borrarTodos(){
		List<Contacto> l=getAll();
		while(!l.isEmpty())
		{
			borrar(l.get(0).id);
			l.remove(0);
		}
		
	}
	
	
	public Cursor getTodos() {
	
		return bsSql.query(DATABASE_TABLE, todasColumnas,null,null,null,null,null);
	}
	
	
	public Cursor get(long expediente) throws SQLException{
	
		Cursor mCursor = bsSql.query(true, DATABASE_TABLE, todasColumnas,
					KEY_ROWID + "=" + expediente,null,null,null,null,null);
		
		if (mCursor != null)  mCursor.moveToFirst();
		
		return mCursor;
	}
	
	
	
	
	public boolean actualizar(int expediente, String nombre, String telefono, String direccion, String correo){
		ContentValues args = new ContentValues();
		args.put(KEY_NOMBRE, nombre);
		args.put(KEY_TELEFONO, telefono);
		args.put(KEY_DIRECCION, direccion);
		args.put(KEY_CORREO, correo);
		return bsSql.update(DATABASE_TABLE, args,KEY_ROWID + "=" + expediente, null) > 0;
	}
	
	public boolean actualizar(Contacto contacto){
		ContentValues args = new ContentValues();
		args.put(KEY_NOMBRE, contacto.getNombre());
		args.put(KEY_TELEFONO, contacto.getTelefono());
		args.put(KEY_DIRECCION, contacto.getDireccion());
		args.put(KEY_CORREO, contacto.getCorreo());
		return bsSql.update(DATABASE_TABLE, args,KEY_ROWID + "=" + contacto.getid(), null) > 0;
	}
	
	public String Mostrar(long expediente){
		String cadena=null;
		
		 Cursor c = get(expediente);
		 
		 if (c.moveToFirst()){

			 cadena= 
				"Id: " + c.getString(0) + "\n" +
				"Nombre: " + c.getString(1) + "\n" +
				"Telefono: " + c.getString(2) + "\n" +
				"Direccion: " + c.getString(3) + "\n" +
				"Correo: " + c.getString(4) ;
		 }
		 
		return cadena;
	}
	
	public String Mostrar(Cursor c){
		String cadena=null;
		
		 	 cadena= 
				"Id: " + c.getString(0) + "\n" +
				"Nombre: " + c.getString(1) + "\n" +
				"TELEFONO: " + c.getString(2);
		 
		return cadena;
	}
	
	public List<Contacto> getAll() {
		
		List<Contacto> listaAlumnos = new ArrayList<Contacto>();
		Cursor cursor = this.getTodos();
				
				cursor.moveToFirst(); 
				
				while (!cursor.isAfterLast()) {
					Contacto comment = cursorToContactos(cursor); 
					listaAlumnos.add(comment); 
					cursor.moveToNext();
				}
				cursor.close(); 
				return listaAlumnos;
				
	}
	
	private Contacto cursorToContactos(Cursor cursor) { 
		Contacto contacto = new Contacto(); 
		contacto.setExpediente(cursor.getLong(0));
		contacto.setNombre(cursor.getString(1));
		contacto.setTelefono(cursor.getString(2));
		contacto.setDireccion(cursor.getString(3));
		contacto.setCorreo(cursor.getString(4));
		
		return contacto;
	}
	
	
//**** CLASE PRIVADA ***/	
	
	private static class BaseDatosHelper extends SQLiteOpenHelper{
		BaseDatosHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		@Override
		public void onCreate(SQLiteDatabase db)	{
			db.execSQL(DATABASE_CREATE);
		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion){
				Log.w(TAG, "Actualizando base de datos de la versi—n " + oldVersion
				+ " a "
				+ newVersion + ", borraremos todos los datos");
				db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
				onCreate(db);
		}
		
	}

	
}
