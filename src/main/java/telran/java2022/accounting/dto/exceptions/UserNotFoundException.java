package telran.java2022.accounting.dto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -7266666800025193952L;

	public UserNotFoundException(String id) {
		super("User with login " + id + " not found");
	}
}
