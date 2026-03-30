# 🛡️ Nexus Auth Service  
**Spring Boot 3.x + JWT + Refresh Token + Blacklist Mechanism**

Nexus Auth Service; modern, güvenli ve stateless (durumsuz) bir kimlik doğrulama servisidir.  
Standart JWT mekanizmasının ötesine geçerek, **Refresh Token** ve **Logout (Blacklist)** özellikleriyle tam donanımlı bir güvenlik katmanı sunar.

---

## 🚀 Öne Çıkan Özellikler

- **Spring Security 6.x** – Güncel güvenlik filtre zinciri yapılandırması  
- **Stateless JWT Authentication** – Session bağımlılığı olmadan ölçeklenebilir yapı  
- **Refresh Token Sistemi** – Access token süresi dolduğunda tekrar login olmadan oturum yenileme  
- **Blacklist Logout Mekanizması** – Logout sonrası token anında geçersiz hale getirilir  
- **Role-Based Access Control (RBAC)** – `ROLE_USER`, `ROLE_MODERATOR`, `ROLE_ADMIN`  
- **BCrypt Password Hashing** – Güvenli şifre saklama  
- **H2 Persistent Database** – Dosya tabanlı kalıcı veri saklama  

---

## 🛠️ Teknolojiler ve Mimari

| Katman | Teknoloji |
|-------|----------|
| Dil & Framework | Java 17, Spring Boot 3.x |
| Güvenlik | Spring Security, JJWT (io.jsonwebtoken) |
| Veritabanı | H2 Database (File Mode), Spring Data JPA |
| Mimari Pattern | Controller → Service → Repository → Security Filter Chain |
| Araçlar | Maven, Postman, Git |

---

## 📡 API Endpoints

### 🔐 Kimlik Yönetimi

| Method | Endpoint | Açıklama | Yetki |
|--------|---------|----------|-------|
| POST | `/api/v1/auth/signup` | Yeni kullanıcı kaydı oluşturur | Public |
| POST | `/api/v1/auth/signin` | Access & Refresh Token döner | Public |
| POST | `/api/v1/auth/refreshtoken` | Yeni access token üretir | Public |
| POST | `/api/v1/auth/logout` | Token blacklist'e alınır | Authenticated |

---

### 🧪 Test Endpoints

| Method | Endpoint | Açıklama | Gerekli Rol |
|--------|---------|----------|------------|
| GET | `/api/v1/test/user` | Kullanıcı içeriği | ROLE_USER, ROLE_ADMIN |
| GET | `/api/v1/test/mod` | Moderatör içeriği | ROLE_MODERATOR |
| GET | `/api/v1/test/admin` | Admin içeriği | ROLE_ADMIN |

---

## 🏗️ Güvenlik Akışı (Security Workflow)

1. Kullanıcı `/signin` ile giriş yapar  
2. Sunucu:
   - kısa ömürlü **accessToken**
   - uzun ömürlü **refreshToken** üretir  
3. Client, her request’te:
   ```
   Authorization: Bearer <accessToken>
   ```
   gönderir  
4. `AuthTokenFilter`:
   - token doğrular  
   - blacklist kontrolü yapar  
5. Token süresi dolarsa:
   - `/refreshtoken` ile yeni token alınır  
6. Logout durumunda:
   - token blacklist’e eklenir  
   - artık geçersiz olur (401 Unauthorized)  

---

## ⚙️ Kurulum

```bash
# Projeyi klonla
git clone https://github.com/huseyinceykel/nexus-auth-service.git

# Klasöre gir
cd nexus-auth-service

# Uygulamayı başlat
./mvnw spring-boot:run
```

Uygulama:
```
http://localhost:8080
```

---

## 📊 H2 Console

Uygulama çalışırken veritabanını inceleyebilirsiniz:

- URL: http://localhost:8080/h2-console  
- JDBC URL: `jdbc:h2:file:./data/nexusauthdb`  
- Username: `sa`  
- Password: `password`  

---

## 👨‍💻 Geliştirici

**Hüseyin Eren Çeykel**  
Computer Engineer & Backend Developer  

- LinkedIn: www.linkedin.com/in/huseyinerenceykel
            

---

## 📌 Notlar

- Stateless mimari (session yok)  
- Token expiration yapılandırılabilir  
- Refresh token rotation eklenebilir  
- Production için Redis blacklist önerilir  