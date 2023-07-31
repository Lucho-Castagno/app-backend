# AppBackend

Aplicación **Backend** para la resolución de la consigna de entrenamiento de nuevos recursos de **ODT**.
Para poder levantar el proyecto se necesita tener instalado antes:
- `Spring Tool 4` ó `Eclipse` con su plugin de Spring Tool 4 (que puede ser encontrado en el marketplace de Eclipse).
- `Java` en su versión 17
- `Postgres` v15.3


## Pasos

1. Clonar el proyecto utilizando el comando `git clone` seguido de:
    - HTTP: `https://github.com/Lucho-Castagno/app-backend.git`
    - SSH: `git@github.com:Lucho-Castagno/app-backend.git`

2. Entramos al IDE que se esté utilizando, en este caso Eclipse ó Spring Tool 4.

3. Debemos importar el proyecto que clonamos como proyecto de **Gradle**.
    - click derecho en la sección de Package Explorer, `Import` --> `Gradle` --> `Existing Gradle Project`.
    - con esto se descargan automáticamente las dependencias.
    - si por alguna razón, el proyecto da errores porque faltan dependencias, puede dirigirse al archivo `build.gradle` y "refrezcarlo" dando click derecho sobre el archivo, seleccionar `Gradle` --> `Refresh Gradle Project`.

4. Antes de poder levantar el proyecto, se debe crear la base de datos en postgres con nombre `entrenamiento_db`, el usuario y la contraseña deben ser `odt`
    - esta información se puede visualizar/modificar en el archivo application.properties que se encuentra dentro del proyecto.
    - en el caso de que se este usando Docker, se debe de iniciar el container de postgres antes de levantar el proyecto, de lo contrario dara error.

5. Luego de creada la base de datos con la información requerida, podemos iniciar el proyecto.
    - para levantarlo se puede ir a la sección de **Boot Dashboard** --> `local` --> seleccionamos `app-backend`, aparecerá un icono de Stop/Play, al presionarlo empezará a levantarse el proyecto.

6. El proyecto se visualizará en `http://localhost:8080/`


## JUnit e integración de JaCoCo

Para hacer las pruebas con Junit debemos de haber realizado los pasos que se mencionaron anteriormente.
Si se quiere hacer pruebas con Junit deberá darle click derecho a la carpeta de `src/test/java` y seleccionar la opción `Run As` --> `JUnit Test`, esto es para el caso de que se quieran probar todos los test que la carpeta contiene. En caso de que se quiera probar un archivo en particular se debe de hacer click derecho sobre ese archivo y desde ahí los mismos pasos.

Para poder ver los informes de JaCoCo deberemos abrir un terminal (en mi caso se eligio la opcion `git bash here`) en la carpeta del proyecto (**app-backend**) y ejecutar el comando `./gradlew clean test jacocoTestRepost`. Este comando limpiará el proyecto, ejecutará las pruebas unitarias y luego generará el informe de cobertura de JaCoCo.

Para visualizar los informes deberemos ingresar a la carpeta del proyecto `app-backend` --> `build` --> `reports` --> `jacoco` --> `test` --> `html`, en esta última carpeta encontraremos el archivo `index.html` el cual presenta el informe de JaCoCo en nuestro navegador web por defecto.
