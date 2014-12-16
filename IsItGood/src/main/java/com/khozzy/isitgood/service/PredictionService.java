package com.khozzy.isitgood.service;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RFileInputStream;
import org.rosuda.REngine.Rserve.RserveException;
import org.rosuda.REngine.Rserve.protocol.RTalk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PredictionService {

    @Autowired
    private RConnection RConnection;

    public void doSomething() throws RserveException, REXPMismatchException, IOException {

        loadModelIntoR();

        REXP result = RConnection.eval("myFunc()");
        System.out.println("result: " + result.asString());
    }

    private void loadModelIntoR() throws RserveException, IOException {
        ClassPathResource modelResource = new ClassPathResource("R/script.R");
        String filePath = modelResource.getFile().getAbsolutePath();
        RConnection.eval(String.format("source(\"%s\")", filePath)); // Load R File
    }
}
