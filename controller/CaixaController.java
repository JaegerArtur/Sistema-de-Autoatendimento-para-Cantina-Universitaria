package controller;

import service.CaixaService;

public class CaixaController {
    /**
     * Retira um valor do caixa, descontando do total, e retorna o detalhamento das notas/moedas retiradas.
     * @param valor Valor a ser retirado
     * @return Mapa com a quantidade de cada nota/moeda retirada
     */
    public static java.util.Map<model.enums.Dinheiro, Integer> retirarDoCaixaDetalhado(double valor) {
        return CaixaService.retirarDoCaixaDetalhado(valor);
    }

    /**
     * Compatibilidade: método antigo sem detalhamento
     */
    public static void retirarDoCaixa(double valor) {
        CaixaService.retirarDoCaixa(valor);
    }
}
