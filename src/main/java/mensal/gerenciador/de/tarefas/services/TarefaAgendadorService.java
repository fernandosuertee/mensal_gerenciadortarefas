
package mensal.gerenciador.de.tarefas.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import mensal.gerenciador.de.tarefas.models.Tarefa;
import mensal.gerenciador.de.tarefas.repositories.TarefaRepository;

@Service
public class TarefaAgendadorService {

	@Autowired
	private TarefaRepository tarefaRepository;

	@Autowired
	private NotificacaoService notificacaoService;

	@Scheduled(cron = "0 0 8 * * ?")
	public void verificarTarefas() {
		List<Tarefa> tarefas = tarefaRepository.findAll();
		LocalDate hoje = LocalDate.now();

		for (Tarefa tarefa : tarefas) {
			if (tarefa.getDataVencimento().isBefore(hoje)) {
				notificacaoService.enviarNotificacaoVencida(tarefa);
			} else if (tarefa.getDataVencimento().isEqual(hoje.plusDays(1))) {
				notificacaoService.enviarNotificacaoPertoDeVencer(tarefa);
			}
		}
	}
}
