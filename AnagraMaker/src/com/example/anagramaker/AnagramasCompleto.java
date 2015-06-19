package com.example.anagramaker;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class AnagramasCompleto extends Activity {

	private ListView list;
	private String[] info;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_anagramas_completo);

		list = (ListView) findViewById(R.id.listview);
		Intent men = getIntent();
		info = men.getStringArrayExtra(MainActivity.ACT_INFO1);
		// aqui ya tengo en el array las dos palabras.

		final Permutacion perm = new Permutacion();

		// creo que aqui esta el problema
		String[] elementosMal = info[0].split("");
		String[] elementos = corregirArray(elementosMal);

		if (info[1].isEmpty()) {
			perm.perm(elementos, 0, 0);
		} else {
			perm.perm(elementos, 0, Integer.parseInt(info[1]));
		}
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				AnagramasCompleto.this,
				android.R.layout.simple_selectable_list_item,
				perm.listaPermutaciones);
		list.setAdapter(adapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.anagramas_completo, menu);
		return true;
	}

	// todasPalabras
	public String[] corregirArray(String[] array) {
		String[] nuevo = new String[array.length - 1];
		for (int i = 0; i < array.length - 1; i++) {
			nuevo[i] = array[i + 1];
		}
		return nuevo;
	}
}