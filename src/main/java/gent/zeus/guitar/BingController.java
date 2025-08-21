package gent.zeus.guitar;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bing")
public class BingController {

    @GetMapping("/get")
    public String get() {
        return "bing !!!!!!";
    }
}
