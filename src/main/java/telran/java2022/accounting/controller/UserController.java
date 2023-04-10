package telran.java2022.accounting.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import telran.java2022.accounting.dto.RolesChangeDto;
import telran.java2022.accounting.dto.UserDto;
import telran.java2022.accounting.dto.UserRegisterDto;
import telran.java2022.accounting.dto.UserUpdateDto;
import telran.java2022.accounting.service.UserService;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class UserController {

	final UserService service;
	
	@PostMapping("/register")
	public UserDto register(@RequestBody UserRegisterDto userRegisterDto) {
		return service.register(userRegisterDto);
	}
	
	@PostMapping("/login")
	public UserDto login(Principal principal) {
		return service.login(principal.getName());
	}
	
	@DeleteMapping("/user/{login}")
	public UserDto deleteUser(@PathVariable String login) {
		return service.deleteUser(login);
	}
	
	@PutMapping("/user/{login}")
	public UserDto updateUser(@PathVariable String login,@RequestBody UserUpdateDto userUpdateDto) {
		return service.updateUser(login, userUpdateDto);
	}
	
	@PutMapping("/user/{login}/role/{role}")
	public RolesChangeDto addRole(@PathVariable String login,@PathVariable String role) {
		return service.changeRoles(login, role, true);
	}
	
	@DeleteMapping("/user/{login}/role/{role}")
	public RolesChangeDto deleteRole(@PathVariable String login,@PathVariable String role) {
		return service.changeRoles(login, role, false);
	}
	
	@PutMapping("/changePassword/user/{login}")
	public void changePassword(@PathVariable String login, @RequestHeader("X-Password") String newPassword) {
		service.changePassword(login, newPassword);
	}
}
