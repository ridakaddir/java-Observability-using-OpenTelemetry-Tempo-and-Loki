package com.ridakaddir.demootellokitempo;

import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class DemoController {

    private static final Logger log = LoggerFactory.getLogger(DemoController.class);

    @GetMapping("/hello")
    public String getMethodName() {
        log.info("Hello info!");
        log.error("Hello error!");
        log.debug("Hello debug!");

        return "Hello !";
    }

}
