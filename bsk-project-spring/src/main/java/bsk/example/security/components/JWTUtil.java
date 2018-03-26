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

/*
 * Klasa zawierająca statyczne metody do operacji na tokenach.
 */
public class JWTUtil {

	private static Logger log = Logger.getLogger(JWTUtil.class);
	
	// Określenie nazwy headera http, w jakim będzie znajdował się token.
	public final static String HEADER = "Authorization";

	// Składowe tokenu
	private final static String CLAIM_SUB = "sub"; // Nazwa użytkownika
	private final static String CLAIM_ROLES = "roles"; // Tablica uprawnień
	private final static String CLAIM_CREATED = "created"; // Data utworzenia tokenu
	private final static Long EXP = (long) 604800; // Czas po jakim token wygaśnie (w sekundach)
	private final static String SECRET = "secret"; // Sygnatura tokenu

	/*
	 * Generowanie tokenu na podstawie obiektu użytkownika.
	 * @param userDetails Obiekt użytkownika, dla którego generujemy token.
	 * @return String Zakodowany token w postaci łańcucha znaków.
	 */
	public static String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(CLAIM_SUB, userDetails.getUsername());
		claims.put(CLAIM_ROLES, userDetails.getAuthorities());
		claims.put(CLAIM_CREATED, new Date());

		return generateToken(claims);
	}
	
	/*
	 * Metoda pomocnicza do generowania tokenu. Tworzy token na podstawie właściwości, czasu wygaśnięcia, sygnatury i wybranego algorytmu(HS512)
	 * @param claims Właściwości tokenu
	 * @return String Zakodowany token w postaci łańcucha znaków
	 */
	private static String generateToken(Map<String, Object> claims) {
		return Jwts.builder().setClaims(claims).setExpiration(new Date(System.currentTimeMillis() + EXP * 1000))
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();
	}

	/*
	 * Pobiera nazwę użytkownika z tokenu
	 * @param token Zakodowany token
	 * @return String Nazwa użytkownika zawarta w tokenie, jeśli nie znajdzie w tokenie nazwy użytkownika zwraca null.
	 */
	public static String getUsernameFromToken(String token) {
		String username = null;
		try {
			username = getClaimsFromToken(token).getSubject();
		} catch (Exception e) {
			log.error("Exception in getting username from token.");
		}

		return username;
	}

	/*
	 * Walidacja tokenu.
	 * @param token Zakodowany token
	 * @param userDetails Użytkownik, który przesłał token
	 * @return boolean Zwraca true, jeżeli czas wygaśniecia tokenu nie minął oraz wysyłający token zgadza się z tym zawartym w nim
	 */
	public static boolean validateToken(String token, UserDetails userDetails) {
		User user = (User) userDetails;
		String username = getUsernameFromToken(token);

		return username.equals(user.getUsername()) && new Date().before(getExpirationDateFromToken(token));
	}

	/*
	 * Pobiera date wygaśnięcia Tokenu
	 * @param token Zakodowany token
	 * @return Date Data wygaśnięcia tokenu
	 * @see java.util.Date
	 */
	private static Date getExpirationDateFromToken(String token) {
		Date date = null;
		try {
			date = getClaimsFromToken(token).getExpiration();
		} catch (Exception e) {
			log.error("Exception in getting expiration date from token");
		}
		return date;
	}
	
	/*
	 * Pobiera właściwości tokenu
	 * @param token Zakodowany token
	 * @return Claims
	 * @see io.jsonwebtoken.Claims
	 */
	private static Claims getClaimsFromToken(String token) {
		Claims claims;
		try {
			claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			claims = null;
		}
		return claims;
	}

	/*
	 * Metoda sprawdzająca czy możemy odświeżyć token.
	 * Token możemy odświeżyć, gdy jego czas wygaśnięcia jeszcze nie minął.
	 * @param token Zakodowany token
	 * @return boolean
	 */
	public static boolean canTokenBeRefreshed(String token) {
		return !getExpirationDateFromToken(token).before(new Date());
	}

	/*
	 * Metoda odświeżająca token. Działa na zasadzie wygenerowania nowego dokenu z nową datą utworzenia i wygaśnięcia
	 * @param token Zakodowany token do odświeżenia
	 * @return String Odświeżony token
	 */
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

	/*
	 * Metoda przycinająca token odebrany od okaziciela z headera Authorization w zapytaniu HTTP
	 * @param header Header Http
	 * @return String Jeśli header jest w odpowiednim formacie zwraca czysty token, jeżeli nie zwraca null.
	 */
	public static String trimToken(String header) {
		if (header != null && header.startsWith("Bearer ")) {
			return header.substring(7);
		} else return null;
	}
	
}
