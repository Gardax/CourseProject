package com.example.whattowatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListMovies extends ListActivity {

	  public String SessionKey="none";
	  public int page=1;
	  public List<Movie> Movies;
	  public Context that;
	  
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    SharedPreferences settings = getSharedPreferences("SessionKey", 0);
        SessionKey = settings.getString("SessionKey", "none");
        that=this;
	    
	    Thread thread = new Thread(new Runnable(){
		    @Override
		    public void run() {
		try {
			
			DefaultHttpClient httpClient = new DefaultHttpClient();
		    HttpGet httpGet = new HttpGet("http://whattowatch-1.apphb.com/api/movies/GetTopMovies/" +
		    		SessionKey+"?page="+page);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
		    String output = EntityUtils.toString(httpEntity);
		    Gson gson=new Gson();
		    Movies=gson.fromJson(output, new TypeToken<List<Movie>>(){}.getType());
		    runOnUiThread(new Runnable() {
                @Override
                public void run() {
                	
            	    ArrayAdapter<Movie> adapter = new ArrayAdapter<Movie>(that,
            	        android.R.layout.simple_list_item_1, Movies);
            	    	
            	    setListAdapter(adapter);
                }
            });
		} catch (ClientProtocolException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		}});
	    thread.start();
	    
		
	  }

	  @Override
	  public void onListItemClick(ListView l, View v, int position, long id) {
		  Intent intent = new Intent(ListMovies.this, MovieDetails.class);
		  Movie movie=(Movie)getListView().getItemAtPosition(position);
		  intent.putExtra("movieId", movie.Id);
			ListMovies.this.startActivity(intent);

	  }
	} 

