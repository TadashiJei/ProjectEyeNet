package com.eyenet.model.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user_preferences")
public class UserPreferencesDocument {
    @Id
    private UUID id;

    @Field("user_id")
    @Indexed(unique = true)
    private UUID userId;

    @Field("theme")
    private String theme;

    @Field("language")
    private String language;

    @Field("timezone")
    private String timezone;

    @Field("notification_settings")
    private NotificationSettings notificationSettings;

    @Field("dashboard_layout")
    private Map<String, DashboardWidget> dashboardLayout;

    @Field("custom_settings")
    private Map<String, String> customSettings;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class NotificationSettings {
        private boolean emailNotifications;
        private boolean pushNotifications;
        private boolean smsNotifications;
        private Map<String, Boolean> alertTypes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DashboardWidget {
        private String widgetId;
        private String type;
        private int position;
        private boolean enabled;
        private Map<String, Object> configuration;
    }
}
