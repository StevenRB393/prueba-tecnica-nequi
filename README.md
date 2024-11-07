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

