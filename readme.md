Cuando se hacen aplicaciones empresariales, lo normal es que, como mínimo, primero se  desplieguen en un entorno de pruebas  y después ya en el entorno producción. Cada entorno de pruebas tendrá diferentes bases de datos, diferentes URLs y toda una serie de parámetros específicos, con el fin de que una aplicación en desarrollo no acceda nunca a datos reales.

Spring provee una manera sencilla de gestionar esta situación haciendo uso de los perfiles.  

Hay varias maneras de establecer el perfil a utilizar por una aplicación:

1. Pasandolo como variable de entorno en JAVA 

   java -jar -Dspring.profiles.active=MI_PROFILE application.jar

2. Pasandolo como un argumento al programa.

   java -jar application.jar --spring.profiles.active=MI_PROFILE

3. Especificandolo con una anotación en el propio programa, en la etiqueta @SpringBootTest, pasandole el  parametro "spring.profiles.active". Muy útil para prueba unitarias.

   @SpringBootTest("spring.profiles.active=test")

4. Con la anotación @ActiveProfiles 

   @ActiveProfiles("MI_PROFILE")



En el ejemplo que esta disponible en https://github.com/chuchip/profilestest se pueden ver como una aplicación saca unos mensajes y accede a diferentes datos según el perfil establecido.



