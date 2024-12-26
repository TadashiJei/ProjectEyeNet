# Compilation Issues and Tasks

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
- [ ] Fix `DepartmentDashboardService`:
  - [ ] Import missing `java.util.Map`
  - [ ] Fix return types in methods to use Document suffix

- [ ] Fix `SystemConfigurationService`:
  - [ ] Add missing methods to `SystemConfiguration` entity
  - [ ] Add builder pattern to `SystemLog` entity

- [ ] Fix `AlertService`:
  - [ ] Add missing methods to `Alert` entity
  - [ ] Add missing methods to `AlertRule` entity

- [ ] Fix `AccessControlService`:
  - [ ] Fix type mismatches between entity and document classes
  - [ ] Update method signatures to use correct types

- [ ] Fix `UserProfileService`:
  - [ ] Add missing methods to `UserProfileUpdateRequest` DTO
  - [ ] Fix type mismatches in mapper methods

## Entity to Document Migration
- [ ] Convert remaining entity classes to document classes:
  - [ ] `Alert` → `AlertDocument`
  - [ ] `AlertRule` → `AlertRuleDocument`
  - [ ] `SystemConfiguration` → `SystemConfigurationDocument`
  - [ ] `SystemLog` → `SystemLogDocument`
  - [ ] `User` → `UserDocument`
  - [ ] `UserActivity` → `UserActivityDocument`

## General Tasks
- [ ] Update all repository interfaces to use Document suffix consistently
- [ ] Update all service classes to use Document classes instead of entities
- [ ] Add Lombok annotations (@Data, @Builder, etc.) to all Document classes
- [ ] Update all DTOs to match the new Document structure
- [ ] Fix all type mismatches between Documents and DTOs

## Testing
- [ ] Create unit tests for all Document classes
- [ ] Create integration tests for repository interfaces
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
