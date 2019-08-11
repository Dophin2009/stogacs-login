package net.edt.security.configuration;

import net.edt.persistence.domain.Role;
import net.edt.security.service.BasicAuthenticationService;
import net.edt.security.service.NoEncoder;
import net.edt.security.service.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;

@EnableWebSecurity
public class SecurityConfiguration {

    private static final String TOKEN_ENDPOINTS = "/user/auth/token/**";

    @Autowired
    private SimpleUrlAuthenticationSuccessHandler successHandler;

    @Autowired
    private SimpleUrlAuthenticationFailureHandler failureHandler;

    @Configuration
    @Order(1)
    public class BasicAuthenticationConfiguration extends WebSecurityConfigurerAdapter {

        @Autowired
        private BasicAuthenticationService basicAuthenticationService;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.authenticationProvider(basicAuthProvider());
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher(TOKEN_ENDPOINTS)
                .cors()
                .and()
                .csrf().disable().authorizeRequests()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(TOKEN_ENDPOINTS).authenticated()
//                .and()
//                .formLogin()
//                .successHandler(successHandler)
//                .failureHandler(failureHandler)
                .and()
                .httpBasic()
                .and()
                .logout();
        }

        @Bean
        public DaoAuthenticationProvider basicAuthProvider() {
            DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
            authProvider.setUserDetailsService(basicAuthenticationService);
            authProvider.setPasswordEncoder(passwordEncoder());
            return authProvider;
        }
    }

    @Configuration
    public class TokenAuthenticationConfiguration extends WebSecurityConfigurerAdapter {

        @Autowired
        private TokenAuthenticationService tokenAuthenticationService;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.authenticationProvider(tokenAuthProvider());
        }

        @Override
        protected void configure(final HttpSecurity http) throws Exception {
            http.antMatcher("/**")
                .cors()
                .and()
                .csrf().disable().authorizeRequests()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/user/auth/register").permitAll()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/admin/**").hasAuthority(Role.ADMIN.getValue())
//                .and()
//                .formLogin()
//                .successHandler(successHandler)
//                .failureHandler(failureHandler)
                .and()
                .httpBasic()
                .and()
                .logout();
        }

        @Bean
        public DaoAuthenticationProvider tokenAuthProvider() {
            DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
            authProvider.setUserDetailsService(tokenAuthenticationService);
            authProvider.setPasswordEncoder(noEncoder());
            return authProvider;
        }

    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public NoEncoder noEncoder() {
        return new NoEncoder();
    }

    @Bean
    public SimpleUrlAuthenticationSuccessHandler simpleUrlAuthenticationSuccessHandler() {
        return new SimpleUrlAuthenticationSuccessHandler();
    }

    @Bean
    public SimpleUrlAuthenticationFailureHandler simpleUrlAuthenticationFailureHandler() {
        return new SimpleUrlAuthenticationFailureHandler();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Collections.singletonList("*") /* test client origins */);
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setAllowedMethods(Collections.singletonList("*"));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}
