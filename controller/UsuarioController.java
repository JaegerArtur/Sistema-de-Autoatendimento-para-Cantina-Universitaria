
/**
 * Controller responsável por operações de saldo e busca de usuários (membro/admin).
 * Permite adicionar, subtrair e consultar saldo, além de buscar usuários por CPF/login.
 *
 * @author Grupo Artur, João Lucas e Miguel
 * @version 1.0
 */
package controller;

import model.Usuario;
import service.UsuarioService;
import exception.UsuarioNaoExisteException;
import exception.SaldoInsuficienteException;

public class UsuarioController {
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
     * Altera o saldo de um membro (valor positivo para adicionar, negativo para subtrair).
     */
    public static void alterarSaldo(String cpf, double valor) throws SaldoInsuficienteException, UsuarioNaoExisteException {
        UsuarioService.alterarSaldoMembro(cpf, valor);
    }

    /**
     * Consulta o saldo de um membro.
     */
    public static double consultarSaldo(String cpf) throws UsuarioNaoExisteException {
        return UsuarioService.consultarSaldoMembro(cpf);
    }
}
