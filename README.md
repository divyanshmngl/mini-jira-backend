# Mini Jira Service

Spring Boot backend aligned with Axestrack / xswift service patterns.

## Package layout

```
controller/          → thin REST, JsonResponse only
service/             → business logic, @Transactional, throws TaskException
repository/          → JpaRepository + native @Query(nativeQuery = true)
model/               → JPA entities
dto/                 → TaskDto (single DTO), JsonResponse<T>
pojo/                → RequestInfo
customException/     → TaskException(MessageEnum)
enums/               → ApiResponseEnum, MessageEnum, Swimlane, LogMessage
constants/           → AppConstants
utils/               → GlobalExceptionHandler
kafka/               → KafkaConsumerService (@KafkaListener only), KafkaProducerService
client/              → @FeignClient
config/              → KafkaConfig
```

## Response format

All APIs return:

```json
{ "success": true, "message": "Success", "data": { } }
```

### Enum usage

| Enum | When to use |
|------|-------------|
| `ApiResponseEnum.SUCCESS.name()` | Short success label → `"SUCCESS"` |
| `ApiResponseEnum.SAVED.getMessage()` | Full user message → `"Record Saved Successfully"` |
| `MessageEnum.TASK_NOT_FOUND.getMessage()` | Error text in `TaskException` and `GlobalExceptionHandler` |
| `MessageEnum.NOTIFICATIONS_PENDING.getCode()` | Error code in response → `"NOTIFICATIONS_PENDING"` (409) |

Errors are handled by `GlobalExceptionHandler` → `JsonResponse` with `success: false` and `MessageEnum.getMessage()`.

## Auth

Header: `X-User-Id: <userId>` (resolved via `RequestInfoService` like company `RequestInfo` from headers).

## APIs

| Method | Path | Description |
|--------|------|-------------|
| POST | `/createTask` | Create task (TODO) |
| GET | `/getTask?taskId=` | Get one task |
| GET | `/getTasksWithFilters` | Filter by id, assignedTo, createdBy, swimlane, fromTime, toTime |
| GET | `/getVisibleTasks` | Tasks visible to user |
| POST | `/updateTask?taskId=` | Update title, description, assignment, swimlane |

## Kafka

- **Producer** (`KafkaProducerService`) — publishes task create/update/move to `task-events`
- **Consumer** (`KafkaConsumerService`) — only `@KafkaListener`; delegates to `TaskService.processNotificationFromKafka()`

## Database (PostgreSQL)

### Option A — Docker (recommended)

```bash
docker compose up -d
```

Creates DB `mini_jira` on `localhost:5432` (user/password: `postgres` / `postgres`).

### Option B — Local PostgreSQL

1. Install PostgreSQL 14+.
2. Create database:

```sql
CREATE DATABASE mini_jira;
```

3. Point connection in `.env` or environment variables (see `.env.example`).

### Connection settings

| Property | Default |
|----------|---------|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/mini_jira` |
| `SPRING_DATASOURCE_USERNAME` | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | `postgres` |
| `SPRING_PROFILES_ACTIVE` | `dev` |

Dev profile (`application-dev.yml`): `ddl-auto: update`, SQL logging on.  
Prod profile: `ddl-auto: validate` — run `src/main/resources/db/schema.sql` first if needed.

HikariCP pool is configured in `application.yml` (`MiniJiraHikariPool`).

### Verify connection

```bash
.\gradlew.bat bootRun
```

On startup you should see Hikari pool connected and no datasource errors. Table `tasks` is created automatically in dev (`ddl-auto: update`).

## Run

```bash
.\gradlew.bat bootRun
```
