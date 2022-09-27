1. Создать Spring Boot Web Rest приложение.

2. Технические требования:

                - Java 17

                - пакетный менеджер: Maven

                - база данных: H2

                - миграция схемы: flyway

                - тесты: JUnit 5, Mockito

3. Бизнес требования:

Создание пользователя, изменение пользователя, бан пользователя по ID, получение всех пользователей, получение незабаненных пользователей.

4. Схема данных:

CREATE TABLE users (

    id BIGINT PRIMARY KEY,

    login VARCHAR(25) UNIQUE,

    password VARCHAR(50),

    name VARCHAR(25),

    surname VARCHAR(25),

    patronymic VARCHAR(25),

    is_banned BOOLEAN DEFAULT false

);

5. Обязательные требования к реализации:

                - Наличие двух репозиториев: Jpa (Без использования @Query) и jdbcTemplate

                - Выбор используемого репозитория в сервисе должно определяться через переменную окружения - SELECTED_REPO, которая может принимать одно из двух значений JPA или JDBC.

                - Значение порта (server.port) будет передаваться через переменную окружения SERVER_PORT

                - Наличие Unit тестов



6. Дополнительные требования к реализации:

- Интеграционные тесты сервиса с JDBC репозиторием с использованием TestContainers https://mvnrepository.com/artifact/org.testcontainers/junit-jupiter

- Наличие docker-compose.yml файла запускающее 2 копии приложения на разных портах, первое запускается с использованием JPA репозитория, второе с JDBC.

- Исходный код выложить в публичный git репозиторий (https://github.com или https://bitbucket.org).  Ссылку на репозиторий отправить по электронной почте.