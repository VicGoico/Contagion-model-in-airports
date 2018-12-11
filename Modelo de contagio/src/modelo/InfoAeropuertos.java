package modelo;

public class InfoAeropuertos {

	private String Id;
	private String name;
	private String city;
	private String country;
	private String iata;
	private String icao;
	private String latitude;
	private String longitude;
	private String altitude;
	private String indegree;
	private String outdegree;
	private String degree;
	
	public InfoAeropuertos(String id, String name, String city, String country,
			String iata, String icao, String latitude, String longitude,
			String altitude, String indegree, String outdegree, String degree) {
		this.Id = id;
		this.name = name;
		this.city = city;
		this.country = country;
		this.iata = iata;
		this.icao = icao;
		this.latitude = latitude;
		this.longitude = longitude;
		this.altitude = altitude;
		this.indegree = indegree;
		this.outdegree = outdegree;
		this.degree = degree;
	}
	
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getIata() {
		return iata;
	}
	public void setIata(String iata) {
		this.iata = iata;
	}
	public String getIcao() {
		return icao;
	}
	public void setIcao(String icao) {
		this.icao = icao;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getAltitude() {
		return altitude;
	}
	public void setAltitude(String altitude) {
		this.altitude = altitude;
	}
	public String getIndegree() {
		return indegree;
	}
	public void setIndegree(String indegree) {
		this.indegree = indegree;
	}
	public String getOutdegree() {
		return outdegree;
	}
	public void setOutdegree(String outdegree) {
		this.outdegree = outdegree;
	}
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	
}
