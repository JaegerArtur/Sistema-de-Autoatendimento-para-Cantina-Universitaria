
/**
 * Controller responsável pela autenticação de usuários (membro e admin) no sistema.
 * Realiza validação de senha e delega busca de usuários ao serviço correspondente.
 *
 * @author Grupo Artur, João Lucas e Miguel
 * @version 1.0
 */
package controller;

import service.UsuarioService;
import model.Usuario;
import model.Membro;
import model.Admin;
import exception.UsuarioNaoExisteException;

public class LoginController {

    public static void carregarUsuarios() {
        UsuarioService.carregarUsuarios();
    }

    public Usuario loginMembro(String cpf, String senha) throws UsuarioNaoExisteException {
        Usuario usuario = UsuarioService.getUsuarioPorCpf(cpf);
        if (usuario instanceof Membro) {
            if (((Membro)usuario).validaSenha(senha)) {
                return usuario;
            } else {
                throw new IllegalArgumentException("Senha incorreta.");
            }
        } else {
            throw new IllegalArgumentException("Usuário não é membro.");
        }
    }

    public Usuario loginAdmin(String login, String senha) throws UsuarioNaoExisteException {
        Usuario usuario = UsuarioService.getUsuarioPorLogin(login);
        if (usuario instanceof Admin) {
            if (((Admin)usuario).validaSenha(senha)) {
                return usuario;
            } else {
                throw new IllegalArgumentException("Senha incorreta.");
            }
        } else {
            throw new IllegalArgumentException("Usuário não é admin.");
        }
    }
}
