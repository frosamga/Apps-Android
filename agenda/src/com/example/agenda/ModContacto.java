package com.example.agenda;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;



public class ModContacto extends AddContacto {
	
	private Contacto c;
	private Context cont = this;

	
	public void onCreate(Bundle savedInstanceState) {
        Bundle b = getIntent().getExtras();
        int id = b.getInt("id");
		AdaptadorBD  db = new AdaptadorBD (this);
		db.open();
		List <Contacto> l=db.getAll();
		db.close();
		c=l.get(id);
		super.onCreate(savedInstanceState);
		ednom.setText(c.getNombre());
		edtel.setText(c.getTelefono());
		eddir.setText(c.getDireccion());
		edmail.setText(c.getCorreo());
	}
	public void addContacto(String nombre, String telefono,
			String direccion, String correo) {
		AlertDialog.Builder builder = new AlertDialog.Builder(cont);
 		builder.setTitle("Aviso");
		builder.setIcon(R.drawable.ic_launcher);
		builder.setMessage("¿Estás seguro de que deseas guardar los cambios?");
		builder.setPositiveButton("Si",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				c.setNombre(ednom.getText().toString());
				c.setTelefono(edtel.getText().toString());
				c.setDireccion(eddir.getText().toString());
				c.setCorreo(edmail.getText().toString());
				AdaptadorBD  db = new AdaptadorBD (cont);
				db.open();
				db.actualizar(c);
				db.close();
				Toast.makeText(cont, "Cambios guardados", Toast.LENGTH_LONG).show();
				finish();
			}
			});
		builder.setNegativeButton("No",null);
		builder.create();
		builder.show();


		
	}

}
