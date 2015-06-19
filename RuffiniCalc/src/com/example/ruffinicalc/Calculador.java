package com.example.ruffinicalc;

import java.util.ArrayList;
import java.util.List;

public class Calculador {

	public List<Float> cuadrado(List<Integer> faltaCuadrado) {
		float a, b, c;
		a = faltaCuadrado.get(0);
		b = faltaCuadrado.get(1);
		c = faltaCuadrado.get(2);

		float aux = (float) (Math.pow(b, 2) - (4 * a * c));
		List<Float> listaAux = new ArrayList<Float>();
		if (aux < 0) {
			listaAux.clear();
		} else {
			float r1, r2;
			aux = (float) Math.sqrt(aux);
			r1 = (-b + aux) / (2 * a);
			r2 = (-b - aux) / (2 * a);
			listaAux.add(r1);
			listaAux.add(r2);
		}
		return listaAux;
	}

	public List<Integer> divisores(int terminoInd) {
		int n;
		if (terminoInd < 0) {
			n = -terminoInd;
		} else {
			n = terminoInd;
		}

		List<Integer> divisores = new ArrayList<Integer>();
		List<Integer> divisoresNegativos = new ArrayList<Integer>();
		for (int i = 1; i <= n; i++) {
			if (n % i == 0) {
				divisores.add(i);
			}
		}
		for (int x = 1; x <= n; x++) {
			if (n % x == 0) {
				divisoresNegativos.add(-x);
			}
		}
		divisores.addAll(divisoresNegativos);
		return divisores;
	}

	public List<Integer> ruffini1(List<Integer> polinomio,
			List<Integer> divisores) {

		List<Integer> listaAux = new ArrayList<Integer>();
		List<Integer> listaAux1 = new ArrayList<Integer>();
		listaAux1.addAll(polinomio);
		boolean raiz = false;
		int x = 0;

		while (!raiz && x < divisores.size()) {
			int aux = polinomio.get(0);
			for (int i = 1; i < polinomio.size(); i++) {
				aux *= divisores.get(x);
				aux += polinomio.get(i);
				listaAux.add(aux);

			}
			if (listaAux.get(listaAux.size() - 1) != 0) {
				listaAux.clear();
			} else {
				raiz = true;
				listaAux1.clear();
				listaAux1.add(polinomio.get(0));
				// Coge el nuevo polinomio
				listaAux1.addAll(listaAux.subList(0, listaAux.size() - 1));
				// añade de ultimo a la lista el divisor
				listaAux1.add(divisores.get(x));
				// ruffini1(listaAux1,
				// divisores(listaAux1.get(listaAux1.size() - 1)));
			}
			x++;
		}
		return listaAux1;
	}

	public List<List<Integer>> ruffiniTotal(List<Integer> polinomio,
			List<Integer> divisores) {
		// suponemos que al menos entra con grado 3, eso se hace desde la gui de
		// android.
		List<List<Integer>> listaFinal = new ArrayList<List<Integer>>();
		List<Integer> aux1 = ruffini1(polinomio,
				divisores(polinomio.get(polinomio.size() - 1)));
		List<Integer> aux2 = new ArrayList<Integer>();
		System.out.println(aux1);
		boolean noRaiz = false;

		List<Integer> raices = new ArrayList<Integer>();
		if (!polinomio.equals(aux1)) {
			while (aux1.size() > 3 && !noRaiz) {
				if (aux1.size() == 4) {

					raices.add(aux1.get(3));
					aux1.remove(aux1.size() - 1);
				} else {
					raices.add(aux1.get(aux1.size() - 1));
					aux1.remove(aux1.size() - 1);
					aux2 = aux1;
					aux1 = ruffini1(aux1, divisores(aux1.get(aux1.size() - 1)));
					if (aux2.equals(aux1)) {
						noRaiz = true;
					}
				}
			}
		}
		listaFinal.add(aux1);
		listaFinal.add(raices);

		return listaFinal;
	}
}
