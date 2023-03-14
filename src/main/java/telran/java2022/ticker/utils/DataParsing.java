package telran.java2022.ticker.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import telran.java2022.ticker.model.Ticker;
import telran.java2022.ticker.model.TickerId;

public class DataParsing {
	
	/**
	 * Universal method of parsing  
	 * @return List of tickers
	 */
	public static List<Ticker> parsingWithoutApache(String fileName, String name, String pattern, int numberOfClose) {
		List<Ticker> res = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(fileName));){
			String line = br.readLine();
			line = br.readLine();
			while(line != null) {
				String[] arr = line.split(",");
				res.add(fillTicker(arr, name, pattern,numberOfClose));
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	private static Ticker fillTicker(String[] arr, String name, String pattern, int numberOfClose) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		LocalDate date = LocalDate.parse(arr[0], formatter);
	    double close = Double.parseDouble(arr[numberOfClose]);
	    Ticker res = new Ticker(new TickerId(name, date), close);
		return res;
	}
	
//	/**
//	 * Метод парсинга 1 файла без Apache
//	 * @return лист необходимых моделей
//	 */
//	public static List<Ticker> parsingWithoutApache() {
//		List<Ticker> res = new ArrayList<>();
//		try (BufferedReader br = new BufferedReader(new FileReader("Sap5years.csv"));){
//			String line = br.readLine();
//			line = br.readLine();
//			while(line != null) {
//				String[] arr = line.split(",");
//				res.add(fillTicker(arr));
//				line = br.readLine();
//			}
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return res;
//	}
//	
//	private static Ticker fillTicker(String[] arr) {
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
//		LocalDate date = LocalDate.parse(arr[0], formatter);
//	    double close = Double.parseDouble(arr[1]);
//	    Ticker res = new Ticker(new TickerId("S&P", date), close);
//		return res;
//	}
	
//	/**
//	 * Метод парсинга файла с помощью Apache
//	 * @return лист моделей
//	 */
//	@SuppressWarnings("deprecation")
//	public static List<Ticker> parsingWithApache() {
//		List<Ticker> res = new ArrayList<>();	
//		try (BufferedReader br = new BufferedReader(new FileReader("Sap5years.csv"));
//				CSVParser csvParser = new CSVParser(br,
//						CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase())) {
//			List<CSVRecord> csvRecords = csvParser.getRecords();
//			res = csvRecords.stream()
//					.map(r -> fillTicker(r))
//					.collect(Collectors.toList());
//		}  catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return res;
//	}
//	
//	private static Ticker fillTicker(CSVRecord csvRecord) {
//	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
//		LocalDate date = LocalDate.parse(csvRecord.get(0), formatter);
//	    double close = Double.parseDouble(csvRecord.get(1));
//	    Ticker res = new Ticker(new TickerId("S&P", date), close);
//		return res;
//	}
}
