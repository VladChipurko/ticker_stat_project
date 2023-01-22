package telran.java2022.sandp.dto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundExeption extends RuntimeException {

	private static final long serialVersionUID = 76602304155960045L;
	
	public NotFoundExeption() {
		super("S&P not found");
	}

}
