package com.khozzy.isitgood.service;

import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.springframework.stereotype.Service;

@Service
public class RService {

    private RConnection connection = null;

    public RConnection getConnection() throws RserveException {

        if (connection == null) {
            connection = new RConnection();
            return connection;
        }

        return connection;
    }


}
