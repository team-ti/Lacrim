Test

Se utilizó la versión  go1.12.7 windows/amd64 para la realización de la presente prueba. 

Empezando

El archivo .zip cuenta con el código fuente (test.go), el archivo ejecutable (test.exe) y el archivo 
que corresponde al ejemplo de los logs del dispositivo (example.log).

Pre-reqiusitos

Para ejecutar la prueba es necesario obtener la libreria "go-polyline", abriendo la consola
dentro de la carpeta de la prueba y ejecutando el comando "go get go-polyline".

Ejecutando

Una vez obtenida, se debe ejecutar en la misma consola el comando "go run test.go", se obtendrá 
la lista de cadenas códificadas que corresponden a las coordenadas encontradas en el archivo de log.
