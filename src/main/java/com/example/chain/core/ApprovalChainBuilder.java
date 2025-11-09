package com.example.chain.core;

import com.example.chain.handlers.AprobacionFinalHandler;
import com.example.chain.handlers.DocumentacionHandler;
import com.example.chain.handlers.EndeudamientoHandler;
import com.example.chain.handlers.MontoHandler;
import com.example.chain.handlers.PlazoHandler;
import com.example.chain.handlers.ScoreHandler;
import com.example.config.BankConfig;

public class ApprovalChainBuilder {

    public ApprovalHandler build(BankConfig config) {
        var score = new ScoreHandler();
        var monto = new MontoHandler();
        var plazo = new PlazoHandler();
        var endeudamiento = new EndeudamientoHandler(config.getRatioEndeudamientoMaximo());
        var docs = new DocumentacionHandler();
        var finalHandler = new AprobacionFinalHandler();

        score.setNext(monto);
        monto.setNext(plazo);
        plazo.setNext(endeudamiento);
        endeudamiento.setNext(docs);
        docs.setNext(finalHandler);

        return score;
    }
}