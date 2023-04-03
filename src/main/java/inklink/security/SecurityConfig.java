package inklink.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import inklink.service.UserRepositoryUserDetailsService;

@Configuration
public class SecurityConfig{
	
	private UserRepositoryUserDetailsService userDetailsService;
	
	public SecurityConfig(UserRepositoryUserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http.authorizeHttpRequests().requestMatchers("/api","/api/**","/js/**","/css/**","/","/home","/index","/images/**","/admin/login","/signUp","/login","/next").permitAll();
		http.authorizeHttpRequests().requestMatchers("/cart").hasRole("USER");
		http.authorizeHttpRequests().requestMatchers("/admin/dashboard").hasRole("ADMIN");
		http.csrf().disable().cors();
		http.authorizeHttpRequests((authz)->authz.anyRequest().permitAll());
		
		//http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
		//http.headers().frameOptions().sameOrigin();
		http.logout().permitAll();
		http.logout().logoutUrl("/logout").logoutSuccessUrl("/");
		return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
	
	@Bean 
	public AuthenticationManager authneticationManager(AuthenticationConfiguration configuration) throws Exception{
		return configuration.getAuthenticationManager();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
}