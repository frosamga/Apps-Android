package com.example.agenda;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MiAdaptador extends BaseAdapter {

	private Activity activity;
	private List <Contacto> contacto;
	
	public  MiAdaptador(Activity a, List <Contacto> al){
		super();
		this.activity=a;
		this.contacto=al;
	}
	public  MiAdaptador(Activity a, AdaptadorBD db){
		super();
		this.activity=a;
		db.open();
		this.contacto=db.getAll();
		db.close();
	}

	@Override
	public int getCount() {
		return contacto.size();
	}

	@Override
	public Object getItem(int arg0) {
		return contacto.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.elemento_lista, null);
        TextView textView1 =(TextView)view.findViewById(R.id.titulo);
        textView1.setText(contacto.get(arg0).getNombre());
        TextView textView2 =(TextView)view.findViewById(R.id.subtitulo);
        textView2.setText(contacto.get(arg0).getTelefono());
        return view;
        
	}
	
}
