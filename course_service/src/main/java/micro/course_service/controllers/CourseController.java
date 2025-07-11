package micro.course_service.controllers;

import jakarta.validation.Valid;
import micro.course_service.dtos.CourseDTO;
import micro.course_service.dtos.CourseWithTeacherDTO;
import micro.course_service.dtos.createDtos.CreateCourseDTO;
import micro.course_service.dtos.createDtos.PaymentResponse;
import micro.course_service.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(
            @Valid @RequestBody CreateCourseDTO createCourseDTO,
            @RequestAttribute("role") String role,
            @RequestAttribute("userId") Long userId) {
        CourseDTO createdCourse = courseService.createCourse(createCourseDTO, userId, role);
        return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseWithTeacherDTO> getCourse(@PathVariable Long id) {
        CourseWithTeacherDTO course = courseService.getCourseById(id);
        return ResponseEntity.ok(course);
    }

    @GetMapping("/available")
    public ResponseEntity<List<CourseDTO>> getAvailableCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/pending")
    public ResponseEntity<List<CourseDTO>> getPendingCourses(@RequestAttribute("role") String role) {
        return ResponseEntity.ok(courseService.getPendingCourses(role));
    }

    @PostMapping("/enroll/{courseId}")
    public ResponseEntity<PaymentResponse> enrollInCourse(
            @PathVariable Long courseId,
            @RequestHeader String token,
            @RequestAttribute("userId") Long userId
    ){
        return courseService.enrollInCourse(courseId, userId, token);

    }

    @GetMapping("/enrolled")
    public ResponseEntity<List<CourseDTO>> getEnrolledCourses(@RequestAttribute("userId") Long userId) {
        return ResponseEntity.ok(courseService.getEnrolledCoursesByUserId(userId));
    }
}