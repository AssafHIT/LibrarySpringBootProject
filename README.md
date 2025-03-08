# Harry Potter Library Spring Boot Project

## 📌 Project Overview
This is a **Library Management System** built with **Spring Boot** that provides a RESTful API for managing Harry Potter books. Users can search for books, filter them based on price range, and retrieve book details from an external API.

## 🛠 Tech Stack
- **Backend**: Spring Boot, Java
- **Database**: H2 / MySQL
- **Data Access**: Spring Data JPA, Hibernate
- **API Calls**: RestTemplate / WebClient
- **Dependency Management**: Maven
- **DTOs & Response Handling**: ModelMapper, HATEOAS

## 📁 Project Structure
```
📦 LibrarySpringBootProject
 ┣ 📂 src/main/java/com/example/library
 ┃ ┣ 📂 controller        # Handles API requests
 ┃ ┣ 📂 service           # Business logic layer
 ┃ ┣ 📂 repository        # Data access layer (Spring Data JPA)
 ┃ ┣ 📂 model             # Entity classes
 ┃ ┣ 📂 dto               # Data Transfer Objects
 ┃ ┣ 📂 configuration     # App config (WeatherConfig, etc.)
 ┣ 📜 pom.xml             # Maven dependencies
 ┣ 📜 application.properties # Config settings (DB, API keys)
```

## 🌟 Features
✅ Fetch books from an external API using asynchronous calls  
✅ Search and filter books by title, author, and price range  
✅ REST API with HATEOAS for easy navigation  
✅ Structured DTO responses for clean data representation  

## 🚀 API Endpoints
### 📚 Get All Books
```http
GET /api/books
```
_Response:_
```json
[
  {
    "id": 1,
    "title": "Spring Boot in Action",
    "price": 29.99,
    "_links": { "self": { "href": "http://localhost:8080/api/books/1" } }
  }
]
```

### 🔍 Search Books by Title
```http
GET /api/books?title=Spring
```

### 💰 Filter Books by Price Range
```http
GET /api/books?minPrice=20&maxPrice=50
```

### 🌐 Fetch Books from External API (Async)
```http
GET /api/books/external
```

## 🔧 Setup & Installation
### Prerequisites
- Java 17+
- Maven
- (Optional) MySQL setup for persistence

### Run the Project
1. Clone the repository:
   ```sh
   git clone https://github.com/AssafHIT/LibrarySpringBootProject.git
   cd LibrarySpringBootProject
   ```
2. Build and run the application:
   ```sh
   mvn spring-boot:run
   ```
3. Access the API at: `http://localhost:8080/api/books`

## 🛠 Future Enhancements
- Implement **unit tests** using JUnit and Mockito
- Add **Spring Security** for authentication
- Develop a **frontend UI** using React or Angular

---
