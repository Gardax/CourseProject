package com.example.whattowatch;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Main extends Activity{
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
	}
	
	public void onRegister(View v) {
		setContentView(R.layout.register);
      }  
	
	public void Login(View v) {
		Intent myIntent = new Intent(Main.this, Movies.class);
		//myIntent.putExtra("key", value); //Optional parameters
		Main.this.startActivity(myIntent);
      }  
}
