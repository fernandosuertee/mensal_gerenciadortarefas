package mensal.gerenciador.de.tarefas.services;

import mensal.gerenciador.de.tarefas.models.Usuario;
import mensal.gerenciador.de.tarefas.repositories.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    public UsuarioServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve encontrar todos os usuários")
    public void deveEncontrarTodosUsuarios() {
        
        Usuario usuario = new Usuario("Maria", "maria@example.com");
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario));

        
        List<Usuario> usuarios = usuarioService.encontrarTodos();

        
        assertNotNull(usuarios);
        assertEquals(1, usuarios.size());
        assertEquals("Maria", usuarios.get(0).getNome());
    }

    @Test
    @DisplayName("Deve salvar um usuário")
    public void deveSalvarUsuario() {
       
        Usuario usuario = new Usuario("João", "joao@example.com");
        when(usuarioRepository.findByEmail("joao@example.com")).thenReturn(Optional.empty());
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        
        Usuario salvo = usuarioService.salvar(usuario);

        
        assertNotNull(salvo);
        assertEquals("João", salvo.getNome());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar usuário com e-mail já cadastrado")
    public void deveLancarExcecaoEmailJaCadastrado() {
        
        Usuario usuario = new Usuario("Carlos", "carlos@example.com");
        when(usuarioRepository.findByEmail("carlos@example.com")).thenReturn(Optional.of(usuario));

        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.salvar(usuario);
        });

        assertEquals("E-mail já cadastrado. Tente com outro e-mail.", exception.getMessage());
        verify(usuarioRepository, never()).save(usuario);
    }

    @Test
    @DisplayName("Deve deletar um usuário")
    public void deveDeletarUsuario() {
        
        Long usuarioId = 1L;
        doNothing().when(usuarioRepository).deleteById(usuarioId);

       
        usuarioService.deletarPorId(usuarioId);

        
        verify(usuarioRepository, times(1)).deleteById(usuarioId);
    }
    
    
    @Test
    @DisplayName("Deve lançar exceção ao deletar usuário inexistente")
    public void deveLancarExcecaoAoDeletarUsuarioInexistente() {
       
        Long usuarioId = 999L;
        doThrow(new EmptyResultDataAccessException(1)).when(usuarioRepository).deleteById(usuarioId);

        
        assertThrows(EmptyResultDataAccessException.class, () -> {
            usuarioService.deletarPorId(usuarioId);
        });

        verify(usuarioRepository, times(1)).deleteById(usuarioId);
    }

}