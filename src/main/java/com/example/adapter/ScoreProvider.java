package com.example.adapter;

import com.example.model.Score;

public interface ScoreProvider {
    Score obtenerScore(String clienteId); 
}