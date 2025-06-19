package pl.dmcs.rkotas.springbootlab2.message.reponse;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private Collection<? extends GrantedAuthority> authorities;

    // ✅ Constructeur avec les 5 arguments attendus
    public JwtResponse(String token, Long id, String username, String email,
                       Collection<? extends GrantedAuthority> authorities) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.authorities = authorities;
    }

    // 🔄 Getters et setters

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }
}
