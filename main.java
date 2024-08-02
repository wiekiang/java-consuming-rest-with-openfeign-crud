package au.edu.cqu.App;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue; 
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.boot.ApplicationArguments; 
import org.springframework.boot.ApplicationRunner; 
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication; 
import org.springframework.cloud.openfeign.EnableFeignClients; 
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping; 
import org.springframework.web.bind.annotation.GetMapping; 
import org.springframework.web.bind.annotation.PathVariable; 
import org.springframework.web.bind.annotation.PostMapping; 
import org.springframework.web.bind.annotation.PutMapping; 
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author wiekiang
 */

@Entity @Data
class Interest { 
    @Id @GeneratedValue 
    private Long id; 
    
    private String interest;
}

@RepositoryRestResource
interface InterestRepository extends JpaRepository<Interest, Long> {}

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

@Component @Data
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

@SpringBootApplication @EnableFeignClients
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
