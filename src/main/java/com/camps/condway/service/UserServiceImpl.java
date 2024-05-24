package com.camps.condway.service;

import com.camps.condway.entity.Role;
import com.camps.condway.entity.User;
import com.camps.condway.exceptions.UserException;
import com.camps.condway.repository.RoleRepository;
import com.camps.condway.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final Tracer tracer;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, Tracer tracer) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.tracer = tracer;
    }

    public Optional<User> register(User user) {

        String uuid = UUID.randomUUID().toString();
        Span span = tracer.nextSpan().name("userSpan").tag("transactionId", uuid).start();

        log.info("Registrando usuario com email: {}", user.getEmail());
        try (Tracer.SpanInScope spanInScope = this.tracer.withSpan(span.start())) {
            Role defaultRole = roleRepository.findByName("USER");
            if (defaultRole == null) {
                log.error("Role USER nao encontrada para o email: {}", user.getEmail());
                return Optional.empty();
            }

            user.getRoles().add(defaultRole);
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            User savedUser = userRepository.save(user);
            log.info("Usuario registrado com sucesso com o email: {}", user.getEmail());
            return Optional.of(savedUser);
        } catch (Exception e) {
            log.error("Erro ao registrar usuario com o email: {}", user.getEmail(), e);
            throw new UserException("Erro ao registrar usuario", e.getCause());
        } finally {
            span.end();
        }
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            log.warn("Usuario nao encontrado com o e-mail: {}", user.getEmail());
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                .collect(Collectors.toSet());

        log.info("Usuario logado com sucesso com email: {}", user.getEmail());
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}
