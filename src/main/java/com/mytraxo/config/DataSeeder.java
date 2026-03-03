package com.mytraxo.config;

import com.mytraxo.auth.entity.Role;
import com.mytraxo.auth.entity.User;
import com.mytraxo.auth.repo.RoleRepository;
import com.mytraxo.auth.repo.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public DataSeeder(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder encoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {

        // ✅ Seed Roles
        seedRole("ADMIN");
        seedRole("HR");
        seedRole("USER");

        // ✅ Seed Users
        seedUser("admin@mytraxo.com", "Admin@123", List.of("ADMIN"));
        seedUser("hr@mytraxo.com", "Hr@123", List.of("HR"));
        seedUser("user@mytraxo.com", "User@123", List.of("USER"));
    }

    private void seedRole(String name) {
        if (!roleRepository.existsByName(name)) {
            roleRepository.save(new Role(null, name));
        }
    }

    private void seedUser(String email, String password, List<String> roles) {
        if (userRepository.existsByEmail(email)) return;

        User u = new User();
        u.setEmail(email.toLowerCase());
        u.setPasswordHash(encoder.encode(password));
        u.setEnabled(true);
        u.setRoles(roles);

        userRepository.save(u);
    }
}