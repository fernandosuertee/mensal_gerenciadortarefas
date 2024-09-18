package mensal.gerenciador.de.tarefas.services;

import mensal.gerenciador.de.tarefas.models.Tarefa;
import mensal.gerenciador.de.tarefas.models.Usuario;
import mensal.gerenciador.de.tarefas.repositories.TarefaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TarefaServiceTest {

	
    @Mock
    private TarefaRepository tarefaRepository;

    @InjectMocks
    private TarefaService tarefaService;

    private Usuario usuario;
    
    private Tarefa tarefa;

    
    @BeforeEach
    void configurar() {
    	
        MockitoAnnotations.openMocks(this);
        
        usuario = new Usuario("João", "joao@example.com");
        
        usuario.setId(1L); 
        
        tarefa = new Tarefa("Estudar", "Estudar para a prova", LocalDate.now().plusDays(1), usuario);
    }

    @AfterEach
    void limpar() {
    	
        usuario = null;
        
        tarefa = null;
    }

    @Test
    void deveEncontrarTodasTarefas() {
        when(tarefaRepository.findAll()).thenReturn(List.of(tarefa));

        List<Tarefa> tarefas = tarefaService.encontrarTodas();

        assertFalse(tarefas.isEmpty());
        assertEquals(1, tarefas.size());
        verify(tarefaRepository, times(1)).findAll();
    }

    @Test
    void deveEncontrarTarefaPorId() {
    	
        when(tarefaRepository.findById(any(Long.class))).thenReturn(Optional.of(tarefa));

        Tarefa tarefaEncontrada = tarefaService.encontrarPorId(1L);

        assertNotNull(tarefaEncontrada);
        assertEquals("Estudar", tarefaEncontrada.getTitulo());
        verify(tarefaRepository, times(1)).findById(1L);
    }

    @Test
    void deveRetornarNuloQuandoTarefaNaoEncontrada() {
    	
        when(tarefaRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        Tarefa tarefaEncontrada = tarefaService.encontrarPorId(1L);

        assertNull(tarefaEncontrada);
        
        verify(tarefaRepository, times(1)).findById(1L);
    }

    @Test
    void deveSalvarTarefa() {
    	
        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefa);

        Tarefa tarefaSalva = tarefaService.salvar(tarefa);

        assertNotNull(tarefaSalva);
        
        assertEquals("Estudar", tarefaSalva.getTitulo());
        
        verify(tarefaRepository, times(1)).save(tarefa);
    }

    @Test
    void deveDeletarTarefaPorId() {
    	
        doNothing().when(tarefaRepository).deleteById(any(Long.class));

        tarefaService.deletarPorId(1L);

        verify(tarefaRepository, times(1)).deleteById(1L);
    }

    @Test
    void deveEncontrarTarefasPorUsuarioId() {
        // Configura o mock para retornar uma lista com a tarefa quando o ID for o esperado
        when(tarefaRepository.findByUsuarioId(usuario.getId())).thenReturn(List.of(tarefa));

        // Chama o método a ser testado
        List<Tarefa> tarefas = tarefaService.encontrarPorUsuarioId(usuario.getId());

        // Verificações adicionais de depuração
        System.out.println("ID do usuário: " + usuario.getId());
        System.out.println("Tarefas retornadas: " + tarefas.size());

        // Verifica se a lista de tarefas não está vazia
        assertFalse(tarefas.isEmpty(), "A lista de tarefas deveria conter elementos.");
        assertEquals(1, tarefas.size(), "A lista de tarefas deveria conter 1 tarefa.");
        verify(tarefaRepository, times(1)).findByUsuarioId(usuario.getId());
    }
    
}