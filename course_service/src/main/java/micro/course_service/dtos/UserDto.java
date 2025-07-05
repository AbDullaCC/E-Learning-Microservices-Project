package micro.course_service.dtos;

import com.netflix.discovery.provider.Serializer;

//@Serializer
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
}

