package bsk.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import bsk.example.domain.ActivationCode;
import bsk.example.domain.User;
import bsk.example.repository.ActivationCodeRepository;
import bsk.example.repository.UserRepository;

@RestController
public class ActivationController {

	@Autowired
	private ActivationCodeRepository codeRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@PostMapping("api/activate")
	public ResponseEntity<String> activateUser(@RequestBody ActivationCode activationCode) {
		String requestCode = activationCode.getCode();
		ActivationCode reloadedCodeFromDb = codeRepo.getOne(activationCode.getId());
		String reloadedCode = reloadedCodeFromDb.getCode();
		System.out.println(reloadedCode);
		
		if (requestCode.equals(reloadedCode)) {
			User userToActivate = reloadedCodeFromDb.getUser();
			userToActivate.setEnabled(true);
			userRepo.save(userToActivate);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return ResponseEntity.badRequest().body("BAD REQUEST!");
		}
	}
	
}
