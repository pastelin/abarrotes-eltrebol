package com.eltrebol.pos.inventory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryService {

    private final ProductoRepository productoRepository;

    public InventoryService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> listarActivos() {
        return productoRepository.findByActivoTrueOrderByNombreAsc();
    }

    public List<Producto> buscar(String query) {
        if (query == null || query.isBlank()) {
            return listarActivos();
        }
        return productoRepository.buscarRapido(query.trim());
    }

    public List<Producto> obtenerStockBajo() {
        return productoRepository.findProductosConStockBajo();
    }

    public Producto guardar(Producto producto) {
        if (producto.getPrecioVenta() == null || producto.getPrecioVenta() <= 0) {
            throw new IllegalArgumentException("El precio de venta debe ser mayor a 0");
        }
        if (producto.getStockActual() == null || producto.getStockActual() < 0) {
            throw new IllegalArgumentException("El stock actual no puede ser negativo");
        }
        return productoRepository.save(producto);
    }

    public Producto actualizar(Long id, Producto datos) {
        Producto existente = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        existente.setCodigo(datos.getCodigo());
        existente.setNombre(datos.getNombre());
        existente.setCategoria(datos.getCategoria());
        existente.setPrecioVenta(datos.getPrecioVenta());
        existente.setStockActual(datos.getStockActual());
        existente.setStockMinimo(datos.getStockMinimo());
        return productoRepository.save(existente);
    }

    public void desactivar(Long id) {
        Producto existente = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        existente.setActivo(false);
        productoRepository.save(existente);
    }

    @Transactional
    public Producto registrarResurtido(Long id, int cantidadAgregar) {
        if (cantidadAgregar <= 0) {
            throw new IllegalArgumentException("La cantidad de resurtido debe ser mayor a 0");
        }
        Producto p = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        p.setStockActual(p.getStockActual() + cantidadAgregar);
        return productoRepository.save(p);
    }

    @Transactional
    public Producto descontarStock(Long id, int cantidadVendida) {
        Producto p = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: ID " + id));
        if (cantidadVendida <= 0) {
            throw new IllegalArgumentException("La cantidad vendida debe ser mayor a 0");
        }
        if (p.getStockActual() < cantidadVendida) {
            throw new IllegalStateException("Stock insuficiente para '" + p.getNombre() + "'. Disponible: " + p.getStockActual());
        }
        p.setStockActual(p.getStockActual() - cantidadVendida);
        return productoRepository.save(p);
    }
}