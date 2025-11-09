package com.example.adapter.score;

import com.example.model.score.Score;

public interface ScoreProvider {
    Score obtenerScore(String clienteId); 
}