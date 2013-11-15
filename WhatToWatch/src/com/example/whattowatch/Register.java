package com.example.whattowatch;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Source;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.google.gson.Gson;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Register extends Activity{
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
	}
	
	
	public String URL = "http://whattowatch-1.apphb.com/api/users/register";  
    public String result = "";  
    public String error="";
    public Context that;
    public String username;
    public String firstname;
    public String lastname;
    public String authcode;
    public String sessionKey;
    LoggedUser user;
    
	public void Register(View v) {
		
		that=this;
		EditText swap = (EditText) findViewById(R.id.username);
		username=swap.getText().toString();
		swap=(EditText) findViewById(R.id.firstname);
		firstname=swap.getText().toString();
		swap=(EditText) findViewById(R.id.lastname);
		lastname=swap.getText().toString();
		swap=(EditText) findViewById(R.id.password);
		authcode=swap.getText().toString();
		
		Thread thread = new Thread(new Runnable(){
		    @Override
		    public void run() {
		        try {
		        	HttpPost post = new HttpPost(URL);    

		    		DefaultHttpClient client = new DefaultHttpClient();
		    		
		    		
		    		try {
		    			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
			            nameValuePairs.add(new BasicNameValuePair("Username", username));
			            nameValuePairs.add(new BasicNameValuePair("FirstName", firstname));
			            nameValuePairs.add(new BasicNameValuePair("LastName", lastname));
			            nameValuePairs.add(new BasicNameValuePair("AuthCode", authcode));
			            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		    		    HttpResponse response = client.execute(post);

		    		    result = EntityUtils.toString(response.getEntity());
		    		    
		    		    Gson gson=new Gson();
		    		    user=gson.fromJson(result, LoggedUser.class);
		    		    
		    		    sessionKey=user.SessionKey;
		    		    
		    		    SharedPreferences settings = getSharedPreferences("SessionKey", 0);
		    		    SharedPreferences.Editor editor = settings.edit();
		    		    editor.putString("SessionKey", sessionKey);
		    		    editor.commit();
		    		    
		    		    Intent reg = new Intent(Register.this, Movies.class);
		    			reg.putExtra("sessionKey", sessionKey);
		    			Register.this.startActivity(reg);
		    		}catch (IOException e) {
		    		post.abort();
		    		error=e.toString();
		    		runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        	TextView t=new TextView(that); 
    		    		    t=(TextView)findViewById(R.id.error); 
    		    		    t.setText(error.toString());
                        }
                    });
		    		}  
		        } catch (Exception e) {
		            e.printStackTrace();
		            error=e.toString();
		            runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        	TextView t=new TextView(that); 
    		    		    t=(TextView)findViewById(R.id.error); 
    		    		    t.setText(error.toString());
                        }
                    });
		        }
		    }
		});

		thread.start();
      }  
	
}
