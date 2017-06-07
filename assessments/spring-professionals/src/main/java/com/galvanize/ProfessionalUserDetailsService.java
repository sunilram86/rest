package com.galvanize;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ProfessionalUserDetailsService implements UserDetailsService {

    private final ProfessionalRepository repo;

    public ProfessionalUserDetailsService(ProfessionalRepository repo) {
        this.repo = repo;
    }

    @Override
    public Professional loadUserByUsername(String email) throws UsernameNotFoundException {
        Professional currentUser = repo.findByEmail(email);
        if (currentUser == null) throw new UsernameNotFoundException("Could not find ProfessionalView with email " + email);
        return currentUser;
    }

}
