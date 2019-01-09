package modelo.metricas.tools;

import java.util.HashMap;

public class CorrespondingCountry {
	public static HashMap<String, String> map = null;
	public static String DEFAULTVALUE = "";
	
	/**
	 * Paises que no están bien definidos o no están reconocidos internacionalmente
	 * y generan conflicto
	 */
	public CorrespondingCountry() {
		map = new HashMap<>();
		
		map.put("Greenland", "Denmark");
		map.put("United States", "United States of America");
		map.put("Russia", "Russian Federation");
		map.put("Cote d'Ivoire", DEFAULTVALUE);
		map.put("Guernsey", "United Kingdom");
		map.put("Jersey", "United Kingdom");
		map.put("Isle of Man", "United Kingdom");
		map.put("Falkland Islands", "United Kingdom");
		map.put("Faroe Islands", "Denmark");
		map.put("Congo (Brazzaville)", "Dem. Rep. of the Congo");
		map.put("Congo (Kinshasa)", "Dem. Rep. of the Congo");
		map.put("Swaziland", DEFAULTVALUE);
		map.put("Mayotte", "France");
		map.put("Reunion", "France");
		map.put("Vietnam", "Viet Nam");
		map.put("Cape Verde", "Cabo Verde");
		map.put("Somalia", DEFAULTVALUE);
		map.put("Tanzania", DEFAULTVALUE);
		map.put("Saint Pierre and Miquelon", "France");
		map.put("Czech Republic", "Czechia");
		map.put("Moldova", "Republic of Moldova");
		map.put("Macedonia", "TFYR of Macedonia");
		map.put("Gibraltar", "United Kingdom");
		map.put("Turks and Caicos Islands", "United Kingdom");
		map.put("Cuba", DEFAULTVALUE);
		map.put("Cayman Islands", "United Kingdom");
		map.put("Wallis and Futuna", "France");
		map.put("American Samoa", "United States of America");
		map.put("Northern Mariana Islands", "United States of America");
		map.put("Guam", "United States of America");
		map.put("French Polynesia", "France");
		map.put("New Caledonia", "France");
		map.put("Iran", "Iran (Islamic Republic of)");
		map.put("Micronesia", DEFAULTVALUE);
		map.put("Taiwan", "China");
		map.put("South Korea", "Republic of Korea");
		map.put("Bolivia", "Bolivia (Plurin. State of)");
		map.put("French Guiana", "France");
		map.put("Venezuela", "Venezuela (Boliv. Rep. of)");
		map.put("Martinique", "France");
		map.put("Guadeloupe", "France");
		map.put("Virgin Islands", "United States of America");
		map.put("Puerto Rico", "United States of America");
		map.put("Aruba", "Netherlands");
		map.put("Netherlands Antilles", "Netherlands");
		map.put("Anguilla", "United Kingdom");
		map.put("British Virgin Islands", "United Kingdom");
		map.put("Saint Vincent and the Grenadines", "United Kingdom");
		map.put("Hong Kong", "China");
		map.put("Laos", "Lao People's Dem. Rep.");
		map.put("Macau", "China");
		map.put("East Timor", "Timor-Leste");
		map.put("Burma", DEFAULTVALUE);
		map.put("Brunei", DEFAULTVALUE);
		map.put("Christmas Island", "Australia");
		map.put("Norfolk Island", "Australia");
		map.put("North Korea", DEFAULTVALUE);
		map.put("Bermuda", "United Kingdom");
		map.put("Western Sahara", "Spain");
		map.put("Cocos (Keeling) Islands", "Australia");
	}
}
