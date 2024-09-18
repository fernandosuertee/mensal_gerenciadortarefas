package mensal.gerenciador.de.tarefas.services;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mensal.gerenciador.de.tarefas.models.Tarefa;
import mensal.gerenciador.de.tarefas.models.Usuario;

class NotificacaoServiceTest {

    private NotificacaoService notificacaoService;
    private Tarefa tarefa;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        notificacaoService = new NotificacaoService();
        usuario = new Usuario("Test User", "test@example.com");
        tarefa = new Tarefa("Titulo Teste", "Descricao Teste", LocalDate.now().plusDays(5), usuario);
    }

    @Test
    void testEnviarNotificacaoComSucesso() {
        String expectedMessage = String.format("Notificação enviada para %s sobre a tarefa: %s",
                usuario.getEmail(), tarefa.getTitulo());

        String result = notificacaoService.enviarNotificacao(tarefa);
        assertEquals(expectedMessage, result);
    }

    @Test
    void testEnviarNotificacaoSemUsuario() {
        tarefa.setUsuario(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            notificacaoService.enviarNotificacao(tarefa);
        });

        assertEquals("Usuário ou e-mail do usuário é inválido", exception.getMessage());
    }

    @Test
    void testAnalisarAtividades() {
    	
        Tarefa tarefa2 = new Tarefa("Outra Tarefa", "Outra Descricao", LocalDate.now().plusDays(2), usuario);
        List<Tarefa> tarefas = Arrays.asList(tarefa, tarefa2);

        assertDoesNotThrow(() -> notificacaoService.analisarAtividades(tarefas));
    }
}