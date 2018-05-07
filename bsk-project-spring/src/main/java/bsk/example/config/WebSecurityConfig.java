package bsk.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import bsk.example.security.components.AuthenticationFilter;

/*
 * Klasa konfiguracyjna dla Spring Security.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	// Obiekt encodera haseł, służący do kodowania i dekodowania haseł użytkowników.
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// Obiekt filtra uwierzytelniania, zdefiniowanego przez nas w klasie
	// AuthenticationFilter
	@Bean
	public AuthenticationFilter authFilter() {
		return new AuthenticationFilter();
	}

	/*
	 * Główna metoda konfigurująca Spring Security. Wyłącza ochronę CSRF - Dzięki
	 * Tokenowi nie będzie nam potrzebna.
	 * 
	 * Wyłącza tworzenie sesji http przez Spring Security oraz nigdy nie użyje jej
	 * do stworzenia kontextu Security
	 * (sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	 * 
	 * Określa ścieżki URL (/auth, /register, /content/anyone i wszystkie zapytania OPTIONS) które
	 * nie będą filtrowane przez AuthenticationFilter
	 * 
	 * Dodaje nasz filtr do łańcucha filtrów FilterChain (addFilterBefore(...))
	 * 
	 * Wyłącza cache w całej aplikacji. Zapobiega to wchodzeniu na autoryzowane
	 * podstrony przez historię przeglądarki (po wylogowaniu)
	 * 
	 * @see org.springframework.security.config.annotation.web.configuration.
	 * WebSecurityConfigurerAdapter#configure(org.springframework.security.config.
	 * annotation.web.builders.HttpSecurity)
	 */
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
		.csrf().disable()
			.cors()
				.and()
		.			sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
						.and()
							.authorizeRequests()
								.antMatchers("/auth", "/content/anyone", "/register", "/activate").permitAll()
								.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
								.anyRequest().authenticated();

		httpSecurity.addFilterBefore(authFilter(), UsernamePasswordAuthenticationFilter.class);

		httpSecurity.headers().cacheControl();
	}

}
