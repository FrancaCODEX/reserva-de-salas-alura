package br.com.francacodex.reservas.controller;

import br.com.francacodex.reservas.dto.Requests.SalaRequest;
import br.com.francacodex.reservas.dto.Responses.SalaResponse;
import br.com.francacodex.reservas.service.SalaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/v1/salas")
public class SalaController {
    private final SalaService service;
    public SalaController(SalaService service) { this.service = service; }
    @GetMapping public Page<SalaResponse> listar(Pageable pageable) { return service.listar(pageable).map(SalaResponse::of); }
    @GetMapping("/{id}") public SalaResponse buscar(@PathVariable Long id) { return SalaResponse.of(service.buscar(id)); }
    @PostMapping public ResponseEntity<SalaResponse> criar(@Valid @RequestBody SalaRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(SalaResponse.of(service.criar(r)));
    }
    @PutMapping("/{id}") public SalaResponse atualizar(@PathVariable Long id, @Valid @RequestBody SalaRequest r) { return SalaResponse.of(service.atualizar(id, r)); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void remover(@PathVariable Long id) { service.remover(id); }
}
