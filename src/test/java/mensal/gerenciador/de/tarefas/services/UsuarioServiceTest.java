package mensal.gerenciador.de.tarefas.services;

import mensal.gerenciador.de.tarefas.models.Usuario;
import mensal.gerenciador.de.tarefas.repositories.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void configurar() {
        MockitoAnnotations.openMocks(this);
        usuario = new Usuario("Maria", "maria@example.com");
    }

    @AfterEach
    void limpar() {
        usuario = null;
    }

    @Test
    void deveEncontrarTodosUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        List<Usuario> usuarios = usuarioService.encontrarTodos();

        assertFalse(usuarios.isEmpty());
        assertEquals(1, usuarios.size());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void deveEncontrarUsuarioPorId() {
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuario));

        Usuario usuarioEncontrado = usuarioService.encontrarPorId(1L);

        assertNotNull(usuarioEncontrado);
        assertEquals("Maria", usuarioEncontrado.getNome());
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    void deveRetornarNuloQuandoUsuarioNaoEncontrado() {
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        Usuario usuarioEncontrado = usuarioService.encontrarPorId(1L);

        assertNull(usuarioEncontrado);
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    void deveSalvarUsuario() {
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario usuarioSalvo = usuarioService.salvar(usuario);

        assertNotNull(usuarioSalvo);
        assertEquals("Maria", usuarioSalvo.getNome());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaCadastrado() {
        when(usuarioRepository.findByEmail(any(String.class))).thenReturn(Optional.of(usuario));

        assertThrows(IllegalArgumentException.class, () -> usuarioService.salvar(usuario));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void deveDeletarUsuarioPorId() {
        doNothing().when(usuarioRepository).deleteById(anyLong());

        usuarioService.deletarPorId(1L);

        verify(usuarioRepository, times(1)).deleteById(1L);
    }
}