package tn.esprit.spring.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tn.esprit.spring.Security.jwt.AuthEntryPointJwt;
import tn.esprit.spring.Security.jwt.AuthTokenFilter;
import tn.esprit.spring.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
		// securedEnabled = true,
		// jsr250Enabled = true,
		prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable()
			.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.authorizeRequests().antMatchers("/auth/**").permitAll()
				.antMatchers("/csv/**").hasAuthority("ROLE_ADMIN")
				.antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
				.antMatchers("/ForumPosts/**").hasAuthority("ROLE_EMPLOYEE")
				.antMatchers("/categorie/**").hasAuthority("ROLE_ADMIN")
				.antMatchers("/Answers/**").hasAuthority("ROLE_EMPLOYEE")
				.antMatchers("/likes/**").hasAuthority("ROLE_EMPLOYEE")
				.antMatchers("/NotificationObject/**").hasAuthority("ROLE_EMPLOYEE")
				.antMatchers("/Notification/**").hasAuthority("ROLE_EMPLOYEE")
				.antMatchers("/Questionnaire/**").hasAuthority("ROLE_ADMIN")
				.antMatchers("/Question/**").hasAuthority("ROLE_ADMIN")

			.antMatchers("/user/**").permitAll()
			.antMatchers("/test/**").permitAll()
			.anyRequest().authenticated();


		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}
}