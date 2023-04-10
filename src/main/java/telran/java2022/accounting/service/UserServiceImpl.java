package telran.java2022.accounting.service;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java2022.accounting.dao.UserRepository;
import telran.java2022.accounting.dto.RolesChangeDto;
import telran.java2022.accounting.dto.UserDto;
import telran.java2022.accounting.dto.UserRegisterDto;
import telran.java2022.accounting.dto.UserUpdateDto;
import telran.java2022.accounting.dto.exceptions.UserExistException;
import telran.java2022.accounting.dto.exceptions.UserNotFoundException;
import telran.java2022.accounting.model.User;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
	
	final UserRepository repository;
	final ModelMapper mapper;
	final PasswordEncoder encoder;

	@Override
	public UserDto register(UserRegisterDto userRegisterDto) {
		if(repository.existsById(userRegisterDto.getLogin())) {
			throw new UserExistException(userRegisterDto.getLogin());
		}
		String password = encoder.encode(userRegisterDto.getPassword());
		User user = new User(userRegisterDto.getLogin(), password,
				userRegisterDto.getFirstName(), userRegisterDto.getLastName());
		user = repository.save(user);
		return mapper.map(user, UserDto.class);
	}

	@Override
	public UserDto login(String login) {
		User user = repository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
		return mapper.map(user, UserDto.class);
	}

	@Override
	public UserDto deleteUser(String login) {
		User user = repository.findById(login)
				.orElseThrow(() -> new UserNotFoundException(login));
		repository.deleteById(login);
		return mapper.map(user, UserDto.class);
	}

	@Override
	public UserDto updateUser(String login, UserUpdateDto userUpdateDto) {
		User user = repository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
		if (userUpdateDto.getFirstName() != null) {
			user.setFirstName(userUpdateDto.getFirstName());
		}
		if (userUpdateDto.getLastName() != null) {
			user.setLastName(userUpdateDto.getLastName());
		}
		user = repository.save(user);
		return mapper.map(user, UserDto.class);
	}

	@Override
	public RolesChangeDto changeRoles(String login, String role, boolean isAddRole) {
		User user = repository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
		if(isAddRole) {
			user.addRole(role);
		} else {
			user.deleteRole(role);
		}
		user = repository.save(user);
		return mapper.map(user, RolesChangeDto.class);
	}

	@Override
	public void changePassword(String login, String newPassword) {
		User user = repository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
		String password = encoder.encode(newPassword);
		user.setPassword(password);
		repository.save(user);		
	}

}
