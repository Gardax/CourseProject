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
		Intent myIntent = new Intent(Main.this, Register.class);
		Main.this.startActivity(myIntent);
      }  
	
	public void Login(View v) {
		Intent myIntent = new Intent(Main.this, Movies.class);
		Main.this.startActivity(myIntent);
      }  
}
