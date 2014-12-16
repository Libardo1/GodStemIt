package com.khozzy.isitgood.controller;

import com.khozzy.isitgood.service.PredictionService;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RserveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class HomeController {

    @Autowired
    private PredictionService predictionService;

    @RequestMapping("/")
    public String home() throws REXPMismatchException, RserveException, IOException {
        predictionService.doSomething();
        return "Not protected resource";
    }
}
