---


---

<h1 id="prueba-técnica---api-franquicias">Prueba técnica - API Franquicias</h1>
<p><strong>Lorem Ipsum</strong> is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry’s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book</p>
<h2 id="estructura-del-proyecto">Estructura del proyecto</h2>
<pre><code>
    medical-hour-management/
    │
    ├── .github/
    │   └── workflows/
    │       └── main.yml
    │
    ├── src/
    │   └── main/
    │      ├── java/com/medicalhourmanagement/
    │      │   ├── configs/
    │      │   ├── controllers/
    │      │   ├── dtos/
    │      │   │   ├── request/
    │      │   │   └── response/
    │      │   ├── entities/
    │      │   ├── exceptions/
    │      │   ├── repositories/
    │      │   ├── security/
    │      │   │   ├── filter/
    │      │   │   └── service/
    │      │   ├── services/
    │      │   │   └── impl/
    │      │   ├── utils/
    │      │   │   ├── constant/
    │      │   │   ├── enums/
    │      │   │   └── validator/
    │      │   └── MedicalHourManagementApplication.java
    │      │
    │      └── resources/
    │          ├── application.properties
    │          ├── application-dev.properties
    │          ├── application-prod.properties
    │   
    ├── .gitignore
    ├── mvnw
    ├── mvnw.cmd
    ├── pom.xml
    └── README.md

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
<h1 id="guía-de-instalación">Guía de instalación:</h1>
<p><strong>Lorem Ipsum</strong> is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry’s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book</p>
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

