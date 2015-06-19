package com.example.ruffinicalc;

import java.util.ArrayList;
import java.util.List;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Raices extends ActionBarActivity {
	private String[] info;
	private static TextView polinomioVista, polinomioOriginal;
	private Calculador calc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_raices);
		calc = new Calculador();
		polinomioVista = (TextView) findViewById(R.id.textView1);
		polinomioOriginal = (TextView) findViewById(R.id.pol);
		Bundle b = this.getIntent().getExtras();
		info = b.getStringArray("Poli");
		List<Integer> lista = new ArrayList<Integer>();
		for (int i = 0; i < info.length; i++) {
			lista.add(Integer.parseInt(info[i]));
		}
		// pone el polinomio que has puesto pero bien, para comprobar que es el
		// mismo.
		polinomioOriginal.setText(Html.fromHtml(componerPolinomio(lista)));
		factorizar(lista);
	}

	// genera el polinomio para la comprobacion de lo que se ha metido este
	// bien.
	public String componerPolinomio(List<Integer> polinomio) {
		// el StringBuilder no reconoce el html como si, no deja escribir la
		// formula
		String sb = "";
		for (int i = 0; i < polinomio.size(); i++) {
			if (polinomio.get(i) != 0) {
				if (i != 0 && polinomio.get(i) > 0) {
					sb += "+";
				}
				if (i == polinomio.size() - 2) {
					sb += polinomio.get(i) + "x";
				} else if (i == polinomio.size() - 1) {
					sb += polinomio.get(i);
				} else {
					sb += polinomio.get(i) + "x" + "<small><sup>"
							+ (polinomio.size() - (i + 1)) + "</sup></small>";
				}
			}
		}
		return sb;
	}

	// se pone a factorizar usando el algoritmo de ruffini, solo baja el primer
	// valor de la lista y lo multiplica por el primer divisor, y continuamente,
	// si el resto es 0 entonces para y devuelve un valor, es recursivo hasta
	// que el resto no sea 0. Si un divisor no da 0 pasa al siguiente divisor y
	// tal, si ninguno va, no existe una factorizacion entera para el polinomio
	public void factorizar(List<Integer> polinomio) {
		// Comprobaciones de sistema
		// System.out.println("Polinomio" + polinomio);
		// System.out.println("Divisores: "
		// + calc.divisores(polinomio.get(polinomio.size() - 1)));
		// System.out.println("polinomio +  raices debajo");
		if (polinomio.size() == 3) {
			if (calc.cuadrado(polinomio).isEmpty()) {
				polinomioVista
						.setText("No hay raices enteras para este polinomio");
			} else {
				StringBuilder str = new StringBuilder();
				List<Float> l = calc.cuadrado(polinomio);
				for (int i = 0; i < l.size(); i++) {
					if (l.get(i) > 0) {
						str.append("(x - " + l.get(i) + ")");
					} else {
						str.append("(x + " + Math.abs(l.get(i)) + ")");
					}
				}
				polinomioVista.setText("Las raices son:" + str.toString());

			}
		} else {
			List<List<Integer>> factorizado = calc.ruffiniTotal(polinomio,
					calc.divisores(polinomio.get(polinomio.size() - 1)));
			// raices
			if (factorizado.get(1).isEmpty()) {
				polinomioVista
						.setText("No hay raices enteras para este polinomio");
			} else {
				StringBuilder str = new StringBuilder();
				String stPol = ("(");
				for (int i = 0; i < factorizado.get(1).size(); i++) {
					if (factorizado.get(1).get(i) > 0) {
						str.append("(x - " + factorizado.get(1).get(i) + ")");
					} else {
						str.append("(x + "
								+ Math.abs(factorizado.get(1).get(i)) + ")");
					}

				}
				// pone los exponentes a las x y genera el modelo bien
				for (int x = 0; x < factorizado.get(0).size(); x++) {
					if (factorizado.get(0).size() - x == 7) {
						if (factorizado.get(0).get(x) > 0) {
							stPol += ("+" + factorizado.get(0).get(x) + "x<small><sup>6</sup></small>");
						} else {
							stPol += ("-" + factorizado.get(0).get(x) + "x<small><sup>6</sup></small>");
						}
					}
					if (factorizado.get(0).size() - x == 6) {
						if (factorizado.get(0).get(x) > 0) {
							stPol += ("+" + factorizado.get(0).get(x) + "x<small><sup>5</sup></small>");
						} else {
							stPol += ("-" + factorizado.get(0).get(x) + "x<small><sup>5</sup></small>");
						}

					}
					if (factorizado.get(0).size() - x == 5) {
						if (factorizado.get(0).get(x) > 0) {
							stPol += ("+" + factorizado.get(0).get(x) + "x<small><sup>4</sup></small>");
						} else {
							stPol += ("-" + factorizado.get(0).get(x) + "x<small><sup>4</sup></small>");
						}

					}
					if (factorizado.get(0).size() - x == 4) {
						if (factorizado.get(0).get(x) > 0) {
							stPol += ("+" + factorizado.get(0).get(x) + "x<small><sup>3</sup></small>");
						} else {
							stPol += ("-" + factorizado.get(0).get(x) + "x<small><sup>3</sup></small>");
						}
					}
					if (factorizado.get(0).size() - x == 3) {
						if (factorizado.get(0).get(x) > 0) {
							stPol += ("+" + factorizado.get(0).get(x) + "x<small><sup>2</sup></small>");
						} else {
							stPol += ("-" + factorizado.get(0).get(x) + "x<small><sup>2</sup></small>");
						}

					}
					if (factorizado.get(0).size() - x == 2) {
						if (factorizado.get(0).get(x) > 0) {
							stPol += ("+" + factorizado.get(0).get(x) + "x");
						} else {
							stPol += ("-" + factorizado.get(0).get(x) + "x");
						}

					}
					if (factorizado.get(0).size() - x == 1) {
						if (factorizado.get(0).get(x) > 0) {
							stPol += ("+" + factorizado.get(0).get(x) + ")");
						} else {
							stPol += ("-" + factorizado.get(0).get(x) + ")");
						}

					}
				}

				polinomioVista.setText(Html.fromHtml("El resultado es: "
						+ stPol + str.toString()));

			}

			// polinomioVista.setText(factorizado.toString());

			// En un principio iba a hacer el cuadrado del polinomio de segundo
			// grado, pero debido a errores con soluciones reales lo hemos
			// dejado solo si se ingresa un polinomio de grado 2 exclusivamente
			//Asi que estas variables las dejare por si el codigo se puede optimizar
			List<Integer> raicesTotales = factorizado.get(1);
			List<Integer> faltaCuadrado = factorizado.get(0);
			List<Float> cuadrado1 = new ArrayList<Float>();

		}

	}

}
