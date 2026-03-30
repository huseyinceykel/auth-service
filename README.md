# 🛡️ Nexus Auth Service  
**Spring Boot 3 + JWT ile Stateless Authentication & Authorization Servisi**

Nexus Auth Service, modern mikroservis mimarilerine uygun olarak geliştirilmiş, stateless (durumsuz) çalışan bir kimlik doğrulama ve yetkilendirme servisidir.  
Spring Security ve JWT tabanlı bu yapı sayesinde, ölçeklenebilir ve güvenli bir authentication mekanizması sunar.

---

## 🚀 Öne Çıkan Özellikler

- Spring Security 6.x – Güncel güvenlik filtre zinciri
- JWT (JSON Web Token) – Stateless authentication
- Role-Based Access Control (RBAC) – ROLE_USER, ROLE_MODERATOR, ROLE_ADMIN
- BCrypt Password Hashing – Şifreler hashlenir, plaintext tutulmaz
- H2 File-Based Database – Kalıcı veri saklama

---

## 🛠️ Kullanılan Teknolojiler

| Katman | Teknoloji |
|-------|----------|
| Backend | Java 17, Spring Boot 3.x |
| Security | Spring Security, JJWT |
| Database | H2 Database, Spring Data JPA |
| Build Tool | Maven |
| Testing | Postman |

---

## 📡 API Endpoints

| Method | Endpoint | Açıklama | Yetki |
|--------|---------|----------|-------|
| POST | /api/v1/auth/signup | Yeni kullanıcı oluşturur | Public |
| POST | /api/v1/auth/signin | Giriş yapar, JWT döner | Public |
| GET  | /api/v1/test/all   | Herkese açık | Public |
| GET  | /api/v1/test/user  | Kullanıcı içeriği | ROLE_USER, ROLE_ADMIN |
| GET  | /api/v1/test/mod   | Moderatör içeriği | ROLE_MODERATOR |
| GET  | /api/v1/test/admin | Admin içeriği | ROLE_ADMIN |

---

## 🏗️ Mimari

Controller → Service → Repository → Database

- Controller: Request/Response yönetimi
- Service: Business logic
- Repository: Data access
- Security: JWT filter + Spring Security

---

## ⚙️ Kurulum ve Çalıştırma

git clone https://github.com/huseyinceykel/nexus-auth-service.git  
cd nexus-auth-service  
./mvnw spring-boot:run  

Uygulama:  
http://localhost:8080  

---

## 🧪 Test (Postman)

### Signup
POST /api/v1/auth/signup

{
  "username": "testuser",
  "email": "test@mail.com",
  "password": "123456"
}

### Signin
POST /api/v1/auth/signin

{
  "username": "testuser",
  "password": "123456"
}

Response:

{
  "accessToken": "JWT_TOKEN",
  "tokenType": "Bearer"
}

### Protected Request

Authorization: Bearer <JWT_TOKEN>

GET /api/v1/test/user

---

## 📊 H2 Console

URL: http://localhost:8080/h2-console  
JDBC: jdbc:h2:file:./data/nexusauthdb  
Username: sa  
Password: password  

---

## 🔐 JWT Flow

1. Kullanıcı login olur  
2. JWT üretilir  
3. Client token saklar  
4. Request ile gönderir  
5. Token doğrulanır  
6. Role kontrolü yapılır  

---

## 📌 Notlar

- Stateless yapı (session yok)
- Token süresi ayarlanabilir
- RBAC genişletilebilir
- Refresh Token eklenebilir

---

## 👨‍💻 Developer

Hüseyin Eren Çeykel  
Backend Developer (Spring Boot & Java)

---
