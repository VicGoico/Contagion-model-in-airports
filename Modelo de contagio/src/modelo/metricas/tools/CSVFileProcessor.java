package modelo.metricas.tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import modelo.CSVReaders.ReaderConsumer;

public class CSVFileProcessor {
	String fileName;
	private BufferedReader fileBuffer;
	private ReaderConsumer fProcessLine;

	public CSVFileProcessor(String fileName, ReaderConsumer fProcessLine) throws IOException {
		this.fileName = fileName;
		this.fProcessLine = fProcessLine;

		this.openFile();
		System.out.println("CSV Abierto: '" + fileName + "'");

		this.readCSVLines();

		fProcessLine.atEndProcessing();

		this.closeFileBuffer(); // No es necesario
	}

	private void openFile() throws FileNotFoundException {
		this.fileBuffer = new BufferedReader(new FileReader(this.fileName));
	}

	private void readCSVLines() throws IOException {
		String line;

		try {
			while ((line = fileBuffer.readLine()) != null && this.fProcessLine.processing()) {
				this.fProcessLine.accept(new ArrayList<String>(splitLine(line)));
			}
		} catch (IOException e) {
			throw e;
		} finally {
			this.closeFileBuffer();
		}
	}

	private void closeFileBuffer() throws IOException {
		if (this.fileBuffer != null) {
			try {
				this.fileBuffer.close();
			} catch (IOException e) {
				throw e;
			}
		}
	}

	public static Collection<String> splitLine(String line) {
		String[] lineParts = line.split(","); // Separo todas las lineas con ,
		ArrayList<String> lnFParts = new ArrayList<>();

		for (int i = 0; i < lineParts.length; i++) {
			if (lineParts[i].trim().startsWith("\"")) {
				if(!lineParts[i].trim().substring(1).contains("\"")) {
					int j; // Busco la comilla doble final
					for (j = i + 1; j < lineParts.length && !lineParts[j].contains("\""); j++) {
					}
					lnFParts.add(String.join(",", Arrays.copyOfRange(lineParts, i, j + 1)).trim());
					i = j; // Adelando el bucle
				} else {
					lnFParts.add(lineParts[i].trim());
				}
			} else {
				lnFParts.add(lineParts[i].trim());
			}
		}

		return lnFParts;
	}
}
