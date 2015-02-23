package com.khozzy.isitgood.controller;

import com.khozzy.isitgood.domain.Sentence;
import com.khozzy.isitgood.service.PredictionService;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RserveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@RestController
public class HomeController {

    @Autowired
    private PredictionService predictionService;

    @RequestMapping("/")
    public String home() throws REXPMismatchException, RserveException, IOException {
        return "Not protected resource";
    }

    @RequestMapping(
            value = "/predict",
            consumes = "application/json",
            method = RequestMethod.POST)
    public ResponseEntity<Sentence> predictDev(@RequestBody @Valid Sentence sentence)
            throws REXPMismatchException, RserveException, IOException {

        sentence = predictionService.predict(sentence);

        return new ResponseEntity<>(sentence, HttpStatus.OK);
    }

}
