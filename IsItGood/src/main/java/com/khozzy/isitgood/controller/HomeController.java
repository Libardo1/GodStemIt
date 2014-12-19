package com.khozzy.isitgood.controller;

import com.khozzy.isitgood.domain.Sentence;
import com.khozzy.isitgood.service.PredictionService;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RserveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class HomeController {

    @Autowired
    private PredictionService predictionService;

    @RequestMapping("/")
    public String home() throws REXPMismatchException, RserveException, IOException {
        return "Not protected resource";
    }

    @RequestMapping("/predict")
    public Sentence predictDev(@RequestParam("sentence") String original)
            throws REXPMismatchException, RserveException, IOException {

        Sentence sentence = new Sentence(original);

        sentence = predictionService.predict(sentence);

        return sentence;
    }

}
