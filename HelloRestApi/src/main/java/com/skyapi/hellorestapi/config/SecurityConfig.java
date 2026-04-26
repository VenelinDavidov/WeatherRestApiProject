package com.skyapi.hellorestapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    UserDetailsService userDetailsService(){

        UserDetails user1 = User.withUsername ("admin").password ("{noop}nimda").build ();
        UserDetails user2 = User.withUsername ("john").password ("{noop}johny").build ();
        UserDetails user3 = User.withUsername ("tim").password ("{noop}timmy").build ();

        return new InMemoryUserDetailsManager (user1, user2, user3);

    }


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests (auth-> auth.anyRequest ().authenticated ())
                .httpBasic (httpBasic -> httpBasic.realmName ("My Realm"));

        return http.build ();
    }
}
