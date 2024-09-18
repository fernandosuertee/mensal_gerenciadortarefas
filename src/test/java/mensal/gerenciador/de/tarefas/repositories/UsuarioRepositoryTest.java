package mensal.gerenciador.de.tarefas.repositories;

import mensal.gerenciador.de.tarefas.models.Usuario;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class UsuarioRepositoryTest {

	
    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuario;

    
    @BeforeEach
    void setUp() {
        
        usuario = new Usuario("Maria", "maria@example.com");
        usuarioRepository.save(usuario);
    }

    
    @AfterEach
    void tearDown() {
        
        usuarioRepository.deleteAll();
    }

    
    @Test
    void deveSalvarUsuarioComSucesso() {
        
        Optional<Usuario> usuarioSalvo = usuarioRepository.findById(usuario.getId());
        assertTrue(usuarioSalvo.isPresent());
        assertEquals("Maria", usuarioSalvo.get().getNome());
        assertEquals("maria@example.com", usuarioSalvo.get().getEmail());
    }

    
    @Test
    void deveBuscarUsuarioPorEmail() {
        
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByEmail("maria@example.com");

        
        assertTrue(usuarioEncontrado.isPresent());
        assertEquals("Maria", usuarioEncontrado.get().getNome());
    }

    
    @Test
    void deveRetornarVazioQuandoEmailNaoExistir() {
        
        Optional<Usuario> usuarioNaoExistente = usuarioRepository.findByEmail("inexistente@example.com");

        
        assertFalse(usuarioNaoExistente.isPresent());
    }

    
    @Test
    void deveAtualizarUsuarioComSucesso() {
        
        usuario.setNome("Maria Silva");
        usuario.setEmail("maria.silva@example.com");
        Usuario usuarioAtualizado = usuarioRepository.save(usuario);

        
        assertEquals("Maria Silva", usuarioAtualizado.getNome());
        assertEquals("maria.silva@example.com", usuarioAtualizado.getEmail());
    }

    
    @Test
    void deveDeletarUsuarioComSucesso() {
        
        usuarioRepository.delete(usuario);

        
        Optional<Usuario> usuarioDeletado = usuarioRepository.findById(usuario.getId());
        assertFalse(usuarioDeletado.isPresent());
    }
}