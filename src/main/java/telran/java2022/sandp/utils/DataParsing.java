package telran.java2022.sandp.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import telran.java2022.sandp.model.Sandp;

public class DataParsing {

	public static List<Sandp> parsing() {
		List<Sandp> res = new ArrayList<>();
//		try (Reader in = new FileReader("HistoricalPrices.csv");){
//			Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
//			for (CSVRecord record : records) {
//				String[] dateString = record.get("Date").split("/");
//			    LocalDate date = LocalDate.of(Integer.parseInt("20" + dateString[2]), 
//			    		Integer.parseInt(dateString[0]), Integer.parseInt(dateString[1]));
//			    double open = Double.parseDouble(record.get("Open"));
//			    double high = Double.parseDouble(record.get("High"));
//			    double low = Double.parseDouble(record.get("Low"));
//			    double close = Double.parseDouble(record.get("Close"));
//			    Sandp entity = new Sandp(date, open, high, low, close);
//			    res.add(entity);
//			}
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		try (BufferedReader br = new BufferedReader(new FileReader("HistoricalPrices.csv"));){
			String line = br.readLine();
			line = br.readLine();
			while(line != null) {
				String[] arr = line.split(",");
				res.add(fillSandp(arr));
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return res;
	}

	private static Sandp fillSandp(String[] arr) {
		String[] dateString = arr[0].split("/");
	    LocalDateTime date = LocalDateTime.of(Integer.parseInt("20" + dateString[2]), 
	    		Integer.parseInt(dateString[0]), Integer.parseInt(dateString[1]), 18, 10);
	    double open = Double.parseDouble(arr[1]);
	    double high = Double.parseDouble(arr[2]);
	    double low = Double.parseDouble(arr[3]);
	    double close = Double.parseDouble(arr[4]);
	    Sandp res = new Sandp(date, open, high, low, close);
		return res;
	}
}
