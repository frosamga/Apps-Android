package com.example.anagramaker;

import java.util.ArrayList;
import java.util.List;

public class Diccionario {

	private List<String> dicc;

	public Diccionario() {
	}

	public Diccionario(List<String> diccionario) {
		dicc = diccionario;
	}

	public String sacarPalabra(int x) {
		return dicc.get(x);
	}

	public boolean existePalabra(String palabra) {
		if (dicc.contains(palabra)) {
			return true;
		} else {
			return false;
		}
	}

	public <T> List<T> interseccion(List<T> list1, List<T> list2) {
		List<T> list = new ArrayList<T>();
		for (T t : list1) {
			if (list2.contains(t)) {
				list.add(t);
			}
		}
		return list;
	}

	public String toString() {
		return dicc.toString();
	}
	public List<String> devolverDiccionario(){
		return dicc;
	}

	public boolean estaVacio() {
		return dicc.isEmpty();
	}
}
