package br.com.francacodex.reservas.service;

import br.com.francacodex.reservas.domain.Sala;
import br.com.francacodex.reservas.dto.Requests.SalaRequest;
import br.com.francacodex.reservas.error.*;
import br.com.francacodex.reservas.repository.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SalaService {
    private final SalaRepository salas;
    private final ReservaRepository reservas;
    public SalaService(SalaRepository salas, ReservaRepository reservas) { this.salas = salas; this.reservas = reservas; }
    @Transactional(readOnly = true) public Page<Sala> listar(Pageable pagina) { return salas.findAll(pagina); }
    @Transactional(readOnly = true) public Sala buscar(Long id) { return salas.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Sala nao encontrada: " + id)); }
    @Transactional public Sala criar(SalaRequest r) { return salas.save(new Sala(r.nome(), r.capacidade(), r.ativa())); }
    @Transactional public Sala atualizar(Long id, SalaRequest r) {
        var sala = buscar(id); sala.atualizar(r.nome(), r.capacidade(), r.ativa()); return sala;
    }
    @Transactional public void remover(Long id) {
        buscar(id);
        if (reservas.existePorSala(id)) throw new ConflitoException("Sala possui historico de reservas");
        salas.deleteById(id);
    }
}
