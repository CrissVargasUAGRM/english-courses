package com.cristhian.springcloud.couponservice.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    UserDetailsService userDetailsService;

    @Bean
    BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityContextRepository securityContextRepository(){
        return new DelegatingSecurityContextRepository(
                new RequestAttributeSecurityContextRepository(),
                new HttpSessionSecurityContextRepository()
        );
    }

    @Bean
    AuthenticationManager authManager(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //http.formLogin(Customizer.withDefaults());  -> formulario de spring security
        //http.httpBasic(Customizer.withDefaults());  -> para permitir basic auth desde cualquier cliente
        http.authorizeHttpRequests(authorize ->
            authorize.requestMatchers(HttpMethod.GET, "/couponapi/coupon/**", "/index", "/showGetCoupon", "/getCoupon")
                .hasAnyRole("USER", "ADMIN")
                    .requestMatchers(HttpMethod.GET, "/showCreateCoupon", "/createCoupon", "/createResponse")
                    .hasAnyRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/couponapi/coupon", "/saveCoupon").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/getCoupon").hasAnyRole("USER", "ADMIN")
                    .requestMatchers("/", "/login").permitAll()
                );
        //http.csrf(AbstractHttpConfigurer::disable);
        http.csrf(csrfCustomizer -> {
            csrfCustomizer.ignoringRequestMatchers("/couponapi/coupon/**");
            RequestMatcher requestMatcher = new RegexRequestMatcher("/couponapi/coupon/**", "POST");
            requestMatcher = new MvcRequestMatcher(new HandlerMappingIntrospector(), "/getCoupon");
            csrfCustomizer.ignoringRequestMatchers(requestMatcher);
        });

        http.cors(corsCustomizer -> {
            CorsConfigurationSource configurationSource = request -> {
                CorsConfiguration corsConfiguration = new CorsConfiguration();
                corsConfiguration.setAllowedOrigins(List.of("localhost:3000"));
                corsConfiguration.setAllowedMethods(List.of("GET"));
                return corsConfiguration;
            };
        });

        http.securityContext((securityContext -> securityContext.requireExplicitSave(true)));
        return http.build();
    }
}
