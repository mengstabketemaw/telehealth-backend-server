package io.telehelth.authorizationserver.service;

import io.jsonwebtoken.lang.Strings;
import io.telehelth.authorizationserver.entity.User;
import io.telehelth.authorizationserver.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (Objects.equals(username, "")) {
            throw new UsernameNotFoundException("Invalid user.");
        }
        final String uname = username.trim();
        Optional<User> oUserEntity = userRepository.findUserByEmail(uname);
        User userEntity = oUserEntity.orElseThrow(
                () -> new UsernameNotFoundException(String.format("Given user(%s) not found.", uname)));

        return org.springframework.security.core.userdetails.User.builder()
                .username(userEntity.getEmail())
                .password(userEntity.getPassword())
                .roles(userEntity.getRole().name())
                .build();

    }
}
