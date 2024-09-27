package mensal.gerenciador.de.tarefas.services;

import mensal.gerenciador.de.tarefas.models.*;
import mensal.gerenciador.de.tarefas.repositories.TarefaRepository;
import mensal.gerenciador.de.tarefas.repositories.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;

// Importações estáticas para facilitar a leitura
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TarefaServiceTest {

    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private TarefaService tarefaService;

    public TarefaServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve encontrar todas as tarefas")
    public void deveEncontrarTodasTarefas() {
   
        Usuario usuario = new Usuario("Teste", "teste@example.com");
        Tarefa tarefa = new Tarefa("Título", "Descrição", LocalDate.now().plusDays(5), usuario, Prioridade.MEDIA, Status.NAO_INICIADA);
        when(tarefaRepository.findAll()).thenReturn(Arrays.asList(tarefa));


        List<Tarefa> tarefas = tarefaService.encontrarTodas();


        assertNotNull(tarefas);
        assertEquals(1, tarefas.size());
        assertEquals("Título", tarefas.get(0).getTitulo());
    }

    @Test
    @DisplayName("Deve salvar uma tarefa")
    public void deveSalvarTarefa() {

        Usuario usuario = new Usuario("Teste", "teste@example.com");
        Tarefa tarefa = new Tarefa("Teste", "Descrição teste", LocalDate.now().plusDays(5), usuario, Prioridade.ALTA, Status.NAO_INICIADA);
        when(tarefaRepository.save(tarefa)).thenReturn(tarefa);

        Tarefa salva = tarefaService.salvar(tarefa);


        assertNotNull(salva);
        assertEquals("Teste", salva.getTitulo());
        verify(tarefaRepository, times(1)).save(tarefa);
    }

    @Test
    @DisplayName("Deve encontrar tarefa por ID")
    public void deveEncontrarTarefaPorId() {

        Usuario usuario = new Usuario("Teste", "teste@example.com");
        Tarefa tarefa = new Tarefa("Teste", "Descrição teste", LocalDate.now().plusDays(5), usuario, Prioridade.ALTA, Status.NAO_INICIADA);
        tarefa.setId(1L);
        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefa));

     
        Tarefa encontrada = tarefaService.encontrarPorId(1L);


        assertNotNull(encontrada);
        assertEquals(1L, encontrada.getId());
    }

    @Test
    @DisplayName("Deve deletar uma tarefa")
    public void deveDeletarTarefa() {
  
        Long tarefaId = 1L;
        doNothing().when(tarefaRepository).deleteById(tarefaId);

      
        tarefaService.deletarPorId(tarefaId);


        verify(tarefaRepository, times(1)).deleteById(tarefaId);
    }

    @Test
    @DisplayName("Deve atualizar uma tarefa")
    public void deveAtualizarTarefa() {
        
        Usuario usuario = new Usuario("Teste", "teste@example.com");
        Tarefa tarefaExistente = new Tarefa("Tarefa Antiga", "Descrição antiga", LocalDate.now().plusDays(5), usuario, Prioridade.MEDIA, Status.NAO_INICIADA);
        Tarefa novosDados = new Tarefa("Tarefa Atualizada", "Descrição atualizada", LocalDate.now().plusDays(10), usuario, Prioridade.ALTA, Status.EM_ANDAMENTO);

        when(tarefaRepository.save(tarefaExistente)).thenReturn(tarefaExistente);

      
        Tarefa atualizada = tarefaService.atualizar(tarefaExistente, novosDados);

   
        assertEquals("Tarefa Atualizada", atualizada.getTitulo());
        verify(tarefaRepository, times(1)).save(tarefaExistente);
    }
    
    
    @Test
    @DisplayName("Deve lançar exceção ao deletar tarefa inexistente")
    public void deveLancarExcecaoAoDeletarTarefaInexistente() {
       
        Long tarefaId = 999L;
        doThrow(new EmptyResultDataAccessException(1)).when(tarefaRepository).deleteById(tarefaId);

        
        assertThrows(EmptyResultDataAccessException.class, () -> {
            tarefaService.deletarPorId(tarefaId);
        });

        verify(tarefaRepository, times(1)).deleteById(tarefaId);
    } 
    
    
    @Test
    @DisplayName("Deve lançar exceção ao atualizar tarefa com dados nulos")
    public void deveLancarExcecaoAoAtualizarTarefaComDadosNulos() {
        
        Tarefa tarefaExistente = new Tarefa();
        Tarefa novosDados = null;

        
        assertThrows(NullPointerException.class, () -> {
            tarefaService.atualizar(tarefaExistente, novosDados);
        });
    }
}