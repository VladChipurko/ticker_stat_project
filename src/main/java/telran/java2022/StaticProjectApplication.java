package telran.java2022;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import telran.java2022.sandp.dao.SandpRepository;
import telran.java2022.sandp.utils.DataParsing;

@SpringBootApplication
public class StaticProjectApplication implements CommandLineRunner{
	@Autowired
	SandpRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(StaticProjectApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		if(repository.count() != 0) {
//			repository.deleteAll();
//		}
		if(repository.count() == 0) {
//			repository.saveAll(DataParsing.parsingWithApache());
			repository.saveAll(DataParsing.parsingWithoutApache());
		}
	}

}
