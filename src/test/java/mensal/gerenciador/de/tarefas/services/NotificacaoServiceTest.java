package mensal.gerenciador.de.tarefas.services;


import mensal.gerenciador.de.tarefas.models.Tarefa;
import mensal.gerenciador.de.tarefas.models.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class NotificacaoServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private NotificacaoService notificacaoService;

    public NotificacaoServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve enviar e-mail de notificação de criação de tarefa")
    public void deveEnviarEmailNotificacaoCriacao() {
        
        Usuario usuario = new Usuario("João", "fernandosuertemiranda@gmail.com");
        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo("Nova Tarefa");
        tarefa.setUsuario(usuario);

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

    
        notificacaoService.enviarNotificacaoCriacao(tarefa);

        
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("Deve enviar e-mail de notificação de tarefa perto de vencer")
    public void deveEnviarEmailNotificacaoPertoDeVencer() {
        
        Usuario usuario = new Usuario("Maria", "fernandosuertemiranda@gmail.com");
        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo("Tarefa Perto de Vencer");
        tarefa.setUsuario(usuario);

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        
        notificacaoService.enviarNotificacaoPertoDeVencer(tarefa);

        
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("Deve enviar e-mail de notificação de tarefa vencida")
    public void deveEnviarEmailNotificacaoVencida() {
        
        Usuario usuario = new Usuario("Carlos", "fernandosuertemiranda@gmail.com");
        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo("Tarefa Vencida");
        tarefa.setUsuario(usuario);

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        
        notificacaoService.enviarNotificacaoVencida(tarefa);

        
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao falhar no envio de e-mail")
    public void deveLancarExcecaoAoFalharEnvioEmail() {
        
        Usuario usuario = new Usuario("João", "joao@example.com");
        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo("Nova Tarefa");
        tarefa.setUsuario(usuario);

        doThrow(new MailSendException("Falha no envio")).when(mailSender).send(any(SimpleMailMessage.class));

        
        assertThrows(MailSendException.class, () -> {
            notificacaoService.enviarNotificacaoCriacao(tarefa);
        });

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}