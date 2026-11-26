package com.eltrebol.pos.sales;

import com.eltrebol.pos.inventory.InventoryService;
import com.eltrebol.pos.inventory.Producto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Service
public class SalesService {

    private final VentaRepository ventaRepository;
    private final InventoryService inventoryService;

    public SalesService(VentaRepository ventaRepository, InventoryService inventoryService) {
        this.ventaRepository = ventaRepository;
        this.inventoryService = inventoryService;
    }

    public record ItemCarrito(Long productoId, Integer cantidad) {}

    @Transactional
    public Venta procesarVenta(String cajero, String metodoPago, Double montoRecibido, List<ItemCarrito> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("El carrito de venta está vacío");
        }

        Venta venta = new Venta();
        venta.setCajero(cajero);
        venta.setMetodoPago(metodoPago != null ? metodoPago : "EFECTIVO");
        venta.setFechaHora(LocalDateTime.now());

        double totalCalculado = 0.0;

        for (ItemCarrito item : items) {
            Producto producto = inventoryService.descontarStock(item.productoId(), item.cantidad());

            DetalleVenta detalle = new DetalleVenta();
            detalle.setVenta(venta);
            detalle.setProductoId(producto.getId());
            detalle.setNombreProducto(producto.getNombre());
            detalle.setCantidad(item.cantidad());
            detalle.setPrecioUnitario(producto.getPrecioVenta());
            detalle.setSubtotal(Math.round(producto.getPrecioVenta() * item.cantidad() * 100.0) / 100.0);

            venta.getDetalles().add(detalle);
            totalCalculado += detalle.getSubtotal();
        }

        totalCalculado = Math.round(totalCalculado * 100.0) / 100.0;
        double recibido = (montoRecibido != null) ? montoRecibido : totalCalculado;

        if ("EFECTIVO".equalsIgnoreCase(venta.getMetodoPago()) && recibido < totalCalculado) {
            throw new IllegalArgumentException("El monto recibido ($" + recibido + ") es menor al total ($" + totalCalculado + ")");
        }

        venta.setTotal(totalCalculado);
        venta.setMontoRecibido(recibido);
        venta.setCambio(Math.round((recibido - totalCalculado) * 100.0) / 100.0);

        return ventaRepository.save(venta);
    }

    public Map<String, Object> obtenerCorteDiario(LocalDate fecha) {
        LocalDate dia = (fecha != null) ? fecha : LocalDate.now();
        List<Venta> ventas = ventaRepository.findByFechaHoraBetweenOrderByFechaHoraDesc(
                dia.atStartOfDay(), dia.atTime(LocalTime.MAX)
        );
        double totalDia = ventas.stream().mapToDouble(Venta::getTotal).sum();
        totalDia = Math.round(totalDia * 100.0) / 100.0;

        return Map.of(
                "fecha", dia.toString(),
                "cantidadVentas", ventas.size(),
                "totalRecaudado", totalDia,
                "ventas", ventas
        );
    }
}