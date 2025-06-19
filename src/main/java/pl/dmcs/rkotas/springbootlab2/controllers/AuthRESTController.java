package pl.dmcs.rkotas.springbootlab2.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.dmcs.rkotas.springbootlab2.message.request.LoginForm;
import pl.dmcs.rkotas.springbootlab2.message.request.SignUpForm;
import pl.dmcs.rkotas.springbootlab2.message.reponse.ResponseMessage;
import pl.dmcs.rkotas.springbootlab2.message.reponse.JwtResponse;
import pl.dmcs.rkotas.springbootlab2.model.*;
import pl.dmcs.rkotas.springbootlab2.repository.*;
import pl.dmcs.rkotas.springbootlab2.security.jwt.JwtProvider;
import pl.dmcs.rkotas.springbootlab2.security.services.UserPrinciple;

import java.util.HashSet;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthRESTController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwtToken(authentication);

        UserPrinciple userDetails = (UserPrinciple) authentication.getPrincipal();

        return ResponseEntity.ok(
                new JwtResponse(jwt,
                        userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        userDetails.getAuthorities())
        );
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpForm signUpRequest) {

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>(new ResponseMessage("Fail -> Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>(new ResponseMessage("Fail -> Email is already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Créer l'utilisateur
        User user = new User(
                signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getEmail()
        );

        // Rôles
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        for (String role : strRoles) {
            switch (role.toLowerCase()) {
                case "admin":
                    roles.add(roleRepository.findByName(RoleName.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Role ADMIN not found")));
                    break;
                case "teacher":
                    roles.add(roleRepository.findByName(RoleName.ROLE_TEACHER)
                            .orElseThrow(() -> new RuntimeException("Role TEACHER not found")));
                    break;
                case "student":
                    roles.add(roleRepository.findByName(RoleName.ROLE_STUDENT)
                            .orElseThrow(() -> new RuntimeException("Role USER not found")));
                    break;
                default:
                    return new ResponseEntity<>(new ResponseMessage("Invalid role: " + role),
                            HttpStatus.BAD_REQUEST);
            }
        }

        user.setRoles(roles);

        // Créer le Student si rôle = student
        if (strRoles.contains("student")) {
            Student student = new Student();
            student.setFirstname(signUpRequest.getFirstname());
            student.setLastname(signUpRequest.getLastname());
            student.setEmail(signUpRequest.getEmail());
            student.setPhone(signUpRequest.getPhone());
            student.setUser(user);
            user.setStudent(student); // ⚠️ Important pour le mapping bidirectionnel
        }

        // Créer le Teacher si rôle = teacher
        if (strRoles.contains("teacher")) {
            Teacher teacher = new Teacher();
            teacher.setFirstname(signUpRequest.getFirstname());
            teacher.setLastname(signUpRequest.getLastname());
            teacher.setEmail(signUpRequest.getEmail());
            teacher.setPhone(signUpRequest.getPhone());
            teacher.setUser(user);
            user.setTeacher(teacher); // ⚠️ Important aussi
        }

        // Tout est persisté avec cascade
        userRepository.save(user);

        return new ResponseEntity<>(new ResponseMessage("User registered successfully!"), HttpStatus.OK);
    }
    @GetMapping("/test/user")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> testStudent(@AuthenticationPrincipal UserPrinciple user) {
        return ResponseEntity.ok("Connected as: " + user.getUsername() + ", id: " + user.getId());
    }


}
