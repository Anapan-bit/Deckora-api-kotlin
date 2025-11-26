package com.deckora.Dekora;

import com.deckora.Dekora.model.Categoria;
import com.deckora.Dekora.model.Carpeta;
import com.deckora.Dekora.model.Carta;
import com.deckora.Dekora.model.Usuario;

import com.deckora.Dekora.repository.CategoriaRepository;
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

    @Override
    public void run(String... args) throws Exception {

        Faker faker = new Faker();
        Random random = new Random();

        //Categorias
        List<Categoria> categorias = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Categoria categoria = new Categoria();
            categoria.setNombre_categoria(faker.commerce().department());
            categoriaRepo.save(categoria);
            categorias.add(categoria);
        }


        //Usuarios
        List<Usuario> usuarios = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Usuario usuario = new Usuario();
            usuario.setContrasenia_usuario(faker.leagueOfLegends().champion());
            usuario.setNombre_usuario(faker.name().firstName());
            usuario.setApellido_usuario(faker.name().lastName());
            usuario.setCorreo_usuario(faker.internet().emailAddress());
            usuario.setNumero_telefono(Integer.parseInt(faker.number().digits(9)));


            usuarioRepo.save(usuario);
            usuarios.add(usuario);
        }

        //Carpetas
        List<Carpeta> carpetas = new ArrayList<>();

        for (Usuario u : usuarios) {

            // cada usuario tendrá 1 a 3 carpetas
            int cantidadCarpetas = random.nextInt(3) + 1;

            for (int i = 0; i < cantidadCarpetas; i++) {

                Carpeta carpeta = new Carpeta();
                carpeta.setNombre_carpeta("Carpeta " + faker.word().noun());
                carpeta.setUsuario(u);

                carpetaRepo.save(carpeta);
                carpetas.add(carpeta);
            }
        }

        //Cartas
        for (int i = 0; i < 12; i++) {

            Carta carta = new Carta();

            // Campos obligatorios según tu entidad
            carta.setNombre_carta(faker.book().title());
            carta.setEstado(faker.options().option("NM", "EX", "VG", "G"));
            carta.setDescripcion(faker.leagueOfLegends().champion());
            carta.setImagen_url(faker.internet().image()); // URL falsa válida

            // asignar categoría random
            Categoria categoriaRandom = categorias.get(random.nextInt(categorias.size()));
            carta.setCategoria(categoriaRandom);

            // asignar carpetas random (1 o 2)
            List<Carpeta> carpetasAsignadas = new ArrayList<>();

            int carpetasParaAsignar = random.nextInt(2) + 1;

            for (int j = 0; j < carpetasParaAsignar; j++) {
                Carpeta carpetaRandom = carpetas.get(random.nextInt(carpetas.size()));
                if (!carpetasAsignadas.contains(carpetaRandom)) {
                    carpetasAsignadas.add(carpetaRandom);
                }
            }

            carta.setCarpetas(carpetasAsignadas);
            cartaRepo.save(carta);

            // actualizar la relación inversa en Carpeta (ManyToMany)
            for (Carpeta c : carpetasAsignadas) {
                if (c.getCartas() == null)
                    c.setCartas(new ArrayList<>());
                c.getCartas().add(carta);
                carpetaRepo.save(c);
            }
        }

    }
}
