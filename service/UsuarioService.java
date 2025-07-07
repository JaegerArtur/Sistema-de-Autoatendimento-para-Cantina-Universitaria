package service;

import model.Usuario;
import model.Membro;
import model.Admin;
import java.util.*;
import util.JsonManager;
import exception.UsuarioNaoExisteException;
import exception.SaldoInsuficienteException;

public class UsuarioService
{
    public static final String CAMINHO_USUARIOS = "data/usuarios.json";
    public static List<Usuario> usuarios;

    /**
     * Carrega a lista de usuários do arquivo JSON.
     * 
     * @return Lista de usuários carregados.
     */
    public static void carregarUsuarios() {
        usuarios = JsonManager.carregarUsuarios(CAMINHO_USUARIOS);
    }

    /**
     * Salva a lista de usuários no arquivo JSON.
     * 
     * @param usuarios Lista de usuários a serem salvos.
     */
    public static void salvarUsuarios(List<Usuario> usuarios) {
        JsonManager.salvarUsuarios(usuarios, CAMINHO_USUARIOS);
    }
    
    /**
     * Busca um usuário pelo seu CPF.
     * 
     * @param cpf CPF do usuário a ser buscado.
     * @return O usuário correspondente ao CPF.
     * @throws UsuarioNaoExisteException se o usuário não for encontrado.
     */
    public static Usuario getUsuarioPorCpf(String cpf) throws UsuarioNaoExisteException {
        for (Usuario usuario : usuarios) {
            if (usuario instanceof Membro && ((Membro)usuario).getCpf().equals(cpf)) {
                return usuario;
            }
        }
        throw new UsuarioNaoExisteException(cpf);
    }

    /**
     * Adiciona saldo ao usuário.
     */
    public static void adicionarSaldoUsuario(String cpf, double valor) throws UsuarioNaoExisteException {
        Usuario usuario = getUsuarioPorCpf(cpf);
        if (usuario instanceof Membro) {
            ((Membro)usuario).addSaldo(valor);
            salvarUsuarios(usuarios);
        } else {
            throw new UnsupportedOperationException("Apenas membros possuem saldo.");
        }
    }

    /**
     * Subtrai saldo do usuário.
     */
    public static void subtraiSaldoUsuario(String cpf, double valor) throws SaldoInsuficienteException,UsuarioNaoExisteException {
        Usuario usuario = getUsuarioPorCpf(cpf);
        if (usuario instanceof Membro) {
            ((Membro)usuario).subtraiSaldo(valor);
            salvarUsuarios(usuarios);
        } else {
            throw new UnsupportedOperationException("Apenas membros possuem saldo.");
        }
    }

    /**
     * Busca usuário por login (para admin).
     */
    public static Usuario getUsuarioPorLogin(String login) throws UsuarioNaoExisteException {
        for (Usuario usuario : usuarios) {
            if (usuario instanceof Admin && ((Admin)usuario).getLogin().equals(login)) {
                return usuario;
            }
        }
        throw new UsuarioNaoExisteException(login);
    }

    /**
     * Adiciona saldo ao usuário especificado.
     * 
     * @param cpf   CPF do usuário que receberá o saldo.
     * @param valor Valor a ser adicionado ao saldo do usuário.
     */
    public static double getSaldoUsuario(String cpf) throws UsuarioNaoExisteException {
        Usuario usuario = getUsuarioPorCpf(cpf);
        if (usuario instanceof Membro) {
            return ((Membro)usuario).getSaldo();
        } else {
            throw new UnsupportedOperationException("Apenas membros possuem saldo.");
        }
    }
}
