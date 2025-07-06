package micro.course_service.controllers;

import jakarta.validation.Valid;
import micro.course_service.dtos.CourseDTO;
import micro.course_service.dtos.CourseWithTeacherDTO;
import micro.course_service.dtos.createDtos.CreateCourseDTO;
import micro.course_service.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(@Valid @RequestBody CreateCourseDTO createCourseDTO) {
        CourseDTO createdCourse = courseService.createCourse(createCourseDTO, 2L);
        return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseWithTeacherDTO> getCourse(@PathVariable Long id) {
        CourseWithTeacherDTO course = courseService.getCourseById(id);
        return ResponseEntity.ok(course);
    }
}
