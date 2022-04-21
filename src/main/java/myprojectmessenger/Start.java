package myprojectmessenger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@ComponentScan(basePackages = "myprojectmessenger")
@EntityScan(basePackages = "myprojectmessenger.entity")
public class Start {
    public static void main(String[] args) {
        SpringApplication.run(Start.class, args);
    }

}
