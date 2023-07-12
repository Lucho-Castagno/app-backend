# AppBackend

Aplicacion **Backend** para la resolucion de la consigna de entrenamiento de nuevos recursos de **ODT**.
Para poder levantar el proyecto se necesita tener instalado antes **Spring Tool 4**, **Java 17** y **Postgres**.

## Pasos
1. clonar el proyecto utilizando el comando `git clone` seguido de:
    - HTTP: `https://github.com/Lucho-Castagno/app-backend.git`
    - SSH: `git@github.com:Lucho-Castagno/app-backend.git`

2. entramos al IDE que se este utilizando, en este caso Eclipse ó Spring Tool 4.

3. debemos importar el proyecto que clonamos como proyecto de **Gradle**.

4. antes de poder levantar el proyecto, se debe crear la base de datos en postgres con nombre `entrenamiento_db`, el usuario y la contraseña deben ser `odt`
    - esta informacion se puede visualizar/modificar en el archivo application.properties que se encuentra dentro del proyecto.

5. luego de creada la base de datos con la informacion requerida, podemos iniciar el proyecto.

6. el proyecto se visualizara en `http://localhost:8080/`

