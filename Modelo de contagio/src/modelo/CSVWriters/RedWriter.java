package modelo.CSVWriters;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import modelo.red.Red;

public class RedWriter {
	public RedWriter() {}
	
	public void write(Red red, String outputFileName) throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(outputFileName));
		out.write("source,target" + System.getProperty("line.separator"));
		red.toCSVFormat(out);
		out.close();
	}
}
