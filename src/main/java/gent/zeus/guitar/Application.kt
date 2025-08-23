package gent.zeus.guitar

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment

@SpringBootApplication
open class Application {
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


    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(Application::class.java, *args)
        }
    }
}
