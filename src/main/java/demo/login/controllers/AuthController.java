package demo.login.controllers;

import demo.login.payload.request.LoginRequest;
import demo.login.payload.request.SignupRequest;
import demo.login.payload.response.JwtResponse;
import demo.login.payload.response.MessageResponse;

import java.util.*;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import demo.login.repository.RoleRepository;
import demo.login.repository.UserRepository;
import demo.login.security.jwt.JwtUtils;
import demo.login.security.service.UserDetailsImp;

import demo.login.data.User;
import demo.login.data.Role;
import demo.login.data.ERole;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/signin")
	public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
				loginRequest.getPassword());
		Authentication authentication = authenticationManager.authenticate(token);

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImp userDetails = (UserDetailsImp) authentication.getPrincipal();
		Boolean isAdmin = userDetails.getAuthorities().stream()
				.filter(authority -> ERole.valueOf(authority.getAuthority()).equals(ERole.ROLE_ADMIN)).findAny()
				.isPresent();

		return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getUserId(), isAdmin));
	}

	@PostMapping("/signup")
	public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
		if (userRepository.existsByEnrollmentNo(Long.parseLong(signupRequest.getEnrollmentNo()))) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: EnrollmentNo is already taken!"));
		}

		if (userRepository.existsByEmail(signupRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		User user = new User();
		user.setEmail(signupRequest.getEmail());
		user.setEnrollmentNo(Long.parseLong(signupRequest.getEnrollmentNo()));
		user.setPhoneNumber(Long.parseLong(signupRequest.getPhoneNumber()));
		user.setFirstname(signupRequest.getFirstname());
		user.setLastname(signupRequest.getLastname());
		user.setCourse(signupRequest.getCourse());
		user.setBranch(signupRequest.getBranch());
		user.setPassoutYear(signupRequest.getPassoutYear());
		user.setDateOfBirth(signupRequest.getDateOfBirth());
		user.setGender(signupRequest.getGender());
		user.setPassword(encoder.encode(signupRequest.getPassword()));

		Set<String> strRoles = signupRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				Role userRole = roleRepository.findByName(ERole.valueOf(role))
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(userRole);
			});
		}
		user.setRoles(roles);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User regisered successfully"));
	}

	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}
}