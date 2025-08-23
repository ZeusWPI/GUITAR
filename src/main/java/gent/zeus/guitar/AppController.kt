package gent.zeus.guitar

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.view.RedirectView
import java.util.*

@RestController
class AppController {
    @GetMapping("/")
    fun rootRedirect(): RedirectView {
        return RedirectView("/index")
    }

    @GetMapping("/index")
    fun index(): HelloResponse {
        return HelloResponse("weeee!!")
    }

    @GetMapping("/echo")
    fun echo(@RequestParam message: String, @RequestParam(defaultValue = "1") times: Int): HelloResponse {
        return HelloResponse(message.repeat(times))
    }

    @GetMapping("/saturn")
    fun saturn(@RequestParam password: String): HelloResponse {
        val encPassword = String(Base64.getEncoder().encode(password.toByteArray()))
        println(encPassword)
        if (encPassword == "Y29yZG9uLWJsdWU=") {
            return HelloResponse("Welcome to the cool zone 8)")
        } else {
            return HelloResponse("You are not cool enough :(")
        }
    }

    @JvmRecord
    data class HelloResponse(
        val response: String?
    )
}
