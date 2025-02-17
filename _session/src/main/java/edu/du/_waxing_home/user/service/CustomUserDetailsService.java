package edu.du._waxing_home.user.service;

import edu.du._waxing_home.user.domain.User;
import edu.du._waxing_home.user.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // findByUsername이 Optional<User>를 반환한다고 가정
        Optional<User> optionalUser = userRepository.findByUsername(username);

        // Optional에서 값을 꺼내는 방법: get() 또는 orElseThrow() 사용
        User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthorities(user));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {

        return Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()));
    }

}
