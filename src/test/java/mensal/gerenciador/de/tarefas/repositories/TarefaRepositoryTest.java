package mensal.gerenciador.de.tarefas.repositories;

import mensal.gerenciador.de.tarefas.models.Tarefa;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import mensal.gerenciador.de.tarefas.models.Usuario;


@SpringBootTest
public class TarefaRepositoryTest {

	
    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuario;

    
    @BeforeEach
    void setUp() {
        
        usuario = new Usuario("João", "joao@example.com");
        usuarioRepository.save(usuario);
    }

    
    @AfterEach
    void tearDown() {
        
        tarefaRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    
    @Test
    void deveSalvarETrazerTarefaComSucesso() {
        
        Tarefa tarefa = new Tarefa("Estudar", "Estudar para a prova", LocalDate.now().plusDays(1), usuario);
        Tarefa tarefaSalva = tarefaRepository.save(tarefa);

        
        assertNotNull(tarefaSalva.getId());
        assertEquals("Estudar", tarefaSalva.getTitulo());
        assertEquals(usuario.getId(), tarefaSalva.getUsuario().getId());
    }

    
    @Test
    void deveBuscarTarefasPorUsuario() {
        
        Tarefa tarefa = new Tarefa("Estudar", "Estudar para a prova", LocalDate.now().plusDays(1), usuario);
        tarefaRepository.save(tarefa);

        
        List<Tarefa> tarefas = tarefaRepository.findByUsuarioId(usuario.getId());

        
        assertFalse(tarefas.isEmpty());
        assertEquals(1, tarefas.size());
        assertEquals("Estudar", tarefas.get(0).getTitulo());
    }
}