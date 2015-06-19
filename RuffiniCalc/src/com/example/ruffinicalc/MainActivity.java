package com.example.ruffinicalc;

import java.util.StringTokenizer;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	private EditText polinomio;
	private static TextView ejemplo;
	private Object[] cadena;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ejemplo = (TextView) findViewById(R.id.ejemplo);
		// con html se ven las formulas
		ejemplo.setText(Html.fromHtml("Por ejemplo "
				+ "4x<small><sup>6</sup></small>"
				+ "-2x<small><sup>2</sup></small>"
				+ "+1 seria (4,0,0,0,-2,0,1)."));
		polinomio = (EditText) findViewById(R.id.polinomioEntrada);

		cadena = new String[1];
		context = this;
	}

	public void calcular(View v) {
		StringTokenizer st;
		int maximo = 0;
		if (polinomio.getText().toString().length() >= 30) {
			// limitar tamaño por computo
			Toast toast = Toast.makeText(this,
					"Ese polinomio es muy largo para esta aplicación",
					Toast.LENGTH_LONG);
			toast.show();
		} else if (polinomio.getText().toString().length() <= 3) {
			// por si introduce una solucion tipo x-2 (evitar errores de
			// introduccion minima)
			Toast toast = Toast.makeText(this,
					"El polinomio deberá ser mas grande", Toast.LENGTH_LONG);
			toast.show();
		} else {
			// mediante expresiones regulares limitamos que solo hayan esos
			// elementos, faltaria que la coma solo se repita una vez cada token
			// para ser perfecto
			if (!polinomio.getText().toString().matches("[0-9,-]*")) {
				Toast toast = Toast.makeText(this,
						"Introduce solo numeros separados por coma.",
						Toast.LENGTH_LONG);
				toast.show();
			} else {
				// si la introduccion de datos va bien
				st = new StringTokenizer(polinomio.getText().toString(), ",");
				maximo = st.countTokens();
				// ver el grado del polinomio, aunque el algoritmo puede llevar
				// a cabo sin problemas de tiempo hasta altos grados, la
				// siguiente activity esta preparada con hasta 6 grados para
				// mostrar el resultado con formulas visuales comprensibles para
				// el usuario, ademas de que no es usual ver polinomios mayores
				// de grado 6
				if (maximo > 7) {
					Toast toast = Toast
							.makeText(
									this,
									"El tiempo de computo es muy largo con polinomios de grado mayor que 7, lo sentimos. Introduce uno menor",
									Toast.LENGTH_LONG);
					toast.show();
				} else {
					cadena = new String[maximo];
					int contador = 0;
					while (st.hasMoreTokens()) {
						cadena[contador] = st.nextToken();
						contador++;
					}
					//si el TI es 0 pide que se factorize(una siguiente mejora podra ser factorizarlo automaticamente y notificarlo).
					if (cadena[maximo - 1] == "0") {
						Toast toast = Toast
								.makeText(this, "Saca factor común primero.",
										Toast.LENGTH_LONG);
						toast.show();
					//evita que acabe en coma.	
					} else if (cadena[maximo - 1] == ",") {
						Toast toast = Toast.makeText(this,
								"Quita la coma del final", Toast.LENGTH_LONG);
						toast.show();
					} else {
						//ejecuta la actividad que llevará a cabo el calculo
						Intent act = new Intent(this, Raices.class);
						act.putExtra("Poli", cadena);
						startActivity(act);
					}
				}
			}
		}

	}

	// ejecuta el tutorial con instrucciones
	public void tutorial(View v) {
		Intent act = new Intent(this, Tutorial.class);
		startActivity(act);
	}
}
