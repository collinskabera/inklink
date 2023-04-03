package inklink.web.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import inklink.Messenger;
import inklink.User;
import inklink.data.UserRepository;
import inklink.service.SL;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path="/api/admin", produces="application/json")
public class AdminControllerApi {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	private UserRepository userRepo;
	
	public AdminControllerApi(UserRepository userRepo) {
		this.userRepo = userRepo;
	}
	
	@GetMapping(path="/isLoggedIn")
	public Messenger isLoggedIn(@AuthenticationPrincipal User user) {
		if(user==null) {
			return SL.respondError("Not logged in.");
		}
		User dummy = new User();
		dummy.setUsername(user.getUsername());
		dummy.setRole(user.getRole());
		return SL.respondSuccess("You are logged in.", dummy);
	}

	@PostMapping(path="/processLogin")
	public Messenger processLogin(@RequestBody @Valid User loginUser, Errors errors, HttpServletRequest request) {
		if(errors.hasErrors()) {
			return SL.respondError(errors);
		}
		try{
			checkIfThereAreAnyAdmins();
			
			Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword()));
			
			SecurityContext sc = SecurityContextHolder.getContext();
			sc.setAuthentication(authentication);
			
			if(authentication.isAuthenticated()) {
				HttpSession session = request.getSession(true);
				session.setAttribute("SPRING_SECURITY_CONTEXT", sc);
				
				return SL.respondSuccess("Successfully logged in.");
			}else {
				return SL.respondError("Either username or password or both are incorrect.");
			}
					
		}catch(UsernameNotFoundException une) {
			une.printStackTrace();
			return SL.respondError(une.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return SL.respondError("Error logging in.");
		}
	}

	private void checkIfThereAreAnyAdmins() {
		List<User> users = userRepo.selectAllAdmins();
		if(users.size()==0) {
			User user = new User();
			user.setUsername("root");
			user.setPassword(passwordEncoder.encode("guess"));
			user.setRole("ROLE_ADMIN");
			
			userRepo.save(user);
		}
	}
	
}
