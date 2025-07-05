package micro.user_service.service;

import lombok.RequiredArgsConstructor;
import micro.user_service.dto.LoginRequest;
import micro.user_service.dto.LoginResponse;
import micro.user_service.dto.RegisterRequest;
import micro.user_service.dto.RegisterResponse;
import micro.user_service.model.User;
import micro.user_service.repository.UserRepository;
import micro.user_service.security.SimpleEncryptionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final SimpleEncryptionService encryptionService;
    private final PasswordEncoder passwordEncoder; // ✅ inject encoder

    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already in use");
        }
        // ✅ hash password
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(hashedPassword) // ✅ store hashed
                .role(request.getRole())
                .build();

        User savedUser = userRepository.save(user);

        String dataToEncrypt = savedUser.getId() + ":" + savedUser.getRole().name();
        String token = encryptionService.encrypt(dataToEncrypt);

        savedUser.setToken(token);
        userRepository.save(savedUser);

        return RegisterResponse.builder().token(token).build();
    }

    public LoginResponse login(LoginRequest request) {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + request.getEmail())) ;

        // ✅ Check password match
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        return LoginResponse.builder().token(user.getToken()).build();
    }
}
