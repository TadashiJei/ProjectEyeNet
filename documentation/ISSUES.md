# Compilation Issues and Tasks

## Current Compilation Issues
- [ ] Fix 969 compilation issues found in the Problems view
  - [x] Fix import statements and missing dependencies in AnalyticsEngineService
  - [x] Fix type mismatches in DepartmentDashboardController
  - [x] Fix test class DepartmentDashboardServiceTest
  - [x] Fix test class UserMapperTest
  - [x] Fix test class UserProfileServiceTest
  - [x] Fix test class NetworkDeviceMapperTest
  - [x] Fix test class FlowRuleMapperTest
  - [x] Fix test class OpenFlowMessageHandlerTest
  - [x] Create test class NetworkDeviceRepositoryTest
  - [x] Create test class FlowRuleRepositoryTest
  - [x] Create test class UserProfileRepositoryTest
  - [x] Create test class PerformanceMetricsRepositoryTest
  - [x] Create test class AlertRepositoryTest
  - [ ] Review and fix remaining compilation errors
  - [ ] Address missing or incorrect annotations
  - [ ] Fix method signature mismatches

## Repository Naming Issues
- [x] Rename repository files to match their interface names:
  - [x] `AnalyticsReportRepository.java` → `AnalyticsReportDocumentRepository.java`
  - [x] `WebsiteAccessLogRepository.java` → `WebsiteAccessLogDocumentRepository.java`
  - [x] `SecurityMetricsRepository.java` → `SecurityMetricsDocumentRepository.java`
  - [x] `TrafficAnalyticsRepository.java` → `TrafficAnalyticsDocumentRepository.java`
  - [x] `NetworkMetricsRepository.java` → `NetworkMetricsDocumentRepository.java`
  - [x] `AlertRepository.java` → `AlertDocumentRepository.java`
  - [x] `DepartmentAnalyticsRepository.java` → `DepartmentAnalyticsDocumentRepository.java`
  - [x] `UserNetworkUsageRepository.java` → `UserNetworkUsageDocumentRepository.java`

## Missing Document Classes
- [x] Create missing document classes:
  - [x] `WebsiteAccessLogDocument.java`
  - [x] `ReportConfigDocument.java` (updated with @Builder)
  - [x] `ReportScheduleConfigDocument.java` (updated with @Builder)

## Model Class Issues
- [x] Add missing methods/annotations to `AnalyticsReportDocument`:
  - [x] Add `@Builder` annotation
  - [x] Add getters/setters for all fields (via @Data)

- [x] Add missing methods/annotations to `ReportConfigDocument`:
  - [x] Add getters for `type`, `departmentId`, `requestedBy`, `parameters`

- [x] Add missing methods/annotations to `ReportScheduleDocument`:
  - [x] Add `@Builder` annotation
  - [x] Add getters for `name`, `description`, `cronExpression`, `reportType`, `departmentId`

## Service Layer Issues
- [x] Fix `DepartmentDashboardService`:
  - [x] Import missing `java.util.Map`
  - [x] Fix return types in methods to use Document suffix

- [x] Fix `SystemConfigurationService`:
  - [x] Add missing methods to `SystemConfiguration` entity (converted to Document)
  - [x] Add builder pattern to `SystemLog` entity (converted to Document)

- [x] Fix `AlertService`:
  - [x] Add missing methods to `Alert` entity
  - [x] Add missing methods to `AlertRule` entity

- [x] Fix `AccessControlService`:
  - [x] Fix type mismatches between entity and document classes
  - [x] Update method signatures to use correct types

- [x] Fix `UserProfileService`:
  - [x] Add missing methods to `UserProfileUpdateRequest` DTO
  - [x] Fix type mismatches in mapper methods

## Entity to Document Migration
- [ ] Convert remaining entity classes to document classes:
  - [ ] `Alert` → `AlertDocument`
  - [ ] `AlertRule` → `AlertRuleDocument`
  - [x] `SystemConfiguration` → `SystemConfigurationDocument`
  - [x] `SystemLog` → `SystemLogDocument`
  - [ ] `User` → `UserDocument`
  - [ ] `UserActivity` → `UserActivityDocument`

## Cleanup Tasks
- [ ] Remove duplicate non-Document files:
  - [ ] `DepartmentAnalytics.java`
  - [ ] `FlowRule.java`
  - [ ] `NetworkUsage.java`
  - [ ] `PerformanceMetrics.java`
  - [ ] `ReportConfig.java`
  - [ ] `ReportSchedule.java`
  - [ ] `ReportScheduleConfig.java`
  - [ ] `SecurityMetrics.java`
  - [ ] `TrafficAnalytics.java`
  - [ ] `WebsiteAccessLog.java`

## General Tasks
- [x] Update all repository interfaces to use Document suffix consistently
- [x] Update all service classes to use Document classes instead of entities
- [x] Add Lombok annotations (@Data, @Builder, etc.) to all Document classes
- [x] Update all DTOs to match the new Document structure
- [x] Fix all type mismatches between Documents and DTOs

## Testing
- [x] Update test classes to use Document classes
- [x] Update mapper test classes to use Document and DTO classes
- [x] Update service test classes to use Document classes
- [x] Update SDN test classes to use Document classes
- [x] Create repository test classes with MongoDB integration tests
  - [x] NetworkDeviceRepositoryTest
  - [x] FlowRuleRepositoryTest
  - [x] UserProfileRepositoryTest
  - [x] PerformanceMetricsRepositoryTest
  - [x] AlertRepositoryTest
  - [ ] Add remaining repository tests
- [ ] Create unit tests for all Document classes
- [ ] Create service layer tests with Document classes
- [ ] Verify all CRUD operations work with Document classes

## Documentation
- [ ] Update API documentation to reflect Document class changes
- [ ] Update technical documentation with new MongoDB schema
- [ ] Document the entity to document migration process
- [ ] Update development guide with MongoDB best practices

## Notes
- Priority should be given to fixing compilation errors first
- After compilation is fixed, focus on testing the basic CRUD operations
- Consider creating a migration script for existing data
- Document all breaking changes in the API

## Future Issues to Watch
1. Type Consistency:
   - Ensure all Document classes use consistent types (e.g., UUID vs String for IDs)
   - Check for proper type conversion in service layer methods
   - Verify DTO mappings handle type conversions correctly

2. MongoDB Indexing:
   - Add appropriate indexes for frequently queried fields
   - Consider compound indexes for complex queries
   - Monitor query performance after migration

3. Data Migration:
   - Plan data migration strategy for existing data
   - Create backup before migration
   - Test migration scripts thoroughly
   - Consider downtime requirements

4. API Compatibility:
   - Check all REST endpoints for breaking changes
   - Update API documentation
   - Consider versioning strategy
   - Plan client-side updates

5. Performance Monitoring:
   - Set up monitoring for MongoDB operations
   - Track query performance
   - Monitor memory usage
   - Set up alerts for slow queries

6. Security Considerations:
   - Review MongoDB security settings
   - Check field-level encryption needs
   - Update access control policies
   - Audit sensitive data access

7. Testing Coverage:
   - Unit tests for all Document classes
   - Integration tests for repositories
   - End-to-end tests for critical flows
   - Performance tests for complex queries
