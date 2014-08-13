package org.example.calculo_de_propinas;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Consulta extends Activity {

	EditText montoText;
	SeekBar propinaBar;
	TextView propinaValor;
	Spinner personasSpinner;
	EditText selected;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_consulta);
		
		montoText = (EditText) findViewById(R.id.editTextMonto);
		propinaBar = (SeekBar) findViewById(R.id.seekBarPropina);
		propinaValor = (TextView) findViewById(R.id.textViewPropinaValor);
		personasSpinner = (Spinner) findViewById(R.id.spinnerPersonas);
		
		registerForContextMenu(montoText);

		configurarSeekBar();
		configurarSpinner();
	}

	private void configurarSpinner() {
		String[] items = {"0", "1", "2", "3", "4", "5", "6", "7", "8"};
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		personasSpinner.setAdapter(arrayAdapter);
	}

	private void configurarSeekBar() {

		propinaBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				propinaValor.setText("" + progress + "%");
			}
		});
		propinaValor.setText("" + propinaBar.getProgress() + "%");
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_consulta, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.itemLimpiar:
			Toast.makeText(this, "limpiando todo...", Toast.LENGTH_SHORT).show();
			limpiar();
			break;
		case R.id.itemDefault:
			Toast.makeText(this, "poniendo valores default...", Toast.LENGTH_SHORT).show();
			valoresDefault();
			break;
		default:
			return super.onOptionsItemSelected(item);
		}	
		return true;
	}
	
	private void limpiar() {
		montoText.setText("");
		propinaBar.setProgress(0);
		personasSpinner.setSelection(0);
	}
	
	private void valoresDefault() {
		montoText.setText("100");
		propinaBar.setProgress(10);
		personasSpinner.setSelection(1);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		
		switch (v.getId()) {
		case R.id.editTextMonto:
			getMenuInflater().inflate(R.menu.menu_individual, menu);
			selected = (EditText) v;
			break;
		default:
			super.onCreateContextMenu(menu, v, menuInfo);
			break;
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.itemLimpiarIndividual:
			Toast.makeText(this, "limpiando item...", Toast.LENGTH_SHORT).show();
			selected.setText("");
			break;
		default:
			return super.onContextItemSelected(item);
		}	
		return true;
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
	
	private int obtenerValorEntero(String str) {
//		String str = text.getText().toString();
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
		propina = propinaBar.getProgress();
		personas = obtenerValorEntero((String) personasSpinner.getSelectedItem());
		if (personas <= 0) {
			Toast.makeText(this, "Campo \"Personas\" debe ser un número entero positivo", Toast.LENGTH_SHORT).show();
			return;
		}
		
		double total = monto + monto*propina/100;
		double indiv = total / personas;
		
//		String result = String.format("Monto total a pagar: %.2f\nCada persona debe pagar: %.2f", total, indiv);
		
		String strResult = "Monto total a pagar: " + String.format("%.2f", total) + "\n"
				      	 + "Cada persona debe pagar: " + String.format("%.2f", indiv);
				
		String strNotify = "Total: " + String.format("%.2f", total) + " -- Persona: " + String.format("%.2f", indiv);
		
		Intent intent = new Intent(this, Resultado.class);
		intent.putExtra("mensaje", strResult);
		
		notificar(strNotify, intent);

	}
	
	private void notificar(String msj, Intent intent) {

		PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
			.setSmallIcon(R.drawable.ic_stat_notify)
			.setPriority(NotificationCompat.PRIORITY_DEFAULT)
			.setContentText("Resultado")
			.setAutoCancel(true)
			.setContentText(msj)
			.setContentIntent(pendingIntent);

		NotificationManager notiMan = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notiMan.notify(1, builder.build());

	}
	
}
