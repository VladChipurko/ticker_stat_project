package telran.java2022;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import telran.java2022.ticker.dao.TickerRepository;
import telran.java2022.ticker.utils.DataParsing;

@SpringBootApplication
public class StaticProjectApplication implements CommandLineRunner{
	@Autowired
	TickerRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(StaticProjectApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		if(repository.count() != 0) {
//			repository.deleteAll();
//		}
		if(repository.count() == 0) {
//			repository.saveAll(DataParsing.parsingWithoutApache("Sap5years.csv", "sap", "MM/dd/yyyy", 1));
//			repository.saveAll(DataParsing.parsingWithoutApache("Gold5years.csv", "gold", "MM/dd/yyyy", 1));
//			repository.saveAll(DataParsing.parsingWithoutApache("Tesla5years.csv", "tesla", "yyyy-MM-dd", 4));
//			repository.saveAll(DataParsing.parsingWithoutApache("Microsoft5years.csv", "microsoft", "yyyy-MM-dd", 4));
			repository.saveAll(DataParsing.parsingWithoutApache("sap500.csv", "sap", "yyyy-MM-dd", 4));
			repository.saveAll(DataParsing.parsingWithoutApache("gold.csv", "gold", "yyyy-MM-dd", 4));
			repository.saveAll(DataParsing.parsingWithoutApache("tesla.csv", "tesla", "yyyy-MM-dd", 4));
			repository.saveAll(DataParsing.parsingWithoutApache("microsoft.csv", "microsoft", "yyyy-MM-dd", 4));
		}
	}

}
