package telran.java2022.accounting.dto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class UserExistException extends RuntimeException{

	private static final long serialVersionUID = 7103886033816937647L;

	public UserExistException(String login) {
		super("User with login " + login + " exist");
	}
}
