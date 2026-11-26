package com.eltrebol.pos.sales;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sales")
public class SalesController {

    private final SalesService salesService;

    public SalesController(SalesService salesService) {
        this.salesService = salesService;
    }

    public record SolicitudVentaRequest(
            String metodoPago,
            Double montoRecibido,
            List<SalesService.ItemCarrito> items
    ) {}

    @PostMapping
    public ResponseEntity<?> registrarVenta(@RequestBody SolicitudVentaRequest req, Authentication auth) {
        try {
            String cajero = (auth != null) ? auth.getName() : "mostrador";
            Venta v = salesService.procesarVenta(cajero, req.metodoPago(), req.montoRecibido(), req.items());
            return ResponseEntity.ok(v);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/daily-summary")
    public ResponseEntity<?> corteDiario(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(salesService.obtenerCorteDiario(date));
    }
}