package modelo.metricas.tools;

import java.util.ArrayList;
import java.util.HashMap;

public class Performance {
	private static HashMap<String, ArrayList<Performance>> history = new HashMap<>();
	private static final long MEGABYTE = 1024L * 1024L;
	protected long currentTimeMillis, maxMemory, freeMemory;
	private String desc;
	
	public static void Begin(String id) {
		Performance.history.put(id, new ArrayList<>());
	}

	public static void Register(String id, String desc) {
		Performance.history.get(id).add(new Performance(desc));
	}
	
	public static void Register(String id) {
		Performance.history.get(id).add(new Performance());
	}

	public static void getSummary(String id) {
		ArrayList<Performance> prm = Performance.history.get(id);
		if (prm == null || prm.get(0) == null)
			return;

		int i = 0;

		System.out.println("############# RESUMEN DE USO DE RECURSOS #############");
		for (i = 0; i < prm.size(); i++) {
			System.out.println("Registro: " + id + "-" + i);
			if(prm.get(i).desc != null) System.out.println(prm.get(i).desc);
			System.out.println(
					"	Tiempo de inicio: " + prm.get(i).timeMillis() + " ms (" + prm.get(i).timeSeconds() + "s)");
			System.out.println("	Memoria usada: " + prm.get(i).usedMemory() + " MB");
			if (i > 0) {
				System.out.println("Respecto al estado anterior:");
				System.out.println("	Duracion: " + (prm.get(i).timeMillis() - prm.get(i - 1).timeMillis()) + " ms ("
						+ (prm.get(i).timeSeconds() - prm.get(i - 1).timeSeconds()) + "s)");
				System.out
						.println("	Memoria usada: " + (prm.get(i).usedMemory() - prm.get(i - 1).usedMemory()) + " MB");
			}
		}

		if (i > 1) {
			System.out.println("TOTAL:");
			System.out.println("	Duracion: " + (prm.get(i - 1).timeMillis() - prm.get(0).timeMillis()) + " ms ("
					+ (prm.get(i - 1).timeSeconds() - prm.get(0).timeSeconds()) + "s)");
			System.out.println("	Memoria usada: " + (prm.get(i - 1).usedMemory() - prm.get(0).usedMemory()) + " MB");
		}

		Performance.history.remove(id);
	}

	public Performance() {
		this.currentTimeMillis = System.currentTimeMillis();
		this.maxMemory = Runtime.getRuntime().maxMemory();
		this.freeMemory = Runtime.getRuntime().freeMemory();
	}
	
	public Performance(String desc) {
		this.currentTimeMillis = System.currentTimeMillis();
		this.maxMemory = Runtime.getRuntime().maxMemory();
		this.freeMemory = Runtime.getRuntime().freeMemory();
		this.desc = desc;
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
	
	public String getDesc() {
		return this.desc;
	}
}
