package mensal.gerenciador.de.tarefas.services;

import org.springframework.stereotype.Service;

import mensal.gerenciador.de.tarefas.models.Tarefa;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class NotificacaoService {

    // Método para enviar notificação sobre uma tarefa específica
    public String enviarNotificacao(Tarefa tarefa) {
        if (tarefa.getUsuario() == null || tarefa.getUsuario().getEmail() == null) {
            throw new IllegalArgumentException("Usuário ou e-mail do usuário é inválido");
        }

        // Mensagem de notificação simulada
        String mensagem = String.format("Notificação enviada para %s sobre a tarefa: %s",
                tarefa.getUsuario().getEmail(), tarefa.getTitulo());

        System.out.println(mensagem);
        return mensagem;
    }

    // Método para analisar as tarefas e notificar sobre o tempo restante
    public void analisarAtividades(List<Tarefa> tarefas) {
        LocalDate hoje = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Tarefa tarefa : tarefas) {
            LocalDate dataVencimento = tarefa.getDataVencimento();
            long diasRestantes = Duration.between(hoje.atStartOfDay(), dataVencimento.atStartOfDay()).toDays();

            String mensagem = String.format(
                "Usuário: %s | Tarefa: %s | Data de Vencimento: %s | Tempo Restante: %d dias",
                tarefa.getUsuario().getNome(),
                tarefa.getTitulo(),
                dataVencimento.format(formatter),
                diasRestantes
            );

            // Simulação de envio da mensagem como se fosse um e-mail
            System.out.println(mensagem);
        }
    }
}