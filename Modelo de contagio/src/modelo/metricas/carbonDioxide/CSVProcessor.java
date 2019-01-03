package modelo.metricas.carbonDioxide;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import modelo.makeUsesfullCSV;

public class CSVProcessor {
	String fileName;
	private BufferedReader fileBuffer;
	private makeUsesfullCSV processCSVDatas;
	
	public CSVProcessor(String fileName, makeUsesfullCSV mUfCSV) throws FileNotFoundException {
		this.fileName = fileName;
		this.processCSVDatas = mUfCSV;
		
		this.openFile();
	}
	
	private void openFile() throws FileNotFoundException {
		this.fileBuffer = new BufferedReader(new FileReader(this.fileName));
	}
	
	private Collection<String> splitLine(String line){
		String[] lineParts = line.split(","); // Separo todas las lineas con ,
		List<String> lnFParts = new ArrayList<String>();
		String buff = "";
		
		
		for (int i = 0; i < lineParts.length; i++) {
			if(lineParts[i].startsWith("\"")) {
				// He encontrado una cadena, uno todo eso de nuevo para procesarlo
				buff = String.join(",", Arrays.copyOfRange(lineParts, i, lineParts.length));
				System.out.println("CADENA DETECTADA: ");
				System.out.println(buff);
			} else {
				lnFParts.add(lineParts[i]);
			}
		}
		return null;
	}
	
	private void readCSV() throws IOException {
		String line;
		
		try {
			while ((line = fileBuffer.readLine()) != null) {
				Collection<String> ln = this.splitLine(line);
				
				// Una vez tengo separadas las lineas extraigo de cada una lo que me interesa...
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if (this.fileBuffer != null) {
				try {
					this.fileBuffer.close();
				} catch (IOException e) {
					throw e;
				}
			}
		}
	}
}
