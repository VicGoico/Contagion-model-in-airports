package modelo;

public class TAirport {
	private int id;				//0
	private String name;		//1
	private String city;		//2
	private String country;		//3
	private String iata;		//4
	private String icao;		//5
	private double latitude;	//6
	private double longitude;	//7
	private int altitude;		//8
	private int indegree;		//9
	private int outdegree;		//10
	private int degree;			//11
	private double umbral;		//12
	
	// Constructora para poder calcular los umbrales, posteriomente
	public TAirport(String id, String name, String city, String country, String iata, String icao,
			String latitude, String longitude, String altitude, String indegree, String outdegree, String degree){
		
		this.id = Integer.parseInt(id);
		/*if(this.id == 641){
			System.out.println("Hola");
		}*/
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
		this.umbral = 0.0;
	}
	// Construtora para leer el CSV bueno, con todos los datos
	public TAirport(String id, String name, String city, String country, String iata, String icao,
			String latitude, String longitude, String altitude, String indegree, String outdegree, String degree, String umbral){
		
		this.id = Integer.parseInt(id);
		/*if(this.id == 641){
			System.out.println("Hola");
		}*/
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
		this.umbral  = Double.parseDouble(umbral);
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
	public double getUmbral() {
		return umbral;
	}
	public void setUmbral(double umbral) {
		this.umbral = umbral;
	}
	
}

