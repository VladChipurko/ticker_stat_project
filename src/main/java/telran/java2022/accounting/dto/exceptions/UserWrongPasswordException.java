package telran.java2022.accounting.dto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class UserWrongPasswordException extends RuntimeException{

	private static final long serialVersionUID = -8516031580425019173L;

	public UserWrongPasswordException() {
		super("Wrong password");
	}
}
