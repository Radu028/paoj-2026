package com.pao.laboratory07.exercise2;

import com.pao.laboratory07.exercise1.OrderState;

public abstract sealed class Comanda permits ComandaStandard, ComandaRedusa, ComandaGratuita {
    protected String nume;
    protected String client;
    protected OrderState state = OrderState.PLACED;

    public Comanda(String nume) {
        this.nume = nume;
    }

    public String getNume() { return nume; }
    public String getClient() { return client; }
    public void setClient(String client) { this.client = client; }
    public OrderState getState() { return state; }

    public abstract double pretFinal();
    public abstract String descriere();
}
