package com.academy.fintech.core.db.user;

import com.academy.fintech.core.exception.UsernameExistsException;
import com.academy.fintech.dto.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public User create(RegistrationRequest request) {
        User user;
        if (userRepository.findByUsername(request.email()).isPresent()) {
            throw new UsernameExistsException(String.format("User with username '%s' exists", request.email()));
        } else {
            user = userRepository.save(buildUser(request, roleRepository.findByName("user")));
        }
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User '%s' not found", username)
        ));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Set.of(new SimpleGrantedAuthority(user.getRole().getName()))
        );
    }

    private User buildUser(RegistrationRequest request, Role role) {
        return new User(
                request.email(),
                passwordEncoder.encode(request.password()),
                request.firstName(),
                request.lastName(),
                new BigDecimal(request.salary()),
                request.email(),
                role
        );
    }
}
