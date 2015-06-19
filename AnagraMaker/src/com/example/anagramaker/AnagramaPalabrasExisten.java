package com.example.anagramaker;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AnagramaPalabrasExisten extends Activity {

	private ListView list;
	private String[] info;
	private List<String> dicc = new ArrayList<String>();
	// private ProgressDialog pd;
	private Context context;
	private List<String> coincidencias;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_anagrama_palabras_existen);
		context = this;
		coincidencias = new ArrayList<String>();

		list = (ListView) findViewById(R.id.listView1);
		Intent men = getIntent();
		info = men.getStringArrayExtra(MainActivity.ACT_INFO2);
		/*
		 * AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>()
		 * {
		 * 
		 * @Override protected void onPreExecute() { pd = new
		 * ProgressDialog(context); pd.setTitle("Processing...");
		 * pd.setMessage("Please wait."); pd.setCancelable(false);
		 * pd.setIndeterminate(true); pd.show(); }
		 * 
		 * @Override protected Void doInBackground(Void... arg0) { try {
		 * Thread.sleep(5000); } catch (InterruptedException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } return null; }
		 * 
		 * @Override protected void onPostExecute(Void result) { if (pd != null)
		 * { pd.dismiss(); } }
		 * 
		 * }; task.execute((Void[]) null);
		 */
		// aqui ya tengo en el array las dos palabras.

		Permutacion1 perm = new Permutacion1();
		Diccionario dic = new Diccionario(dicc);
		this.cargaFichero();

		String[] elementosMal = info[0].split("");
		String[] elementos = corregirArray(elementosMal);

		if (info[1].isEmpty()) {
			perm.perm(elementos, 0, 2);
		} else {
			perm.perm(elementos, 0, Integer.parseInt(info[1]));
		}
	
		Iterator it = perm.devolverListaPermutaciones().iterator();
		int indice;
		while (it.hasNext()) {
			indice = Collections.binarySearch(dic.devolverDiccionario(), it
					.next().toString());
			if (indice >= 0) {
				coincidencias.add(dic.devolverDiccionario().get(indice));
			}
		}
		HashSet<String> hashSet = new HashSet<String>(coincidencias);
		coincidencias.clear();
		coincidencias.addAll(hashSet);
		Collections.sort(coincidencias);

		if (coincidencias.isEmpty()) {
			coincidencias.add("No existen palabras");
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					AnagramaPalabrasExisten.this,
					android.R.layout.simple_selectable_list_item, coincidencias);
			list.setAdapter(adapter);
		} else {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					AnagramaPalabrasExisten.this,
					android.R.layout.simple_selectable_list_item, coincidencias);
			list.setAdapter(adapter);
			list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long id) {
					// TODO Auto-generated method stub

					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
							.parse("http://lema.rae.es/drae/?val="
									+ coincidencias.get(position).toString()));

					startActivity(browserIntent);

				}
			});

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.anagrama_palabras_existen, menu);
		return true;
	}

	public String[] corregirArray(String[] array) {
		String[] nuevo = new String[array.length - 1];
		for (int i = 0; i < array.length - 1; i++) {
			nuevo[i] = array[i + 1];
		}
		return nuevo;
	}



	private void cargaFichero() {
		String p1 = null;
		try {
			InputStream fraw = getResources().openRawResource(R.raw.words8pal);
			BufferedReader brin = new BufferedReader(
					new InputStreamReader(fraw));
			String linea = brin.readLine();
			while (linea != null) {
				p1 = linea;
				linea = brin.readLine();
				dicc.add(p1.toLowerCase());
			}
			fraw.close();
		} catch (Exception ex) {
			Log.e("Ficheros", "Error al leer fichero desde recurso raw");
		}
	}
}
