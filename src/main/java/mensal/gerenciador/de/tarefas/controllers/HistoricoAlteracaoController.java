package mensal.gerenciador.de.tarefas.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mensal.gerenciador.de.tarefas.models.HistoricoDeAlteracao;
import mensal.gerenciador.de.tarefas.services.HistoricoDeAlteracaoService;

@RestController
@RequestMapping("/api/historico")
public class HistoricoAlteracaoController {

	@Autowired
	private HistoricoDeAlteracaoService historicoDeAlteracaoService;

	@GetMapping
	public ResponseEntity<List<HistoricoDeAlteracao>> obterHistorico() {
		List<HistoricoDeAlteracao> historico = historicoDeAlteracaoService.encontrarTodos();
		return ResponseEntity.ok(historico);
	}

	
	@GetMapping("/{id}")
	public ResponseEntity<?> obterHistoricoPorId(@PathVariable Long id) {
		List<HistoricoDeAlteracao> historico = historicoDeAlteracaoService.encontrarPorEntidadeId(id);

		if (historico.isEmpty()) {
			return ResponseEntity.status(404).body("Nenhuma alteração encontrada para o ID fornecido.");
		}

		return ResponseEntity.ok(historico);
	}
}