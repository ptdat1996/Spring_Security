package com.dac.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("admin").password("{noop}admin").roles("ADMIN");
        auth.inMemoryAuthentication().withUser("user1").password("{noop}user1").roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // For example: Use only Http Basic and not form login.
        http.httpBasic().disable();

        http.authorizeRequests().antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')");

        http.authorizeRequests().antMatchers("/user/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')");

        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");

        http.authorizeRequests().and().formLogin()
                .loginProcessingUrl("/j_spring_security_login")
                .loginPage("/login")
                .defaultSuccessUrl("/user")
                .failureUrl("/login?message=error")
                .usernameParameter("username")
                .passwordParameter("password")
                .and().logout().logoutUrl("/j_spring_security_logout").logoutSuccessUrl("/login?message=logout");
    }


}
