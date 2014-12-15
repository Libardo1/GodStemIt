package com.khozzy.isitgood.service;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RserveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PredictionService {

    @Autowired
    private RService R;

    public void doSomething() throws RserveException, REXPMismatchException {
        REXP result = R.getConnection().eval("2+2");
        System.out.println("result: " + result.asString());
    }
}
