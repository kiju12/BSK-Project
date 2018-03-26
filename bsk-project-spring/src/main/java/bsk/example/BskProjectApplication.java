package bsk.example;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.sun.istack.logging.Logger;

import bsk.example.domain.Authority;
import bsk.example.domain.User;
import bsk.example.repository.AuthorityRepository;
import bsk.example.repository.UserRepository;

@SpringBootApplication
public class BskProjectApplication {
	
	private Logger log = Logger.getLogger(BskProjectApplication.class);
	
	@Autowired
	private AuthorityRepository authRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPassEncoder;
	
	public static void main(String[] args) {
		SpringApplication.run(BskProjectApplication.class, args);
	}
	
	@PostConstruct
	public void initializeDb() {
		Authority adminAuth = new Authority("ROLE_ADMIN");
		Authority userAuth = new Authority ("ROLE_USER");
		
		log.info("Authority " + authRepo.save(adminAuth).getAuthority() + " added.");
		log.info("Authority " + authRepo.save(userAuth).getAuthority() + " added.");
		
		List<Authority> authorities = new ArrayList<>();
		authorities.add(authRepo.findByAuthority("ROLE_USER"));
		authorities.add(authRepo.findByAuthority("ROLE_ADMIN"));
		User user = new User("admin", bCryptPassEncoder.encode("admin123"), authorities);
		
		log.info("User " + userRepo.save(user).getUsername() + " added.");
	}
}
