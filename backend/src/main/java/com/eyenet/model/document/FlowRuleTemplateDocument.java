package com.eyenet.model.document;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "flow_rule_templates")
public class FlowRuleTemplateDocument {
    @Id
    private UUID id;
    private String name;
    private String description;
    private String type;
    private Map<String, Object> defaultValues;
    private String templateContent;
    private boolean isActive;
    private UUID createdBy;
}
