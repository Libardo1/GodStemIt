package com.khozzy.isitgood.service;

import com.khozzy.isitgood.domain.Sentence;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PredictionService {

    @Autowired
    private RConnection RConnection;

    public Sentence predict(final Sentence sentence) throws RserveException, REXPMismatchException, IOException {

        loadModelIntoR();
        sentence.stem();

        REXP result = RConnection.eval("class(gbm)");
        System.out.println("result: " + result.asString());
        System.out.println("stemmed sentence: " + sentence.getStemmed());

        return sentence;
    }

    private void loadModelIntoR() throws RserveException, IOException {
        ClassPathResource modelResource = new ClassPathResource("R/loadModel.R");
        String filePath = modelResource.getFile().getAbsolutePath();
        RConnection.eval(String.format("source(\"%s\")", filePath));
    }


}
