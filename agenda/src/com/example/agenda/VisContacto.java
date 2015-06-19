package com.example.agenda;

import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class VisContacto extends Activity {
	
	private Contacto c=null;
	Context cont;
	int id;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        setContentView(R.layout.viscontacto);
        cont=this;
        Bundle b = getIntent().getExtras();
        id = b.getInt("id");
		AdaptadorBD  db = new AdaptadorBD (this);
		db.open();
		List <Contacto> l=db.getAll();
		db.close();
		c=l.get(id);
		TextView name =(TextView) findViewById(R.id.tnombre);
		TextView tel =(TextView) findViewById(R.id.ttelefono);
		TextView dir =(TextView) findViewById(R.id.tdireccion);
		TextView mail =(TextView) findViewById(R.id.tcorreo);
		name.setText(c.getNombre());
		tel.setText(c.getTelefono());
		dir.setText(c.getDireccion());
		mail.setText(c.getCorreo());
		Button botonTlf= (Button) findViewById(R.id.button1);
		botonTlf.setOnClickListener(new ImageButton.OnClickListener() {
        	public void onClick(View v) {
        
        		String tlfS=c.getTelefono();
    	        Intent intent =new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tlfS));
    	        startActivity(intent);	
    	      }});
		Button botonSMS= (Button) findViewById(R.id.button2);
		botonSMS.setOnClickListener(new ImageButton.OnClickListener() {
        	public void onClick(View v) {
        
        		String tlfS=c.getTelefono();
    	        Intent intent =new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + tlfS));
    	        startActivity(intent);	
    	      }});
		Button botonMail= (Button) findViewById(R.id.button4);
		botonMail.setOnClickListener(new ImageButton.OnClickListener() {
        	public void onClick(View v) {
        
        		String mail=c.getCorreo();
        		Intent intent =new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + mail));
        		startActivity(intent);
    	      }});
		Button botonMaps= (Button) findViewById(R.id.button3);
		botonMaps.setOnClickListener(new ImageButton.OnClickListener() {
        	public void onClick(View v) {
        
        		String dir=c.getDireccion();
        		Intent intent =new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("geo:0,0?q="+dir));
        		startActivity(intent);
    	      }});
	}
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menucontacto, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
		case R.id.Borrar :{
			AlertDialog.Builder builder = new AlertDialog.Builder(cont);
     		builder.setTitle("Aviso");
    		builder.setIcon(R.drawable.ic_launcher);
    		builder.setMessage("¿Estás seguro de que quieres eliminar este contacto?");
    		builder.setPositiveButton("Si",new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {
    				AdaptadorBD db=new AdaptadorBD (cont);
    				db.open();
    				db.borrar(c.getid());
    				db.close();
    				Toast.makeText(cont, "El contacto ha sido borrado", Toast.LENGTH_LONG).show();
    				finish();
    			}
    			});
    		builder.setNegativeButton("No",null);
    		builder.create();
    		builder.show();

			
		}break;
		case R.id.Modificar :{
			Intent Intentmod = new Intent(VisContacto.this,ModContacto.class);
			Intentmod.putExtra("id",id);
			startActivity(Intentmod);
			
		}break;
		}
		return true;} 
}
