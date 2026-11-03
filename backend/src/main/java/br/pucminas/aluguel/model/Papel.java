package br.pucminas.aluguel.model;

public enum Papel {
    CLIENTE, EMPRESA, BANCO;

    public boolean isAgente() { return this != CLIENTE; }
}
