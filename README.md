# cc_crm_server
Сервис для помощи в администрировании показателей счетчиков многоквартирного дома.

Инструкция для запуска сервера сервиса локально:
1. Установить Postgres SQL. Перезаписать настройки входа(имя бд, логин, пароль). Запомнить.
2. Зайти в файл application.properties в папке Resources сервера, и в эти строчки
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=
подставить параметры из 1 пункта.


Инструкция для запуска сервера сервиса на хостинге OpenShift:
1. Завести Openshift аккаунт.
2. Зайти в консоль сервисов.
3. В ней создать сервисы под названием "PotgresSQL"(сохранить параметры для подключения к бд) и "OpenJDK 8". 
4. Зайти в файл application.properties в папке Resources сервера, и в эти строчки
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=
подставить параметры из 3 пункта.

5. В OpenShift консоли зайти в builds/open-jdk8* и перенастроить билд на получение файлов с гитхаба (для настройки ветки и подпапки использовать advanced options)
