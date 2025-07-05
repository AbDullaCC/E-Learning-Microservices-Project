package micro.user_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import micro.user_service.dto.CreateTeacherRequest;
import micro.user_service.dto.CreateTeacherResponse;
import micro.user_service.dto.UserResponse;
import micro.user_service.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping
    public String create(@RequestBody String s){
        return s + "users for sure";
    }

    @PostMapping("/teachers/create")
    public ResponseEntity<CreateTeacherResponse> register(@RequestBody @Valid CreateTeacherRequest request) {
        return ResponseEntity.ok(userService.createTeacher(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }
}
