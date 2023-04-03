package inklink.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path="/admin")
public class AdminController {
	
	@GetMapping("/login")
	public String getLogin() {
		return "adminLogin";
	}
	
	@GetMapping("/dashboard")
	public String getDashboard() {
		//Include authentication test
		return "adminDashboard";
	}
}
