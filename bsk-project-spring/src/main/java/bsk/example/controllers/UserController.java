package bsk.example.controllers;

import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
import bsk.example.domain.validators.UserValidator;
import bsk.example.repository.AuthorityRepository;
import bsk.example.repository.UserRepository;
import bsk.example.security.components.AuthenticationRequest;
import bsk.example.security.components.AuthenticationResponse;
import bsk.example.security.components.JWTUtil;

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
	
	@PostMapping("/register")
	public boolean registerUser(@RequestBody User user, BindingResult errors) {
		user.setUsername(user.getUsername().trim());
		user.setEmail(user.getEmail().trim());
		userValidate.validate(user, errors);
		
		if (errors.hasErrors()) {
			for (ObjectError e : errors.getAllErrors()) {
				log.error(e.getCode() + " - " + e.getDefaultMessage());
			}
			return false;
		}
		
		if (userRepo.existsByUsername(user.getUsername())) {
			log.info("User: " + user.getUsername() + " exists.");
			return false;
		} else {
			Authority userRole = authRepo.findByAuthority("ROLE_USER");
			user.getAuthorities().add(userRole);
			user.setPassword(bCryptPassEncoder.encode(user.getPassword()));
			user.setEnabled(false);
			userRepo.save(user);
			log.info("User registered.");
			return true;
		}

	}

	@PostMapping("/auth")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest request) {
		SecurityContextHolder.getContext().setAuthentication(authManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())));

		UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
		return ResponseEntity.ok(new AuthenticationResponse(JWTUtil.generateToken(userDetails)));
	}

	@GetMapping("/refresh")
	public ResponseEntity<?> refreshToken(HttpServletRequest request) {
		String token = JWTUtil.trimToken(request.getHeader(JWTUtil.HEADER));
		
		String newToken = null;
		if (JWTUtil.canTokenBeRefreshed(token)) {
			newToken = JWTUtil.refrehToken(token);
			log.info("TOKEN ODSWIEZONY " + newToken);
		} else {
			log.error("CHUJA WYSZLO Z ODSWIEZANIA TOKENU");
			return ResponseEntity.badRequest().body(null);
		}
		
		return ResponseEntity.ok(new AuthenticationResponse(newToken));
	}

}
