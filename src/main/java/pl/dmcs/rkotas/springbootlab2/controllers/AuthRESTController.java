package pl.dmcs.rkotas.springbootlab2.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.dmcs.rkotas.springbootlab2.message.reponse.JwtResponse;
import pl.dmcs.rkotas.springbootlab2.message.request.SignUpForm;
import pl.dmcs.rkotas.springbootlab2.model.*;
import pl.dmcs.rkotas.springbootlab2.repository.*;
import pl.dmcs.rkotas.springbootlab2.security.jwt.JwtProvider;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthRESTController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;  // Properly declared

    @Autowired
    public AuthRESTController(UserRepository userRepository,
                              RoleRepository roleRepository,
                              PasswordEncoder passwordEncoder,
                              JwtProvider jwtProvider,
                              AuthenticationManager authenticationManager) {  // Added parameter
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;  // Properly initialized
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpForm signUpRequest) {
        // Validate username
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest()
                    .body("Error: Username is already taken!");
        }

        // Create new user
        User user = new User(signUpRequest.getUsername(),  // Fixed getUsername() call
                passwordEncoder.encode(signUpRequest.getPassword()));

        // Process roles and create profile
        Set<Role> roles = new HashSet<>();
        Set<String> strRoles = signUpRequest.getRole();

        if (strRoles == null || strRoles.isEmpty()) {
            roles.add(getRole(RoleName.ROLE_STUDENT));
            createStudentProfile(user, signUpRequest);
        } else {
            strRoles.forEach(role -> {
                switch (role.toLowerCase()) {
                    case "admin":
                        roles.add(getRole(RoleName.ROLE_ADMIN));
                        break;
                    case "teacher":
                        roles.add(getRole(RoleName.ROLE_TEACHER));
                        createTeacherProfile(user, signUpRequest);
                        break;
                    default: // student
                        roles.add(getRole(RoleName.ROLE_STUDENT));
                        createStudentProfile(user, signUpRequest);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        // Authenticate and generate token
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signUpRequest.getUsername(),  // Fixed getUsername() call
                        signUpRequest.getPassword()
                )
        );

        String jwt = jwtProvider.generateJwtToken(authentication);
        return ResponseEntity.ok(new JwtResponse(
                jwt,
                "Bearer",
                signUpRequest.getUsername(),  // Fixed getUsername() call
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                        .collect(Collectors.toList())
        ));
    }

    private Role getRole(RoleName roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Error: " + roleName + " Role not found."));
    }

    private void createStudentProfile(User user, SignUpForm signUpRequest) {
        Student student = new Student();
        student.setFirstname(signUpRequest.getFirstname());
        student.setLastname(signUpRequest.getLastname());
        student.setEmail(signUpRequest.getEmail());
        student.setPhone(signUpRequest.getPhone());
        student.setUser(user);
        user.setStudent(student);
    }

    private void createTeacherProfile(User user, SignUpForm signUpRequest) {
        Teacher teacher = new Teacher();
        teacher.setFirstName(signUpRequest.getFirstname());
        teacher.setLastName(signUpRequest.getLastname());
        teacher.setEmail(signUpRequest.getEmail());
        teacher.setAcademicTitle(signUpRequest.getAcademicTitle());
        teacher.setUser(user);
        user.setTeacher(teacher);
    }
}