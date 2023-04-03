package inklink.web.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
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
@RequestMapping(path="/api/signUp")
public class SignUpControllerApi {
	
	private UserRepository userRepo;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	PasswordEncoder encoder;
	
	public SignUpControllerApi(UserRepository userRepo) {
		this.userRepo = userRepo;
	}
	
	@PostMapping(produces="application/json")
	public Messenger doSignUp(@RequestBody @Valid User newUser, Errors errors, HttpServletRequest request) {
		
		if(errors.hasErrors()) {
			
			ArrayList<String> erm = new ArrayList<>();
			for(ObjectError objError: errors.getAllErrors()) {
				erm.add(objError.getDefaultMessage());
			}
			return SL.respondError(erm);
		}else {
			List<User> testUser = userRepo.findByUsername(newUser.getUsername());
			if(testUser.size()!=0) {
				return SL.respondError("Username '"+newUser.getUsername()+"' already exists. Choose a different username.");
			}
			else {
				String pwd = newUser.getPassword();
				newUser.setPassword(encoder.encode(newUser.getPassword()));
				
				try {
					newUser = userRepo.save(newUser);

					Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(newUser.getUsername(), pwd));
					SecurityContext sc = SecurityContextHolder.getContext();
					sc.setAuthentication(authentication);
					
					HttpSession session = request.getSession();
					session.setAttribute("SPRING_SECURITY_CONTEXT", sc);
					
					return SL.respondSuccess("Hello "+newUser.getUsername()+". Welcome to InkLink. We hope you will enjoy our vast catalogue of books.");
				}catch(Exception e) {
					e.printStackTrace();
					return SL.respondError("Error adding user.");
				}
			}
		}
	}	
}
