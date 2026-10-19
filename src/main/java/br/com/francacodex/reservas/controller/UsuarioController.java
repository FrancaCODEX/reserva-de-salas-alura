package br.com.francacodex.reservas.controller;

import br.com.francacodex.reservas.dto.Requests.UsuarioRequest;
import br.com.francacodex.reservas.dto.Responses.UsuarioResponse;
import br.com.francacodex.reservas.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/v1/usuarios")
public class UsuarioController {
    private final UsuarioService service;
    public UsuarioController(UsuarioService service) { this.service = service; }
    @GetMapping public Page<UsuarioResponse> listar(Pageable pageable) { return service.listar(pageable).map(UsuarioResponse::of); }
    @GetMapping("/{id}") public UsuarioResponse buscar(@PathVariable Long id) { return UsuarioResponse.of(service.buscar(id)); }
    @PostMapping public ResponseEntity<UsuarioResponse> criar(@Valid @RequestBody UsuarioRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioResponse.of(service.criar(r)));
    }
    @PutMapping("/{id}") public UsuarioResponse atualizar(@PathVariable Long id, @Valid @RequestBody UsuarioRequest r) { return UsuarioResponse.of(service.atualizar(id, r)); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void remover(@PathVariable Long id) { service.remover(id); }
}
