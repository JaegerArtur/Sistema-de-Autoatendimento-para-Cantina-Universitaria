package model;

import java.util.UUID;
import model.enums.CategoriaProduto;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Representa um produto disponível na cantina universitária.<p>
 * 
 * Contém informações como nome, preço, validade, imagem e categoria do produto, além de um identificador único.<br>
 * 
 * 
 * @author Grupo Artur, João, Miguel
 * @version 1.0
 */
public class Produto
{
    private String id, nome, dataValidade, img;
    private double preco;
    private CategoriaProduto categoria;

    /**
     * Construtor para a classe Produto.
     * 
     * @param nome Nome do produto.
     * @param dataValidade Data de validade no formato "yyyy-MM-dd".
     * @param img Caminho ou referência para a imagem do produto.
     * @param preco Preço do produto.
     * @param categoria Categoria do produto (COMIDA ou BEBIDA).
     */
    public Produto (String nome, String dataValidade, String img, double preco, CategoriaProduto categoria) {
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.dataValidade = dataValidade;
        this.img = img;
        this.preco = preco;
        this.categoria = categoria;
    }
    
    /**
     * Verifica se o produto está vencido com base na data atual do sistema.
     * 
     * @return {@code true} se a data de validade for anterior à data atual, caso contrário {@code false}.
     */
    public boolean isVencido() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate validade = LocalDate.parse(this.dataValidade, formatter);
        LocalDate hoje = LocalDate.now();
        return validade.isBefore(hoje);
    }

    /** @return O identificador único do produto. */
    public String getId() {
        return this.id;
    }

    /** @return O nome do produto. */
    public String getNome() {
        return this.nome;
    }

    /** @return A data de validade do produto, no formato "yyyy-MM-dd". */
    public String getDataValidade() {
        return this.dataValidade;
    }

    /** @return O caminho ou referência da imagem do produto. */
    public String getImg() {
        return this.img;
    }

    /** @return O preço do produto. */
    public double getPreco() {
        return this.preco;
    }

    /** @return A categoria do produto (COMIDA ou BEBIDA). */
    public CategoriaProduto getCategoria() {
        return this.categoria;
    }

    /**
     * Define uma nova data de validade para o produto.
     * @param novaDataValidade Nova data de validade no formato "yyyy-MM-dd".
     */
    public void setDataValidade(String novaDataValidade) {
        this.dataValidade = novaDataValidade;
    }

    /**
     * Define um novo preço para o produto.
     * @param novoPreco Novo preço do produto.
     */
    public void setPreco(double novoPreco) {
        this.preco = novoPreco;
    }
}
