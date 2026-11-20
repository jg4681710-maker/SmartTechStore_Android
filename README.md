# SmartTech Store Android

**Proyecto académico:** SmartTech Store
**Actividad:** Proyecto final – Aplicativo Android
**Programa:** Ingeniería de Software
**Asignatura:** Desarrollo de Aplicaciones Móviles Nativas
**Tecnologías:** Android nativo, Java, XML, Firebase, Cloud Firestore, SQLite y Gradle
**Paquete de aplicación:** `com.smarttech.store`
**Entorno de desarrollo:** Android Studio


## 1. Descripción del proyecto

SmartTech Store es una aplicación móvil nativa desarrollada en Android Studio, orientada a la gestión y comercialización de productos tecnológicos mediante una tienda virtual.

El proyecto contempla tres perfiles de usuario: **comprador, vendedor y administrador**, cada uno con espacios de trabajo y opciones organizadas de acuerdo con sus responsabilidades dentro de la aplicación.

La solución incorpora interfaces para el inicio de sesión, registro de compradores, consulta de productos, visualización de detalles, carrito de compras, proceso de checkout e historial de compras. También incluye módulos orientados a las actividades administrativas y comerciales de la tienda.

El desarrollo utiliza Java para la lógica de programación, XML para la construcción de las interfaces y servicios de Firebase para determinadas operaciones de autenticación y almacenamiento de información. Asimismo, incorpora SQLite para el almacenamiento local de información relacionada con las compras.

El propósito del proyecto es integrar los conocimientos de desarrollo móvil nativo, organización de componentes, navegación, gestión de datos y diseño de interfaces, de acuerdo con los requerimientos de la actividad académica.


## 2. Objetivo general

Desarrollar una aplicación móvil nativa para una tienda virtual de productos tecnológicos, integrando interfaces gráficas, navegación entre módulos, gestión de información y servicios de autenticación y almacenamiento, de acuerdo con los requerimientos establecidos para el proyecto académico.

## 3. Objetivos específicos

- Diseñar interfaces gráficas para los perfiles de comprador, vendedor y administrador.
- Implementar la navegación entre las diferentes secciones de la aplicación.
- Desarrollar funcionalidades relacionadas con la consulta y visualización de productos.
- Incorporar mecanismos de autenticación y registro de usuarios.
- Organizar la información mediante modelos y componentes de acceso a datos.
- Integrar servicios de Firebase para las operaciones contempladas en el desarrollo.
- Implementar mecanismos de almacenamiento local para la información correspondiente.
- Mantener una estructura organizada del código fuente y los recursos de Android.
- Documentar el proyecto para facilitar su instalación, revisión y mantenimiento.


## 4. Tecnologías utilizadas

| Tecnología | Descripción |
|---|---|
| Android Studio | Entorno de desarrollo de la aplicación móvil. |
| Java | Lenguaje utilizado para las Activities, modelos y lógica de la aplicación. |
| XML | Definición de interfaces gráficas y recursos de Android. |
| Gradle | Sistema de configuración y construcción del proyecto. |
| Android SDK | Herramientas necesarias para desarrollar y ejecutar la aplicación. |
| Firebase Authentication | Servicio utilizado para gestionar la autenticación de usuarios. |
| Cloud Firestore | Base de datos remota utilizada para determinadas operaciones de información. |
| SQLite | Almacenamiento local de información relacionada con las compras. |
| Git | Control de versiones del código fuente. |
| GitHub | Publicación y consulta del repositorio del proyecto. |


## 5. Estructura del proyecto

El proyecto se encuentra organizado mediante la estructura estándar de una aplicación Android, separando las clases Java, los recursos gráficos, las interfaces XML y los archivos de configuración.

```text
SmartTech_Store/
│
├── app/
│   ├── build.gradle.kts
│   │
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com/
│           │       └── smarttech/
│           │           └── store/
│           │               ├── Activities
│           │               ├── Models
│           │               ├── Adapters
│           │               └── Modules
│           │
│           ├── res/
│           │   ├── drawable/
│           │   ├── layout/
│           │   ├── values/
│           │   └── mipmap/
│           │
│           └── AndroidManifest.xml
│
├── gradle/
│   └── Configuración y archivos del wrapper
│
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
└── README.md
```

La estructura representa la organización general del proyecto. Para identificar las clases y recursos específicos, se debe consultar directamente el contenido de las carpetas correspondientes.


## 6. Interfaces y módulos de la aplicación

### 6.1. Módulo de autenticación

Este módulo reúne las operaciones relacionadas con el acceso de los usuarios a la aplicación.

Contempla:

- Inicio de sesión mediante correo electrónico y contraseña.
- Registro de compradores.
- Consulta del perfil del usuario.
- Identificación del rol registrado.
- Redirección al módulo correspondiente.
- Solicitud de recuperación de contraseña mediante correo electrónico.
- Cierre de sesión.

La autenticación se integra con Firebase Authentication y la información del perfil se consulta mediante Cloud Firestore, de acuerdo con la configuración del proyecto.

### 6.2. Módulo comprador

El módulo comprador reúne las pantallas relacionadas con la experiencia de consulta y compra de productos tecnológicos.

Entre sus componentes se encuentran:

- Pantalla principal.
- Catálogo de productos.
- Detalle de producto.
- Carrito de compras.
- Pantalla de checkout.
- Perfil del comprador.
- Historial de compras.

El catálogo está preparado para consultar información de productos y presentar datos como nombre, descripción y precio.

El carrito utiliza almacenamiento temporal en memoria durante la sesión. Por su parte, el registro de compras contempla almacenamiento local mediante SQLite.

### 6.3. Módulo vendedor

El módulo vendedor presenta un espacio de trabajo organizado para las actividades relacionadas con la operación comercial de la tienda.

Contempla accesos orientados a:

- Consulta de productos.
- Administración de productos, según las operaciones disponibles.
- Historial de pedidos.
- Perfil del vendedor.

El alcance de este módulo debe interpretarse de acuerdo con las operaciones realmente implementadas. No se declara una administración completa y persistente de pedidos e inventario.

### 6.4. Módulo administrador

El módulo administrador presenta un espacio de trabajo destinado a las operaciones administrativas de la tienda.

Contempla opciones relacionadas con:

- Gestión de productos.
- Administración de usuarios.
- Consulta de información de ventas.

La gestión de productos cuenta con operaciones preparadas para trabajar con Cloud Firestore, sujetas a los permisos y a la configuración del perfil administrador.

La administración CRUD completa de usuarios y la generación integral de reportes de ventas requieren desarrollo adicional.


## 7. Diseño de interfaces gráficas

Las interfaces de SmartTech Store se desarrollan mediante layouts XML y componentes nativos de Android.

El diseño busca ofrecer una navegación organizada y una presentación clara de los productos y las diferentes secciones de la aplicación.

Los recursos utilizados incluyen:

- Layouts XML para las pantallas.
- Imágenes y elementos gráficos.
- Colores personalizados.
- Textos y cadenas de la aplicación.
- Temas y estilos de presentación.
- Componentes de navegación.
- Elementos visuales para la presentación de productos.

Los recursos se organizan dentro de la carpeta `app/src/main/res`.


## 8. Recursos del proyecto

### 8.1. Drawable

La carpeta `drawable` contiene los recursos gráficos utilizados en las interfaces, incluyendo imágenes y elementos visuales de la aplicación.

### 8.2. Layout

La carpeta `layout` contiene los archivos XML utilizados para definir la estructura visual de las Activities y los componentes de la aplicación.

### 8.3. Values

La carpeta `values` contiene recursos utilizados para la configuración visual y textual.

Entre ellos se encuentran:

- `colors.xml`: definición de colores.
- `strings.xml`: textos y cadenas de la aplicación.
- `themes.xml`: configuración de temas visuales.

### 8.4. AndroidManifest.xml

El archivo `AndroidManifest.xml` contiene la configuración principal de la aplicación Android y la declaración de sus componentes.


## 9. Gestión de información y almacenamiento

El proyecto contempla diferentes mecanismos de almacenamiento, según el tipo de información y las operaciones implementadas.

| Tecnología | Uso dentro del proyecto |
|---|---|
| Firebase Authentication | Autenticación de usuarios mediante correo electrónico y contraseña. |
| Cloud Firestore | Almacenamiento remoto de perfiles y operaciones relacionadas con productos. |
| SQLite | Registro local de información de compras. |
| Memoria de la aplicación | Administración temporal del carrito durante la sesión. |

La utilización de estos mecanismos no significa que todos los módulos estén sincronizados remotamente. Cada operación debe interpretarse de acuerdo con su implementación actual.


## 10. Seguridad y control de acceso

La aplicación contempla Firebase Authentication para gestionar el acceso mediante credenciales.

La consulta del perfil permite identificar el rol del usuario y orientar la navegación hacia el módulo correspondiente.

El acceso a las operaciones de Cloud Firestore depende de las reglas de seguridad configuradas en Firebase. Las operaciones administrativas requieren perfiles autorizados y permisos correspondientes.

La asignación de privilegios administrativos debe realizarse mediante un procedimiento controlado y no mediante una modificación libre desde el cliente.

Las funciones adicionales de seguridad, como la autenticación biométrica, requieren integración y validación específica en un dispositivo compatible.


## 11. Alcance funcional de la entrega

El proyecto reúne funcionalidades relacionadas con el desarrollo de una tienda virtual Android y presenta avances en la integración de servicios de autenticación y almacenamiento.

El alcance documentado comprende:

- Organización de la aplicación por perfiles.
- Interfaces gráficas para comprador, vendedor y administrador.
- Inicio de sesión mediante Firebase Authentication.
- Registro de compradores contemplado mediante Firebase.
- Consulta del perfil y orientación de navegación según el rol.
- Catálogo y detalle de productos.
- Operaciones de gestión de productos preparadas para Firestore.
- Carrito de compras con administración temporal en memoria.
- Registro local de compras mediante SQLite.
- Organización de recursos gráficos y layouts XML.
- Configuración del proyecto Android.
- Documentación técnica y manual de usuario.

### Funcionalidades que requieren integración o validación adicional

Las siguientes operaciones no se presentan como implementaciones completas de producción:

- Administración CRUD completa y persistente de usuarios.
- Sincronización remota completa de pedidos y ventas.
- Integración completa de Firebase Storage para imágenes.
- Selección y carga de imágenes mediante cámara y galería.
- Autenticación biométrica integrada y validada.
- Geolocalización.
- Procesamiento de pagos mediante una pasarela certificada.
- Reportes administrativos completos.
- Pruebas integrales de todas las funciones en un dispositivo físico.

La presencia de una pantalla, botón o acceso visual no implica que la operación correspondiente esté completamente implementada. El comportamiento debe verificarse mediante el código fuente y las pruebas de funcionamiento.


## 12. Requisitos para ejecutar el proyecto

Para abrir el proyecto se requiere:

- Android Studio.
- Android SDK compatible con la configuración Gradle.
- JDK compatible con la versión de Gradle utilizada.
- Dispositivo Android o emulador compatible.
- Conexión a internet para descargar dependencias y utilizar los servicios remotos.
- Configuración válida de Firebase cuando se requieran sus funcionalidades.

Las versiones y dependencias específicas deben consultarse en los archivos de configuración Gradle del proyecto.


## 13. Instrucciones de instalación y ejecución

1. Descargar o clonar el repositorio.
2. Extraer el proyecto si se recibe como archivo ZIP.
3. Abrir Android Studio.
4. Seleccionar la opción **Open**.
5. Ubicar la carpeta raíz del proyecto.
6. Abrir el proyecto desde el archivo `settings.gradle.kts`.
7. Esperar a que finalice la sincronización de Gradle.
8. Seleccionar un dispositivo Android o emulador compatible.
9. Ejecutar la aplicación desde Android Studio.

La ejecución depende de que el entorno local tenga configurados correctamente Android Studio, Android SDK, Gradle y los servicios externos requeridos.


## 14. Configuración de Firebase

Para utilizar las funcionalidades que dependen de Firebase es necesario contar con un proyecto correctamente configurado.

La configuración contempla:

- Firebase Authentication con el método de correo electrónico y contraseña habilitado.
- Cloud Firestore para las colecciones utilizadas por la aplicación.
- Archivo `google-services.json` correspondiente al proyecto Firebase.
- Reglas de seguridad compatibles con las operaciones autorizadas.
- Perfiles de usuario registrados con sus respectivos roles.

Las operaciones administrativas requieren que el perfil tenga los permisos correspondientes en Firestore.

La integración de Firebase Storage debe completarse para habilitar el almacenamiento remoto de imágenes cuando esta funcionalidad se incorpore.


## 15. Control de versiones y repositorio

El proyecto se organiza para su publicación y revisión mediante Git y GitHub.

**Repositorio del proyecto:**

https://github.com/jg4681710-maker/SmartTechStore_Android

El repositorio permite consultar:

- Código fuente Java.
- Interfaces XML.
- Recursos gráficos.
- Configuración de Gradle.
- Archivos de configuración de Android.
- Documentación del proyecto.

Git permite registrar los cambios realizados durante el desarrollo y mantener un historial de versiones.


## 16. Documentación complementaria

La entrega académica se acompaña de documentación destinada a explicar el desarrollo y facilitar la utilización de la aplicación.

Los documentos complementarios son:

- **Documento técnico:** describe la arquitectura, tecnologías, componentes, alcance funcional y aspectos de implementación.
- **Manual de usuario:** presenta las instrucciones de navegación y utilización de los módulos de comprador, vendedor y administrador.
- **Código fuente:** contiene los archivos del proyecto Android para su consulta y revisión.

La documentación debe interpretarse en correspondencia con las funcionalidades realmente incorporadas al proyecto.


## 17. Mejoras y funcionalidades pendientes

Como parte de la evolución de SmartTech Store, se identifican las siguientes oportunidades de mejora:

- Completar la administración de usuarios mediante operaciones CRUD.
- Consolidar la gestión de productos y sus permisos.
- Integrar Firebase Storage para imágenes.
- Incorporar selección de imágenes desde cámara y galería.
- Implementar persistencia remota de carritos y pedidos.
- Integrar autenticación biométrica.
- Incorporar geolocalización.
- Conectar una pasarela de pagos certificada.
- Completar los reportes administrativos.
- Realizar pruebas integrales en dispositivos Android físicos.

Estas mejoras permitirán avanzar hacia una solución más completa, con mayor persistencia de información, seguridad e integración de servicios.


## 18. Conclusión

SmartTech Store constituye un proyecto académico de desarrollo móvil nativo que integra conocimientos de programación en Java, diseño de interfaces XML, navegación entre Activities, organización de componentes y utilización de servicios de autenticación y almacenamiento.

La aplicación presenta una estructura organizada por perfiles y reúne funcionalidades relacionadas con la consulta de productos y la experiencia de compra.

El desarrollo establece una base que permite continuar incorporando servicios, mejorar la persistencia de la información y ampliar las capacidades administrativas y comerciales de la tienda virtual.


**Proyecto:** SmartTech Store Android
**Actividad:** Proyecto final – Aplicativo Android
**Programa:** Ingeniería de Software
**Asignatura:** Desarrollo de Aplicaciones Móviles Nativas
**Lenguaje:** Java
**Interfaces:** XML
**Servicios:** Firebase Authentication y Cloud Firestore
**Almacenamiento local:** SQLite
**Entorno:** Android Studio
**Repositorio:** https://github.com/jg4681710-maker/SmartTechStore_Android
