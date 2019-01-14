package modelo.metricas.tools;

import java.util.ArrayList;
import java.util.HashMap;

public class Performance {
	private static HashMap<String, ArrayList<Performance>> history = new HashMap<>();
	private static final long MEGABYTE = 1024L * 1024L;
	protected long currentTimeMillis, maxMemory, freeMemory;

	public static void Begin(String id) {
		Performance.history.put(id, new ArrayList<>());
	}

	public static void Register(String id) {
		Performance.history.get(id).add(new Performance());
	}

	public static void getSummary(String id) {
		ArrayList<Performance> prm = Performance.history.get(id);

		System.out.println("############# RESUMEN DE USO DE RECURSOS #############");
		for (int i = 0; i < prm.size(); i++) {
			System.out.println("Registro: " + id + "-" + i);
			System.out
					.println("	Tiempo de inicio: " + prm.get(i).timeMillis() + " ms (" + prm.get(i).timeSeconds() + "s)");
			System.out.println("	Memoria usada: " + prm.get(i).usedMemory() + " MB");
			if (i > 0) {
				System.out.println("Respecto al estado anterior:");
				System.out.println("	Duracion: " + (prm.get(i).timeMillis() - prm.get(i - 1).timeMillis()) + " ms ("
						+ (prm.get(i).timeSeconds() - prm.get(i - 1).timeSeconds()) + "s)");
				System.out.println("	Memoria usada: " + (prm.get(i).usedMemory() - prm.get(i -  1).usedMemory()) + " MB");
			}
		}
		
		Performance.history.remove(id);
	}

	public Performance() {
		this.currentTimeMillis = System.currentTimeMillis();
		this.maxMemory = Runtime.getRuntime().maxMemory();
		this.freeMemory = Runtime.getRuntime().freeMemory();
	}

	public long usedMemory() {
		return (this.maxMemory - this.freeMemory) / MEGABYTE;
	}

	public long timeMillis() {
		return this.currentTimeMillis;
	}

	public long timeSeconds() {
		return this.currentTimeMillis / 1000;
	}
}
