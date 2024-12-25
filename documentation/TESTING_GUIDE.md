# EyeNet Testing Guide

## 1. Local Development Testing

### 1.1 Prerequisites
```bash
# Required software
- Java 17
- Maven
- Docker & Docker Compose
- MongoDB
- Postman (for API testing)
```

### 1.2 Starting Development Environment
```bash
# 1. Start MongoDB and other services
docker-compose -f docker-compose.dev.yml up -d

# 2. Run Spring Boot application
./mvnw spring-boot:run

# 3. Run React frontend (in separate terminal)
cd frontend
npm start
```

### 1.3 Running Tests
```bash
# Run all backend tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=UserServiceTest

# Run with coverage report
./mvnw test jacoco:report
```

## 2. API Testing

### 2.1 Authentication API
```http
# Login
POST http://localhost:8080/api/v1/auth/login
Content-Type: application/json

{
    "username": "admin",
    "password": "admin123"
}

# Register
POST http://localhost:8080/api/v1/auth/register
Content-Type: application/json

{
    "username": "newuser",
    "password": "password123",
    "role": "DEPARTMENT_STAFF",
    "departmentId": "dept123"
}
```

### 2.2 Network Monitoring API
```http
# Get network status
GET http://localhost:8080/api/v1/network/status
Authorization: Bearer {jwt_token}

# Get department metrics
GET http://localhost:8080/api/v1/network/metrics/{departmentId}
Authorization: Bearer {jwt_token}
```

## 3. Unit Testing Examples

### 3.1 Service Layer Test
```java
@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;
    
    @MockBean
    private UserRepository userRepository;
    
    @Test
    void testCreateUser() {
        // Given
        UserDocument user = new UserDocument();
        user.setUsername("testuser");
        
        // When
        when(userRepository.save(any())).thenReturn(user);
        
        // Then
        UserDocument result = userService.createUser(user);
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }
}
```

### 3.2 Controller Layer Test
```java
@WebMvcTest(AuthController.class)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private AuthService authService;
    
    @Test
    void testLogin() throws Exception {
        // Given
        LoginRequest request = new LoginRequest("user", "pass");
        
        // When/Then
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isOk());
    }
}
```

## 4. Integration Testing

### 4.1 Database Integration
```java
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MongoIntegrationTest {
    @Autowired
    private UserRepository userRepository;
    
    @Test
    void testUserOperations() {
        // Test CRUD operations
        UserDocument user = new UserDocument();
        user.setUsername("integrationTest");
        
        UserDocument saved = userRepository.save(user);
        assertNotNull(saved.getId());
        
        Optional<UserDocument> found = userRepository.findById(saved.getId());
        assertTrue(found.isPresent());
    }
}
```

### 4.2 Network Integration
```java
@SpringBootTest
class NetworkIntegrationTest {
    @Autowired
    private NetworkService networkService;
    
    @Test
    void testNetworkMonitoring() {
        NetworkMetrics metrics = networkService.collectMetrics();
        assertNotNull(metrics);
        assertTrue(metrics.getBytesTransferred() >= 0);
    }
}
```

## 5. Performance Testing

### 5.1 JMeter Test Plan
```xml
<?xml version="1.0" encoding="UTF-8"?>
<jmeterTestPlan version="1.2">
    <hashTree>
        <TestPlan guiclass="TestPlanGui" testclass="TestPlan" testname="EyeNet Load Test">
            <elementProp name="TestPlan.user_defined_variables" elementType="Arguments">
                <collectionProp name="Arguments.arguments"/>
            </elementProp>
        </TestPlan>
        <!-- Add test scenarios -->
    </hashTree>
</jmeterTestPlan>
```

### 5.2 Load Testing Script
```bash
# Run JMeter test
jmeter -n -t load_test.jmx -l results.jtl

# Generate HTML report
jmeter -g results.jtl -o report
```

## 6. Security Testing

### 6.1 JWT Testing
```java
@Test
void testJWTValidation() {
    String token = jwtService.generateToken("user123");
    assertTrue(jwtService.validateToken(token));
    assertEquals("user123", jwtService.getUsernameFromToken(token));
}
```

### 6.2 Authentication Testing
```java
@Test
void testUnauthorizedAccess() throws Exception {
    mockMvc.perform(get("/api/v1/secure/endpoint"))
           .andExpect(status().isUnauthorized());
}
```

## 7. Frontend Testing

### 7.1 React Component Tests
```javascript
import { render, screen } from '@testing-library/react';
import Dashboard from './Dashboard';

test('renders dashboard', () => {
    render(<Dashboard />);
    expect(screen.getByText(/Network Status/i)).toBeInTheDocument();
});
```

### 7.2 E2E Testing with Cypress
```javascript
describe('Login Flow', () => {
    it('should login successfully', () => {
        cy.visit('/login');
        cy.get('#username').type('admin');
        cy.get('#password').type('admin123');
        cy.get('button[type="submit"]').click();
        cy.url().should('include', '/dashboard');
    });
});
```

## 8. Continuous Integration Testing

### 8.1 GitHub Actions Workflow
```yaml
name: CI

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Set up JDK
        uses: actions/setup-java@v2
        with:
          java-version: '17'
      - name: Build and Test
        run: ./mvnw verify
```

## 9. Production Testing

### 9.1 Smoke Tests
```bash
#!/bin/bash
# Check if services are running
curl -f http://localhost:8080/health
curl -f http://localhost:8080/api/v1/auth/health
```

### 9.2 Monitoring Tests
```java
@Scheduled(fixedRate = 60000) // Every minute
public void healthCheck() {
    try {
        // Check system health
        SystemHealth health = monitoringService.checkHealth();
        if (!health.isHealthy()) {
            alertService.sendAlert("System unhealthy: " + health.getDetails());
        }
    } catch (Exception e) {
        log.error("Health check failed", e);
    }
}
```
