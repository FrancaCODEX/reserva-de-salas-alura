package br.com.francacodex.reservas.service;

import br.com.francacodex.reservas.domain.Usuario;
import br.com.francacodex.reservas.dto.Requests.UsuarioRequest;
import br.com.francacodex.reservas.error.*;
import br.com.francacodex.reservas.repository.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarios;
    private final ReservaRepository reservas;
    public UsuarioService(UsuarioRepository usuarios, ReservaRepository reservas) { this.usuarios = usuarios; this.reservas = reservas; }
    @Transactional(readOnly = true) public Page<Usuario> listar(Pageable pagina) { return usuarios.findAll(pagina); }
    @Transactional(readOnly = true) public Usuario buscar(Long id) { return usuarios.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Usuario nao encontrado: " + id)); }
    @Transactional public Usuario criar(UsuarioRequest r) { return usuarios.save(new Usuario(r.nome(), r.email())); }
    @Transactional public Usuario atualizar(Long id, UsuarioRequest r) {
        var usuario = buscar(id); usuario.atualizar(r.nome(), r.email()); return usuario;
    }
    @Transactional public void remover(Long id) {
        buscar(id);
        if (reservas.existePorUsuario(id)) throw new ConflitoException("Usuario possui historico de reservas");
        usuarios.deleteById(id);
    }
}
