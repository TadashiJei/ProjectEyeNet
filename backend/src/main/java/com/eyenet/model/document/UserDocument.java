package com.eyenet.model.document;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

@Data
@Document(collection = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDocument {
    @Id
    private String id;

    @Indexed(unique = true)
    private String username;

    private String passwordHash;

    private Role role;

    private String departmentId;

    private LocalDateTime createdAt;

    private LocalDateTime lastLogin;

    public enum Role {
        ADMIN,
        DEPARTMENT_STAFF
    }
}
