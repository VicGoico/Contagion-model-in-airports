package soc.ucm.es.GoogleReviews;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws ApiException, InterruptedException, IOException
    {
        System.out.println( "Hello World!" );
        GeoApiContext context = new GeoApiContext.Builder()
        	    .apiKey("AIzaSyBKfTFLkbKc0bwuT_kmDeEC9z7nfpSPbXg") // Directions API, Geocoding API
				.build();
				
        	GeocodingResult[] results =  GeocodingApi.geocode(context,
        	    "1600 Amphitheatre Parkway Mountain View, CA 94043").await();
        	Gson gson = new GsonBuilder().setPrettyPrinting().create();
        	System.out.println(gson.toJson(results));
        	
    }
}
