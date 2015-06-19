package com.example.ruffinicalc;

import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Tutorial extends ActionBarActivity {
	private static TextView ejemplos, ejemplos1, ejemplos2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tutorial);
		ejemplos = (TextView) findViewById(R.id.ejemplosTuto);
		ejemplos1 = (TextView) findViewById(R.id.ejemplosTuto1);
		ejemplos2 = (TextView) findViewById(R.id.ejemplosTuto2);

		// Estos ejemplos los escribo desde aqui para evitar problemas con el
		// Html.fromHtml
		ejemplos.setText(Html.fromHtml("4x<small><sup>6</sup></small>"
				+ "-2x<small><sup>2</sup></small>"
				+ "+1 seria (4,0,0,0,-2,0,1)."));
		ejemplos1
				.setText(Html
						.fromHtml("Si el polinomio no termina en 0, debes sacar factor comun: "
								+ "4x<small><sup>4</sup></small>"
								+ "-2x<small><sup>2</sup></small>"
								+ "seria lo mismo que x<small><sup>2</sup></small>(4x<small><sup>2</sup></small>-2) y deberías poner 4,0,-2"));
		ejemplos2
				.setText(Html
						.fromHtml("Ejemplo de polinomios de 2º grado : "
								+ "x<small><sup>2</sup></small>-4 sería 1,0,-4 "
								+ "-2x<small><sup>2</sup></small>"
								+ "+1 seria (4,0,0,0,-2,0,1)."));
	}

}
