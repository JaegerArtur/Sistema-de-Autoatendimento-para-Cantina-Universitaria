package model.enums;

/**
 * Enumeração que representa as diferentes denominações de dinheiro disponíveis no sistema.
 * Inclui moedas e cédulas com seus respectivos valores.
 *
 * @author Grupo Artur, João, Miguel
 * @version 1.0
 */

public enum Dinheiro {
    MOEDA_5(0.05),
    MOEDA_10(0.10),
    MOEDA_25(0.25),
    MOEDA_50(0.50),
    MOEDA_1(1.00),
    CEDULA_2(2.00),
    CEDULA_5(5.00),
    CEDULA_10(10.00),
    CEDULA_20(20.00),
    CEDULA_50(50.00),
    CEDULA_100(100.00);

    private final double valor;

    Dinheiro(double valor) {
        this.valor = valor;
    }

    public double getValor() {
        return valor;
    }
}
