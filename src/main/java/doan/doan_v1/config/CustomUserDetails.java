package doan.doan_v1.config;

import doan.doan_v1.entity.Role;
import doan.doan_v1.entity.User;
import doan.doan_v1.repository.RoleRepository;
import doan.doan_v1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CustomUserDetails implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public CustomUserDetails(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            Role role = roleRepository.findById(user.getRoleId())
                    .orElseThrow(() -> new RuntimeException("not found role"));
            List<GrantedAuthority> authorities = role != null ?
                    Collections.singletonList(new SimpleGrantedAuthority(role.getName())) :
                    Collections.emptyList();

            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    authorities
            );
        } else {
            throw new UsernameNotFoundException("Invalid email or password");
        }
    }
}
