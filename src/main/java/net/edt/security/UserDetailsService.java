package net.edt.security;

import net.edt.persistence.domain.Role;
import net.edt.persistence.domain.User;
import net.edt.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        Optional<User> foundUser = userRepository.findByEmail(email);
        if (!foundUser.isPresent()) {
            throw new UsernameNotFoundException("User with email '" + email + "' not found");
        }

        User user = foundUser.get();
        boolean enabled = true;
        boolean accountNotExpired = true;
        boolean credentialsNotExpired = true;
        boolean accountNotLocked = true;
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), enabled,
                                                                      accountNotExpired, credentialsNotExpired,
                                                                      accountNotLocked,
                                                                      getAuthorities(user.getRoles()));
    }

    private static List<GrantedAuthority> getAuthorities(Set<Role> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getValue()));
        }
        return authorities;
    }

}
