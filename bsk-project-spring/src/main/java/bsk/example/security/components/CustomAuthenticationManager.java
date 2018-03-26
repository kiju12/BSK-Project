package bsk.example.security.components;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import bsk.example.domain.User;
import bsk.example.repository.UserRepository;

/*
 * Implementacja AuthenticationManager'a - służy on do sprawdzenia czy użytkownik do którego się logujemy istnieje w bazie danych
 * i jezeli istnieje, sprawdza poprawność wprowadzonego hasła
 */
@Component
public class CustomAuthenticationManager implements AuthenticationManager {

	private Logger log = Logger.getLogger(CustomAuthenticationManager.class);
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPassEncoder;
	
	/*
	 * Metoda do uwierzytelniania użytkownika
	 * @param auth Obiekt typu Authentication (w naszym przypadku jest to UsernamePasswordAuthenticationToken) zawierający dane logowania
	 * @return Authentication Gdy uwierzytelnianie użytkownika zakończy się pomyślnie, zwraca obiekt Authentication wzbogacony o uprawnienia
	 * @exception AuthenticationException Wyrzuca wyjątek w przypadku, gdy nie istnieje użytkownik o podanej nazwie lub hasło 
	 * użytkownika nie jest prawidłowe.
	 */
	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		String username = auth.getPrincipal().toString();
		String password = auth.getCredentials().toString();
		
		log.info("Authentication user: " + username + " ...");
		
		User user = userRepo.findByUsername(username);
		
		if (user == null) throw new BadCredentialsException("User " + username + " does not exist.");
		if (!bCryptPassEncoder.matches(password, user.getPassword())) throw new BadCredentialsException("Wrong password for user: " + username + ".");
		
		return new UsernamePasswordAuthenticationToken(username, password, user.getAuthorities());
	}

}
