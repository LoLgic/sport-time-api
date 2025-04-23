# 🌤️ Sport Time

**Sport Time** es una plataforma web que recomienda actividades deportivas diarias en función del clima de tu ciudad, usando datos en tiempo real de la API de [OpenWeatherMap](https://openweathermap.org/).

## 🚀 Características

- Consulta del pronóstico diario del clima por ciudad.
- Recomendación de deportes según:
  - Temperatura
  - Humedad
  - Velocidad del viento
- Visualización de porcentajes de idoneidad por deporte.
- Sistema de puntuación con umbrales personalizados.
- Almacenamiento de datos en base de datos MySQL.
- Cacheo inteligente de resultados por ciudad y fecha.
- Manejo robusto de errores y excepciones.

## 🛠️ Tecnologías Usadas

- **Java 17**
- **Spring Boot 3**
- **Spring Data JPA**
- **Spring Web**
- **OpenWeatherMap API**
- **MySQL**
- **Spring Cache (Caffeine)**
- **Lombok**
- **Maven**

## 📦 Instalación

1. Clona el repositorio:

```bash
git clone https://github.com/tu-usuario/sport-time-api.git
cd sport-time-clima
