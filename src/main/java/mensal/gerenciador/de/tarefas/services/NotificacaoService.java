package mensal.gerenciador.de.tarefas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import mensal.gerenciador.de.tarefas.models.Tarefa;

@Service
public class NotificacaoService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:no-reply@dominio.com}") 
    private String fromEmail; 

    public void enviarNotificacaoCriacao(Tarefa tarefa) {
        enviarEmail(tarefa.getUsuario().getEmail(), "Nova Tarefa Criada", "Sua tarefa " + tarefa.getTitulo() + " foi criada com sucesso.");
    }

    public void enviarNotificacaoPertoDeVencer(Tarefa tarefa) {
        enviarEmail(tarefa.getUsuario().getEmail(), "Tarefa Perto de Vencer", "Sua tarefa " + tarefa.getTitulo() + " está perto de vencer.");
    }

    public void enviarNotificacaoVencida(Tarefa tarefa) {
        enviarEmail(tarefa.getUsuario().getEmail(), "Tarefa Vencida", "Sua tarefa " + tarefa.getTitulo() + " já venceu.");
    }

    public void enviarEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail); 
        message.setTo(to);          
        message.setSubject(subject); 
        message.setText(text);      
        mailSender.send(message);   
    }
}