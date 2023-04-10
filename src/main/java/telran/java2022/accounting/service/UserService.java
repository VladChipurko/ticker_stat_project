package telran.java2022.accounting.service;

import telran.java2022.accounting.dto.RolesChangeDto;
import telran.java2022.accounting.dto.UserDto;
import telran.java2022.accounting.dto.UserRegisterDto;
import telran.java2022.accounting.dto.UserUpdateDto;

public interface UserService {

	UserDto register(UserRegisterDto userRegisterDto);
	
	UserDto login(String login);
	
	UserDto deleteUser(String login);
	
	UserDto updateUser(String login, UserUpdateDto userUpdateDto);
	
	RolesChangeDto changeRoles(String login, String role, boolean isAddRole);
	
	void changePassword(String login, String newPassword);
}
