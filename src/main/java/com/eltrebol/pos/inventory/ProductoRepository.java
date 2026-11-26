package com.eltrebol.pos.inventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByActivoTrueOrderByNombreAsc();

    Optional<Producto> findByCodigoAndActivoTrue(String codigo);

    @Query("SELECT p FROM Producto p WHERE p.activo = true AND (LOWER(p.nombre) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(p.codigo) LIKE LOWER(CONCAT('%', :q, '%')))")
    List<Producto> buscarRapido(@Param("q") String query);

    @Query("SELECT p FROM Producto p WHERE p.activo = true AND p.stockActual <= p.stockMinimo")
    List<Producto> findProductosConStockBajo();
}