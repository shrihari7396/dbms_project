# CarBook Backend - Spring Boot API

A comprehensive Spring Boot REST API for the CarBook car rental platform.

## Features

- **JWT Authentication** with role-based access control
- **User Management** (Admin, Owner, Customer roles)
- **Car Management** with status tracking (Free, Booked, Defective)
- **Booking System** with date validation and cost calculation
- **Review System** with rating and comments
- **RESTful API** with proper HTTP status codes
- **Data Validation** with comprehensive error handling
- **CORS Support** for frontend integration

## Tech Stack

- **Java 17**
- **Spring Boot 3.5.6**
- **Spring Security** with JWT
- **Spring Data JPA** with Hibernate
- **MySQL Database**
- **Maven** for dependency management
- **Lombok** for reducing boilerplate code

## API Endpoints

### Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login
- `GET /api/auth/me` - Get current user

### Cars
- `GET /api/cars` - List all cars (with filtering)
- `GET /api/cars/{id}` - Get car by ID
- `POST /api/cars` - Create new car (Owner/Admin)
- `PUT /api/cars/{id}` - Update car (Owner/Admin)
- `DELETE /api/cars/{id}` - Delete car (Owner/Admin)

### Reviews
- `GET /api/cars/{carId}/reviews` - Get car reviews
- `POST /api/cars/{carId}/reviews` - Create review (Customer)

### Bookings
- `POST /api/bookings` - Create booking (Customer)
- `GET /api/bookings` - List bookings (with filtering)

## Database Schema

### Users Table
- `id` - Primary key
- `name` - User full name
- `email` - Unique email address
- `password` - Encrypted password
- `phone` - Phone number
- `role` - Enum (CUSTOMER, OWNER, ADMIN)
- `created_at`, `updated_at` - Timestamps

### Cars Table
- `id` - Primary key
- `owner_id` - Foreign key to users
- `make` - Car manufacturer
- `model` - Car model
- `year` - Manufacturing year
- `color` - Car color
- `price` - Price per day
- `status` - Enum (FREE, BOOKED, DEFECTIVE)
- `image` - Image URL
- `description` - Car description
- `mileage` - Car mileage
- `created_at`, `updated_at` - Timestamps

### Bookings Table
- `id` - Primary key
- `customer_id` - Foreign key to users
- `car_id` - Foreign key to cars
- `start_date` - Booking start date
- `end_date` - Booking end date
- `total_cost` - Calculated total cost
- `status` - Enum (CONFIRMED, CANCELLED, COMPLETED)
- `created_at`, `updated_at` - Timestamps

### Reviews Table
- `id` - Primary key
- `car_id` - Foreign key to cars
- `customer_id` - Foreign key to users
- `rating` - Rating (1-5)
- `comment` - Review comment
- `created_at`, `updated_at` - Timestamps

## Setup Instructions

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- IDE (IntelliJ IDEA, Eclipse, VS Code)

### Database Setup
1. Create MySQL database:
```sql
CREATE DATABASE dbmsProject;
```

2. Update database credentials in `application.properties`:
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Running the Application

1. **Clone and navigate to backend directory:**
```bash
cd Backend
```

2. **Install dependencies:**
```bash
mvn clean install
```

3. **Run the application:**
```bash
mvn spring-boot:run
```

4. **Access the API:**
- Base URL: `http://localhost:8081/api`
- Swagger UI: `http://localhost:8081/swagger-ui.html`

### Demo Users

The application automatically seeds demo users:

- **Admin**: `admin@example.com` / `admin123`
- **Owner**: `owner@example.com` / `owner123`
- **Customer**: `customer@example.com` / `customer123`

## API Usage Examples

### Register User
```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password123",
    "phone": "1234567890",
    "role": "CUSTOMER"
  }'
```

### Login User
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "customer@example.com",
    "password": "customer123"
  }'
```

### Get All Cars
```bash
curl -X GET http://localhost:8081/api/cars
```

### Create Car (with JWT token)
```bash
curl -X POST http://localhost:8081/api/cars \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "make": "Toyota",
    "model": "Camry",
    "year": 2022,
    "color": "Silver",
    "price": 50.0,
    "image": "https://example.com/car.jpg",
    "description": "Comfortable sedan",
    "features": ["AC", "Bluetooth"],
    "mileage": 15000
  }'
```

## Security Features

- **JWT Authentication** with 24-hour expiration
- **Role-based Authorization** (CUSTOMER, OWNER, ADMIN)
- **Password Encryption** using BCrypt
- **CORS Configuration** for frontend integration
- **Input Validation** with comprehensive error messages

## Error Handling

The API returns consistent error responses:

```json
{
  "message": "Error description",
  "field": "Specific field error (if applicable)"
}
```

Common HTTP status codes:
- `200` - Success
- `201` - Created
- `400` - Bad Request
- `401` - Unauthorized
- `403` - Forbidden
- `404` - Not Found
- `500` - Internal Server Error

## Development

### Project Structure
```
src/main/java/edu/pict/dbmsbackend/
├── config/          # Configuration classes
├── controller/      # REST controllers
├── dto/            # Data Transfer Objects
├── exception/      # Exception handling
├── model/          # JPA entities
├── repository/     # Data repositories
├── security/       # Security configuration
├── service/        # Business logic
└── util/           # Utility classes
```

### Adding New Features

1. **Create Entity** in `model/` package
2. **Create Repository** in `repository/` package
3. **Create Service** in `service/` package
4. **Create Controller** in `controller/` package
5. **Add Security Rules** in `SecurityConfig.java`
6. **Create DTOs** in `dto/` package

## Testing

Run tests with:
```bash
mvn test
```

## Deployment

### Build JAR file:
```bash
mvn clean package
```

### Run JAR file:
```bash
java -jar target/dbmsBackend-0.0.1-SNAPSHOT.jar
```

## Frontend Integration

The backend is configured to work with the React frontend:
- CORS enabled for `http://localhost:5173`
- API endpoints match frontend expectations
- JWT token authentication
- Consistent JSON response format

## Contributing

1. Fork the repository
2. Create feature branch
3. Commit changes
4. Push to branch
5. Create Pull Request

## License

This project is licensed under the MIT License.

## Support

For support or questions, please contact the development team or create an issue in the repository.
