package bsk.example.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

/*
 * Obiekt roli użytkownika
 */
@Entity
public class Authority implements Serializable, GrantedAuthority {

	private static final long serialVersionUID = 8775494512860670394L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String authority;
    
    @JsonIgnore
    @ManyToMany(mappedBy="authorities", fetch = FetchType.LAZY)
    private List<User> users;
    
    public Authority() {}
    
    public Authority(String name) {
    	this.authority = name;
    }
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	@Override
	public String getAuthority() {
		return authority;
	}
	
	public void setAuthority(String name) {
		authority = name;
	}
	
    
}
