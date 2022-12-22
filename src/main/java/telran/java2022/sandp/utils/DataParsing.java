package telran.java2022.sandp.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import telran.java2022.sandp.model.Sandp;

public class DataParsing {

	@SuppressWarnings("deprecation")
	public static List<Sandp> parsingWithApache() {
		List<Sandp> res = new ArrayList<>();	
		try (BufferedReader br = new BufferedReader(new FileReader("HistoricalPrices.csv"));
				CSVParser csvParser = new CSVParser(br,
						CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase())) {
			List<CSVRecord> csvRecords = csvParser.getRecords();
			res = csvRecords.stream()
					.map(r -> fillSandp(r))
					.collect(Collectors.toList());
		}  catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	private static Sandp fillSandp(CSVRecord csvRecord) {
		String[] dateString = csvRecord.get(0).split("/");
	    LocalDateTime date = LocalDateTime.of(Integer.parseInt("20" + dateString[2]), 
	    		Integer.parseInt(dateString[0]), Integer.parseInt(dateString[1]), 18, 10);
	    double open = Double.parseDouble(csvRecord.get(1));
	    double high = Double.parseDouble(csvRecord.get(2));
	    double low = Double.parseDouble(csvRecord.get(3));
	    double close = Double.parseDouble(csvRecord.get(4));
	    Sandp res = new Sandp(date, open, high, low, close);
		return res;
	}
	
	public static List<Sandp> parsingWithoutApache() {
		List<Sandp> res = new ArrayList<>();
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
