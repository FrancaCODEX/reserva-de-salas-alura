package br.com.francacodex.reservas.controller;

import br.com.francacodex.reservas.dto.Requests.ReservaRequest;
import br.com.francacodex.reservas.dto.Responses.ReservaResponse;
import br.com.francacodex.reservas.service.ReservaService;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/v1/reservas")
public class ReservaController {
    private final ReservaService service;
    public ReservaController(ReservaService service) { this.service = service; }
    @GetMapping public Page<ReservaResponse> listar(@RequestParam(required = false) Long salaId,
            @RequestParam(required = false) LocalDateTime inicio, @RequestParam(required = false) LocalDateTime fim, Pageable pageable) {
        return service.listar(salaId, inicio, fim, pageable).map(ReservaResponse::of);
    }
    @GetMapping("/{id}") public ReservaResponse buscar(@PathVariable Long id) { return ReservaResponse.of(service.buscar(id)); }
    @PostMapping public ResponseEntity<ReservaResponse> criar(@Valid @RequestBody ReservaRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ReservaResponse.of(service.criar(r)));
    }
    @PutMapping("/{id}") public ReservaResponse atualizar(@PathVariable Long id, @Valid @RequestBody ReservaRequest r) { return ReservaResponse.of(service.atualizar(id, r)); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void cancelar(@PathVariable Long id) { service.cancelar(id); }
    @PatchMapping("/{id}/cancelar") public ReservaResponse cancelarExplicitamente(@PathVariable Long id) { service.cancelar(id); return ReservaResponse.of(service.buscar(id)); }
}
