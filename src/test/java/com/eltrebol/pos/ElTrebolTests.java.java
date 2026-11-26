package com.eltrebol.pos;

import com.eltrebol.pos.auth.AuthService;
import com.eltrebol.pos.inventory.InventoryService;
import com.eltrebol.pos.inventory.Producto;
import com.eltrebol.pos.sales.SalesService;
import com.eltrebol.pos.sales.Venta;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ElTrebolTests {

    @Autowired private AuthService authService;
    @Autowired private InventoryService inventoryService;
    @Autowired private SalesService salesService;

    @Test
    void pruebaLoginExitosoYTokenJwt() {
        Map<String, Object> resp = authService.login("admin", "admin123");
        assertNotNull(resp.get("token"), "Debe retornar un token JWT válido");
        assertEquals("ADMIN", resp.get("rol"));
    }

    @Test
    void pruebaLoginCredencialesInvalidas() {
        assertThrows(IllegalArgumentException.class, () -> {
            authService.login("admin", "claveIncorrecta");
        });
    }

    @Test
    void pruebaRegistroProductoYAlertaStockBajo() {
        Producto p = new Producto();
        p.setCodigo("TEST-001");
        p.setNombre("Galletas Marías 170g");
        p.setCategoria("Galletas");
        p.setPrecioVenta(16.50);
        p.setStockActual(2);
        p.setStockMinimo(5);

        Producto guardado = inventoryService.guardar(p);
        assertNotNull(guardado.getId());
        assertTrue(guardado.isStockBajo(), "Debe detectar stock bajo cuando stockActual <= stockMinimo");
    }

    @Test
    void pruebaVentaDescuentaStockYCalculaCambio() {
        Producto p = new Producto();
        p.setCodigo("TEST-002");
        p.setNombre("Refresco Manzana 600ml");
        p.setCategoria("Bebidas");
        p.setPrecioVenta(20.00);
        p.setStockActual(10);
        p.setStockMinimo(2);
        Producto guardado = inventoryService.guardar(p);

        Venta venta = salesService.procesarVenta(
                "cajero",
                "EFECTIVO",
                100.00,
                List.of(new SalesService.ItemCarrito(guardado.getId(), 3))
        );

        assertEquals(60.00, venta.getTotal());
        assertEquals(40.00, venta.getCambio());
        assertEquals(7, inventoryService.buscar("TEST-002").get(0).getStockActual());
    }

    @Test
    void pruebaVentaRechazadaPorStockInsuficiente() {
        Producto p = new Producto();
        p.setCodigo("TEST-003");
        p.setNombre("Atún en Agua");
        p.setCategoria("Enlatados");
        p.setPrecioVenta(25.00);
        p.setStockActual(1);
        p.setStockMinimo(2);
        Producto guardado = inventoryService.guardar(p);

        assertThrows(IllegalStateException.class, () -> {
            salesService.procesarVenta(
                    "cajero",
                    "EFECTIVO",
                    100.00,
                    List.of(new SalesService.ItemCarrito(guardado.getId(), 5))
            );
        });
    }
}