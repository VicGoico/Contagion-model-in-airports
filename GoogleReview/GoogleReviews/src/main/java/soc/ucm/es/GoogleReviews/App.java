package soc.ucm.es.GoogleReviews;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        
        GeoApiContext context = new GeoApiContext.Builder()
        	    .apiKey("AIza...")
        	    .build();
        	GeocodingResult[] results =  GeocodingApi.geocode(context,
        	    "1600 Amphitheatre Parkway Mountain View, CA 94043").await();
        	Gson gson = new GsonBuilder().setPrettyPrinting().create();
        	System.out.println(gson.toJson(results[0].addressComponents));
        	
    }
}
