package mensal.gerenciador.de.tarefas.repositories;

import mensal.gerenciador.de.tarefas.models.HistoricoDeAlteracao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class HistoricoDeAlteracaoRepositoryTest {

    @Autowired
    private HistoricoDeAlteracaoRepository historicoRepository;

    @AfterEach
    public void tearDown() {
        historicoRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve salvar e recuperar histórico de alteração")
    public void deveSalvarERecuperarHistorico() {
        
        HistoricoDeAlteracao historico = new HistoricoDeAlteracao(1L, "tarefa", "criado", "Descrição de teste");
        historico.setDataAlteracao(LocalDateTime.now());
        historicoRepository.save(historico);

        
        List<HistoricoDeAlteracao> encontrados = historicoRepository.findByEntidadeId(1L);

       
        assertNotNull(encontrados);
        assertEquals(1, encontrados.size());
        assertEquals("Descrição de teste", encontrados.get(0).getDescricao());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há histórico para o ID")
    public void deveRetornarListaVazia() {
      
        List<HistoricoDeAlteracao> encontrados = historicoRepository.findByEntidadeId(999L);

 
        assertTrue(encontrados.isEmpty());
    }
}