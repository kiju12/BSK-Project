package bsk.example.domain.validators;

import java.util.HashMap;
import java.util.Map;

/*
 * Klasa zawierająca pary obiektów String. 
 * Kluczem jest nazwa pola użytkownika przy którym walidacja nie przeszła pomyślnie
 * Wartością jest domyślna wiadomość błędu
 */
public class UserValidationErrors {

	private Map<String, String> errors;

	public UserValidationErrors(Map<String, String> errors) {
		this.errors = errors;
	}
	
	public UserValidationErrors() {
		this.errors = new HashMap<>();
	}

	public void addError(String key, String value) {
		this.errors.put(key, value);
	}
	
	public Map<String, String> getErrors() {
		return errors;
	}

	public void setErrors(Map<String, String> errors) {
		this.errors = errors;
	}
	
}
