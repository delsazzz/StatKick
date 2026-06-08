# ⚽ StatKick

Aplicación Android desarrollada como Trabajo Fin de Grado (DAM) para registrar partidos de fútbol presenciados en directo y generar estadísticas personalizadas sobre equipos, jugadores, competiciones y estadios.

StatKick está orientada a aficionados al fútbol y groundhoppers que desean llevar un historial organizado de los encuentros a los que han asistido y analizar posteriormente sus propios datos de manera visual e intuitiva.

---

# 📱 Descripción

La aplicación permite registrar partidos de fútbol vistos en directo y construir automáticamente un historial personal con información detallada sobre:

* Equipos vistos.
* Jugadores vistos.
* Competiciones presenciadas.
* Estadios visitados.
* Goles observados.
* Estadísticas personalizadas.
* Logros desbloqueados.

Además, integra datos reales mediante API-Football y muestra geográficamente los estadios visitados mediante Google Maps.

---

# ✨ Funcionalidades principales

## Gestión de usuarios

* Registro de usuarios.
* Inicio de sesión.
* Persistencia de sesión.
* Edición de perfil.
* Sistema de logros.

## Registro de partidos

* Búsqueda de partidos.
* Consulta de detalles.
* Asociación de partidos al usuario.
* Historial completo de encuentros registrados.

## Estadísticas personalizadas

* Número total de partidos vistos.
* Equipos más vistos.
* Jugadores más vistos.
* Goleadores más vistos.
* Estadios visitados.
* Competiciones presenciadas.
* Total de goles observados.

## Mapa de estadios

* Visualización geográfica de estadios visitados.
* Integración con Google Maps SDK.
* Consulta visual de ubicaciones.

## Datos reales

* Integración con API-Football.
* Equipos reales.
* Jugadores reales.
* Competiciones reales.
* Partidos reales.

---

# 🏗 Arquitectura

La aplicación está desarrollada siguiendo la arquitectura MVVM (Model-View-ViewModel), separando claramente la lógica de negocio, la gestión de datos y la interfaz de usuario.

<img width="1536" height="1024" alt="image" src="https://github.com/user-attachments/assets/461983d3-b30d-485c-a045-c7f7759963e4" />

### Capas principales

### View

Interfaz de usuario desarrollada con Jetpack Compose.

### ViewModel

Gestiona estados, validaciones y comunicación con la capa de datos.

### Repository

Actúa como intermediario entre ViewModels y fuentes de datos.

### Model

Persistencia local mediante Room y acceso a datos remotos mediante API-Football.

---

# 🗄 Base de datos

La persistencia local se implementa mediante Room sobre SQLite.

Entidades principales:

* Usuario
* Partido
* Equipo
* Jugador
* Competición
* Estadio
* País
* Localidad
* Temporada
* Logro

Además, se implementan relaciones muchos-a-muchos mediante tablas intermedias.

<img width="2048" height="1462" alt="image" src="https://github.com/user-attachments/assets/96d09b0b-9c27-49ba-8c67-2a72a1ca160e" />

---

# 🌐 Integración con API-Football

La aplicación utiliza API-Football como fuente de datos externa.

La integración se realiza mediante:

* Retrofit
* Gson
* Repository Pattern
* Estrategia Offline First

Los datos obtenidos se almacenan localmente para permitir el funcionamiento incluso sin conexión a Internet.

---

# 🛠 Tecnologías utilizadas

## Desarrollo Android

* Kotlin
* Android Studio
* Jetpack Compose

## Arquitectura

* MVVM
* Repository Pattern
* StateFlow
* Coroutines

## Persistencia

* Room
* SQLite

## Consumo de APIs

* Retrofit
* Gson

## Mapas

* Google Maps SDK

## Control de versiones

* Git
* GitHub

---

# 📸 Capturas de pantalla

## Pantalla Splash

<img width="1536" height="1024" alt="image" src="https://github.com/user-attachments/assets/4da7a0f6-fe1b-4f5b-9d7e-d472f8139cdf" />

## Inicio de sesión

<img width="1024" height="1536" alt="image" src="https://github.com/user-attachments/assets/1cfb4af3-a151-42f0-842c-2359bac24ceb" />

## Registro

<img width="1024" height="1536" alt="image" src="https://github.com/user-attachments/assets/20b4170b-4238-406e-9c6a-0ecae129431b" />

## Perfil de usuario

<img width="1024" height="1536" alt="image" src="https://github.com/user-attachments/assets/d219ea36-a5e0-48bb-8562-9cbf095bb766" />

## Estadísticas

<img width="1024" height="1536" alt="image" src="https://github.com/user-attachments/assets/562e19e4-6ae6-4b42-92b5-153e9427d77b" />

## Mapa de estadios

<img width="1055" height="1491" alt="image" src="https://github.com/user-attachments/assets/55dbfd24-053c-41cf-896b-82e288dc8121" />

---

# 🎯 Objetivos del proyecto

* Aplicar los conocimientos adquiridos durante el ciclo DAM.
* Desarrollar una aplicación Android funcional.
* Implementar una arquitectura moderna y escalable.
* Gestionar persistencia local mediante Room.
* Consumir datos reales mediante APIs.
* Aplicar patrones de diseño utilizados en entornos profesionales.

---

# 🚀 Posibles mejoras futuras

* Sincronización en la nube.
* Versión multiplataforma.
* Sistema social entre usuarios.
* Comparación de estadísticas.
* Exportación de estadísticas.
* Publicación en Google Play.
* Notificaciones personalizadas.

---

# 👨‍💻 Autor

Proyecto desarrollado como Trabajo Fin de Grado del ciclo de Desarrollo de Aplicaciones Multiplataforma (DAM).

Participación principal en:

* Diseño de la base de datos relacional.
* Persistencia de datos mediante Room.
* Arquitectura de la aplicación.
* Flujo de navegación entre pantallas.
