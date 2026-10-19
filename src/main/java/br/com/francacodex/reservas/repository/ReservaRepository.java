package br.com.francacodex.reservas.repository;

import br.com.francacodex.reservas.domain.Reserva;
import br.com.francacodex.reservas.domain.StatusReserva;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    boolean existsBySalaId(Long salaId);
    boolean existsByUsuarioId(Long usuarioId);
    default boolean existePorSala(Long salaId) { return existsBySalaId(salaId); }
    default boolean existePorUsuario(Long usuarioId) { return existsByUsuarioId(usuarioId); }
    // Interseccao entre intervalos semiabertos: [inicio, fim)
    @Query("select count(r) > 0 from Reserva r where r.sala.id = :salaId and r.status = :status and r.id <> :ignorarId and r.inicio < :fim and :inicio < r.fim")
    boolean existeConflito(@Param("salaId") Long salaId, @Param("status") StatusReserva status,
        @Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim, @Param("ignorarId") Long ignorarId);

    @Query("select r from Reserva r join fetch r.sala join fetch r.usuario where (:salaId is null or r.sala.id = :salaId) and (:inicio is null or r.fim > :inicio) and (:fim is null or r.inicio < :fim)")
    Page<Reserva> consultar(@Param("salaId") Long salaId, @Param("inicio") LocalDateTime inicio,
        @Param("fim") LocalDateTime fim, Pageable pageable);
}
