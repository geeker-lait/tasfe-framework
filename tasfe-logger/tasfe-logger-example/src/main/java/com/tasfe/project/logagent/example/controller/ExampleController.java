package com.tasfe.project.logagent.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wuxueyou on 2017/6/2.
 */
@RestController
@RequestMapping("/logagent")
public class ExampleController {
    private Logger logger = LoggerFactory.getLogger(ExampleController.class);

    @RequestMapping("/test1")
    public String test1() {
        int k = 0;
        System.out.println(3 / k);
        for (int i = 0; i < 1000; i++) {
            logger.info("xixi");
        }
        return "ok";
    }

    @RequestMapping("/testBatch")
    public String testBatch(int size) throws Exception {
        for (int i = 0; i < size; i++) {
            logger.info(i + "");
        }
        return "ok";
    }

    @RequestMapping("/batch")
    public String batch(String input) throws Exception {
        try {
           int a = 1 / 0;
        }catch (Exception e){
            logger.error(input,e);
        }
        return "ok";
    }
}
