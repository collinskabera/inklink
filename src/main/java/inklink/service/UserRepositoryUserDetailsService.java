package inklink.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import inklink.User;
import inklink.data.UserRepository;

@Service
public class UserRepositoryUserDetailsService implements UserDetailsService{

	private UserRepository userRepo;
	
	public UserRepositoryUserDetailsService(UserRepository userRepo) {
		this.userRepo = userRepo;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<User> users = userRepo.findByUsername(username);
		
		if(users.size()==1) {
			return users.get(0);
		}
		throw new UsernameNotFoundException("User with username '"+username+"' not found.");
	}

}
