package service;

import util.JsonManager;
import java.io.File;
import java.util.*;
import model.enums.Dinheiro;

/**
 * Classe de serviço responsável por operações relacionadas ao caixa da cantina.
 * Permite adicionar, remover notas/moedas, calcular o valor total e verificar se é possível dar troco.
 *
 * @author Grupo Artur, João, Miguel
 * @version 1.0
 */
public class CaixaService {
    
    private static final String CAMINHO_CAIXA = "data/caixa.json";
    private static Map<Dinheiro, Integer> notasMoedas = new HashMap<>();
    
    /**
     * Retorna o valor total do caixa (estático, para uso em controllers).
     */
    public static double getValorTotalCaixa() {
        carregarCaixa();
        return getValorTotal();
    }

    /**
     * Retorna o conteúdo detalhado do caixa (estático, para uso em controllers).
     */
    public static Map<Dinheiro, Integer> getCaixaDetalhado() {
        carregarCaixa();
        return detalharConteudoCaixa();
    }

    /**
     * Calcula o detalhamento do troco a partir do valor e das notas/moedas disponíveis.
     * Retorna um mapa com a quantidade de cada nota/moeda a ser devolvida, ou null se não for possível.
     */
    public static Map<Dinheiro, Integer> calcularTrocoDetalhado(double troco) {
        carregarCaixa();
        List<Dinheiro> ordenadas = new ArrayList<>(Arrays.asList(Dinheiro.values()));
        ordenadas.sort((a, b) -> Double.compare(b.getValor(), a.getValor()));
        int trocoCentavos = (int)Math.round(troco * 100);
        return backtrackTroco(ordenadas, notasMoedas, trocoCentavos, 0, new HashMap<>());
    }

    // Algoritmo de backtracking para encontrar uma combinação de troco
    private static Map<Dinheiro, Integer> backtrackTroco(List<Dinheiro> tipos, Map<Dinheiro, Integer> caixa, int restante, int idx, Map<Dinheiro, Integer> atual) {
        if (restante == 0) {
            return new HashMap<>(atual);
        }
        if (restante < 0 || idx >= tipos.size()) {
            return null;
        }
        Dinheiro tipo = tipos.get(idx);
        int disponivel = caixa.getOrDefault(tipo, 0);
        int valorCentavos = (int)Math.round(tipo.getValor() * 100);
        if (valorCentavos == 0) {
            // Pula denominação inválida
            return backtrackTroco(tipos, caixa, restante, idx + 1, atual);
        }
        for (int qtd = Math.min(restante / valorCentavos, disponivel); qtd >= 0; qtd--) {
            if (qtd > 0) {
                atual.put(tipo, qtd);
            } else {
                atual.remove(tipo);
            }
            Map<Dinheiro, Integer> res = backtrackTroco(tipos, caixa, restante - qtd * valorCentavos, idx + 1, atual);
            if (res != null) return res;
        }
        return null;
    }
    
    /**
     * Adiciona um valor ao caixa, distribuindo em notas/moedas disponíveis (ganancioso).
     * Atualiza o arquivo JSON.
     */
    public static void adicionarCaixa(double valor) {
        carregarCaixa();
        List<Dinheiro> tipos = Arrays.asList(Dinheiro.values());
        tipos.sort((a, b) -> Double.compare(b.getValor(), a.getValor()));
        double restante = valor;
        for (Dinheiro tipo : tipos) {
            int qtd = (int) (restante / tipo.getValor());
            if (qtd > 0) {
                adicionarNotaMoeda(tipo, qtd);
                restante -= qtd * tipo.getValor();
            }
        }
        salvarCaixa();
    }

    /**
     * Adiciona uma quantidade de uma nota/moeda ao caixa.
     */
    public static void adicionarNotaMoeda(Dinheiro tipo, int quantidade) {
        notasMoedas.put(tipo, notasMoedas.getOrDefault(tipo, 0) + quantidade);
    }

    /**
     * Remove uma quantidade de uma nota/moeda do caixa.
     */
    public static void removerNotaMoeda(Dinheiro tipo, int quantidade) {
        int atual = notasMoedas.getOrDefault(tipo, 0);
        if (quantidade > atual) throw new IllegalArgumentException("Quantidade insuficiente no caixa.");
        notasMoedas.put(tipo, atual - quantidade);
    }

    /**
     * Retorna o valor total disponível no caixa.
     */
    public static double getValorTotal() {
        double total = 0.0;
        for (Map.Entry<Dinheiro, Integer> entry : notasMoedas.entrySet()) {
            total += entry.getKey().getValor() * entry.getValue();
        }
        return total;
    }

    /**
     * Retorna o mapa de notas/moedas disponíveis.
     */
    public static Map<Dinheiro, Integer> getNotasMoedas() {
        return notasMoedas;
    }

    /**
     * Verifica se é possível dar o troco solicitado com as notas/moedas disponíveis.
     */
    public static boolean podeDarTroco(double troco) {
        double restante = troco;
        Map<Dinheiro, Integer> temp = new HashMap<>(notasMoedas);
        List<Dinheiro> ordenadas = new ArrayList<>(temp.keySet());
        ordenadas.sort((a, b) -> Double.compare(b.getValor(), a.getValor()));
        for (Dinheiro tipo : ordenadas) {
            double valor = tipo.getValor();
            int qtd = temp.get(tipo);
            while (restante >= valor - 0.001 && qtd > 0) {
                restante -= valor;
                qtd--;
            }
            temp.put(tipo, qtd);
        }
        return restante < 0.009;
    }

    /**
     * Salva o estado atual do caixa no arquivo JSON.
     */
    public static void salvarCaixa() {
        JsonManager.salvarCaixa(notasMoedas, CAMINHO_CAIXA);
    }

    /**
     * Carrega o estado do caixa do arquivo JSON.
     * Substitui o mapa atual pelo carregado.
     */
    public static void carregarCaixa() {
        Map<Dinheiro, Integer> carregado = JsonManager.carregarCaixa(CAMINHO_CAIXA);
        if (carregado != null) {
            notasMoedas = carregado;
        }
    }

    /**
     * Retorna o saldo total do caixa.
     */
    public static double consultarSaldoCaixa() {
        return getValorTotal();
    }

    /**
     * Registra uma entrada manual de dinheiro no caixa.
     */
    public static void registrarEntrada(Dinheiro tipo, int quantidade) {
        adicionarNotaMoeda(tipo, quantidade);
        salvarCaixa();
    }

    /**
     * Registra uma saída manual de dinheiro do caixa.
     */
    public static void registrarSaida(Dinheiro tipo, int quantidade) {
        removerNotaMoeda(tipo, quantidade);
        salvarCaixa();
    }

    /**
     * Retorna um mapa imutável detalhando o conteúdo do caixa.
     */
    public static Map<Dinheiro, Integer> detalharConteudoCaixa() {
        return Collections.unmodifiableMap(new HashMap<>(notasMoedas));
    }

    /**
     * Retira um valor do caixa, usando as maiores denominações possíveis.
     * Lança IllegalArgumentException se não for possível.
     */
    /**
     * Retira um valor do caixa e retorna o detalhamento das notas/moedas retiradas.
     * @param valor Valor a ser retirado
     * @return Mapa com a quantidade de cada nota/moeda retirada
     */
    public static Map<Dinheiro, Integer> retirarDoCaixaDetalhado(double valor) {
        carregarCaixa();
        if (valor <= 0) throw new IllegalArgumentException("Valor inválido.");
        int valorCentavos = (int)Math.round(valor * 100);
        List<Dinheiro> ordenadas = new ArrayList<>(Arrays.asList(Dinheiro.values()));
        ordenadas.sort((a, b) -> Double.compare(b.getValor(), a.getValor()));
        Map<Dinheiro, Integer> retirada = new HashMap<>();
        int restante = valorCentavos;
        for (Dinheiro tipo : ordenadas) {
            int disponivel = notasMoedas.getOrDefault(tipo, 0);
            int valTipo = (int)Math.round(tipo.getValor() * 100);
            int qtd = Math.min(restante / valTipo, disponivel);
            if (qtd > 0) {
                retirada.put(tipo, qtd);
                restante -= qtd * valTipo;
            }
        }
        if (restante > 0) throw new IllegalArgumentException("Não há notas/moedas suficientes para retirar esse valor.");
        // Remove as notas/moedas do caixa
        for (Map.Entry<Dinheiro, Integer> entry : retirada.entrySet()) {
            removerNotaMoeda(entry.getKey(), entry.getValue());
        }
        salvarCaixa();
        return retirada;
    }

    /**
     * Compatibilidade: método antigo sem detalhamento (mantido para não quebrar chamadas antigas)
     */
    public static void retirarDoCaixa(double valor) {
        retirarDoCaixaDetalhado(valor);
    }
}
