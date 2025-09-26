# 🃏 Blackjack API

A **Java Spring Boot** API to manage a Blackjack game.  
The application integrates with **two databases**:  
- **MongoDB** (reactive) → stores game sessions.  
- **MySQL** (R2DBC) → manages players.  

It includes game logic, REST endpoints, global exception handling, Swagger documentation, unit tests, and full Dockerization.

---

## 🚀 Technologies

- **Java 17**  
- **Spring Boot 3**  
- **Spring WebFlux (Reactive)**  
- **Spring Data R2DBC (MySQL)**  
- **Spring Data Reactive MongoDB**  
- **Lombok**  
- **JUnit 5 & Mockito**  
- **Swagger / OpenAPI 3**  
- **Docker & Docker Hub**

---

## 📖 Main Endpoints

### 🎮 Game
- **POST** `/games` → Create a new game  
- **GET** `/games/{id}` → Get game details  
- **POST** `/games/{id}/play?move=HIT|STAND` → Make a move  
- **DELETE** `/games/{id}` → Delete a game  

### 🧑 Player
- **POST** `/player` → Create a player  
- **PUT** `/player/{id}?name=NewName` → Update player name  
- **GET** `/player/ranking` → View player ranking  

### ❤️ Health Check
- **GET** `/` → API health check
- 
---

## ✅ Features Implemented

- Reactive API with **Spring WebFlux**  
- Global exception handling (**GlobalExceptionHandler**)  
- Integration with **MySQL (R2DBC)** and **MongoDB (Reactive)**  
- Data validation  
- Swagger auto-generated documentation  
- Unit testing with **JUnit & Mockito**  
- **Dockerfile** + Docker Hub deployment  
