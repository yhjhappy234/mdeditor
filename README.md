<p align="center">
  <a href="./src/main/resources/static/images/logo.svg" target="_blank">
    <img src="./src/main/resources/static/images/logo.svg" alt="Modern MD Editor Logo" width="120" />
  </a>
</p>

<h1 align="center">Modern MD Editor (Spring Boot Version)</h1>
<p align="center">社交平台友好型 Markdown 编辑器 - Spring Boot Web 应用版本</p>

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

> 一款专为极致书写体验与「一键复制为公众号格式」而生的现代化 Markdown 编辑器。界面精致、体验顺滑，所见即所得地预览与复制，帮助你高效创作优雅内容。现已迁移为 Spring Boot Web 应用，支持用户系统、文档云端保存、历史版本管理。

## 项目简介

- **这是什么**：现代化的 Markdown 编辑器，基于 Spring Boot 4.0.5 构建，内置美学设计与强大预览，支持将 Markdown 一键转换为适配微信公众号/社交平台的 HTML（自动内联样式、字体/行高/字距适配、主题化美化）。
- **为什么做**：创作公众号/社交平台文章时，粘贴 Markdown 常丢样式、不统一。此项目提供「一键复制」能力，解决格式调整的低效痛点。同时增加了用户系统和文档管理功能，支持云端保存和历史版本追溯。
- **有什么不同**：
  - 精致 UI 与顺滑交互（预览视口切换、同步滚动、主题预加载防闪烁）。
  - 可调「颜色主题 / 代码样式 / 排版系统 / 字体与字距与行高」。
  - 针对社交平台的粘贴兼容性优化与降级策略。
  - 用户系统支持：注册、登录、JWT认证。
  - 文档管理：创建、编辑、删除、版本历史。

## 技术栈

### 后端技术栈
- **Spring Boot 4.0.5**：核心框架
- **Spring Data JPA**：数据持久化 (Hibernate)
- **Spring Security**：安全框架 (JWT认证)
- **Thymeleaf**：模板引擎
- **SQLite**：嵌入式数据库
- **Flexmark-java**：Markdown 解析
- **Flying Saucer**：PDF 导出
- **JWT (jjwt)**：Token认证

### 前端技术栈
- **Thymeleaf模板**：页面渲染
- **CodeMirror 6**：编辑器核心 (前端JS集成)
- **自定义CSS**：样式系统

## 环境要求

- **JDK**：21 (推荐)
- **Maven**：3.9+
- **浏览器**：现代浏览器（Chrome/Edge/Safari/Firefox 最新版本）

## 安装与运行

### 快速开始

```bash
# 克隆
git clone https://github.com/yhjhappy234/mdeditor.git
cd mdeditor

# 编译打包
mvn clean package -DskipTests

# 运行
java -jar target/mdeditor-1.0.0.jar

# 或者使用 Maven 直接运行
mvn spring-boot:run
```

访问 `http://localhost:8080` 即可使用编辑器。

### 默认用户

系统启动时会自动创建默认管理员账户：
- **用户名**: `admin`
- **密码**: `123456`

### 配置说明

主要配置文件 `src/main/resources/application.yml`：

```yaml
server:
  port: 8080  # 服务端口

spring:
  datasource:
    url: jdbc:sqlite:classpath:mdeditor.db  # SQLite 数据库
  jpa:
    hibernate:
      ddl-auto: update  # 数据库自动更新

jwt:
  secret: your-secret-key  # JWT 密钥
  expiration: 86400000  # Token 有效期 (24小时)
```

## 功能特性

### 用户系统
- 用户注册/登录
- JWT Token认证
- 用户设置持久化

### 文档管理
- 创建、编辑、删除文档
- 文档版本历史
- 自动保存

### Markdown 编辑
- 实时预览
- 所见即所得 (WYSIWYG)
- 同步滚动
- 多端视口切换（桌面/平板/手机）

### 主题系统
- 颜色主题（煤黑、GitHub、微信绿、VuePress、钴蓝、喜庆红等）
- 代码样式（Mac风格、GitHub、Atom、VS Code）
- 排版主题系统
- 字体设置（字体族、字号、行高、字距）

### 导出功能
- 导出 PDF (基于 Flying Saucer)
- 导出图片 (PNG)

### 国际化
- 支持中文 (zh-CN)
- 支持英文 (en)

## API 接口

### 认证接口
```
POST /api/auth/register  # 用户注册
POST /api/auth/login     # 用户登录
POST /api/auth/logout    # 用户登出
```

### 文档接口
```
GET    /api/documents           # 文档列表
POST   /api/documents           # 创建文档
GET    /api/documents/{id}      # 获取文档
PUT    /api/documents/{id}      # 更新文档
DELETE /api/documents/{id}      # 删除文档
GET    /api/documents/{id}/versions # 版本历史
```

### Markdown 接口
```
POST /api/markdown/parse    # Markdown 解析
POST /api/markdown/preview  # 生成预览 HTML
```

### 导出接口
```
POST /api/export/pdf    # 导出 PDF
POST /api/export/image  # 导出图片
```

### 设置接口
```
GET  /api/settings         # 获取用户设置
PUT  /api/settings         # 更新用户设置
GET  /api/themes           # 获取主题列表
```

## 项目结构

```
mdeditor/
├── pom.xml                          # Maven 配置
├── src/
│   ├── main/
│   │   ├── java/com/yhj/mdeditor/
│   │   │   ├── MdEditorApplication.java   # 主启动类
│   │   │   ├── config/             # 配置类
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── DataInitializer.java
│   │   │   ├── controller/         # 控制器
│   │   │   ├── service/            # 服务层
│   │   │   ├── entity/             # 实体类
│   │   │   ├── repository/         # 数据访问层
│   │   │   ├── dto/                # 数据传输对象
│   │   │   ├── util/               # 工具类
│   │   │   ├── exception/          # 异常处理
│   │   │   └── filter/             # 过滤器
│   │   └── resources/
│   │       ├── templates/          # Thymeleaf 模板
│   │       ├── static/             # 静态资源
│   │       ├── application.yml     # 应用配置
│   │       ├── mdeditor.db         # SQLite 数据库
│   │       └── messages/           # 国际化消息
│   └── test/                       # 测试目录
├── README.md                        # 中文说明文档
└── README.en.md                     # 英文说明文档
```

## 贡献指南

- **欢迎一切形式的贡献**：Bug 修复、特性提议、文档完善、示例补充等。
- **提交流程**：
  - Fork 本仓库，创建分支：`feat/xxx` 或 `fix/xxx`
  - 本地运行与验证：`mvn spring-boot:run`、`mvn test`
  - 提交 PR，清晰描述变更动机与效果截图/GIF

## 许可证

- 默认采用 **MIT** 许可证。

## 联系与致谢

- **源码**：`https://github.com/yhjhappy234/mdeditor`
- **灵感与依赖**：原 Vue 版本项目、Spring Boot、Flexmark-java、Flying Saucer 等优秀开源项目。

---

如果这个项目对你有帮助，欢迎 Star ⭐️ 支持！也欢迎提交 Issue/PR 一起把它打磨得更好。