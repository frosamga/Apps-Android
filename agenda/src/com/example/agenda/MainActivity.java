package com.example.agenda;

/**
 * Ejemplo de agenda desarrollada por el profesor Montenegro y continuada por Mik.
 * 
 * Uso solo para aprendizaje, no es proyecto propio ni original
 */

import java.util.List;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ListActivity  {

	Intent IntentAdd=null;
	Intent Intentver=null;
	Context cont;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 cont=this;
		 AdaptadorBD  db = new AdaptadorBD (this);
		 IntentAdd=new Intent (MainActivity.this,AddContacto.class);
	/*	 db.open();
		 List <Contacto> contacto = db.getAll();
		 db.close();
		 if (contacto.isEmpty())
		 {
			 Toast.makeText(this, "La agenda esta vacía", Toast.LENGTH_LONG).show();
		 }
		 else
		 {
			 setListAdapter(new MiAdaptador(this,contacto));

		 }*/
	}
	public void onResume(){ //Para que se actualize la lista al hacer cambios en la base de datos
		super.onResume();
		AdaptadorBD  db = new AdaptadorBD (this);
		db.open();
		List <Contacto> contacto = db.getAll();
		db.close();
		if (contacto.isEmpty())
		{
			 Toast.makeText(this, "La agenda esta vacía", Toast.LENGTH_SHORT).show();
			 Toast.makeText(this, "Pulsa menu para agregar contactos nuevos", Toast.LENGTH_LONG).show();
		}
		else
		{
			 setListAdapter(new MiAdaptador(this,contacto));

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
		case R.id.add : startActivity(IntentAdd); break;
		case R.id.BorraTodo : {
			AlertDialog.Builder builder = new AlertDialog.Builder(cont);
      		builder.setTitle("Aviso");
     		builder.setIcon(R.drawable.ic_launcher);
     		builder.setMessage("¿Estás seguro de que quieres borrar a todos los contactos?");
     		builder.setPositiveButton("Si",new DialogInterface.OnClickListener() {
     			public void onClick(DialogInterface dialog, int which) {
     				AdaptadorBD  db = new AdaptadorBD (cont);
     				db.open();
     				db.borrarTodos();
     				db.close();
     				Toast.makeText(cont, "La base de datos ha sido borrada", Toast.LENGTH_LONG).show();
     			}
     			});
     		builder.setNegativeButton("No",null);
     		builder.create();
     		builder.show();
		} onResume(); break;
		}
		return true;} 
	
	protected void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);
//		Bundle b = new Bundle();
		Intentver=new Intent (MainActivity.this,VisContacto.class);
		Intentver.putExtra("id",position);
		startActivity(Intentver);
		
	}

}
