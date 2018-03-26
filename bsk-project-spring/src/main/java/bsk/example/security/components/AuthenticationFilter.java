package bsk.example.security.components;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;


/*
 * Filtr uwierzytelniający.
 * Przechwytuje wszystkie chronione zapytania (określone w klasie WebSecurityConfig)
 * i wywołuje dla nich doFilterInternal(..)
 */
public class AuthenticationFilter extends OncePerRequestFilter {

	
	@Autowired
	private UserDetailsService userDetailsService;
	
	/*
	 * Pobiera token z zapytania, z tokenu pobieramy nazwę użytkownika - na podstawie której pobieramy go z bazy danych
	 * i sprawdzamy czy dane w zawarte w tokenie zgadzają się z danymi z bazy danych.
	 * Jeśli tak - dodajemy użytkownika do kontextu security/
	 * Jeśli się nie zgadzają Spring odcina dostęp i wyrzuca HTTP 403 - Access Denied.
	 * @see org.springframework.web.filter.OncePerRequestFilter#doFilterInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = JWTUtil.trimToken(request.getHeader(JWTUtil.HEADER));
		String username = JWTUtil.getUsernameFromToken(token);
		
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails user = userDetailsService.loadUserByUsername(username);
			
			if (JWTUtil.validateToken(token, user)) {
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
				auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}
		
		filterChain.doFilter(request, response);
	}

}
