package modelo.CSVWriters;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import modelo.modelos.AristaContagiadaSimple;

public class AristasSimplesWriter {
	public AristasSimplesWriter() {}
	
	public void write(ArrayList<AristaContagiadaSimple> aristas, String outputFileName) throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(outputFileName));
		out.write("Source;Target;Weight" + System.getProperty("line.separator"));
		
		for (int i = 0; i < aristas.size(); i++) {
			AristaContagiadaSimple a = aristas.get(i);
			out.write(a.getNodo1() + ";" + a.getNodo2() + ";" + a.getPeso() + System.getProperty("line.separator"));
		}
		
		out.close();
	}
}
