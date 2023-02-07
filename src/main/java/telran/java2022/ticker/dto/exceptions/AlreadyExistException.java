package telran.java2022.ticker.dto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class AlreadyExistException extends RuntimeException {

	private static final long serialVersionUID = -2580458888036090873L;

	public AlreadyExistException() {
		super("Ticker already exist");
	}
}
