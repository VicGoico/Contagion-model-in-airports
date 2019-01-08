package modelo.CSVReaders;

import java.util.ArrayList;
import java.util.function.Consumer;

public interface ReaderConsumer extends Consumer<ArrayList<String>>{
	default public void atEndProcessing() {
		
	};
	
	public boolean processing();
}
