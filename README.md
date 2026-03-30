# 🛡️ Nexus Auth Service (Spring Boot 3 + JWT)

Bu proje, modern mikroservis mimarileri için geliştirilmiş, **Stateless (Durumsallıktan Bağımsız)** yapıda çalışan, endüstri standartlarında bir Kimlik Doğrulama ve Yetkilendirme (Authentication & Authorization) servisidir.

## 🚀 Öne Çıkan Özellikler
* **Spring Security 6.x:** En güncel güvenlik filtreleri ve yapılandırması.
* **JWT (JSON Web Token):** Güvenli, ölçeklenebilir ve stateless oturum yönetimi.
* **Role Based Access Control (RBAC):** `USER`, `MODERATOR` ve `ADMIN` rolleriyle esnek yetkilendirme.
* **BCrypt Password Encoding:** Kullanıcı şifreleri veritabanında asla düz metin olarak tutulmaz, BCrypt ile hashlenir.
* **H2 Database Persistence:** Dosya tabanlı (file-based) H2 veritabanı ile uygulama kapansa dahi kullanıcı verileri korunur.

## 🛠️ Kullanılan Teknolojiler
* **Backend:** Java 17, Spring Boot 3.x
* **Security:** Spring Security, JJWT Library
* **Database:** H2 Database, Spring Data JPA
* **Build Tool:** Maven
* **Testing:** Postman

## 📡 API Uç Noktaları (Endpoints)

Aşağıdaki tablo, servisin sunduğu tüm uç noktaları ve erişim yetkilerini listeler:

| Metot | Uç Nokta (Endpoint) | Açıklama | Erişim Yetkisi |
| :--- | :--- | :--- | :--- |
| **POST** | `/api/v1/auth/signup` | Yeni kullanıcı kaydı oluşturur. | Herkese Açık |
| **POST** | `/api/v1/auth/signin` | Giriş yapar ve JWT Token döner. | Herkese Açık |
| **GET** | `/api/v1/test/all` | Genel bilgilendirme içeriği. | Herkese Açık |
| **GET** | `/api/v1/test/user` | Kullanıcı paneli içeriği. | `ROLE_USER`, `ROLE_ADMIN` |
| **GET** | `/api/v1/test/mod` | Moderatör paneli içeriği. | `ROLE_MODERATOR` |
| **GET** | `/api/v1/test/admin` | Yönetici paneli içeriği. | `ROLE_ADMIN` |

## 🏁 Başlangıç ve Kullanım

### 1. Projeyi Çalıştırma
Projeyi klonladıktan sonra ana dizinde şu komutu çalıştırarak uygulamayı ayağa kaldırabilirsiniz:
```bash
./mvnw spring-boot:run
