package bsk.example.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * Kontrolery z danymi
 */
@RestController
@RequestMapping("/content")
public class AuthorizedController {

	/*
	 * Kontroler do którego dostęp ma zalogowany użytkownik (lub admin).
	 */
	@GetMapping("/user")
	public String securedUser() {
		return "Dane do których dostęp ma jedynie użytkownik (lub administrator).";
	}
	
	/*
	 * Kontroler do którego dostęp ma jedynie administrator.
	 */
	@GetMapping("/admin")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String securedAdmin() {
		return "Dane do których dostęp ma jedynie administrator.";
	}
	
	/*
	 * Kontroler do którego dostęp ma każdy.
	 */
	@GetMapping("/anyone")
	public String securedAnyone() {
		return "Dane do których dostęp ma każdy.";
	}
}
