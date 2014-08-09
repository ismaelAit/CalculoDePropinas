package org.example.calculo_de_propinas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Resultado extends Activity {
	
	TextView resultadoText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resultado);
		
		resultadoText = (TextView) findViewById(R.id.textViewResultado);
		
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		String resultado = extras.getString("mensaje");
		
		resultadoText.setText(resultado);
	}

}
