package gent.zeus.guitar;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Base64;

@RestController
public class AppController {

    @GetMapping("/")
    public RedirectView rootRedirect() {
        return new RedirectView("/index");
    }

    @GetMapping("/index")
    public HelloResponse index() {
        return new HelloResponse("weeee!!");
    }

    @GetMapping("/echo")
    public HelloResponse echo(@RequestParam String message, @RequestParam(defaultValue = "1") int times) {
        return new HelloResponse(message.repeat(times));
    }

    @GetMapping("/saturn")
    public HelloResponse saturn(@RequestParam String password) {
        String encPassword = new String(Base64.getEncoder().encode(password.getBytes()));
        System.out.println(encPassword);
        if (encPassword.equals("Y29yZG9uLWJsdWU=")) {
            return new HelloResponse("Welcome to the cool zone 8)");
        } else {
            return new HelloResponse("You are not cool enough :(");
        }
    }

    public record HelloResponse(
            String response
    ) {
    }
}
