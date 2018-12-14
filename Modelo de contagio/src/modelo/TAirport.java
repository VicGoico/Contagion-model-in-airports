package modelo;

public class TAirport {
	private int id;//0
	private String name;//3
	private String city;//4
	private String country;//5
	private String iata;//6
	private String icao;//7
	private double latitude;//8
	private double longitude;//9
	private int altitude;//10
	private int indegree;//11
	private int outdegree;//12
	private int degree;//13
	
	public TAirport(String id, String name, String city, String country, String iata, String icao,
			String latitude, String longitude, String altitude, String indegree, String outdegree, String degree){
		
		this.id = Integer.parseInt(id);
		if(this.id == 641){
			System.out.println("Hola");
		}
		this.name = name;
		this.city = city;
		this.country = country;
		this.iata = iata;
		this.icao = icao;
		this.latitude = Double.parseDouble(latitude);
		this.longitude = Double.parseDouble(longitude);
		this.altitude =Integer.parseInt(altitude);
		this.indegree = Integer.parseInt(indegree);
		this.outdegree = Integer.parseInt(outdegree);
		this.degree = Integer.parseInt(degree);
	}
	public int getId(){
		return this.id;
	}
	public String getName(){
		return this.name;
	}
	public String getCity(){
		return this.city;
	}
	public String getCountry(){
		return this.country;
	}
	public String getIata(){
		return this.iata;
	}
	public String getIcao(){
		return this.icao;
	}
	public double getLatitude(){
		return this.latitude;
	}
	public double getLongitude(){
		return this.longitude;
	}
	public int getAltitude(){
		return this.altitude;
	}
	public int getIndegree(){
		return this.indegree;
	}
	public int getOutdegree(){
		return this.outdegree;
	}
	public int getDegree(){
		return this.degree;
	}
	
}

