package moskwa.com.credit.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreditController {

    @PostMapping("/create-credit")
    public void createCredit(){
        System.out.println("create");
    }

    @GetMapping("/get-credit")
    public void getCredit(){
        System.out.println("get");
    }
}
