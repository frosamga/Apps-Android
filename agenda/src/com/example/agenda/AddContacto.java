package com.example.agenda;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class AddContacto extends Activity {
	
	public EditText ednom;
	public EditText edtel;
	public EditText eddir;
	public EditText edmail;
	
	 public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar);
        ednom = (EditText) findViewById(R.id.editnombre);
        edtel = (EditText) findViewById(R.id.edittelefono);
        eddir = (EditText) findViewById(R.id.editdireccion);
        edmail = (EditText) findViewById(R.id.editcorreo);
        
        final Button guardar = (Button) findViewById(R.id.guardar);
        guardar.setOnClickListener(new ImageButton.OnClickListener() {
        	public void onClick(View v) {
        		String nombre = ednom.getText().toString();
        		String telefono = edtel.getText().toString();
        		String direccion = eddir.getText().toString();
        		String correo = edmail.getText().toString();
        		addContacto(nombre,telefono,direccion,correo);
        		
        	}});
	}

	public void addContacto(String nombre, String telefono,
			String direccion, String correo) {
		if ((nombre.length()!=0)& (telefono.length()!=0))
				{
			 		AdaptadorBD  db = new AdaptadorBD (this);
			 		db.open();
			 		db.insertar(nombre,telefono,direccion,correo);
			 		db.close();
			 		finish();
				}
		else Toast.makeText (this,"El nombre y el telefono son campos obligatorios",Toast.LENGTH_SHORT).show();
		
	}

}
