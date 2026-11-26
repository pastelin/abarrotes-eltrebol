package com.eltrebol.pos.sales;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ventas")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fechaHora = LocalDateTime.now();

    @Column(nullable = false, length = 50)
    private String cajero;

    @Column(nullable = false, length = 30)
    private String metodoPago = "EFECTIVO";

    @Column(nullable = false)
    private Double total;

    @Column(nullable = false)
    private Double montoRecibido;

    @Column(nullable = false)
    private Double cambio;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<DetalleVenta> detalles = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public String getCajero() { return cajero; }
    public void setCajero(String cajero) { this.cajero = cajero; }
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
    public Double getMontoRecibido() { return montoRecibido; }
    public void setMontoRecibido(Double montoRecibido) { this.montoRecibido = montoRecibido; }
    public Double getCambio() { return cambio; }
    public void setCambio(Double cambio) { this.cambio = cambio; }
    public List<DetalleVenta> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleVenta> detalles) { this.detalles = detalles; }
}