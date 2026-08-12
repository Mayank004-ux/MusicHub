# 🎵 MusicHub

> **A full-stack music management and discovery web application built with Java, Spring Boot, PostgreSQL, Thymeleaf, HTML, CSS, and JavaScript.**

MusicHub is a full-stack web application designed to help users **manage, organize, search, filter, sort, and explore their personal music collection** through a clean and responsive web interface.

The project demonstrates practical experience with **Java backend development, Spring Boot, REST/MVC architecture, Spring Data JPA, PostgreSQL, server-side rendering with Thymeleaf, frontend development, and Git/GitHub workflows.**

---

## ✨ Features

### 🎵 Music Management

* ➕ Add new songs
* ✏️ Edit existing songs
* 👁️ View song details
* 🗑️ Delete songs
* 💾 Persistent storage using PostgreSQL
* 🖼️ Album artwork support
* 🎼 Genre and music metadata management

### 🔎 Search, Filter & Sort

* 🔍 Search songs by:

  * Title
  * Artist
  * Album
* 🎤 Filter songs by artist
* 🎼 Filter songs by genre
* 🔤 Sort songs by title
* ⭐ Sort songs by rating
* 📅 Sort songs by release year
* 📄 Pagination for large music collections

### ❤️ Personal Music Features

* ❤️ Favorite songs
* 🎶 Create and manage playlists
* ⭐ View top-rated songs
* 🆕 View recently added songs

### 📊 Music Discovery & Analytics

* 📈 Music analytics dashboard
* ⭐ Top-rated music section
* 🎧 Detailed song pages
* 🔗 YouTube and Spotify links
* 🎵 Music-focused browsing experience

### 🎨 User Interface

* 📱 Responsive song-card layout
* 🧭 Navigation bar
* 🖥️ Dashboard-style interface
* 🎨 Custom CSS styling
* ⚡ Interactive frontend functionality using JavaScript

---

# 🏗️ Architecture

MusicHub follows a **layered Spring Boot architecture** that separates presentation, business logic, data access, and database responsibilities.

```text
                    ┌─────────────────────┐
                    │     User / Browser  │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │     Controllers     │
                    │   Spring MVC Layer  │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │      Services       │
                    │   Business Logic    │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │    Repositories     │
                    │ Spring Data JPA     │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │     PostgreSQL      │
                    │      Database       │
                    └─────────────────────┘
```

### 🔄 Request Flow

```text
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
Thymeleaf Template
   ↓
Browser
```

This structure keeps the application modular and makes it easier to maintain, test, debug, and extend.

---

# 📁 Project Structure

```text
MusicHub/
│
├── .mvn/
│
├── src/
│   ├── main/
│   │   │
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── mayank/
│   │   │           └── musichub/
│   │   │               │
│   │   │               ├── controller/
│   │   │               │   ├── HomeController.java
│   │   │               │   └── SongController.java
│   │   │               │
│   │   │               ├── model/
│   │   │               │   └── Song.java
│   │   │               │
│   │   │               ├── repository/
│   │   │               │   └── SongRepository.java
│   │   │               │
│   │   │               ├── service/
│   │   │               │   └── SongService.java
│   │   │               │
│   │   │               └── MusichubApplication.java
│   │   │
│   │   └── resources/
│   │       │
│   │       ├── static/
│   │       │   └── style.css
│   │       │
│   │       ├── templates/
│   │       │   ├── index.html
│   │       │   ├── navbar.html
│   │       │   └── ...
│   │       │
│   │       └── application.properties
│   │
│   └── test/
│
├── .gitignore
├── .gitattributes
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

---

# 🛠️ Tech Stack

## Backend

| Technology          | Purpose                         |
| ------------------- | ------------------------------- |
| ☕ Java              | Core programming language       |
| 🌱 Spring Boot      | Backend application framework   |
| 🌐 Spring MVC       | Web and request handling        |
| 🗃️ Spring Data JPA | Database interaction            |
| 💤 Hibernate        | ORM implementation              |
| 📦 Lombok           | Reducing boilerplate Java code  |
| 🧰 Maven            | Dependency and build management |

## Database

| Technology    | Purpose                                |
| ------------- | -------------------------------------- |
| 🐘 PostgreSQL | Relational database                    |
| 🛠️ pgAdmin   | Database management and administration |

## Frontend

| Technology   | Purpose                       |
| ------------ | ----------------------------- |
| 🌐 HTML5     | Page structure                |
| 🎨 CSS3      | Styling and responsive layout |
| ⚡ JavaScript | Client-side interactions      |
| 🌱 Thymeleaf | Server-side HTML rendering    |

## Development Tools

| Tool                  | Purpose                 |
| --------------------- | ----------------------- |
| 💻 Visual Studio Code | Development environment |
| 📮 Postman            | API testing             |
| 🐙 Git                | Version control         |
| 🐙 GitHub             | Source code hosting     |

---

# 🚀 Getting Started

Follow these steps to run MusicHub locally.

## 1. Clone the Repository

```bash
git clone https://github.com/Mayank004-ux/MusicHub.git
```

Navigate into the project:

```bash
cd MusicHub
```

---

## 2. Create the PostgreSQL Database

Open **pgAdmin** or PostgreSQL and create a database named:

```text
musichub
```

The application expects PostgreSQL to run on:

```text
localhost:5432
```

---

## 3. Configure the Database

Open:

```text
src/main/resources/application.properties
```

Configure your PostgreSQL credentials:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/musichub
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

spring.thymeleaf.cache=false
```

Replace:

```text
YOUR_PASSWORD
```

with your local PostgreSQL password.

> ⚠️ Do not commit real database passwords or other secrets to GitHub.

---

## 4. Run the Application

Using Maven Wrapper:

### Windows

```bash
mvnw.cmd spring-boot:run
```

Or, if Maven is installed globally:

```bash
mvn spring-boot:run
```

---

## 5. Open MusicHub

Once the application starts successfully, open:

```text
http://localhost:8080
```

The MusicHub application should now be running locally.

---

# 🗄️ Database

MusicHub uses **PostgreSQL** as its relational database.

The application uses **Spring Data JPA and Hibernate** to map Java entities to database tables.

Basic data flow:

```text
Java Entity
     ↓
JPA / Hibernate
     ↓
Spring Data Repository
     ↓
PostgreSQL
```

The database schema can be updated automatically during development using:

```properties
spring.jpa.hibernate.ddl-auto=update
```

---

# 🔌 Application Flow

The main application flow is:

```text
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
```

The application separates responsibilities between controllers, services, repositories, and the database instead of placing all application logic inside a single class.

---

# 🧩 Backend Architecture

### Controller Layer

Responsible for handling incoming HTTP requests and returning the appropriate view or response.

Example responsibilities:

```text
GET requests
POST requests
PUT/UPDATE operations
DELETE operations
Search requests
Filtering
Sorting
Pagination
```

### Service Layer

Contains the application's business logic.

```text
Controller
    ↓
SongService
    ↓
SongRepository
```

This prevents business logic from becoming tightly coupled with the controller.

### Repository Layer

The repository layer uses **Spring Data JPA** to communicate with PostgreSQL.

```text
SongRepository
      ↓
Spring Data JPA
      ↓
Hibernate
      ↓
PostgreSQL
```

---

# 🔍 Search & Filtering

MusicHub supports multiple ways of finding music.

Users can search using:

```text
Title
Artist
Album
```

Additional filtering and sorting options include:

```text
Artist
Genre
Title
Rating
Release Year
```

Pagination is also used to prevent displaying a very large number of songs on a single page.

---

# 🎧 Song Details

Each song can have detailed information such as:

* Song title
* Artist
* Album
* Genre
* Rating
* Release year
* Album artwork
* External music links

The detailed song page provides a more focused view of an individual song.

---

# 🔗 Music Links

MusicHub can provide external links for songs, including:

* ▶️ YouTube
* 🎵 Spotify

These links allow users to continue listening through supported external music platforms.

---

# ❤️ Favorites & Playlists

MusicHub is designed to go beyond basic CRUD functionality by supporting personal music organization.

Users can:

* ❤️ Mark songs as favorites
* 🎶 Organize songs into playlists
* ⭐ Identify highly rated songs
* 🆕 Browse recently added songs

---

# 📊 Analytics Dashboard

The project includes a music analytics concept for understanding the user's music collection.

Possible insights include:

```text
Total Songs
Total Artists
Total Albums
Genre Distribution
Average Rating
Top-Rated Songs
Recently Added Songs
```

This turns the application from a simple CRUD project into a more complete music-management system.

---

# 🧪 Testing & API Development

Postman can be used during development to test backend endpoints and verify application behavior.

Typical operations include:

```text
GET     → Retrieve songs
POST    → Add a song
PUT     → Update a song
DELETE  → Delete a song
GET     → Search songs
```

Example search endpoint:

```text
GET /search/songs/{keyword}
```

---

# 📚 What I Learned

This project helped me gain practical experience with:

* ☕ Java
* 🌱 Spring Boot
* 🌐 Spring MVC
* 🗃️ Spring Data JPA
* 💤 Hibernate
* 🐘 PostgreSQL
* 🌱 Thymeleaf
* 🌐 HTML
* 🎨 CSS
* ⚡ JavaScript
* 🔌 REST/MVC concepts
* 🧱 Layered architecture
* 🔎 Search and filtering
* 📄 Pagination
* 🧪 API testing with Postman
* 🐙 Git and GitHub
* 🐞 Debugging full-stack applications
* 🔄 Frontend/backend integration

---

# 🧠 Key Concepts Demonstrated

MusicHub demonstrates practical understanding of:

```text
Client-Server Architecture
        ↓
Spring MVC
        ↓
Layered Architecture
        ↓
Service-Based Business Logic
        ↓
Repository Pattern
        ↓
JPA / Hibernate
        ↓
PostgreSQL
```

The project also demonstrates how a frontend request travels through multiple backend layers before reaching the database and returning a response to the user.

---

# 🔮 Future Improvements

The project can be extended further with:

* 🎵 Built-in music player
* 👤 User authentication and authorization
* 🤖 Personalized music recommendations
* 📊 Advanced listening statistics
* 🌙 Improved dark/light mode
* 🐳 Docker containerization
* 🔄 CI/CD pipeline
* ☁️ Cloud deployment
* 🔐 Improved application security
* 📱 More advanced mobile responsiveness

These improvements can gradually transform MusicHub into a production-oriented music platform.


# 🖥️ Running the Project Locally

Quick version:

```bash
git clone https://github.com/Mayank004-ux/MusicHub.git
cd MusicHub
mvnw.cmd spring-boot:run
```

Then open:

```text
http://localhost:8080
```

Make sure PostgreSQL is running and the `musichub` database exists before starting the application.

---

# 🔐 Security Note

For local development, database credentials can be configured in:

```text
src/main/resources/application.properties
```

However, production deployments should use:

* Environment variables
* Secret management
* Secure database credentials
* HTTPS
* Proper authentication and authorization

Never commit passwords, API keys, tokens, or other secrets to GitHub.

---

# 📌 Project Highlights

> **MusicHub is more than a basic CRUD application.**

The project combines:

```text
Java
+
Spring Boot
+
Spring Data JPA
+
PostgreSQL
+
Thymeleaf
+
HTML/CSS/JavaScript
+
REST/MVC
+
Git/GitHub
```

to create a complete full-stack application for managing and exploring music.

It demonstrates how backend architecture, database design, frontend development, and user-facing features can be combined into one practical project.

---

# 👨‍💻 Author

**Mayank Khare**

B.Tech Computer Science Engineering


## ⭐ If You Like This Project

If you find MusicHub useful or interesting, consider giving the repository a ⭐ on GitHub.

**Repository:**
https://github.com/Mayank004-ux/MusicHub

---

This project is created for **educational, learning, and portfolio purposes**.
