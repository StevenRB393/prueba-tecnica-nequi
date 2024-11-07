---


---

<h1 id="prueba-técnica---api-franquicias">Prueba técnica - API Franquicias</h1>
<p>Este proyecto consiste en desarrollar una <strong>API de gestión de franquicias</strong>, permitiendo manejar una lista de franquicias, cada una con sus sucursales y productos. La API permitirá realizar operaciones CRUD sobre franquicias, sucursales y productos, además de consultas específicas sobre el stock de los productos por sucursal, buscar el producto con mas stock por sucursal para una franquicia puntal etc.</p>
<h2 id="estructura-del-proyecto">Estructura del proyecto</h2>
<pre><code>prueba-practica-backend-nequi/
|
|- .github/
|   |-workflows
|     |- main.yml
|
|--src/
|   |--main/
|         |--java/
|               |--com.appication.prueba/
|				   |--configs
|                  |--controllers         
|                  |--dtos
|                  |--exceptions
|                  |--mappers
|                  |--models
|                  |--repositories
|                  |--services              
|                  |--utils
|
|         |--resources/
|            |--application.yml
|
|--.gitignore
|--docker-compose.yml
|--Dockerfile
|--mvnw
|--mvnw.cmd
|--pom.xml
|--README.md
</code></pre>
<h2 id="componentes-principales">Componentes principales</h2>
<ul>
<li><code>security/</code>: Contiene las clases relacionadas con la autenticación y seguridad.</li>
<li><code>configs/</code>: Configuraciones generales de la aplicación.</li>
<li><code>constants/</code>: Constantes utilizadas en todo el proyecto.</li>
<li><code>controllers/</code>: Controladores REST que manejan las solicitudes HTTP.</li>
<li><code>dtos/</code>: Objetos de Transferencia de Datos (DTOs) para la comunicación entre capas.</li>
<li><code>entities/</code>: Entidades JPA que representan las tablas de la base de datos.</li>
<li><code>enums/</code>: Enumeraciones utilizadas en el proyecto.</li>
<li><code>exceptions/</code>: Manejo centralizado de excepciones.</li>
<li><code>repositories/</code>: Interfaces de repositorio para el acceso a datos.</li>
<li><code>services/</code>: Servicios que contienen la lógica de negocio</li>
</ul>
<h2 id="base-de-datos">Base de datos</h2>
<p><strong>Lorem Ipsum</strong> is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry’s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book<br>
<a href="https://postimages.org/" target="_blank"><img src="https://i.postimg.cc/T3ZQbV23/Captura-de-pantalla-2024-11-07-023542.png" border="0" alt="Captura-de-pantalla-2024-11-07-023542"></a><br><a href="https://postimages.org/es/"></a><br></p>
<ul>
<li>Franquicias:</li>
<li>Sucursales:</li>
<li>Productos: dsds</li>
</ul>
<h1 id="guía-de-instalación">Guía de instalación</h1>
<h2 id="requisitos-previos">Requisitos previos:</h2>
<ul>
<li><strong>Java 21 o superior</strong>: Compilación y ejecución del proyecto.</li>
<li><strong>Maven</strong>: Gestión de dependencias y construcción  del proyecto.</li>
<li><strong>Docker 27.2.0 o superior</strong>: Administración de contenedores y servicios en tu máquina local.</li>
<li><strong>MongoDB 2.2.10</strong> o superior (utilizando MongoDB Atlas para la base de datos en la nube).</li>
</ul>
<p>Si no se tiene algunos de estos programas instalados, se pueden obtener directamente desde su página oficial.</p>
<h2 id="paso-1-clona-el-repositorio">Paso #1: Clona el repositorio</h2>
<p>Primero, clona el repositorio a tu máquina local utilizando git:</p>
<pre><code>git clone https://github.com/StevenRB393/prueba-tecnica-nequi
cd prueba-tecnica-nequi
</code></pre>
<h2 id="paso-2-configuración-de-mongodb-atlas">Paso #2: Configuración de MongoDB Atlas</h2>
<p>Este proyecto utiliza MongoDB Atlas como base de datos. Si aún no tienes una cuenta, puedes crear una desde <a href="https://www.mongodb.com/cloud/atlas">MongoDB Atlas</a>.</p>
<ol>
<li>
<p><strong>Crea un cluster</strong> en MongoDB Atlas.</p>
</li>
<li>
<p>Una vez creado el cluster, ahora crea un usuario de acceso a la base de datos con las siguientes credenciales:</p>
</li>
</ol>
<ul>
<li><strong>Username</strong>: <code>invitado</code></li>
<li><strong>Password</strong>: <code>invitado</code></li>
</ul>
<ol start="4">
<li>
<p><strong>Obtén la cadena de conexión</strong> desde los drivers de MongoDB Atlas.</p>
</li>
<li>
<p><strong>Configura el archivo</strong> <code>application.yml</code> en el proyecto para que apunte a tu base de datos de MongoDB Atlas. La configuración básica es la siguiente:</p>
</li>
</ol>
<p><a href="https://postimages.org/" target="_blank"><img src="https://i.postimg.cc/xj7NXDm2/Captura-de-pantalla-2024-11-07-112200.png" border="0" alt="Captura-de-pantalla-2024-11-07-112200"></a></p>
<p>Configuraremos de manera externalizada las variables de entorno en el archivo docker-compose.yml.</p>
<h2 id="paso-3-configuración-de-docker-imagen-y-docker-compose">Paso #3: Configuración de Docker (imagen y docker-compose)</h2>
<ol>
<li>
<p>Crear la imagen con el <strong>Dockerfile</strong></p>
</li>
<li>
<p>Construir la imagen de Docker:</p>
</li>
</ol>
<blockquote>
<pre><code> docker build -t nombre-de-tu-imagen 
</code></pre>
</blockquote>
<ol start="3">
<li>Configura y levanta los contenedores con Docker Compose</li>
</ol>
<p><strong>IMPORTANTE</strong>: Al configurar el archivo docker-compose.yml, no olvides configurar correctamente las variables de entorno, para que la conexión a la base de datos sea exitosa.<br>
<a href="https://postimages.org/" target="_blank"><img src="https://i.postimg.cc/5tCpHq74/Captura-de-pantalla-2024-11-07-115813.png" border="0" alt="Captura-de-pantalla-2024-11-07-115813"></a></p>
<h2 id="paso-4-ejecutar-docker-compose">Paso #4: Ejecutar Docker Compose</h2>
<p>Una vez que todo esté listo (tanto el Dockerfile como el docker-compose.yml), puedes levantar la aplicación con el siguiente comando:</p>
<pre><code>docker-compose up --build
</code></pre>
<p>El flag <code>--build</code> indica que Docker Compose debe construir la imagen antes de levantar los contenedores ( si has realizado cambios en el <code>Dockerfile</code> o en el código fuente, esto es fundamental).</p>
<h2 id="paso-5-verificar-que-todo-esté-funcionando">Paso #5: Verificar que todo esté funcionando</h2>
<ul>
<li>
<p><strong>Revisa los contenedores</strong>: Para asegurarte de que los contenedores se están ejecutando correctamente, usa el siguiente comando:</p>
<p><code>docker ps</code></p>
<p>Esto te mostrará una lista de los contenedores en ejecución. Deberías ver tu contenedor de la aplicación (<code>nombre-de-tu-contenedor</code>) corriendo en el puerto 8080.</p>
</li>
<li>
<p><strong>Verificar la aplicación</strong>: Abre tu navegador y navega a:</p>
<p><a href="http://localhost:8080">http://localhost:8080</a></p>
<p>Deberías ver la respuesta de tu aplicación Spring Boot</p>
</li>
<li>
<p><strong>Verificar los logs del contenedor</strong>: Si algo no funciona como esperas, puedes revisar los logs del contenedor para identificar posibles errores. Usa este comando:</p>
<p><code>docker logs nombre-de-tu-contenedor</code></p>
</li>
</ul>
<h2 id="paso-6-probar-la-api">Paso #6: Probar la API</h2>
<p>Ahora que ya está todo configurado y verificado de que funciona bien, prueba los endpoints de cada una de las colecciones de la base de datos.</p>
<h1 id="buenas-prácticas-implementadas-en-el-proyecto">Buenas prácticas implementadas en el proyecto</h1>
<h2 id="uso-de-dtos">- Uso de DTOs</h2>
<p>sdasdasdasdasd<br>
<strong>¿En qué beneficia?</strong> fddfsdfsdfsd</p>
<pre><code>public class AppointmentDTO {
    private Long id;
    private DoctorDTO doctor;
    private PatientDTO patient;
    private LocalDateTime date;
    }
</code></pre>
<h2 id="uso-de-loggers">Uso de Loggers</h2>
<p>asdasdasdasdasdas</p>
<h2 id="arquitectura-en-capas">Arquitectura en capas</h2>
<p>dasdasdasdasdasd</p>
<h2 id="manejo-de-excepciones-centralizado">Manejo de excepciones centralizado</h2>
<p>sdfsdfsdfsdfsdfsdf<br>
<strong>¿En qué beneficia?</strong> fddfsdfsdfsd</p>
<pre><code>@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity&lt;ExceptionDTO&gt; handleNotFoundException(NotFoundException ex, WebRequest request) {
        // ...
    }
    // ...
}
</code></pre>
<h2 id="configuración-externalizada-con-variables-de-entorno">Configuración externalizada con variables de entorno</h2>
<p>dasdasdasdasd<br>
<strong>¿En qué beneficia?</strong> fddfsdfsdfsd</p>
<pre><code>spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DATABASE_USERNAME}
spring.datasource.password=${DATABASE_PASSWORD}
</code></pre>

