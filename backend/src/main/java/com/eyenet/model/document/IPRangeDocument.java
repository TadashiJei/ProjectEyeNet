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
@Document(collection = "ip_ranges")
public class IPRangeDocument {
    @Id
    private UUID id;
    private String startIp;
    private String endIp;
    private String subnet;
    private String gateway;
    private String description;
    private boolean reserved;
    private UUID departmentId;
}
