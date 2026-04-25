<p align="center">
  <a href="./src/main/resources/static/images/logo.svg" target="_blank">
    <img src="./src/main/resources/static/images/logo.svg" alt="Modern MD Editor Logo" width="120" />
  </a>
</p>

<h1 align="center">Modern MD Editor (Spring Boot Version)</h1>
<p align="center">Social Platform Friendly Markdown Editor - Spring Boot Web Application</p>

<p align="center">
  <a href="https://spring.io/projects/spring-boot"><img src="https://img.shields.io/badge/Spring%20Boot-4.0.5-6DB33F.svg" alt="Spring Boot" /></a>
  <a href="https://www.oracle.com/java/"><img src="https://img.shields.io/badge/JDK-21-orange.svg" alt="JDK 21" /></a>
  <a href="https://maven.apache.org/"><img src="https://img.shields.io/badge/Maven-3.9-red.svg" alt="Maven 3.9" /></a>
  <a href="https://www.thymeleaf.org/"><img src="https://img.shields.io/badge/Thymeleaf-3.x-005F0F.svg" alt="Thymeleaf" /></a>
  <a href="./LICENSE"><img src="https://img.shields.io/badge/License-MIT-green.svg" alt="MIT License" /></a>
</p>

<p align="center">
  <a href="./README.md">简体中文</a> · <a href="./README.en.md">English</a>
</p>

> A modern Markdown editor designed for ultimate writing experience and "one-click copy to public account format". Beautiful UI, smooth experience, WYSIWYG preview and copy, helping you efficiently create elegant content. Now migrated to Spring Boot Web application with user system, cloud document storage, and version history management.

## Project Overview

- **What is this**: A modern Markdown editor built on Spring Boot 4.0.5, with beautiful design and powerful preview, supporting one-click conversion of Markdown to HTML adapted for WeChat public account/social platforms (automatic inline styles, font/line height/letter spacing adaptation, themed beautification).
- **Why**: When creating articles for public accounts/social platforms, pasting Markdown often loses styles or becomes inconsistent. This project provides "one-click copy" capability to solve the inefficient formatting pain point. Additionally, it adds user system and document management features, supporting cloud storage and version history tracking.
- **What's different**:
  - Beautiful UI and smooth interaction (preview viewport switching, synchronized scrolling, theme preload to prevent flicker).
  - Adjustable "color theme / code style / layout system / font and letter spacing and line height".
  - Compatibility optimization and fallback strategies for social platforms.
  - User system support: registration, login, JWT authentication.
  - Document management: create, edit, delete, version history.

## Tech Stack

### Backend Tech Stack
- **Spring Boot 4.0.5**: Core framework
- **Spring Data JPA**: Data persistence (Hibernate)
- **Spring Security**: Security framework (JWT authentication)
- **Thymeleaf**: Template engine
- **SQLite**: Embedded database
- **Flexmark-java**: Markdown parsing
- **Flying Saucer**: PDF export
- **JWT (jjwt)**: Token authentication

### Frontend Tech Stack
- **Thymeleaf templates**: Page rendering
- **CodeMirror 6**: Editor core (frontend JS integration)
- **Custom CSS**: Style system

## Requirements

- **JDK**: 21 (recommended)
- **Maven**: 3.9+
- **Browser**: Modern browsers (Chrome/Edge/Safari/Firefox latest versions)

## Installation and Running

### Quick Start

```bash
# Clone
git clone https://github.com/yhjhappy234/mdeditor.git
cd mdeditor

# Build
mvn clean package -DskipTests

# Run
java -jar target/mdeditor-1.0.0.jar

# Or run directly with Maven
mvn spring-boot:run
```

Visit `http://localhost:8080` to use the editor.

### Default User

The system automatically creates a default administrator account on startup:
- **Username**: `admin`
- **Password**: `123456`

### Configuration

Main configuration file `src/main/resources/application.yml`:

```yaml
server:
  port: 8080  # Server port

spring:
  datasource:
    url: jdbc:sqlite:classpath:mdeditor.db  # SQLite database
  jpa:
    hibernate:
      ddl-auto: update  # Auto update database

jwt:
  secret: your-secret-key  # JWT secret
  expiration: 86400000  # Token validity (24 hours)
```

## Features

### User System
- User registration/login
- JWT Token authentication
- User settings persistence

### Document Management
- Create, edit, delete documents
- Document version history
- Auto save

### Markdown Editing
- Real-time preview
- WYSIWYG (What You See Is What You Get)
- Synchronized scrolling
- Multi-device viewport switching (desktop/tablet/mobile)

### Theme System
- Color themes (Meihei, GitHub, WeChat Green, VuePress, Cobalt, Marry Red, etc.)
- Code styles (Mac style, GitHub, Atom, VS Code)
- Layout theme system
- Font settings (font family, font size, line height, letter spacing)

### Export Features
- Export PDF (based on Flying Saucer)
- Export image (PNG)

### Internationalization
- Chinese support (zh-CN)
- English support (en)

## API Endpoints

### Authentication
```
POST /api/auth/register  # User registration
POST /api/auth/login     # User login
POST /api/auth/logout    # User logout
```

### Documents
```
GET    /api/documents           # Document list
POST   /api/documents           # Create document
GET    /api/documents/{id}      # Get document
PUT    /api/documents/{id}      # Update document
DELETE /api/documents/{id}      # Delete document
GET    /api/documents/{id}/versions # Version history
```

### Markdown
```
POST /api/markdown/parse    # Markdown parsing
POST /api/markdown/preview  # Generate preview HTML
```

### Export
```
POST /api/export/pdf    # Export PDF
POST /api/export/image  # Export image
```

### Settings
```
GET  /api/settings         # Get user settings
PUT  /api/settings         # Update user settings
GET  /api/themes           # Get theme list
```

## Project Structure

```
mdeditor/
├── pom.xml                          # Maven configuration
├── src/
│   ├── main/
│   │   ├── java/com/yhj/mdeditor/
│   │   │   ├── MdEditorApplication.java   # Main entry
│   │   │   ├── config/             # Configuration classes
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── DataInitializer.java
│   │   │   ├── controller/         # Controllers
│   │   │   ├── service/            # Services
│   │   │   ├── entity/             # Entities
│   │   │   ├── repository/         # Repositories
│   │   │   ├── dto/                # DTOs
│   │   │   ├── util/               # Utilities
│   │   │   ├── exception/          # Exceptions
│   │   │   └── filter/             # Filters
│   │   └── resources/
│   │       ├── templates/          # Thymeleaf templates
│   │       ├── static/             # Static resources
│   │       ├── application.yml     # Application config
│   │       ├── mdeditor.db         # SQLite database
│   │       └── messages/           # i18n messages
│   └── test/                       # Test directory
├── README.md                        # Chinese docs
└── README.en.md                     # English docs
```

## Contributing

- **All forms of contribution are welcome**: Bug fixes, feature proposals, documentation improvements, example additions, etc.
- **Submission process**:
  - Fork this repository, create branch: `feat/xxx` or `fix/xxx`
  - Run and verify locally: `mvn spring-boot:run`, `mvn test`
  - Submit PR with clear description of changes and screenshots/GIF if applicable

## License

- Uses **MIT** license by default.

## Contact and Acknowledgments

- **Source code**: `https://github.com/yhjhappy234/mdeditor`
- **Inspiration and dependencies**: Original Vue project, Spring Boot, Flexmark-java, Flying Saucer and other excellent open source projects.

---

If this project helps you, please Star ⭐️ to support! Also welcome to submit Issue/PR to improve it together.