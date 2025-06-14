package pl.dmcs.rkotas.springbootlab2.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.dmcs.rkotas.springbootlab2.message.request.LoginForm;
import pl.dmcs.rkotas.springbootlab2.message.request.SignUpForm;
import pl.dmcs.rkotas.springbootlab2.message.reponse.JwtResponse;
import pl.dmcs.rkotas.springbootlab2.message.reponse.ResponseMessage;
import pl.dmcs.rkotas.springbootlab2.model.Role;
import pl.dmcs.rkotas.springbootlab2.model.RoleName;
import pl.dmcs.rkotas.springbootlab2.model.User;
import pl.dmcs.rkotas.springbootlab2.repository.RoleRepository;
import pl.dmcs.rkotas.springbootlab2.repository.UserRepository;
import pl.dmcs.rkotas.springbootlab2.security.jwt.JwtProvider;

import java.util.HashSet;
import java.util.Set;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping("/auth")
public class AuthRESTController {

    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Autowired
    public AuthRESTController(DaoAuthenticationProvider daoAuthenticationProvider,
                              UserRepository userRepository, RoleRepository roleRepository,
                              PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.daoAuthenticationProvider = daoAuthenticationProvider;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {
        Authentication authentication = daoAuthenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpForm signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>(new ResponseMessage("Error: Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                passwordEncoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            Role studentRole = roleRepository.findByName(RoleName.ROLE_STUDENT)
                    .orElseThrow(() -> new RuntimeException("Error: Student Role not found."));
            roles.add(studentRole);
        } else {
            strRoles.forEach(role -> {
                switch (role.toLowerCase()) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Admin Role not found."));
                        roles.add(adminRole);
                        break;
                    case "teacher":
                        Role teacherRole = roleRepository.findByName(RoleName.ROLE_TEACHER)
                                .orElseThrow(() -> new RuntimeException("Error: Teacher Role not found."));
                        roles.add(teacherRole);
                        break;
                    default:
                        Role studentRole = roleRepository.findByName(RoleName.ROLE_STUDENT)
                                .orElseThrow(() -> new RuntimeException("Error: Student Role not found."));
                        roles.add(studentRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return new ResponseEntity<>(new ResponseMessage("User registered successfully!"), HttpStatus.OK);
    }
}