package umc.dosports.Domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {
    @GetMapping("/")
    public String home() {
        return "home";
    }
}
