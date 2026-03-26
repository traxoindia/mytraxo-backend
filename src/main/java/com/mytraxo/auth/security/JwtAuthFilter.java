package com.mytraxo.auth.security;
import com.mytraxo.auth.entity.User;
import com.mytraxo.auth.repo.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.mytraxo.employee.entity.Employee;
import com.mytraxo.employee.repo.EmployeeRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository; // 1. Add this

    public JwtAuthFilter(JwtService jwtService, UserRepository userRepository,EmployeeRepository employeeRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
    }

    // Skip filter for login / register APIs
   @Override
protected boolean shouldNotFilter(HttpServletRequest request) {

    String path = request.getServletPath();

    return path.startsWith("/api/auth")
        || path.startsWith("/api/careers")   // allow career APIs
        || request.getMethod().equals("OPTIONS");
}

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // If no token → continue without authentication
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {

            Jws<Claims> parsed = jwtService.parse(token);
            String email = parsed.getBody().getSubject();

            User user = userRepository.findByEmail(email).orElse(null);

           if (user != null && user.isEnabled()) {
            // This is your EXACT original logic - No changes here
            UserPrincipal principal = new UserPrincipal(
                    user.getEmail(),
                    user.getPasswordHash(),
                    user.isEnabled(),
                    user.getRoles()
            );

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            principal,
                            null,
                            principal.getAuthorities()
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } 
        // 🛡️ STEP 2: ONLY IF USER IS NOT FOUND, CHECK FOR EMPLOYEE
        // This is the new "Fallback" logic that only runs for Employees
        else {
            Employee employee = employeeRepository.findByEmailAddress(email).orElse(null);
            
            if (employee != null) {
                // We create a simple principal for the employee
                // This will NOT affect how Admin/HR principals work
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                email, // Use the email as the name
                                null, 
                                List.of(new SimpleGrantedAuthority("ROLE_EMPLOYEE"))
                        );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

    } catch (JwtException e) {
        // Invalid token → ignore and continue
    }

    chain.doFilter(request, response);
}
}