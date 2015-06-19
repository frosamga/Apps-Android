package com.example.anagramaker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

//eCompressor, creado por Alan Nicolás Martellotti y Mónica López Romero
public class MainActivity extends Activity {

	private EditText palabra, nMinimo;
	private Object[] cadena;
	final static String ACT_INFO1 = "com.example.anagramaker.AnagramaCompleto";
	final static String ACT_INFO2 = "com.example.anagramaker.AnagramaPalabrasExisten";
	private ProgressDialog pd;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		palabra = (EditText) findViewById(R.id.texto);
		nMinimo = (EditText) findViewById(R.id.numMinimo);
		cadena = new String[2];
		context = this;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	protected void onDestroy() {
		if (pd != null) {
			pd.dismiss();
		}
		super.onDestroy();
	}

	@SuppressLint("NewApi")
	public void Anagrama1(View v) {
		// usaremos una expresion regular (PL jaja) para ser mas eficientes en
		// la busqueda
		if (palabra.getText().toString().isEmpty()) {
			Toast toast = Toast.makeText(this, "Introduce las letras",
					Toast.LENGTH_SHORT);
			toast.show();
		} else {
			if (palabra.getText().toString().matches("[a-zA-Z]*")
					&& palabra.getText().toString().length() <= 10) {
				cadena[0] = palabra.getText().toString();
				cadena[1] = nMinimo.getText().toString();
				Intent act = new Intent(this, AnagramasCompleto.class);
				act.putExtra(ACT_INFO1, cadena);
				startActivity(act);

			} else {
				Toast toast = Toast
						.makeText(
								this,
								"La palabra no debe contener números o símbolos y debe ser menor de 10 letras",
								Toast.LENGTH_SHORT);
				toast.show();
			}
		}
	}

	@SuppressLint("NewApi")
	public void Anagrama2(View v) {
		if (palabra.getText().toString().isEmpty()) {
			Toast toast = Toast.makeText(this, "Introduce las letras",
					Toast.LENGTH_SHORT);
			toast.show();
		} else {
			if (palabra.getText().toString().matches("[a-zA-Z]*")
					&& palabra.getText().toString().length() <= 8) {
				cadena[0] = palabra.getText().toString();
				cadena[1] = nMinimo.getText().toString();
				final Intent act = new Intent(this,
						AnagramaPalabrasExisten.class);
				act.putExtra(ACT_INFO2, cadena);

				AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
				builder1.setMessage("Depende del número de letras el proceso puede tardar más. Ten paciencia");
				builder1.setPositiveButton("Continuar",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
								dialog.dismiss();
								startActivity(act);
							}
						});
				;
				AlertDialog alert11 = builder1.create();
				alert11.show();
			} else {
				Toast toast = Toast
						.makeText(
								this,
								"La palabra debe ser menor de 9 letras y sin números o simbolos",
								Toast.LENGTH_SHORT);
				toast.show();
			}
		}

	}

}
