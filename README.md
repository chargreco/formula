# 🧪 Formula - Aplicación Móvil de Juegos Educativos

**Formula** es una aplicación nativa para Android desarrollada en **Java**. Su objetivo es permitir a los usuarios crear, compartir y jugar minijuegos y cuestionarios educativos interactivos personalizados de forma sencilla.

---

## 🚀 ¿Cómo Funciona la Aplicación?

La aplicación se estructura en torno a la **creación y resolución de juegos educativos** que los propios usuarios diseñan. Cada juego generado recibe un identificador único que se convierte a un **código hexadecimal**, el cual funciona como una "fórmula" o clave que cualquier usuario puede ingresar para buscar y jugar dicho cuestionario o minijuego.

---

## 🎮 Tipos de Juegos Disponibles

Formula cuenta con **4 modalidades de minijuegos educativos**:

| Tipo | Nombre | Descripción | Archivos Clave |
| :--- | :--- | :--- | :--- |
| **0** | **Cuestionario Simple** | Examen tradicional de opción múltiple (1 pregunta y 4 opciones de respuesta). | [`Quest.java`](app/src/main/java/com/rechargoyo/formula/Quest.java), [`PregOpc.java`](app/src/main/java/com/rechargoyo/formula/PregOpc.java) |
| **1** | **Relacionar Columnas** | Emparejamiento de conceptos ordenando dos columnas paralelas mediante arrastrar y soltar (*drag & drop*). | [`Rel.java`](app/src/main/java/com/rechargoyo/formula/Rel.java), [`Preg1a1.java`](app/src/main/java/com/rechargoyo/formula/Preg1a1.java) |
| **2** | **Tres en Raya (Tic-Tac-Toe)** | Juego de 2 jugadores en turno. Para poder colocar una "X" o un "O" en el tablero, el jugador debe responder correctamente una pregunta. | [`Tictac.java`](app/src/main/java/com/rechargoyo/formula/Tictac.java), [`PregOpc.java`](app/src/main/java/com/rechargoyo/formula/PregOpc.java) |
| **3** | **Memorama** | Juego de memoria donde las cartas contienen preguntas y respuestas; los jugadores deben voltear las cartas para encontrar los pares correctos. | [`Mem.java`](app/src/main/java/com/rechargoyo/formula/Mem.java), [`Preg1a1.java`](app/src/main/java/com/rechargoyo/formula/Preg1a1.java) |

---

## 📱 Pantallas y Navegación Principal

La navegación principal ocurre dentro de [`MainActivity.java`](app/src/main/java/com/rechargoyo/formula/MainActivity.java) mediante una barra inferior (`BottomNavigationView`) con un botón flotante (`FloatingActionButton` central) y un menú lateral deslizable (`DrawerLayout`):

1. **🏠 Inicio (`HomeFragment.java`):**
   - **Buscador por Código Hexadecimal:** Permite buscar cualquier juego ingresando su código.
   - **Lista de Recientes:** Muestra los juegos jugados recientemente por el usuario, indicando likes, creador y fecha.

2. **➕ Crear Juego / Fórmula (`FormulaFragment.java`):**
   - Menú de selección para elegir cuál de los 4 tipos de minijuegos se desea crear.
   - Redirige a los editores de preguntas ([`PregOpc.java`](app/src/main/java/com/rechargoyo/formula/PregOpc.java) o [`Preg1a1.java`](app/src/main/java/com/rechargoyo/formula/Preg1a1.java)).

3. **👤 Perfil (`PerfilFragment.java`):**
   - Muestra los datos del usuario (`@usuario`, nombre completo, número de creaciones).
   - Lista todas las creaciones del usuario. Al tocar cualquier creación, se abre la vista de detalles [`info.java`](app/src/main/java/com/rechargoyo/formula/info.java).
   - Botón para cerrar sesión (`logout`).

4. **🔔 Notificaciones (Menú Lateral / Drawer):**
   - Al tocar el ícono de campana en la barra superior (`Toolbar`), se despliega un panel lateral con notificaciones del sistema (como la bienvenida al registrarse).

5. **🔐 Autenticación:**
   - **Registro ([`Login.java`](app/src/main/java/com/rechargoyo/formula/Login.java)):** Formulario para crear un nuevo usuario y contraseña.
   - **Inicio de Sesión ([`Singin.java`](app/src/main/java/com/rechargoyo/formula/Singin.java)):** Verificación de credenciales con el servidor.

---

## 🛠️ Arquitectura y Componentes del Código

### 📂 Estructura de Paquetes (`app/src/main/java/com/rechargoyo/formula/`)

- **Actividades Principales:**
  - [`MainActivity.java`](app/src/main/java/com/rechargoyo/formula/MainActivity.java): Contenedor principal de la app con el `BottomNavigationView` y el `DrawerLayout`.
  - [`info.java`](app/src/main/java/com/rechargoyo/formula/info.java): Vista previa del juego seleccionado (muestra título, autor, código Hex y botón "Jugar").
  - [`NoConection.java`](app/src/main/java/com/rechargoyo/formula/NoConection.java): Pantalla de error cuando no hay conexión a internet.

- **Juegos (Gameplay):**
  - [`Quest.java`](app/src/main/java/com/rechargoyo/formula/Quest.java) / [`QuestionAdapter.java`](app/src/main/java/com/rechargoyo/formula/QuestionAdapter.java)
  - [`Rel.java`](app/src/main/java/com/rechargoyo/formula/Rel.java) / [`RelAdapter.java`](app/src/main/java/com/rechargoyo/formula/RelAdapter.java)
  - [`Tictac.java`](app/src/main/java/com/rechargoyo/formula/Tictac.java)
  - [`Mem.java`](app/src/main/java/com/rechargoyo/formula/Mem.java) / [`MemAdapter.java`](app/src/main/java/com/rechargoyo/formula/MemAdapter.java)

- **Creación de Juegos:**
  - [`PregOpc.java`](app/src/main/java/com/rechargoyo/formula/PregOpc.java) & [`PreguntasOpcAdapter.java`](app/src/main/java/com/rechargoyo/formula/PreguntasOpcAdapter.java)
  - [`Preg1a1.java`](app/src/main/java/com/rechargoyo/formula/Preg1a1.java) & [`Preguntas1a1Adapter.java`](app/src/main/java/com/rechargoyo/formula/Preguntas1a1Adapter.java)

- **Modelos de Datos (POJOs):**
  - [`Reciente.java`](app/src/main/java/com/rechargoyo/formula/Reciente.java)
  - [`Question.java`](app/src/main/java/com/rechargoyo/formula/Question.java)
  - [`Card.java`](app/src/main/java/com/rechargoyo/formula/Card.java)
  - [`Notificacion.java`](app/src/main/java/com/rechargoyo/formula/Notificacion.java)

- **Utilidades:**
  - [`LowerCaseInputFilter.java`](app/src/main/java/com/rechargoyo/formula/LowerCaseInputFilter.java): Filtro de texto para forzar minúsculas en nombres de usuario.
  - [`DateUtils.java`](app/src/main/java/com/rechargoyo/formula/DateUtils.java): Utilidad para formatear fechas.

---

## 🗄️ Guía Paso a Paso: Configurar Servidor Local (XAMPP) y Conectar desde Celular

Sigue este procedimiento para ejecutar la base de datos y la API PHP en tu computadora de forma local y conectar tu celular físico a través de tu red Wi-Fi:

### Paso 1: Instalar y Ejecutar XAMPP
1. Descarga e instala **XAMPP** para Windows desde [Apache Friends](https://www.apachefriends.org/).
2. Abre el **XAMPP Control Panel**.
3. Inicia los módulos **Apache** y **MySQL** haciendo clic en **Start** en ambos.

---

### Paso 2: Crear la Base de Datos en phpMyAdmin
1. Abre tu navegador e ingresa a `http://localhost/phpmyadmin/`.
2. En el panel izquierdo, haz clic en **Nueva** para crear una base de datos.
3. Asigna el nombre **`formula_db`** y selecciona el cotejamiento `utf8mb4_unicode_ci`.
4. Ve a la pestaña **SQL** en phpMyAdmin, copia y pega la siguiente consulta DDL y ejecuta (**Continuar**):

```sql
CREATE TABLE IF NOT EXISTS `usuarios` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `user` VARCHAR(50) NOT NULL UNIQUE,
  `nombre` VARCHAR(100) NOT NULL,
  `pass` VARCHAR(100) NOT NULL,
  `creaciones` INT DEFAULT 0,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `juegos` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `user` VARCHAR(50) NOT NULL,
  `nombre` VARCHAR(255) NOT NULL,
  `tipo` INT NOT NULL COMMENT '0=Cuestionario, 1=Relacionar, 2=Tres en raya, 3=Memorama',
  `preguntas` LONGTEXT NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`user`) REFERENCES `usuarios`(`user`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `recientes` (
  `id_reciente` INT AUTO_INCREMENT PRIMARY KEY,
  `user` VARCHAR(50) NOT NULL,
  `id_juego` INT NOT NULL,
  `fecha_reciente` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `dio_like` INT DEFAULT 0,
  `numero_likes` INT DEFAULT 0,
  FOREIGN KEY (`id_juego`) REFERENCES `juegos`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `notificaciones` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `user` VARCHAR(50) NOT NULL,
  `notificacion` TEXT NOT NULL,
  `visto` INT DEFAULT 0,
  `fecha` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Datos de prueba iniciales
INSERT INTO `usuarios` (`user`, `nombre`, `pass`, `creaciones`) VALUES ('rechargoyo', 'Eduardo', '12345', 1);

INSERT INTO `juegos` (`id`, `user`, `nombre`, `tipo`, `preguntas`) VALUES
(999990, 'rechargoyo', 'Examen de Matemáticas Básicas', 0, 
'[{"preg":"¿Cuánto es 2 + 2?","resp1":"3","resp2":"5","resp3":"22","respcorrect":"4"},{"preg":"¿Cuánto es 5 x 5?","resp1":"10","resp2":"15","resp3":"30","respcorrect":"25"}]');

INSERT INTO `recientes` (`user`, `id_juego`, `fecha_reciente`, `dio_like`, `numero_likes`) VALUES ('rechargoyo', 999990, NOW(), 1, 5);
INSERT INTO `notificaciones` (`user`, `notificacion`, `visto`) VALUES ('rechargoyo', '¡Bienvenido a Formula, Eduardo!', 0);
```

---

### Paso 3: Guardar los Archivos PHP en `htdocs`
1. Navega a la carpeta de instalación de XAMPP, generalmente ubicada en:  
   `C:\xampp\htdocs\`
2. Crea una nueva carpeta llamada **`formula`** (quedará en `C:\xampp\htdocs\formula\`).
3. Crea el archivo de conexión `db.php`:
   ```php
   <?php
   $host = "localhost";
   $user = "root";
   $pass = "";
   $dbname = "formula_db";

   $conn = new mysqli($host, $user, $pass, $dbname);
   if ($conn->connect_error) {
       die(json_encode(["ans" => "error"]));
   }
   $conn->set_charset("utf8mb4");
   ?>
   ```
4. Copia los scripts PHP correspondientes (`conect.php`, `consultar.php`, `userpass.php`, `insert.php`, `add_form.php`, `get_form.php`, `get_game_by_id.php`, `recientes.php`, `save_recent.php`, `juegosporuser.php`, `get_notificaciones.php`, `create_notification.php`, `marcar_notificaciones_vistas.php`, `like_toggle.php`).

---

### Paso 4: Obtener la IP Local de tu Computadora
1. Abre **PowerShell** o **CMD** en Windows.
2. Ejecuta el comando: `ipconfig`
3. Busca la sección **Adaptador de LAN inalámbrica Wi-Fi** y anota tu **Dirección IPv4** (por ejemplo: `192.168.1.15`).
4. **Verificación desde el Celular:** Asegúrate de que tu celular esté conectado a la **misma red Wi-Fi** que tu PC. Abre el navegador web en tu celular y entra a:  
   `http://192.168.1.15/formula/conect.php`  
   *(Deberías ver en pantalla el mensaje `{"ans":"ok"}`).*

---

### Paso 5: Apuntar la App de Android a tu IP Local
1. En Android Studio, reemplaza la URL base del servidor (`https://prototipoformula.fun/`) por tu IP local:
   `http://192.168.1.15/formula/`
2. Si usas el **Emulador de Android Studio**, puedes usar alternativamente:  
   `http://10.0.2.2/formula/`

---

## ⚙️ Requisitos y Tecnologías

- **Lenguaje:** Java 8+
- **Min SDK:** 26 (Android 8.0 Oreo)
- **Target SDK / Compile SDK:** 34 (Android 14)
- **Librerías Destacadas:**
  - Google Material Components (`com.google.android.material`)
  - AndroidX AppCompat, RecyclerView, ConstraintLayout
  - Volley (`com.android.volley:volley`)
