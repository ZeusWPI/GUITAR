package gent.zeus.guitar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.beans.BeanProperty;
import java.util.Arrays;

@SpringBootApplication
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner envRunner(Environment environment) {
        return args -> {
            log.info(environment.getProperty("spring.application.name"));
        };
    }


/*
    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            System.out.println("-----------------------------");
            System.out.println("  beans provided by spring:  ");
            System.out.println("-----------------------------");

            Arrays.stream(ctx.getBeanDefinitionNames())
                    .sorted()
                    .forEach(System.out::println);
        };
    }
*/

}
