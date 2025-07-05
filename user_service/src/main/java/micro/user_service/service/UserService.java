package micro.user_service.service;

import lombok.RequiredArgsConstructor;
import micro.user_service.dto.*;
import micro.user_service.model.User;
import micro.user_service.repository.UserRepository;
import micro.user_service.security.SimpleEncryptionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SimpleEncryptionService encryptionService;

    public CreateTeacherResponse createTeacher(CreateTeacherRequest request) {
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
        return CreateTeacherResponse.builder().build();
    }

    public UserResponse getUser(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

}
