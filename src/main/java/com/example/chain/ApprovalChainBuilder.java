package com.example.chain;

import com.example.config.BankConfig;
import org.springframework.stereotype.Service;

@Service
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