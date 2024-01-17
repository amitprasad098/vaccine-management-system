package com.project.vaccinemanagement.exceptionhandler;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.project.vaccinemanagement.exceptions.BothShotsTakenException;
import com.project.vaccinemanagement.exceptions.FirstShotAlreadyTakenException;
import com.project.vaccinemanagement.exceptions.FirstShotNotTakenException;
import com.project.vaccinemanagement.exceptions.SecondShotNotEligibleException;
import com.project.vaccinemanagement.exceptions.VaccineUnavailableException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	private static final String VACCINE_UNAVAILABLE = "Vaccine is unavailable";
	private static final String SECOND_SHOT_NOT_ELIGIBLE = "Not eligible to take second shot, can take the shot on or after:";
	private static final String FIRST_SHOT_NOT_TAKEN = "Not eligible,first shot not taken";
	private static final String BOTH_SHOTS_TAKEN = "Not eligible, both the shots taken";
	private static final String FIRST_SHOT_TAKEN = "Not eligible, first shot already taken";
	private static final String ERROR_KEY = "error";
	private Map<String, String> error = new HashMap<>();

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public List<String> invalidMethodArgument(MethodArgumentNotValidException ex) {
		List<String> errors = new ArrayList<>();
		ex.getBindingResult().getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
		return errors;

	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public List<String> constraintViolation(ConstraintViolationException ex) {
		List<String> errors = new ArrayList<>();
		errors.add(ex.getMessage());
		return errors;

	}

	@ExceptionHandler(DateTimeParseException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public Map<String, String> invalidDate(DateTimeParseException e) {
		error.put(ERROR_KEY, "invalid date");
		return error;
	}

	@ExceptionHandler(FirstShotAlreadyTakenException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public Map<String, String> firstShotAlreadyTakenExceptionHandler(FirstShotAlreadyTakenException e) {
		error.put(ERROR_KEY, FIRST_SHOT_TAKEN);
		return error;
	}

	@ExceptionHandler(SecondShotNotEligibleException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public Map<String, String> secondShotNotEligibleExceptionHandler(SecondShotNotEligibleException e) {
		error.put(ERROR_KEY, SECOND_SHOT_NOT_ELIGIBLE + e.getMessage());
		return error;
	}

	@ExceptionHandler(BothShotsTakenException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public Map<String, String> bothShotsTakenExceptionHandler(BothShotsTakenException e) {
		error.put(ERROR_KEY, BOTH_SHOTS_TAKEN);
		return error;
	}

	@ExceptionHandler(FirstShotNotTakenException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public Map<String, String> firstShotNotTakenExceptionHandler(FirstShotNotTakenException e) {
		error.put(ERROR_KEY, FIRST_SHOT_NOT_TAKEN);
		return error;
	}

	@ExceptionHandler(VaccineUnavailableException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public Map<String, String> vaccineUnavailableExceptionHandler(VaccineUnavailableException e) {
		error.put(ERROR_KEY, VACCINE_UNAVAILABLE);
		return error;
	}
}
