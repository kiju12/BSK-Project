package bsk.example.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import bsk.example.domain.Authority;
import bsk.example.domain.User;
import bsk.example.domain.validators.UserValidationErrors;
import bsk.example.domain.validators.UserValidator;
import bsk.example.repository.AuthorityRepository;
import bsk.example.repository.UserRepository;
import bsk.example.security.components.AuthenticationRequest;
import bsk.example.security.components.AuthenticationResponse;
import bsk.example.security.components.JWTUtil;
import bsk.example.services.EmailService;

/*
 * Kontroler przyjmujący zapytania dotyczące odświeżenia tokenu oraz rejestracji/logowania użytkowników.
 */
@RestController
public class UserController {

	private Logger log = Logger.getLogger(UserValidator.class);

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private AuthorityRepository authRepo;

	@Autowired
	private AuthenticationManager authManager;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private BCryptPasswordEncoder bCryptPassEncoder;

	@Autowired
	private UserValidator userValidate;
	
	@Autowired
	private EmailService emailService;
	
	/*
	 * Metoda do rejestracji użytkownika. Waliduje jego pola (klasa UserValidator), dodaje role "ROLE_USER", koduje hasło i domyślnie
	 * ustawia stan konta jako "nieaktywne".
	 * @param user Obiekt użytkownika przechwycony jako JSON i przekonwertowany ja obiekt
	 * @param errors Rejestr błędów automatycznie inicjalizowany przez Spring'a
	 * @return UserValidationErrors Zwraca obiekt zawierający Mapę -> (Kod błędu, Wiadomość błędu). Gdy błędy walidacji nie występują zwraca
	 * pustą mapę
	 */
	@PostMapping("/register")
	public UserValidationErrors registerUser(@RequestBody User user, BindingResult errors) {
		userValidate.validate(user, errors);
		
		if (errors.hasErrors()) {
			Map<String, String> validErrors = new HashMap<>(errors.getAllErrors().size());
			for (ObjectError error : errors.getAllErrors()) {
				 validErrors.put(error.getCode(), error.getDefaultMessage());
			}
			return new UserValidationErrors(validErrors);
		}
		
			Authority userRole = authRepo.findByAuthority("ROLE_USER");
			user.getAuthorities().add(userRole);
			user.setPassword(bCryptPassEncoder.encode(user.getPassword()));
			user.setEnabled(false);
			User savedUser = userRepo.save(user);
			log.info("User registered.");
			
			emailService.sendActivationEmailToUser(savedUser);
			
			return new UserValidationErrors();
	}

	
	/*
	 * Metoda służąca do logowania. Sprawdza login i hasło za pomocą customowego Authentication Managera,
	 * nastepnie dodaje je do kontextu security Spring'a i generuje token dla użytkownika.
	 * @param request Opakowane dane do logowania (username/password)
	 * @return ResponseEntity<?> Zwraca token opakowany w AuthenticationResponse i dołącza do niego status Http
	 */
	@PostMapping("/auth")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest request) {
		Authentication auth = null;
		
		try {
			auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		} catch (AuthenticationException except) {
			log.error("Error during user authentication");
			return ResponseEntity.badRequest().body(except.getMessage());
		}
		
		SecurityContextHolder.getContext().setAuthentication(auth);

		UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
		return ResponseEntity.ok(new AuthenticationResponse(JWTUtil.generateToken(userDetails)));
	}

	/*
	 * Metoda odświeżająca token (przedłużenie czasu wygaśnięcia tokenu)
	 * @param request Zapytanie z tokenem w headerze
	 * @return Zwraca token opakowany w AuthenticationResponse i dołącza do niego status Http
	 */
	@GetMapping("/refresh")
	public ResponseEntity<?> refreshToken(HttpServletRequest request) {
		String token = JWTUtil.trimToken(request.getHeader(JWTUtil.HEADER));
		
		String newToken = null;
		if (JWTUtil.canTokenBeRefreshed(token)) {
			newToken = JWTUtil.refrehToken(token);
			log.info("Token refreshed.");
		} else {
			log.error("Token cannot be refreshed.");
			return ResponseEntity.badRequest().body(null);
		}
		
		return ResponseEntity.ok(new AuthenticationResponse(newToken));
	}

}
