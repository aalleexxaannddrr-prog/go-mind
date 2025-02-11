# GoMind 🧠💡

![GoMind Banner](https://via.placeholder.com/1920x300.png?text=GoMind+Banner+-+A+Smart+Advertising+Platform)

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.0-green)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)](https://www.mysql.com/)
[![Docker](https://img.shields.io/badge/Docker-24.0.2-blue)](https://www.docker.com/)

**Интеллектуальная платформа для интерактивных рекламных аукционов и образовательных викторин**

---

## Содержание
- [О проекте](#о-проекте)
- [Ключевые функции](#ключевые-функции)
- [Технологический стек](#технологический-стек)
- [Архитектура системы](#архитектура-системы)
- [Установка и запуск](#установка-и-запуск)
- [API Документация](#api-документация)
- [Безопасность](#безопасность)
- [База данных](#база-данных)
- [Развертывание](#развертывание)
- [Лицензия](#лицензия)
- [Контакты](#контакты)

---

## О проекте
GoMind - это инновационная платформа, объединяющая:
- 🏷️ Динамическую аукционную систему для рекламных кампаний
- 🧠 Интерактивные образовательные викторины с системой вознаграждений
- 💰 Прозрачную финансовую экосистему для участников

**Цели проекта:**
- Создание эффективного маркетплейса для цифровой рекламы
- Мотивация пользователей через геймификацию обучения
- Обеспечение безопасных финансовых транзакций

---

## Ключевые функции

### 🔐 Аутентификация и авторизация
- Многоуровневая система ролей (Админ/Пользователь/Гость)
- JWT-токены с временем жизни (15 минут)
- Подтверждение email при регистрации
- Восстановление пароля через OTP

### 📢 Рекламный аукцион
- Реалтайм-обновление ставок через WebSocket
- Автоматическая ротация активной рекламы
- История ставок за 30 дней
- Минимальная ставка: 10 "груш"

### 🎓 Образовательные викторины
- Таймбоксированные сессии (60 минут)
- 3 уровня сложности вопросов
- Публичный рейтинг участников
- Ежечасный расчет победителей

### 💸 Финансовый модуль
- Поддержка Visa/Mastercard/Apple Pay
- Автоматическое распределение вознаграждений
- Экспорт транзакций в CSV/XLSX
- Audit-log операций

---

## Технологический стек

### Бэкенд
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.1.0-6DB33F?logo=springboot)
![JWT](https://img.shields.io/badge/JWT-0.11.5-000000?logo=jsonwebtokens)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql)

### Инфраструктура
![Docker](https://img.shields.io/badge/Docker-24.0.2-2496ED?logo=docker)
![NGINX](https://img.shields.io/badge/NGINX-1.25.3-009639?logo=nginx)
![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-2088FF?logo=githubactions)

### Вспомогательные технологии
- MapStruct 1.4.2 (DTO mapping)
- Lombok 1.18.30 (Boilerplate reduction)
- OpenAPI 3.0 (API Documentation)
- Pusher (WebSocket Notifications)

---

## Архитектура системы

```mermaid
graph TD
    A[Клиент] --> B[NGINX]
    B --> C[Spring Boot App]
    C --> D[MySQL Database]
    C --> E[Redis Cache]
    C --> F[Payment Gateway]
    C --> G[Email Service]
    C --> H[Pusher]
