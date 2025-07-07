package controller;

import java.util.Map;
import service.EstoqueService;

public class EstoqueController {

    public static void carregarEstoque() {
        EstoqueService.carregarEstoque();
    }

    public static Map<String, Integer> getMapaProdutos() {
        carregarEstoque();
        return EstoqueService.getMapaProdutos();
    }
}
