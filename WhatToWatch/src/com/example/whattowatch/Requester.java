package com.example.whattowatch;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.R.string;

public class Requester {
	public String URL = "http://the/url/here";  
    public String result = "";  
    
    public void calWebService(String q){  
        HttpClient httpclient = new DefaultHttpClient();  
        HttpGet request = new HttpGet(URL + q);  
        ResponseHandler<String> handler = new BasicResponseHandler();  
        try {  
            result = httpclient.execute(request, handler);  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        httpclient.getConnectionManager().shutdown();   
    }
}
