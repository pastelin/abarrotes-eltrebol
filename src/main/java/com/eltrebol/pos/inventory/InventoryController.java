package com.eltrebol.pos.inventory;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<?> listar(@RequestParam(required = false) String q) {
        return ResponseEntity.ok(inventoryService.buscar(q));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<?> stockBajo() {
        return ResponseEntity.ok(inventoryService.obtenerStockBajo());
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Producto producto) {
        try {
            return ResponseEntity.ok(inventoryService.guardar(producto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @Valid @RequestBody Producto producto) {
        try {
            return ResponseEntity.ok(inventoryService.actualizar(id, producto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/restock")
    public ResponseEntity<?> resurtir(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        try {
            int cantidad = body.getOrDefault("cantidad", 0);
            return ResponseEntity.ok(inventoryService.registrarResurtido(id, cantidad));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> desactivar(@PathVariable Long id) {
        inventoryService.desactivar(id);
        return ResponseEntity.ok(Map.of("mensaje", "Producto desactivado"));
    }
}