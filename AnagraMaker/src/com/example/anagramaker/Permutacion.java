package com.example.anagramaker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Permutacion {

	List<String> listaPermutaciones = new ArrayList<String>();

	/*
	 * public void perm(String[] elem, String act, int n, int r) { if (n == 0) {
	 * listaPermutaciones.add(act); } else { for (int i = 0; i < r; i++) {
	 * perm(elem, act + elem[i], n - 1, r); } } }
	 */
	public void perm(String[] elem, int n, int longMinima) {
		permute(Arrays.asList(elem), n, longMinima);
		HashSet<String> hashSet = new HashSet<String>(listaPermutaciones);
		listaPermutaciones.clear();
		listaPermutaciones.addAll(hashSet);
		Collections.sort(listaPermutaciones);		
	}

	private void permute(List<String> palabra, int min, int longMinima) {

		//es para que entren al menos palabras de tama�o 2
		if(longMinima<=1){
			longMinima=2;
		}
		for (int i = min; i < palabra.size(); i++) {
			Collections.swap(palabra, i, min);
			String aux = "";
			
			
			for (int j = min; j < palabra.size(); j++) {
				aux = aux + palabra.get(j);
			}	
			
			if (aux.length() >= longMinima) {
				listaPermutaciones.add(aux);
			}
			listaPermutaciones.add((ArrayToString((String[]) palabra.toArray())));
			permute(palabra, min + 1, longMinima);
			Collections.swap(palabra, min, i);
		}

	}

	// al final ni lo usare
	private String ArrayToString(String[] palabra) {
		StringBuilder x = new StringBuilder();
		for (int i = 0; i < palabra.length; i++) {
			x.append(palabra[i]);
		}
		return x.toString();
	}

	public List<String> devolverListaPermutaciones() {
		return listaPermutaciones;
	}

	public String toString() {
		return listaPermutaciones.toString();
	}
	public String[] devolverArray(){
		return (String[])listaPermutaciones.toArray();
	}
}
