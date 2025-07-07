package controller;

import model.Usuario;
import service.UsuarioService;
import exception.UsuarioNaoExisteException;
import exception.SaldoInsuficienteException;

public class UsuarioController
{
    /**
     * Busca um usuário pelo CPF.
     */
    public static Usuario buscarPorCpf(String cpf) throws UsuarioNaoExisteException {
        return UsuarioService.getUsuarioPorCpf(cpf);
    }

    /**
     * Busca um admin pelo login.
     */
    public static Usuario buscarPorLogin(String login) throws UsuarioNaoExisteException {
        return UsuarioService.getUsuarioPorLogin(login);
    }

    /**
     * Adiciona saldo a um membro.
     */
    public static void adicionarSaldo(String cpf, double valor) throws UsuarioNaoExisteException {
        UsuarioService.adicionarSaldoUsuario(cpf, valor);
    }

    /**
     * Subtrai saldo de um membro.
     */
    public static void subtrairSaldo(String cpf, double valor) throws SaldoInsuficienteException,UsuarioNaoExisteException {
        UsuarioService.subtraiSaldoUsuario(cpf, valor);
    }

    /**
     * Retorna o saldo de um membro.
     */
    public static double consultarSaldo(String cpf) throws UsuarioNaoExisteException {
        return UsuarioService.getSaldoUsuario(cpf);
    }
}
