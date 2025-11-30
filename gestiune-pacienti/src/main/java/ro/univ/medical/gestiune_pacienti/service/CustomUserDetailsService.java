package ro.univ.medical.gestiune_pacienti.service;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ro.univ.medical.gestiune_pacienti.model.Utilizator;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ro.univ.medical.gestiune_pacienti.repository.UtilizatorRepository;

import java.util.Collections;

public class CustomUserDetailsService implements UserDetailsService {
    private final UtilizatorRepository utilizatorRepository;

    public CustomUserDetailsService(UtilizatorRepository utilizatorRepository) {
        this.utilizatorRepository = utilizatorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utilizator user = utilizatorRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found!");
        }



        return User.builder()
                .username(user.getUsername())
                .password(user.getParola())
                .roles(user.getRol().replace("ROLE_", ""))
                .build();
    }
}
