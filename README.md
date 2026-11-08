# SmartTech Store Android

**Actividad académica:** Interfaces gráficas completas de la tienda virtual Android  
**Proyecto:** SmartTech Store  
**Tecnologías:** Android nativo, Java, XML, Gradle y Android Studio  
**Paquete de aplicación:** `com.smarttech.store`

---

## 1. Descripción del proyecto

SmartTech Store es una aplicación móvil desarrollada en Android Studio como parte de la actividad académica **“Interfaces gráficas completas de la tienda virtual Android”**.

El proyecto representa una tienda virtual de productos tecnológicos mediante interfaces gráficas organizadas por perfiles de usuario: comprador, vendedor y administrador.

La aplicación permite visualizar productos, consultar sus características, acceder al carrito de compras y recorrer diferentes pantallas relacionadas con la experiencia de compra. También incluye espacios de trabajo organizados para los perfiles de vendedor y administrador.

El desarrollo se realiza mediante componentes nativos de Android, utilizando Activities, layouts XML, recursos gráficos y archivos de configuración de Gradle.

El propósito de esta entrega es presentar el código fuente, la estructura del proyecto, los recursos visuales y la organización de las interfaces desarrolladas.

---

## 2. Objetivo general

Desarrollar una aplicación móvil nativa para una tienda virtual de productos tecnológicos, aplicando los conocimientos de diseño de interfaces gráficas, organización de componentes Android, navegación entre Activities y administración de recursos visuales.

## 3. Objetivos específicos

- Diseñar interfaces gráficas para una tienda virtual de productos tecnológicos.
- Organizar las pantallas mediante Activities y layouts XML.
- Implementar navegación entre las diferentes secciones de la aplicación.
- Incorporar recursos gráficos, imágenes, colores, textos y temas personalizados.
- Representar los perfiles de comprador, vendedor y administrador.
- Organizar el código fuente y los recursos dentro de la estructura de un proyecto Android.
- Presentar el proyecto mediante un repositorio GitHub para facilitar la revisión académica.

---

## 4. Tecnologías utilizadas

| Tecnología | Descripción |
|---|---|
| Android Studio | Entorno de desarrollo de la aplicación móvil. |
| Java | Lenguaje utilizado para las Activities, modelos y lógica de navegación. |
| XML | Definición de interfaces gráficas y recursos de Android. |
| Gradle | Sistema de configuración y construcción del proyecto. |
| Android SDK | Herramientas necesarias para desarrollar y ejecutar la aplicación. |
| Git y GitHub | Control de versiones y publicación del código fuente. |

---

## 5. Estructura del proyecto

El proyecto se encuentra organizado mediante la estructura estándar de una aplicación Android.

```text
SmartTech_Store/
│
├── app/
│   │
│   ├── build.gradle.kts
│   │
│   └── src/
│       └── main/
│           │
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
│           │   │
│           │   ├── drawable/
│           │   │   └── Recursos gráficos e imágenes
│           │   │
│           │   ├── layout/
│           │   │   └── Interfaces XML
│           │   │
│           │   ├── values/
│           │   │   ├── colors.xml
│           │   │   ├── strings.xml
│           │   │   └── themes.xml
│           │   │
│           │   └── mipmap/
│           │       └── Recursos de iconos
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

La estructura anterior representa la organización general del proyecto. Las clases Java y los recursos específicos se encuentran dentro de sus respectivas carpetas en `app/src/main/`.

---

## 6. Interfaces y módulos de la aplicación

### 6.1. Inicio de sesión

La aplicación cuenta con una pantalla inicial que permite visualizar los campos de acceso y seleccionar el perfil correspondiente.

Desde esta pantalla se organiza el ingreso a los espacios de trabajo de acuerdo con el rol seleccionado.

### 6.2. Módulo comprador

El módulo comprador reúne las interfaces relacionadas con la navegación y experiencia de compra.

Entre las pantallas presentes se encuentran:

- Pantalla principal del comprador.
- Catálogo de productos.
- Detalle de producto.
- Carrito de compras.
- Pantalla de checkout.
- Accesos al perfil.
- Acceso al historial de pedidos.

El catálogo presenta productos tecnológicos con información como nombre, descripción y precio.

### 6.3. Módulo vendedor

El módulo vendedor presenta un espacio de trabajo organizado mediante accesos relacionados con:

- Productos.
- Pedidos recibidos.
- Perfil del vendedor.

Estas opciones representan la organización visual del módulo vendedor dentro de la aplicación.

**Alcance:** los accesos del módulo no representan un sistema completo de administración persistente de productos y pedidos conectado a un backend.

### 6.4. Módulo administrador

El módulo administrador presenta un espacio de trabajo con accesos relacionados con:

- Usuarios.
- Productos.
- Reportes de ventas.

La interfaz permite identificar la organización general del espacio administrativo de la tienda virtual.

**Alcance:** el panel corresponde a una representación académica de interfaz y navegación. No se declara un CRUD completo y persistente de usuarios o productos ni un sistema real de generación de reportes conectado a una base de datos.

---

## 7. Diseño de interfaces gráficas

Las interfaces de SmartTech Store se desarrollan mediante layouts XML y componentes nativos de Android.

El diseño incorpora recursos visuales destinados a organizar la presentación de los productos y las diferentes secciones de la aplicación.

Los recursos utilizados incluyen:

- Layouts XML para las pantallas.
- Imágenes y elementos gráficos.
- Colores personalizados.
- Textos centralizados.
- Temas y estilos de presentación.
- Componentes de navegación.
- Elementos visuales para la presentación de productos.

Los recursos de la aplicación se organizan dentro de la carpeta `app/src/main/res`.

---

## 8. Recursos del proyecto

### 8.1. Drawable

La carpeta `drawable` contiene recursos gráficos utilizados en las interfaces, incluyendo imágenes y elementos visuales de la tienda.

### 8.2. Layout

La carpeta `layout` contiene los archivos XML utilizados para definir la estructura visual de las Activities y los componentes de la aplicación.

### 8.3. Values

La carpeta `values` contiene recursos utilizados para la configuración visual y textual de la aplicación.

Entre ellos se encuentran:

- `colors.xml`: definición de colores.
- `strings.xml`: textos y cadenas de la aplicación.
- `themes.xml`: configuración de temas visuales.

### 8.4. AndroidManifest.xml

El archivo `AndroidManifest.xml` contiene la configuración principal de la aplicación Android y la declaración de sus componentes.

---

## 9. Funcionamiento general

La aplicación organiza la navegación a partir de la pantalla inicial.

El usuario puede seleccionar el perfil correspondiente y acceder al espacio de trabajo asociado.

En el módulo comprador se encuentran las pantallas relacionadas con la consulta de productos y el recorrido de compra.

Los módulos vendedor y administrador presentan sus respectivos espacios de trabajo con accesos organizados por funciones.

La navegación se implementa mediante componentes Android y Activities incluidas en el código fuente.

---

## 10. Alcance funcional de la entrega

Esta entrega corresponde a una implementación académica de interfaces gráficas y navegación de una tienda virtual Android.

El alcance documentado comprende:

- Organización de la aplicación por perfiles.
- Interfaces gráficas para el comprador.
- Catálogo y detalle de productos.
- Carrito y pantalla de checkout.
- Espacios de trabajo para vendedor y administrador.
- Recursos gráficos y layouts XML.
- Configuración del proyecto Android.
- Organización del código fuente para revisión.

Las siguientes funciones **no se presentan como implementaciones completas de producción**:

- Procesamiento de pagos mediante una pasarela bancaria real.
- Administración CRUD persistente completa de usuarios y productos.
- Backend remoto para sincronización de información.
- Recuperación de contraseña mediante correo electrónico.
- Autenticación biométrica integrada y validada.
- Persistencia remota completa de pedidos y ventas.

Las opciones visuales relacionadas con estas funciones deben interpretarse según el comportamiento realmente implementado en el código fuente.

---

## 11. Requisitos para ejecutar el proyecto

Para abrir y ejecutar el proyecto se requiere:

- Android Studio.
- Android SDK compatible con la configuración del proyecto.
- JDK configurado para Gradle.
- Dispositivo Android o emulador compatible.
- Conexión a internet para descargar las dependencias iniciales de Gradle, si no están disponibles localmente.

La configuración específica de SDK y versiones se encuentra en los archivos Gradle del proyecto.

---

## 12. Instrucciones de instalación y ejecución

1. Descargar o clonar el repositorio.
2. Extraer el proyecto si se recibe como archivo ZIP.
3. Abrir Android Studio.
4. Seleccionar **Open**.
5. Ubicar la carpeta raíz `SmartTech_Store`.
6. Seleccionar el archivo `settings.gradle.kts` como parte del proyecto.
7. Esperar a que finalice Gradle Sync.
8. Seleccionar un dispositivo Android o emulador compatible.
9. Ejecutar la configuración de la aplicación desde Android Studio.

La ejecución depende de que el entorno local tenga configurados correctamente Android Studio, Android SDK y Gradle.

---

## 13. Control de versiones

El proyecto se organiza para su publicación y revisión mediante Git y GitHub.

El repositorio permite consultar:

- Código fuente Java.
- Interfaces XML.
- Recursos gráficos.
- Configuración de Gradle.
- Documentación del proyecto.

Git permite registrar los cambios realizados durante el desarrollo y mantener un historial de versiones.

---

## 14. Entrega académica

El repositorio contiene el proyecto Android SmartTech Store y sus archivos fuente organizados para facilitar la revisión de la estructura.

La entrega se relaciona con la actividad académica:

**“Interfaces gráficas completas de la tienda virtual Android”.**

El proyecto se presenta como una aplicación móvil académica desarrollada con Java, XML y Android Studio.

La documentación describe la organización del proyecto y el alcance funcional correspondiente al código fuente disponible.

---

## 15. Conclusión

El desarrollo de SmartTech Store permite aplicar conocimientos de programación Android, diseño de interfaces gráficas, organización de Activities, layouts XML y administración de recursos visuales.

La estructura del proyecto facilita la identificación de los componentes principales de la aplicación y la revisión de sus módulos.

La implementación reúne una experiencia de navegación para una tienda virtual de productos tecnológicos y presenta espacios de trabajo diferenciados para comprador, vendedor y administrador.

El código fuente y los recursos incluidos constituyen la base de la entrega académica y permiten revisar la organización del proyecto desde Android Studio y GitHub.

---

**Proyecto académico:** SmartTech Store Android  
**Actividad:** Interfaces gráficas completas de la tienda virtual Android  
**Lenguaje:** Java  
**Interfaces:** XML  
**Entorno:** Android Studio  
**Repositorio:** GitHub
