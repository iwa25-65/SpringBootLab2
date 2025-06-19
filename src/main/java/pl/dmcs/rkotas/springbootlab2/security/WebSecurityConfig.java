package pl.dmcs.rkotas.springbootlab2.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.dmcs.rkotas.springbootlab2.security.jwt.JwtAuthEntryPoint;
import pl.dmcs.rkotas.springbootlab2.security.jwt.JwtAuthTokenFilter;
import pl.dmcs.rkotas.springbootlab2.security.services.UserDetailsServiceImpl;
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Autowired
    private JwtAuthEntryPoint unauthorizedHandler;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public JwtAuthTokenFilter authenticationJwtTokenFilter() {
        return new JwtAuthTokenFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults()) // ✅ enable your WebConfig CORS settings
                .exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // ✅ allow sign in/up
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/teachers/**").hasAnyRole("TEACHER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/teachers/grades").hasRole("TEACHER")
                        .requestMatchers("/api/students/**").hasAnyRole("STUDENT", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/enrollments/**").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.GET, "/api/enrollments/**").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.DELETE, "/api/enrollments/**").hasRole("STUDENT")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }



}
