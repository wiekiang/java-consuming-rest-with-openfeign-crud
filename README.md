## Consuming REST with OpenFeign: Consuming Interests CRUD

This Java code is a Spring Boot application that demonstrates the use of OpenFeign for making HTTP requests to a REST API and Spring Data REST for exposing a JPA repository as a RESTful service. Let's break down each part of the code:

### Entity Class: Interest

```
@Entity @Data
class Interest { 
    @Id @GeneratedValue 
    private Long id; 
    
    private String interest;
}
```

* **@Entity**: This annotation marks the class as a JPA entity, which means it maps to a database table.
* **@Data**: This is a Lombok annotation that automatically generates boilerplate code like getters, setters, toString() methods.
* **@Id @GeneratedValue**: This marks the id field as the primary key of the entity and specifies that its value should be generated automatically.
* The Interest class has two fields: **id** (automatically generated) and **interest** (a string representing the interest).

### Repository Interface: InterestRepository

```
@RepositoryRestResource
interface InterestRepository extends JpaRepository<Interest, Long> {}
```

* **@RepositoryRestResource**: This annotation from Spring Data REST automatically exposes the repository as a RESTful service. It allows CRUD operations to be performed via HTTP requests (e.g., GET, POST, PUT, DELETE).
* **InterestRepository**: This interface extends JpaRepository, providing standard methods to interact with the Interest entity in the database.

### Feign Client Interface: InterestClient

```
@FeignClient(value= "InterestClient", url = "http://localhost:8080")
interface InterestClient {    
    @PostMapping(value = "/interests")
    Interest _create(@RequestBody Interest interest); 
    
    @GetMapping(value = "/interests/{id}")
    Interest _retrieve(@PathVariable Long id); 
    
    @PutMapping(value = "/interests/{id}")
    Interest _update(@PathVariable Long id, @RequestBody Interest interest); 
    
    @DeleteMapping(value = "/interests/{id}")
    Interest _delete(@PathVariable Long id);
}
```

* **@FeignClient**: This annotation marks the interface as a Feign client, allowing it to make HTTP requests to the specified url. The value attribute gives a name to this client.
* The **InterestClient** interface defines methods for making CRUD operations on the Interest entity via HTTP requests:
    * **_create**: Sends a POST request to create a new **Interest**.
    * **_retrieve**: Sends a GET request to retrieve an **Interest** by its **id**.
    * **_update**: Sends a PUT request to update an existing **Interest**.
    * **_delete**: Sends a DELETE request to remove an **Interest** by its **id**.

### Application Initialization: AppInit

```@Component @Data
class AppInit implements ApplicationRunner {
    private final InterestClient interestClient;
    
    @Override
    public void run(ApplicationArguments aa) {
        Interest i = new Interest(); 
        
        i.setInterest("volleyball"); 
        interestClient._create(i); 
        
        i.setInterest("cricket"); 
        interestClient._update(1L, i);

        System.out.println(interestClient._retrieve(1L).getInterest()); 
        interestClient._delete(1L);
    }
}
```

* **@Component**: This annotation marks the class as a Spring component, allowing it to be managed by the Spring container.
* **ApplicationRunner**: This is a Spring Boot interface that allows code to be executed after the application context is loaded and the Spring Boot application has started.
* **run** method: This method is executed at application startup:
    * An **Interest** object is created and set with the value "volleyball".
    * The interest is then created using the **_create** method of **InterestClient**.
    * The interest is updated to **"cricket"** and the update is persisted.
    * The updated interest is retrieved and printed to the console.
    * Finally, the interest is deleted.

### Spring Boot Application Class

```
@SpringBootApplication @EnableFeignClients 
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
```

* **@SpringBootApplication**: This annotation marks the class as a Spring Boot application, enabling auto-configuration, component scanning, and property support.
* **@EnableFeignClients**: This annotation enables Feign clients in the Spring Boot application, allowing the **InterestClient** interface to be used.

### Dependencies

```
<!-- Spring Boot Data JPA -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- Spring Boot Data REST -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-rest</artifactId>
</dependency>

<!-- H2 Database -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- Spring Cloud OpenFeign -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
    <version>4.1.3</version>
</dependency>

<!-- Lombok -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <scope>provided</scope>
</dependency>
```