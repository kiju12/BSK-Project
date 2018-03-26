package bsk.example.security.components;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jboss.logging.Logger;
import org.springframework.security.core.userdetails.UserDetails;

import bsk.example.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTUtil {

	private static Logger log = Logger.getLogger(JWTUtil.class);
	
	public final static String HEADER = "Authorization";

	private final static String CLAIM_SUB = "sub";
	private final static String CLAIM_ROLES = "roles";
	private final static String CLAIM_CREATED = "created";
	private final static Long EXP = (long) 604800;
	private final static String SECRET = "secret";

	public static String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(CLAIM_SUB, userDetails.getUsername());
		claims.put(CLAIM_ROLES, userDetails.getAuthorities());
		claims.put(CLAIM_CREATED, new Date());

		return generateToken(claims);
	}
	
	private static String generateToken(Map<String, Object> claims) {
		return Jwts.builder().setClaims(claims).setExpiration(new Date(System.currentTimeMillis() + EXP * 1000))
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();
	}

	public static String getUsernameFromToken(String token) {
		String username = null;
		try {
			username = getClaimsFromToken(token).getSubject();
		} catch (Exception e) {
			log.error("Exception in getting username from token.");
		}

		return username;
	}

	public static boolean validateToken(String token, UserDetails userDetails) {
		User user = (User) userDetails;
		String username = getUsernameFromToken(token);

		return username.equals(user.getUsername()) && new Date().before(getExpirationDateFromToken(token));
	}

	private static Date getExpirationDateFromToken(String token) {
		Date date = null;
		try {
			date = getClaimsFromToken(token).getExpiration();
		} catch (Exception e) {
			log.error("Exception in getting expiration date from token");
		}
		return date;
	}

	private static Claims getClaimsFromToken(String token) {
		Claims claims;
		try {
			claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			claims = null;
		}
		return claims;
	}

	public static boolean canTokenBeRefreshed(String token) {
		return !getExpirationDateFromToken(token).before(new Date());
	}

	public static String refrehToken(String token) {
		String newToken = null;
		try {
			Claims claims = getClaimsFromToken(token);
			claims.put(CLAIM_CREATED, new Date());
			newToken = generateToken(claims);
		} catch (Exception e) {
			log.error("Exception during Token Refresh.");
		}
		return newToken;
	}

	public static String trimToken(String header) {
		if (header != null && header.startsWith("Bearer ")) {
			return header.substring(7);
		} else return null;
	}
	
}
