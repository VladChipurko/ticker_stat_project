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
	
	/**
	 * Универсальный метод парсинга  
	 * @return лист необходимых моделей
	 */
	public static List<Sandp> parsingWithoutApache(String fileName, String name) {
		List<Sandp> res = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(fileName));){
			String line = br.readLine();
			line = br.readLine();
			while(line != null) {
				String[] arr = line.split(",");
				res.add(fillSandp(arr, name));
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	private static Sandp fillSandp(String[] arr, String name) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		LocalDate date = LocalDate.parse(arr[0], formatter);
	    double close = Double.parseDouble(arr[1]);
	    Sandp res = new Sandp(new SandPDate(name, date), close);
		return res;
	}
	
	/**
	 * Метод парсинга 1 файла без Apache
	 * @return лист необходимых моделей
	 */
	public static List<Sandp> parsingWithoutApache() {
		List<Sandp> res = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader("Sap5years.csv"));){
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
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		LocalDate date = LocalDate.parse(arr[0], formatter);
	    double close = Double.parseDouble(arr[1]);
	    Sandp res = new Sandp(new SandPDate("S&P", date), close);
		return res;
	}
	
	/**
	 * Метод парсинга файла с помощью Apache
	 * @return лист моделей
	 */
	@SuppressWarnings("deprecation")
	public static List<Sandp> parsingWithApache() {
		List<Sandp> res = new ArrayList<>();	
		try (BufferedReader br = new BufferedReader(new FileReader("Sap5years.csv"));
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
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		LocalDate date = LocalDate.parse(csvRecord.get(0), formatter);
	    double close = Double.parseDouble(csvRecord.get(1));
	    Sandp res = new Sandp(new SandPDate("S&P", date), close);
		return res;
	}
}
