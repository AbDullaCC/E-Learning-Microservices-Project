package micro.course_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

    private final SimpleEncryptionService encryptionService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String token = request.getHeader("token");
        if (token == null) {
            response.sendError(410, "Missing token");
            return;
        }

        try {
            String decrypted = encryptionService.decrypt(token);
            // Assuming decrypted format: "userId:role"
            String[] parts = decrypted.split(":");
            Long userId = Long.parseLong(parts[0]);

            // Store userId in request attribute
            request.setAttribute("userId", userId);
            request.setAttribute("role", parts[1]);

        } catch (Exception e) {
            System.err.println(e.getMessage());
            response.sendError(410, "Invalid token");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
