package mensal.gerenciador.de.tarefas.repositories;

import mensal.gerenciador.de.tarefas.models.Usuario;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @AfterEach
    public void tearDown() {
        usuarioRepository.deleteAll();
    }

    
    @Test
    @DisplayName("Deve salvar e recuperar usuário por e-mail")
    public void deveSalvarERecuperarUsuarioPorEmail() {
        
        Usuario usuario = new Usuario("Maria", "maria@example.com");
        usuarioRepository.save(usuario);

        
        Optional<Usuario> encontrado = usuarioRepository.findByEmail("maria@example.com");

        
        assertTrue(encontrado.isPresent());
        assertEquals("Maria", encontrado.get().getNome());
    }

    
    @Test
    @DisplayName("Deve retornar vazio quando e-mail não existe")
    public void deveRetornarVazioQuandoEmailNaoExiste() {
        
        Optional<Usuario> encontrado = usuarioRepository.findByEmail("naoexiste@example.com");

        
        assertFalse(encontrado.isPresent());
    }
}