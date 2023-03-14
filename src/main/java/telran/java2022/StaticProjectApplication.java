package telran.java2022;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import telran.java2022.ticker.dao.TickerRepository;

@SpringBootApplication
public class StaticProjectApplication implements CommandLineRunner{
	@Autowired
	TickerRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(StaticProjectApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//======RUN ONLY FOR DOWNLOAD NEW DATA=============================================
//		if(repository.count() == 0) {
//			repository.saveAll(DataParsing.parsingWithoutApache("sap500.csv", "sap", "yyyy-MM-dd", 4));
//			repository.saveAll(DataParsing.parsingWithoutApache("gold.csv", "gold", "yyyy-MM-dd", 4));
//			repository.saveAll(DataParsing.parsingWithoutApache("tesla.csv", "tesla", "yyyy-MM-dd", 4));
//			repository.saveAll(DataParsing.parsingWithoutApache("microsoft.csv", "microsoft", "yyyy-MM-dd", 4));
//			repository.saveAll(DataParsing.parsingWithoutApache("apple.csv", "apple", "yyyy-MM-dd", 4));
//		} else {
//			List<Ticker> allData = new ArrayList<>();
//			allData.addAll(DataParsing.parsingWithoutApache("sap500.csv", "sap", "yyyy-MM-dd", 4));
//			allData.addAll(DataParsing.parsingWithoutApache("gold.csv", "gold", "yyyy-MM-dd", 4));
//			allData.addAll(DataParsing.parsingWithoutApache("tesla.csv", "tesla", "yyyy-MM-dd", 4));
//			allData.addAll(DataParsing.parsingWithoutApache("microsoft.csv", "microsoft", "yyyy-MM-dd", 4));
//			allData.addAll(DataParsing.parsingWithoutApache("apple.csv", "apple", "yyyy-MM-dd", 4));
//			List<Ticker> allBaseData = StreamSupport.stream(repository.findAll().spliterator(), false)
//					.collect(Collectors.toList());
//			List<Ticker> newData = allData.stream()
//					.filter(ticker -> !allBaseData.stream().anyMatch(ticker::equals))
//					.collect(Collectors.toList());
//			repository.saveAll(newData);
//		}
	}
}
