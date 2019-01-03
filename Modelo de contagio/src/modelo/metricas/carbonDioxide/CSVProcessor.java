package modelo.metricas.carbonDioxide;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import modelo.makeUsesfullCSV;

public class CSVProcessor {
	String fileName;
	private BufferedReader fileBuffer;
	private makeUsesfullCSV healthMetric;
	
	public CSVProcessor(String fileName, makeUsesfullCSV mUfCSV) throws FileNotFoundException {
		this.fileName = fileName;
		this.healthMetric = mUfCSV;
		
		this.openFile();
	}
	
	private void openFile() throws FileNotFoundException {
		this.fileBuffer = new BufferedReader(new FileReader(this.fileName));
	}
	
	private void readCSV() {
		String line;
		
		try {
			while ((line = fileBuffer.readLine()) != null) {
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (this.fileBuffer != null) {
				try {
					this.fileBuffer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
