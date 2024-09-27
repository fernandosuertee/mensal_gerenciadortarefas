package mensal.gerenciador.de.tarefas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mensal.gerenciador.de.tarefas.models.HistoricoDeAlteracao;


public interface HistoricoDeAlteracaoRepository extends JpaRepository<HistoricoDeAlteracao, Long> {

	List<HistoricoDeAlteracao> findByEntidadeId(Long entidadeId);
	
}