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

public class AuthenticationFilter extends OncePerRequestFilter {

//	private final String jwtHeader = "Authorization";
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = JWTUtil.trimToken(request.getHeader(JWTUtil.HEADER));
		
//		if (token != null && token.startsWith("Bearer ")) {
//			token = token.substring(7);
//		}
		
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
