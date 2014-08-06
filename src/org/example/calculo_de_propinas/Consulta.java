package org.example.calculo_de_propinas;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Consulta extends Activity {

	EditText montoText;
	EditText propinaText;
	EditText personasText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_consulta);
		
		montoText = (EditText) findViewById(R.id.editTextMonto);
		propinaText = (EditText) findViewById(R.id.editTextPropina);
		personasText = (EditText) findViewById(R.id.editTextPersonas);
	}
	
	private double obtenerValorDecimal(EditText text) {
		String str = text.getText().toString();
		double dbl;
		try {
			dbl = Double.parseDouble(str);
			return dbl;
		} catch (NumberFormatException e) {
			Toast.makeText(this, "Formato numérico incorrecto", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
			return -1;
		}
	}
	
	private int obtenerValorEntero(EditText text) {
		String str = text.getText().toString();
		int entero;
		try {
			entero = Integer.parseInt(str);
			return entero;
		} catch (NumberFormatException e) {
			Toast.makeText(this, "Formato numérico incorrecto", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
			return -1;
		}
	}
	
	public void calcular(View view) {
		
		double monto;
		double propina;
		double personas;
		
		monto = obtenerValorDecimal(montoText);
		if (monto <= 0) {
			Toast.makeText(this, "Campo \"Monto\" debe ser un número positivo", Toast.LENGTH_SHORT).show();
			return;
		}
		propina = obtenerValorDecimal(propinaText);
		if (propina < 0) {
			Toast.makeText(this, "Campo \"Propina\" debe ser un número no negativo", Toast.LENGTH_SHORT).show();
			return;
		}
		personas = obtenerValorEntero(personasText);
		if (personas <= 0) {
			Toast.makeText(this, "Campo \"Personas\" debe ser un número entero positivo", Toast.LENGTH_SHORT).show();
			return;
		}
		
		double total = monto + monto*propina/100;
		double indiv = total / personas;
		
//		String result = String.format("Monto total a pagar: %.2f\nCada persona debe pagar: %.2f", total, indiv);
		
		String result = "Monto total a pagar: " + String.format("%.2f", total) + "\n"
				      + "Cada persona debe pagar: " + String.format("%.2f", indiv);
				
		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		
	}
	
}
