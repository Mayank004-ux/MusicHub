# 🎵 MusicHub

MusicHub is a full-stack music management web application built with **Java, Spring Boot, PostgreSQL, Thymeleaf, HTML, CSS, and JavaScript**.

It allows users to manage, organize, search, filter, and explore their personal music collection.

---

## 🚀 Features

- 🎵 Add, edit, view and delete songs
- 🔎 Search songs by title, artist, or album
- 📄 Pagination
- 🎤 Filter by artist
- 🎼 Filter by genre
- 🔤 Sort by title
- ⭐ Sort by rating
- 📅 Sort by release year
- ❤️ Favorite songs
- 🎵 Create and manage playlists
- ⭐ Top-rated songs
- 📊 Music analytics dashboard
- 🆕 Recently added songs
- 🎧 Detailed song pages
- 🔗 YouTube and Spotify links
- 📱 Responsive song-card layout

---

## 🏗️ Architecture

MusicHub follows a **layered Spring Boot architecture**:

```text
                ┌──────────────────┐
                │   User / Browser │
                └────────┬─────────┘
                         │
                         ▼
                ┌──────────────────┐
                │   Controllers    │
                └────────┬─────────┘
                         │
                         ▼
                ┌──────────────────┐
                │     Services     │
                │  Business Logic  │
                └────────┬─────────┘
                         │
                         ▼
                ┌──────────────────┐
                │   Repositories   │
                │   Spring Data   │
                │       JPA        │
                └────────┬─────────┘
                         │
                         ▼
                ┌──────────────────┐
                │    PostgreSQL    │
                └──────────────────┘

```
## Request Flow
Browser
   ↓
Controller
   ↓
Service
   ↓
Repository
   ↓
PostgreSQL
   ↓
Repository
   ↓
Service
   ↓
Controller
   ↓
Thymeleaf
   ↓
Browser

## 📂 Project Structure
musichub/
│
├── src/main/java/com/mayank/musichub/
│   ├── controller/
│   ├── model/
│   ├── repository/
│   └── service/
│
├── src/main/resources/
│   ├── static/
│   │   └── style.css
│   ├── templates/
│   │   ├── index.html
│   │   ├── navbar.html
│   │   └── ...
│   └── application.properties
│
├── pom.xml
├── .gitignore
└── mvnw

## 🛠️ Tech Stack
# Backend
Java
Spring Boot
Spring MVC
Spring Data JPA
Hibernate
Lombok
Maven
# Database
PostgreSQL
pgAdmin
# Frontend
Thymeleaf
HTML5
CSS3
JavaScript
# Tools
Visual Studio Code
Postman
Git
GitHub

## ⚙️ How to Run

# 1. Clone the repository
git clone https://github.com/Mayank004-ux/MusicHub
cd musichub

# 2. Create PostgreSQL database

Create a database named: musichub
cd musichub

# 3. Configure application.properties

spring.datasource.url=jdbc:postgresql://localhost:5432/musichub
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD

# 4. Run the application
mvn spring-boot:run
Open: http://localhost:8080

## 📊 Main Application Flow

Add Song
   ↓
PostgreSQL
   ↓
Home Dashboard
   ↓
Search / Filter / Sort
   ↓
Song Details
   ↓
Favorites / Playlists
   ↓
Analytics

## 🔮 Future Improvements

🎵 Built-in music player
👤 User authentication
🤖 Music recommendations
📊 Advanced listening statistics
🌙 Improved dark/light mode
🐳 Docker
🔄 CI/CD
☁️ Cloud deployment

## 🎯 Learning Outcomes

This project demonstrates practical experience with:

Java & Spring Boot
Layered architecture
REST/MVC concepts
PostgreSQL & JPA/Hibernate
CRUD operations
Searching and pagination
Filtering and sorting
Frontend/backend integration
Git/GitHub
Debugging and responsive UI development

## 👨‍💻 Author

Mayank Khare
