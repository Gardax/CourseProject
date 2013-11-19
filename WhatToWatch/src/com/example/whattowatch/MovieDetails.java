package com.example.whattowatch;

import java.io.IOException;
import java.net.URL;
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
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieDetails extends Activity{
	
	public int MovieId;
	public String SessionKey="none";
	public SingleMovie Movie;
	public Bitmap bmp;
	
	@Override
	  public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.movie_details);
		
		//Get movie id
		Intent theIntent = getIntent();
        MovieId = theIntent.getIntExtra("movieId", 0);
        
        //Get sessionKey
        SharedPreferences settings = getSharedPreferences("SessionKey", 0); 
        SessionKey = settings.getString("SessionKey", "none");
        
        //Get movie details
        Thread thread = new Thread(new Runnable(){
		    @Override
		    public void run() {
		try {
			
			DefaultHttpClient httpClient = new DefaultHttpClient();
		    HttpGet httpGet = new HttpGet("http://whattowatch-1.apphb.com/api/movies/GetSingleMovie/"+
			SessionKey+"?id="+MovieId);
		    
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
		    String output = EntityUtils.toString(httpEntity);
		    Gson gson=new Gson();
		    Movie=gson.fromJson(output, SingleMovie.class);
		    
		    runOnUiThread(new Runnable() {
                @Override
                public void run() {
                	TextView title = (TextView) findViewById(R.id.title);
        		    title.setText(Movie.Title);
        		    
        		    
        		    Thread picThread = new Thread(new Runnable(){
        			    @Override
        			    public void run() {
					try {
						
						URL url = new URL(Movie.CoverUrl);
	        		    bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
	        		    runOnUiThread(new Runnable() {
	                        @Override
	                        public void run() {
	        		    ImageView image=(ImageView) findViewById(R.id.cover);
	        		    image.setImageBitmap(bmp);
	                        }
	        		    });
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
        			});
        		    picThread.start();
        		   
        		    TextView description = (TextView) findViewById(R.id.description);
        		    description.setText(Movie.Description);
        		    
        		    TextView ratingBox = (TextView) findViewById(R.id.textView1);
        		    ratingBox.setText("Rating - "+Movie.Rating);
                }
            });
		} catch (ClientProtocolException e) {
			
			
		} catch (IOException e) {
			
		}
		}});
	    thread.start();
        
	}
}
