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

    public DataSeeder(RoleRepository roleRepository,
                      UserRepository userRepository,
                      PasswordEncoder encoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {

        // Seed Roles
        seedRole("ADMIN");
        seedRole("HR");
        seedRole("USER");

        // Seed Users
        seedUser("admin@traxoindia.in", "admin@1987", List.of("ADMIN"));
        seedUser("hr@mytraxo.com", "Hr@123", List.of("HR"));
        seedUser("user@mytraxo.com", "User@123", List.of("USER"));
    }

    private void seedRole(String name) {
        if (!roleRepository.existsByName(name)) {
            Role role = new Role();
            role.setName(name);
            roleRepository.save(role);
        }
    }

    private void seedUser(String email, String password, List<String> roles) {

        User existingUser = userRepository.findByEmail(email).orElse(null);

        if (existingUser == null) {

            // Create new user
            User user = new User();
            user.setEmail(email.toLowerCase());
            user.setPasswordHash(encoder.encode(password));
            user.setEnabled(true);
            user.setRoles(roles);

            userRepository.save(user);

            System.out.println("User created: " + email);

        } else {

            // Update password and roles
            existingUser.setPasswordHash(encoder.encode(password));
            existingUser.setRoles(roles);

            userRepository.save(existingUser);

            System.out.println("User updated: " + email);
        }
    }
}
