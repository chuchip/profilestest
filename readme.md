Hay varias maneras de establecer el perfil a utilizar por una aplicación:

1. Pasandolo como variable de entorno en JAVA  

java -jar -Dspring.profiles.active=MI_PROFILE application.jar

2. Pasandolo como un argumento al programa.

java -jar application.jar --spring.profiles.active=MI_PROFILE

3. Especificandolo con una anotación en el propio programa, en la etiqueta @SpringBoot, pasandole el  parametro "spring.profiles.active"

@SpringBoot("spring.profiles.active=MI_PROFILE")

Este caso es más util para pruebas unitarias JUnit, poniendoselo a la etiqueta @SpringBootTest

@SpringBootTest("spring.profiles.active=test")