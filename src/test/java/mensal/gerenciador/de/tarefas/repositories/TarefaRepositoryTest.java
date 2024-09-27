package mensal.gerenciador.de.tarefas.repositories;

import mensal.gerenciador.de.tarefas.models.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TarefaRepositoryTest {

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @AfterEach
    public void tearDown() {
        tarefaRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve salvar e recuperar tarefas por usuário")
    public void deveSalvarERecuperarTarefasPorUsuario() {
        
        Usuario usuario = new Usuario("Usuário Teste", "usuario@example.com");
        usuarioRepository.save(usuario);

        Tarefa tarefa = new Tarefa("Título", "Descrição", LocalDate.now().plusDays(5), usuario, Prioridade.MEDIA, Status.EM_ANDAMENTO);
        tarefaRepository.save(tarefa);

        List<Tarefa> tarefas = tarefaRepository.findByUsuarioId(usuario.getId());

    
        assertEquals(1, tarefas.size());
        assertEquals("Título", tarefas.get(0).getTitulo());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando usuário não tem tarefas")
    public void deveRetornarListaVaziaQuandoUsuarioNaoTemTarefas() {
     
        Usuario usuario = new Usuario("Usuário Sem Tarefas", "sem_tarefas@example.com");
        usuarioRepository.save(usuario);


        List<Tarefa> tarefas = tarefaRepository.findByUsuarioId(usuario.getId());


        assertTrue(tarefas.isEmpty());
    }
}