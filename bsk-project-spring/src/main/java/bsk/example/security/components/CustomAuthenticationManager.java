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

@Component
public class CustomAuthenticationManager implements AuthenticationManager {

	private Logger log = Logger.getLogger(CustomAuthenticationManager.class);
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPassEncoder;
	
	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		String username = auth.getPrincipal().toString();
		String password = auth.getCredentials().toString();
		
		log.info("Authentication user: " + username + " ...");
		
		User user = userRepo.findByUsername(username);
		
		if (user == null) throw new BadCredentialsException("1000");
		if (!bCryptPassEncoder.matches(password, user.getPassword())) throw new BadCredentialsException("1000");
		
		return new UsernamePasswordAuthenticationToken(username, password, user.getAuthorities());
	}

}
