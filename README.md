# EasyJWT

A lightweight Spring Boot starter for JWT (JSON Web Token) authentication. This library provides a seamless integration of JWT authentication into your Spring Boot 3.x applications with minimal configuration.

## Features

- Simple JWT token generation and validation
- Auto-configuration support for Spring Boot 3.x
- Customizable token expiration, prefix, and header name
- Built-in middleware for JWT authentication
- Flexible path inclusion/exclusion patterns
- Support for custom claims in tokens
- Secure token generation using HMAC-SHA algorithms
- Validation and error handling out of the box

## Installation

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.github.dreamcatcher45</groupId>
    <artifactId>easy-jwt</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Configuration

### Required Properties

Add the following properties to your `application.properties` or `application.yml`:

```yaml
jwt:
  secret: your-base64-encoded-secret-key-min-256-bits
```

### Optional Properties

```yaml
jwt:
  expiration-hours: 24  # Token expiration time in hours (default: 24)
  token-prefix: "Bearer "  # Prefix for the JWT token in Authorization header (default: "Bearer ")
  header-name: "Authorization"  # Name of the header containing the JWT token (default: "Authorization")
```

## Usage

### Basic Token Generation

```java
@Autowired
private JwtUtil jwtUtil;

public String generateUserToken(String userId) {
    return jwtUtil.generateToken(userId);
}
```

### Token Generation with Custom Claims

```java
@Autowired
private JwtUtil jwtUtil;

public String generateUserToken(String userId, String role) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("role", role);
    return jwtUtil.generateToken(userId, claims);
}
```

### Setting up Authentication Middleware

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtAuthMiddleware jwtAuthMiddleware;

    @PostConstruct
    public void init() {
        jwtAuthMiddleware
            .addExcludePatterns("/api/public/**", "/api/auth/**")  // Exclude paths from JWT verification
            .addIncludePatterns("/api/**");  // Include paths for JWT verification
    }
}
```

### Token Validation

```java
@Autowired
private JwtUtil jwtUtil;

public boolean isTokenValid(String token) {
    return jwtUtil.validateToken(token);
}
```

### Extracting Claims from Token

```java
@Autowired
private JwtUtil jwtUtil;

public String getUserIdFromToken(String token) {
    return jwtUtil.getValueFromToken(token);
}

public Claims getAllClaimsFromToken(String token) {
    return jwtUtil.getClaims(token);
}
```

## Security Considerations

1. Always use a strong secret key (minimum 256 bits) encoded in Base64
2. Store your secret key securely (e.g., environment variables, secure configuration service)
3. Set appropriate token expiration times
4. Use HTTPS in production
5. Implement proper token revocation strategy if needed

## Example Secret Key Generation

You can generate a secure Base64-encoded secret key using Java:

```java
String secretKey = Base64.getEncoder().encodeToString(Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded());
```

## Best Practices

1. Always validate tokens on the server side
2. Use short expiration times for sensitive operations
3. Implement refresh token mechanism for long-term authentication
4. Include only necessary claims in the token
5. Handle token errors gracefully

## Error Handling

The middleware automatically handles common JWT errors:
- Missing token (401 Unauthorized)
- Invalid token (401 Unauthorized)
- Expired token (401 Unauthorized)

Error responses are returned in JSON format:
```json
{
    "status": 401,
    "message": "Invalid token"
}
```

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Support

If you encounter any issues or have questions, please create an issue in the GitHub repository.