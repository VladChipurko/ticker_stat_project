package telran.java2022.sandp.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import telran.java2022.sandp.model.SandPDate;
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
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
		LocalDate date = LocalDate.parse(csvRecord.get(0), formatter);
	    double close = Double.parseDouble(csvRecord.get(4));
	    Sandp res = new Sandp(new SandPDate("S&P", date), close);
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
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
		LocalDate date = LocalDate.parse(arr[0], formatter);
	    double close = Double.parseDouble(arr[4]);
	    Sandp res = new Sandp(new SandPDate("S&P", date), close);
		return res;
	}
}
