package gent.zeus.guitar;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

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

    public record HelloResponse(
            String response
    ) {
    }
}
