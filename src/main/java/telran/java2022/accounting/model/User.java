package telran.java2022.accounting.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Document(collection = "Users")
@EqualsAndHashCode(of = "login")
public class User {

	@Id
	private String login;
	@Setter
	private String password;
	@Setter
    private String firstName;
	@Setter
    private String lastName;
	@Setter
    private Set<String> roles = new HashSet<>();
	
	public User() {
		roles.add("USER");
	}
	
	public User(String login, String password, String firstName, String lastName) {
		this();
		this.login = login;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public boolean addRole(String role) {
		return roles.add(role.toUpperCase());
	}
	
	public boolean deleteRole(String role) {
		return roles.remove(role.toUpperCase());
	}
}
