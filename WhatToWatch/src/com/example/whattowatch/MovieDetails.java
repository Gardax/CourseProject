package com.example.whattowatch;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.renderscript.Double2;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class MovieDetails extends Activity{
	
	public int MovieId;
	public String SessionKey="none";
	public SingleMovie Movie;
	public Bitmap bmp;
	public int rating=0;
	
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
	
	public void onRate(View v) {
  	  	RatingBar stars=(RatingBar) findViewById(R.id.rating);
  	  	rating=(int)stars.getRating();
  	  Thread thread = new Thread(new Runnable(){
		    @Override
		    public void run() {
		try {
			
			DefaultHttpClient httpClient = new DefaultHttpClient();
		    HttpGet httpGet = new HttpGet("http://whattowatch-1.apphb.com/api/movies/Vote/"+SessionKey+"?movieId="+MovieId
		    		+"&vote="+rating);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			int code=httpResponse.getStatusLine().getStatusCode();
			if(code==HttpStatus.SC_OK){
				runOnUiThread(new Runnable() {
	                @Override
	                public void run() {
	        		    TextView ratingBox = (TextView) findViewById(R.id.textView1);
	        		    ratingBox.setText("Rating - "+(Movie.Rating+rating)/2);
	        		    Context context = getApplicationContext();
	        		    CharSequence text = "Successfully voted!";
	        		    int duration = Toast.LENGTH_SHORT;

	        		    Toast toast = Toast.makeText(context, text, duration);
	        		    toast.show();
	                }
	            });
			}else{
				runOnUiThread(new Runnable() {
	                @Override
	                public void run() {
	        		    
	        		    Context context = getApplicationContext();
	        		    CharSequence text = "You can not vote for this movie!";
	        		    int duration = Toast.LENGTH_SHORT;

	        		    Toast toast = Toast.makeText(context, text, duration);
	        		    toast.show();
	                }
	            });
			}
		    
		} catch (ClientProtocolException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		}});
	    thread.start();
    }  
	
	public void onWatched(View v) {
  	  	
  	  Thread thread = new Thread(new Runnable(){
		    @Override
		    public void run() {
		try {
			
			DefaultHttpClient httpClient = new DefaultHttpClient();
		    HttpGet httpGet = new HttpGet("http://whattowatch-1.apphb.com/api/movies/MarkWatched/"
			+SessionKey+"?movieId="+MovieId);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			int code=httpResponse.getStatusLine().getStatusCode();
			if(code==HttpStatus.SC_OK){
				runOnUiThread(new Runnable() {
	                @Override
	                public void run() {
	        		    
	        		    Context context = getApplicationContext();
	        		    CharSequence text = "Successfully added to watched!";
	        		    int duration = Toast.LENGTH_SHORT;

	        		    Toast toast = Toast.makeText(context, text, duration);
	        		    toast.show();
	                }
	            });
			}else{
				runOnUiThread(new Runnable() {
	                @Override
	                public void run() {
	        		    
	        		    Context context = getApplicationContext();
	        		    CharSequence text = "Unsuccessfully added!";
	        		    int duration = Toast.LENGTH_SHORT;

	        		    Toast toast = Toast.makeText(context, text, duration);
	        		    toast.show();
	                }
	            });
			}
		} catch (ClientProtocolException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		}});
	    thread.start();
    }  
	
	
}
