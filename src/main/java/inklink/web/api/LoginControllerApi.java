package inklink.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import inklink.Messenger;
import inklink.User;
import inklink.service.SL;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path="/api", produces="application/json")
public class LoginControllerApi {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@GetMapping(path="/isLoggedIn")
	public Messenger isLoggedIn(@AuthenticationPrincipal User user) {
		
		if(user==null ) {
			return SL.respondError("Not logged in.");
		}else {
			User dummy = new User();
			dummy.setUsername(user.getUsername());
			dummy.setRole(user.getRole());
			dummy.setPhoneNumber(user.getPhoneNumber());
			return SL.respondSuccess("Logged in.", user);
		}
	}
	
	@PostMapping(path="/processLogin")
	public Messenger login(@RequestBody @Valid User loginUser, Errors errors, HttpServletRequest request) {
		if(errors.hasErrors()) {
			return SL.respondError(errors);
		}
		try{
			
			Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword()));
			SecurityContext sc = SecurityContextHolder.getContext();
			sc.setAuthentication(authentication);
			
			HttpSession session = request.getSession(true);
			session.setAttribute("SPRING_SECURITY_CONTEXT", sc);
			
			if(authentication.isAuthenticated()) {
				return SL.respondSuccess("Welcome, "+loginUser.getUsername()+".");
			}else {
				return SL.respondError("Either the username or password or both are wrong.");
			}
			
		}catch(UsernameNotFoundException une) {
			return SL.respondError("Either the username or password or both are wrong.");
		}catch(BadCredentialsException bce) {
			return SL.respondError("Either the username or password or both are wrong.");
		}catch (Exception e) {
			e.printStackTrace();
			return SL.respondError("Error logging in.");
		}
	}
	
	/*@GetMapping("/logout")
	public Messenger logout(@ModelAttribute User user) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		authentication.setAuthenticated(false);
		
		if(user.getUsername().equals("")) {
			return SL.respondError("You were not logged in.");
		}
		
		user.setUsername("");
		return SL.respondSuccess("Logged out.");
	}*/
	
}
