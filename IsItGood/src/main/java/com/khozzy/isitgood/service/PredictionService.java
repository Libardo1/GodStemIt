package com.khozzy.isitgood.service;

import com.khozzy.isitgood.domain.Sentence;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPGenericVector;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.RList;
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
        loadAllTermsIntoR();
        loadProcessingFunctionInR();

        return makePredictionInR(sentence);
    }

    private Sentence makePredictionInR(Sentence sentence) throws RserveException, REXPMismatchException {
        sentence.stem();

        RConnection.assign("stemmed.sentence", sentence.getStemmed());
        RConnection.eval("new.sentence <- prepareSentence(stemmed.sentence)");

        REXP positiveProbability = RConnection.eval("predict(gbm, new.sentence, type = 'prob')$POS");

        sentence.setPosProb(positiveProbability.asDouble());

        return sentence;
    }

    private void loadModelIntoR() throws RserveException, IOException {
        ClassPathResource modelResource = new ClassPathResource("R/loadModel.R");
        String filePath = modelResource.getFile().getAbsolutePath();
        RConnection.eval(String.format("source(\"%s\")", filePath));
    }

    private void loadAllTermsIntoR() throws RserveException, IOException {
        ClassPathResource modelResource = new ClassPathResource("R/loadAllTerms.R");
        String filePath = modelResource.getFile().getAbsolutePath();
        RConnection.eval(String.format("source(\"%s\")", filePath));
    }

    private void loadProcessingFunctionInR() throws IOException, RserveException {
        ClassPathResource modelResource = new ClassPathResource("R/prepareStatement.R");
        String filePath = modelResource.getFile().getAbsolutePath();
        RConnection.eval(String.format("source(\"%s\")", filePath));
    }
}
