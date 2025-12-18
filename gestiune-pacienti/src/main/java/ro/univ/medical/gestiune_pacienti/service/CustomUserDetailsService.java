package ro.univ.medical.gestiune_pacienti.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ro.univ.medical.gestiune_pacienti.entity.Utilizator;
import ro.univ.medical.gestiune_pacienti.repository.UtilizatorRepository;

import java.util.Collections;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UtilizatorRepository utilizatorRepository;

    public CustomUserDetailsService(UtilizatorRepository utilizatorRepository) {
        this.utilizatorRepository = utilizatorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utilizator utilizator = utilizatorRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilizatorul nu a fost găsit: " + username));

    /*System.out.println("=== DEBUG UserDetails ===");
        System.out.println("Username: " + utilizator.getUsername());
        System.out.println("Password: " + utilizator.getParola());
        System.out.println("Role: " + utilizator.getRol());*/


        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(utilizator.getRol())
        );

        return new User(
                utilizator.getUsername(),
                utilizator.getParola(),
                authorities
        );
    }
}