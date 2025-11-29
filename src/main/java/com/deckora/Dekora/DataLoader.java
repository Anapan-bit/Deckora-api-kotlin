package com.deckora.Dekora;

import com.deckora.Dekora.model.Categoria;
import com.deckora.Dekora.model.Carpeta;
import com.deckora.Dekora.model.Carta;
import com.deckora.Dekora.model.Usuario;
import com.deckora.Dekora.model.Resumen;

import com.deckora.Dekora.repository.CategoriaRepository;
import com.deckora.Dekora.repository.ResumenRepository;
import com.deckora.Dekora.repository.CarpetaRepository;
import com.deckora.Dekora.repository.CartaRepository;
import com.deckora.Dekora.repository.UsuarioRepository;

import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private CategoriaRepository categoriaRepo;
    @Autowired
    private CartaRepository cartaRepo;
    @Autowired
    private CarpetaRepository carpetaRepo;
    @Autowired
    private UsuarioRepository usuarioRepo;
    @Autowired
    private ResumenRepository resumenRepo;

    @Override
    public void run(String... args) throws Exception {

        Faker faker = new Faker();
        Random random = new Random();

        /* =============================
           CATEGORÍAS
        ============================== */
        List<Categoria> categorias = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Categoria categoria = new Categoria();
            categoria.setNombre_categoria(faker.commerce().department());
            categoriaRepo.save(categoria);
            categorias.add(categoria);
        }

        /* =============================
           USUARIOS
        ============================== */
        List<Usuario> usuarios = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Usuario usuario = new Usuario();
            usuario.setContrasenia_usuario(faker.internet().password());
            usuario.setNombre_usuario(faker.name().firstName());
            usuario.setApellido_usuario(faker.name().lastName());
            usuario.setCorreo_usuario(faker.internet().emailAddress());
            usuario.setNumero_telefono(faker.number().numberBetween(600000000, 799999999));

            usuarioRepo.save(usuario);
            usuarios.add(usuario);
        }

        /* =============================
           CARPETAS
        ============================== */
        List<Carpeta> carpetas = new ArrayList<>();

        for (Usuario u : usuarios) {

            int cantidadCarpetas = random.nextInt(3) + 1; // 1–3 carpetas por usuario

            for (int i = 0; i < cantidadCarpetas; i++) {

                Carpeta carpeta = new Carpeta();
                carpeta.setNombre_carpeta("Carpeta " + faker.word().noun());
                carpetaRepo.save(carpeta);

                carpetas.add(carpeta);
            }
        }

        /* =============================
           CARTAS
        ============================== */
        List<Carta> cartas = new ArrayList<>();

        for (int i = 0; i < 12; i++) {

            Carta carta = new Carta();
            carta.setNombre_carta(faker.book().title());
            carta.setEstado(faker.options().option("NM", "EX", "VG", "G"));
            carta.setDescripcion(faker.lorem().sentence());
            carta.setImagen_url(faker.internet().image());

            // categoría
            Categoria categoriaRandom = categorias.get(random.nextInt(categorias.size()));
            carta.setCategoria(categoriaRandom);

            cartaRepo.save(carta);
            cartas.add(carta);
        }

        /* =============================
           RESÚMENES (NEXO)
        ============================== */

        // se generan 20 resúmenes aleatorios conectando usuario - carta - carpeta
        for (int i = 0; i < 20; i++) {

            Resumen resumen = new Resumen();

            Usuario u = usuarios.get(random.nextInt(usuarios.size()));
            Carpeta ca = carpetas.get(random.nextInt(carpetas.size()));
            Carta ct = cartas.get(random.nextInt(cartas.size()));

            resumen.setUsuario(u);
            resumen.setCarpeta(ca);
            resumen.setCarta(ct);

            resumenRepo.save(resumen);
        }

        System.out.println("✔ DataLoader ejecutado correctamente con relaciones actualizadas.");
    }
}
