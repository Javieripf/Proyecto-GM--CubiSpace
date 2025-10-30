
CubiSpace

Shoot ‘em up espacial desarrollado en Java con libGDX como proyecto de la asignatura Programación Avanzada en la Pontificia Universidad Católica de Valparaíso.

Descripción
CubiSpace es un juego de naves donde el jugador debe sobrevivir a oleadas de enemigos, acumular puntuación y mantener sus vidas. Incluye movimiento fluido, disparos con cooldown, sistema de vidas e inmunidad temporal.

Características Principales
- Movimiento: Flechas ← / →
- Disparo: Barra espaciadora (cooldown 0.3 s)
- Vidas: 3 vidas con inmunidad de 1 s tras daño
- Puntuación:
  - Enemigo básico → +100 pts
  - Enemigo avanzado → +300 pts
- HUD con vidas, puntaje y récord
- Música y efectos de sonido

Enemigos
Enemigo Básico: 1 vida, movimiento lateral y descendente, se destruye al colisionar.
Enemigo Avanzado: 3 vidas, dispara cada 2.5 s y lanza un disparo triple al morir.

Arquitectura del Proyecto
- JuegoProgra: Clase principal.
- Pantallas: PantallaMenu, PantallaJuego.
- Entidades: NaveJugador, Bala, Enemigo, EnemigoBasico, Enemigo2, BalaEnemigo.
- Interfaz: Movible (para entidades con movimiento).

Principios de POO
- Herencia: Enemigo → EnemigoBasico / Enemigo2
- Polimorfismo: Manejo unificado de enemigos
- Encapsulamiento: Atributos privados y métodos controlados
- Abstracción: Interfaz Movible y clase abstracta Enemigo

Mecánicas Avanzadas
- Generación dinámica de enemigos cada 1.5 s
- Sistema de inmunidad con parpadeo visual
- Colisiones AABB
- Movimiento con delta time
- Liberación de recursos con dispose()

Recursos
Texturas: fondo.png, nave.png, enemigo.png, enemigo2.png, laser.png, laserEnemigo.png
Audio: mainTheme.mp3, disparo.mp3, enemyShoot.mp3, enemyHit.mp3, explosion.mp3
Todos deben estar en la carpeta assets/

Requisitos
- JDK 11
- libGDX 
- Windows / macOS / Linux

▶️ Instrucciones de Ejecución

Opción 1: Desde el IDE
1. Clonar el repositorio.
2. Abrir en IntelliJ IDEA o Eclipse.
3. Verificar carpeta assets/.
4. Ejecutar DesktopLauncher.java → método main.

Opción 2: Desde la Consola
- Con Gradle:
  gradlew desktop:run   (Windows)
  ./gradlew desktop:run (Linux/Mac)
- Con archivo JAR:
  java -jar CubiSpace.jar

Futuras Mejoras
- Power-ups, jefes, partículas y animaciones.
- Configuración de sonido y controles.
- Tabla de puntuaciones y logros.
- Modos extra (supervivencia, endless).

Créditos
Pontificia Universidad Católica de Valparaíso
Facultad de Ingeniería – Escuela de Ingeniería Informática
Materia: Programación Avanzada
Profesor: Claudio Cubillos
Período: 2025

Autores:
- Javier Poblete
- Joaquín Pérez
- Gonzalo Soto


