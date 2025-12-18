package ro.univ.medical.gestiune_pacienti.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(List.of(daoAuthenticationProvider()));
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .headers(headers -> headers.cacheControl(cache -> cache.disable()))
                .authenticationManager(authenticationManager())

                // SECȚIUNEA DE AUTORIZARE (TOATE regulile)
                .authorizeHttpRequests(auth -> auth

                        // 1. Permite accesul la resursele publice (LOGIN, CSS, JS, etc.)
                        .requestMatchers("/login", "/css/**", "/js/**", "/webjars/**", "/error").permitAll()

                        // 2. Permite accesul la rutele de redirecționare după login (Home)
                        .requestMatchers("/editor/**").hasRole("EDITOR")
                        .requestMatchers("/user/**").hasRole("USER")

                        // 3. Permite accesul la vizualizarea listei de pacienți și la operații (căutare) pentru ORICINE
                        .requestMatchers("/pacienti", "/pacienti/**", "/pacienti/operatii")
                        .hasAnyRole("USER", "EDITOR")

                        // 4. Restricționează OPERAȚIILE CRUD (care modifică date) doar pentru EDITOR
                        .requestMatchers("/pacienti/adauga", "/pacienti/editeaza/**", "/pacienti/sterge/**")
                        .hasRole("EDITOR")

                        // 5. Orice altă cerere necesită autentificare
                        .anyRequest().authenticated()
                )
                // ... (restul configuratiei pentru formLogin, logout, sessionManagement)
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(authenticationSuccessHandler())
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .sessionManagement(session -> session
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                        .expiredUrl("/login?expired")
                );

        return http.build();
    }
}