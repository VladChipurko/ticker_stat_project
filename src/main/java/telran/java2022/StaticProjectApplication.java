package telran.java2022;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import telran.java2022.accounting.dao.UserRepository;
import telran.java2022.accounting.model.User;

@SpringBootApplication
public class StaticProjectApplication implements CommandLineRunner{
	@Autowired
	UserRepository userRepository;
	@Autowired
	PasswordEncoder encoder;

	public static void main(String[] args) {
		SpringApplication.run(StaticProjectApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if(!userRepository.existsById("admin")) {
			String password = encoder.encode("admin");
			User user = new User("admin", password, "", "");
			user.addRole("ADMIN");
			user.addRole("MODERATOR");
			userRepository.save(user);
		}
	}
}
