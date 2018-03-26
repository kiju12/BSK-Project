package bsk.example.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.springframework.security.core.userdetails.UserDetails;

@Entity
public class User implements Serializable, UserDetails {
    
	private static final long serialVersionUID = -3924170999808980476L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
	
    private String username;
    private String password;
    private String email;
    private boolean enabled;

    @ManyToMany(fetch=FetchType.EAGER)
    private Collection<Authority> authorities;

    public User() {
    	this.authorities = Collections.emptyList();
    }
    
    public User(String username, String password, Collection<Authority> authorities) {
		this.username = username;
		this.password = password;
		this.authorities = authorities;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public Collection<Authority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setAuthorities(Collection<Authority> authorities) {
		this.authorities = authorities;
	}
	
	
}