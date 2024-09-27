package mensal.gerenciador.de.tarefas.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import mensal.gerenciador.de.tarefas.models.HistoricoDeAlteracao;

import mensal.gerenciador.de.tarefas.repositories.HistoricoDeAlteracaoRepository;


public class HistoricoDeAlteracaoServiceTest {

    @Mock
    private HistoricoDeAlteracaoRepository historicoRepository;

    @InjectMocks
    private HistoricoDeAlteracaoService historicoService;

    public HistoricoDeAlteracaoServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve encontrar todos os históricos")
    public void deveEncontrarTodosHistoricos() {
    
        HistoricoDeAlteracao historico = new HistoricoDeAlteracao(1L, "tarefa", "criado", "Tarefa criada");
        when(historicoRepository.findAll()).thenReturn(Arrays.asList(historico));


        List<HistoricoDeAlteracao> historicos = historicoService.encontrarTodos();


        assertNotNull(historicos);
        assertEquals(1, historicos.size());
        assertEquals("Tarefa criada", historicos.get(0).getDescricao());
    }

    @Test
    @DisplayName("Deve salvar um histórico de alteração")
    public void deveSalvarHistorico() {

        HistoricoDeAlteracao historico = new HistoricoDeAlteracao(2L, "usuario", "atualizado", "Usuário atualizado");
        when(historicoRepository.save(historico)).thenReturn(historico);


        HistoricoDeAlteracao salvo = historicoService.salvar(historico);

        assertNotNull(salvo);
        assertEquals("Usuário atualizado", salvo.getDescricao());
        verify(historicoRepository, times(1)).save(historico);
    }

    @Test
    @DisplayName("Deve encontrar histórico por entidade ID")
    public void deveEncontrarPorEntidadeId() {

        HistoricoDeAlteracao historico1 = new HistoricoDeAlteracao(3L, "tarefa", "criado", "Tarefa criada");
        HistoricoDeAlteracao historico2 = new HistoricoDeAlteracao(3L, "tarefa", "atualizado", "Tarefa atualizada");
        when(historicoRepository.findByEntidadeId(3L)).thenReturn(Arrays.asList(historico1, historico2));

   
        List<HistoricoDeAlteracao> historicos = historicoService.encontrarPorEntidadeId(3L);

        assertNotNull(historicos);
        assertEquals(2, historicos.size());
    }
    
    
    

}