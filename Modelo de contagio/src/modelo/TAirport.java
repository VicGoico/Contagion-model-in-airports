package modelo;

public class TAirport {
	private int id; // 0
	private String name; // 1
	private String city; // 2
	private String country; // 3
	private String iata; // 4
	private String icao; // 5
	private double latitude; // 6
	private double longitude; // 7
	private int altitude; // 8
	private int calculatedIndegree; // 9
	private int calculatedOutdegree; // 10
	private int calculatedDegree; // 11

	// Construtora para leer el CSV bueno, con todos los datos
	public TAirport(int id, String name, String city, String country, String iata, String icao, double latitude,
			double longitude, int altitude, int indegree, int outdegree, int degree) {
		this.id = id;
		this.name = name;
		this.city = city;
		this.country = country;
		this.iata = iata;
		this.icao = icao;
		this.latitude = latitude;
		this.longitude = longitude;
		this.altitude = altitude;
		this.calculatedIndegree = indegree;
		this.calculatedOutdegree = outdegree;
		this.calculatedDegree = degree;
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getCity() {
		return this.city;
	}

	public String getCountry() {
		return this.country;
	}

	public String getIata() {
		return this.iata;
	}

	public String getIcao() {
		return this.icao;
	}

	public double getLatitude() {
		return this.latitude;
	}

	public double getLongitude() {
		return this.longitude;
	}

	public int getAltitude() {
		return this.altitude;
	}

	public int getCalculatedIndegree() {
		return this.calculatedIndegree;
	}

	public int getCalculatedOutdegree() {
		return this.calculatedOutdegree;
	}

	public int getCalculatedDegree() {
		return this.calculatedDegree;
	}
	
	@Override
	public String toString() {
		return this.id + "," + this.name.replaceAll(",", "-") + "," + this.city + "," + this.country + "," + this.iata
				+ "," + this.icao + "," + this.latitude + "," + this.longitude + "," + this.altitude + ","
				+ this.calculatedIndegree + "," + this.calculatedOutdegree + "," + this.calculatedDegree;
	}
}
