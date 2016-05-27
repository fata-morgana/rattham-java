package kopkaj.rattham.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import kopkaj.rattham.exception.RatthamDataException;
import kopkaj.rattham.exception.RatthamException;

@ControllerAdvice
public class ErrorController {
	@ExceptionHandler(RatthamDataException.class)
	public ResponseEntity<String> handleDataException(RatthamDataException e) {
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(RatthamException.class)
	public ResponseEntity<String> handleException(RatthamException e) {
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
