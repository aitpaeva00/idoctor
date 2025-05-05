
# 🩺 iDoctor Backend API Documentation

## 🔧 Technologies Used

- Spring Boot
- Spring Security
- OAuth2 (Google Login)
- JWT Authentication
- Refresh Tokens
- PostgreSQL / MySQL
- Maven / Gradle

---

## 📌 Core Features

- User Registration & Login (Email & Password)
- OAuth2 Login via Google
- JWT Token Authentication
- Token Refresh
- Secure Logout (Local + Google)
- Doctor & Patient Management *(Coming Soon)*

---

## 🔐 Authentication Endpoints

### 🔹 Registration

**POST** `/api/auth/register`

Registers a new user with email and password.

#### Request Body
```json
{
  "email": "user@idoctor.com",
  "password": "strongpassword"
}
```

#### Response 200 OK
```json
User registered successfully
```

---

### 🔹 Login

**POST** `/api/auth/login`

Authenticates user and returns JWTs.

#### Request Body
```json
{
  "email": "user@idoctor.com",
  "password": "strongpassword"
}
```

#### Response
```json
{
  "accessToken": "jwt-access-token",
  "refreshToken": "jwt-refresh-token"
}
```

---

### 🔹 Refresh Token

**POST** `/api/auth/refresh`

Renews access token using a valid refresh token.

#### Request Body
```json
{
  "refreshToken": "your-refresh-token"
}
```

#### Response
```json
{
  "accessToken": "new-access-token",
  "refreshToken": "new-refresh-token"
}
```

---

### 🔹 OAuth2 Login (Google)

**GET** `/oauth2/authorization/google`

Redirects to Google login. On successful login, redirects to `/oauth2/success` and returns JWT.

#### Spring Configuration
```java
.oauth2Login(oauth2 -> oauth2
    .defaultSuccessUrl("/oauth2/success", false)
    .userInfoEndpoint(userInfo -> userInfo
        .userService(customOAuth2UserService)
    )
)
```

---

## 🔚 Logout

### 🔸 Logout from Application

**POST** `/logout`

Logs the user out from the session and optionally from Google.

#### Spring Configuration
```java
.logout(logout -> logout
    .logoutUrl("/logout")
    .logoutSuccessUrl("/login?logout")
    .invalidateHttpSession(true)
    .clearAuthentication(true)
    .addLogoutHandler((request, response, authentication) -> {
        response.sendRedirect("https://accounts.google.com/Logout");
    })
)
```

---

## 🧪 Testing via Postman

1. **Login** → `POST /api/auth/login`
2. **Get JWTs** → Save access & refresh token
3. **Access Secured Route** → Use `Authorization: Bearer <accessToken>`
4. **Refresh Token** → `POST /api/auth/refresh`
5. **Logout** → `POST /logout`

---

## 📦 Future Modules

- Doctor Profiles and Availability
- Patient Appointment Booking
- Role-Based Access (Admin, Doctor, Patient)
- Notifications and Reminders

---


---

## 🧪 How to Test iDoctor API Using Postman

### 🔸 1. Register a New User

- **Method**: POST
- **URL**: `http://localhost:8080/api/auth/register`
- **Headers**:  
  `Content-Type: application/json`
- **Body**:
```json
{
  "email": "testuser@example.com",
  "password": "testpassword"
}
```
- **Expected Response**:
```json
  User registered successfully
```

---

### 🔸 2. Login with Registered User

- **Method**: POST
- **URL**: `http://localhost:8080/api/auth/login`
- **Headers**:  
  `Content-Type: application/json`
- **Body**:
```json
{
  "email": "testuser@example.com",
  "password": "testpassword"
}
```
- **Expected Response**:
```json
{
  "accessToken": "access-token",
  "refreshToken": "refresh-token"
}
```

---

### 🔸 3. Refresh Access Token

- **Method**: POST
- **URL**: `http://localhost:8080/api/auth/refresh`
- **Headers**:  
  `Content-Type: application/json`
- **Body**:
```json
{
  "refreshToken": "your-refresh-token"
}
```
- **Expected Response**:
```json
{
  "accessToken": "new-access-token",
  "refreshToken": "new-refresh-token"
}
```

---

### 🔸 4. OAuth2 Login with Google

- **Method**: GET
- **URL**: `http://localhost:8080/oauth2/authorization/google`
- Follow the redirect to Google login page. Upon success, you'll be redirected to `/oauth2/success`.

---

### 🔸 5. Logout

- **Method**: POST
- **URL**: `http://localhost:8080/logout`
- **Expected Behavior**: Logs the user out and redirects to `/login?logout` or logs out of Google.

---