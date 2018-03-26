package bsk.example.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/content")
public class AuthorizedController {

	@GetMapping("/1")
	public String secured1() {
		return "Secured content";
	}
	
	@GetMapping("/2")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String secured2() {
		return "Secured ADMIN content";
	}
}
