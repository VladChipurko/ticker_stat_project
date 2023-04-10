package telran.java2022.security;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java2022.accounting.dao.UserRepository;
import telran.java2022.accounting.model.User;
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	final UserRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = repository.findById(username).orElseThrow(() -> new UsernameNotFoundException(username));
		String[] roles = user.getRoles().stream()
				.map(r -> "ROLE_" + r.toUpperCase())
				.toArray(String[]::new);
		return new UserProfile(username, user.getPassword(), AuthorityUtils.createAuthorityList(roles));
	}

}
