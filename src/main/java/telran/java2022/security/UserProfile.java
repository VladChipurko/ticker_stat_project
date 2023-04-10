package telran.java2022.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserProfile extends User{

	private static final long serialVersionUID = -5339509846964186027L;

	public UserProfile(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}

}
