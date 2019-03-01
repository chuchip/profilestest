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



En el ejemplo que esta disponible en https://github.com/chuchip/profilestest se pueden ver como una aplicación saca diferentes mensajes y accede a diferentes datos según el perfil establecido.

El proyecto es una simple aplicación web, que accede a una base de datos H2, mostrando diferentes datos según el perfil. Para crear el proyecto simplemente hay que añadir las siguientes dependencias: JPA, H2 y WEB

![captura_starters](.\captura_starters.PNG)





Empezando por la clase principal ya veremos como definir diferentes **beans** dependiendo del perfil.

````java
@SpringBootApplication
@Configuration
public class ProfilesTests {

	@Autowired
	private Environment environment;
	
	public static void main(String[] args) {
		SpringApplication.run(ProfilesTests.class, args);
	}

	@Bean
	@Profile("test")
	IWrite getWriterTest()
	{
		return new WriteImpl("..test.. "+getProfile());		
	}
	@Bean
	@Profile("default")
	IWrite getWriterDefault()
	{
		return new WriteImpl("..default.. "+getProfile());		
	}
	@Bean
	@Profile("other")
	IWrite getWriterOther()
	{
		return new WriteImpl("..other.. "+getProfile());		
	}
	String getProfile()
	{
		if (environment.getActiveProfiles()==null)
			return "default";
		String[] profiles=environment.getActiveProfiles();
		return profiles.length>0?profiles[0]:"default"; 
	}
}
````

En las funciones getWriterXXX, se define  siempre un **bean**  definido por el interface `IWrite`. La definición de este interfaz es muy simple como se puede ver en el siguiente código:

```java
public interface IWrite {
	public void writeLog(String log);
	public String getProfile();
}
```

Y su implementación en la clase `WriteImpl` es también muy sencilla:

```
public class WriteImpl implements IWrite{
	private String profile;
	
	public WriteImpl(String profile)
	{
		this.profile=profile;
	}
	
	public String getProfile()
	{
		return profile;
	}
	@Override
	public void writeLog(String log) {
		System.out.println("Profile: "+profile+" -> "+ log);
		
	}
}
```

Volviendo a la clase principal, se ve que  la función **getWriterTest()** instancia un objeto   `WriteImpl`, pasando al constructor el literal `..test..` y  lo devuelto por la función **getProfile()**, la cual devuelve un **String** con  el perfil activo. 

La función **getWriterDefault()** hace lo mismo que la anterior pero instanciando un objeto  `WriteImpl`al cual en el constructor se le pasa el literal `..default..`y  lo devuelto por la función **getProfile()**. Y lo mismo con la función **getWriterOther()**  pero pasando el literal ``..other..`

Lo importante de estas funciones es la anotación **@Bean** con la cual le decimos a **Spring**  que cuando si en alguna parte del código se  debe inyectar el objeto devuelto en la función en alguna parte del proyecto, que ejecute esta función para conseguirlo. 

En otras palabras, cuando **Spring** encuentre una anotación **@Autowired** con un objeto tipo **IWrite** como ocurre en el siguiente código:

```java
@Autowired
IWrite out;	
```

buscara de alguna manera de un objeto que implemente ese *interface* o que se llame igual que el objeto en si. 

Así, para conseguir el objeto **Spring** tiene dos opciones:

- Buscar dentro del proyecto una *class* que se llame **IWrite**  o una clase que implemente el interfaz **IWrite** que este anotada con la etiqueta **@Component**
- Buscar una función dentro de una clase anotada con la etiqueta **@Configuration**   una función anotada con **@Bean**  y  que devuelva  un objeto de ese tipo.

Se pueden dar varios casos en este momento:

1. Encuentra un objeto y lo usa. Recalcar que el objeto solo será buscado una vez, de tal manera que solo habrá un objeto tipo **IWrite**
2. No encuentra un objeto con lo cual da un error y la aplicación no se ejecutara.
3. Encuentra más de un objeto y como no sabe con cual quedarse da un error y la aplicación no se ejecuta.

Como se puede ver, en el código hay tres funciones que devuelven un objeto tipo **IWrite** con lo cual **Spring** daría un error, para evitarlo añadimos la etiqueta **@Profile** . De esta manera si el perfil de la aplicación no coincide con el literal dentro de  **@Profile** esa función será ignorada. Y como se puede ver, cada función tiene su propia anotación **@Profile** con lo cual **Spring** solo tratara una de ellas. 

Destacar que si lanzamos la aplicación sin especificar ningún perfil, **Spring** elige el perfil **default**.

Por supuesto, si lanzáramos la aplicación con un perfil que no fuera **default**,**test** o **other** la aplicación fallaría, al no encontrar ningún objeto que implemente el interfaz **IWrite** 

Para demostrar el caso en que **Spring** deba buscar una clase, en el proyecto de ejemplo se puede ver como se define el interfaz **IRead** el cual es implementado por la clases **ReadDefImpl** y **ReadOtherImpl**.

Obsérvese como ambas clases tienen las anotaciones **@Component** y **@Profile** antes de la definición de la clase. Lógicamente en una clase la etiqueta **@Profile** tiene el parámetro **default** y en otra el parámetro es **other** .

El código de la clase `ProfilesController`   es el siguiente:

```
@RestController
public class ProfilesController {
	@Autowired 
	IRead read;
	
	@Value("${app.ID}")
	int id;
	
	@Autowired
	IWrite write;
	
	@GetMapping("/hello")
	public String get(@RequestParam(value="name",required=false) String name)
	{
        return "Hello "+(name==null?"SIN NOMBRE":name)+
        "<br><br> The name of the profile number:"+ id+" in the database H2 is: "+read.readRegistry(id)+
        "<br> The profile used in the application is : "+write.getProfile();
				
	}
}
```

En esta clase podemos ver, la anotación **@Value("${app.ID}")**  con ella lo que le estamos diciendo a **Spring** es que inicialice la variable que hay a continuación ( "`id`") con el valor que encuentre en un fichero de *properites*. Como no especificamos ninguno buscara dentro de `application.properties` y encontrara la siguiente línea:

```
app.ID=1
```

con lo cual la variable `id`tendra el valor **1** 

Pero si nos fijamos en nuestro proyecto además del fichero `application.properties` también tenemos un fichero llamado `application-other.properties`, esto es porque **Spring** en el caso de que el perfil sea **other** buscara primero el fichero `application-other.properties` para establecer las propiedades. 

De esta manera cuando el perfil sea **other** leerá el  fichero `application-other.properties` y encontrara la línea:

```
app.ID=2
```

con lo cual la variable **id** tendrá el valor **2**

Para probar la aplicación en *Eclipse*  podremos crear dos configuraciones. Una para el perfil **default** 

![Configuración para perfil default](.\captura1.png)

donde podemos ver que no especificamos ningún perfil con lo cual **Spring** asignara el perfil **default** y otra para el perfil **other** 

![Configuración para perfil other](.\captura2.png)

La del perfil **default** escuchara en el puerto 8080 y la de **other** en el 8081. Eso esta especificado con la línea `server.port=8080` en el fichero   `application.properties` y con la  línea `server.port:8081`en el fichero  `application-other.properties`

Como se puede ver en las siguientes pantallas, la llamada a http://localhost:8080/hello?name=profesor devuelve la siguiente salida:

![Configuración para perfil default](.\captura-navegador8080.png)

Se puede ver como el valor de la variable **id** es 1 y como lee en la  base de datos H2, el valor del registro correspondiente.

Si llamamos a  http://localhost:8081/hello?name=profesor, veremos la siguiente salida:

![Configuración para perfil default](.\captura-navegador8080.png)

En un articulo siguiente hablare del uso de los perfiles para testear la aplicación con **JUnit** 

Hasta entonces,  ¡¡ que la curiosidad no os abandone ;-) !!

