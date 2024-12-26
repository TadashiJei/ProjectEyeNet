package com.eyenet.model.document;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "ports")
public class PortDocument {
    @Id
    private UUID id;
    private String name;
    private String type;
    private String status;
    private int number;
    private String description;
    private UUID deviceId;
    private UUID qosPolicyId;
}
