package com.eltrebol.pos.config;

import com.eltrebol.pos.auth.Usuario;
import com.eltrebol.pos.auth.UsuarioRepository;
import com.eltrebol.pos.inventory.Producto;
import com.eltrebol.pos.inventory.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(UsuarioRepository userRepo, ProductoRepository prodRepo, PasswordEncoder encoder) {
        return args -> {
            if (userRepo.count() == 0) {
                Usuario admin = new Usuario();
                admin.setNombreCompleto("Dueño Abarrotes El Trébol");
                admin.setUsername("admin");
                admin.setPasswordHash(encoder.encode("admin123"));
                admin.setRol("ADMIN");

                Usuario cajero = new Usuario();
                cajero.setNombreCompleto("Cajero Mostrador");
                cajero.setUsername("cajero");
                cajero.setPasswordHash(encoder.encode("cajero123"));
                cajero.setRol("CAJERO");

                userRepo.saveAll(List.of(admin, cajero));
            }

            if (prodRepo.count() == 0) {
                prodRepo.saveAll(List.of(
                    crearProd("7501055300075", "Coca-Cola 600ml", "Bebidas", 19.00, 24, 6),
                    crearProd("7501000111206", "Pan Blanco Bimbo Grande", "Panadería", 48.50, 10, 3),
                    crearProd("7501020548501", "Leche Entera Lala 1L", "Lácteos", 28.00, 15, 5),
                    crearProd("7501011115606", "Sabritas Sal 45g", "Botanas", 18.00, 4, 5),
                    crearProd("7501025402105", "Aceite Nutrioli 850ml", "Despensa", 42.00, 12, 4),
                    crearProd("7501006559019", "Detergente Roma 1kg", "Limpieza", 36.00, 3, 5)
                ));
            }
        };
    }

    private Producto crearProd(String cod, String nom, String cat, double precio, int stock, int min) {
        Producto p = new Producto();
        p.setCodigo(cod);
        p.setNombre(nom);
        p.setCategoria(cat);
        p.setPrecioVenta(precio);
        p.setStockActual(stock);
        p.setStockMinimo(min);
        return p;
    }
}