package modelo.metricas.tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.function.Consumer;

public class CSVFileProcessor {
	String fileName;
	private BufferedReader fileBuffer;

	public CSVFileProcessor(String fileName, Consumer<LinkedHashSet<String>> fProcessLine) throws IOException {
		this.fileName = fileName;

		this.openFile();
		System.out.println("CSV Abierto: '" + fileName + "'");

		LinkedHashSet<LinkedHashSet<String>> lines = new LinkedHashSet<>();
		this.readCSVLines(lines);

		System.out.println("Lineas leidas, procesando cada linea...");

		lines.forEach(fProcessLine);

		this.closeFileBuffer(); // No es necesario
	}

	private void openFile() throws FileNotFoundException {
		this.fileBuffer = new BufferedReader(new FileReader(this.fileName));
	}

	private void readCSVLines(LinkedHashSet<LinkedHashSet<String>> lines) throws IOException {
		String line;

		try {
			while ((line = fileBuffer.readLine()) != null) {
				lines.add(new LinkedHashSet<String>(splitLine(line)));
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
		LinkedHashSet<String> lnFParts = new LinkedHashSet<>();

		for (int i = 0; i < lineParts.length; i++) {
			if (lineParts[i].startsWith("\"")) {
				int j; // Busco la comilla doble final
				for (j = i + 1; j < lineParts.length && !lineParts[j].contains("\""); j++) {
				}
				lnFParts.add(String.join(",", Arrays.copyOfRange(lineParts, i, j + 1)));
				i = j; // Adelando el bucle
			} else {
				lnFParts.add(lineParts[i]);
			}
		}

		return lnFParts;
	}
}
