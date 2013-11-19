package com.example.whattowatch;

import java.util.Collection;

import android.provider.MediaStore.Images.ImageColumns;

public class Movie {
	public int Id;
	public String Title;
	/*public String Description;
	public String CoverUrl;
	public double Rating;
	public Collection<Comment> Comments;*/
	
	@Override
	public String toString()
	{
		return Title;
	}
}
