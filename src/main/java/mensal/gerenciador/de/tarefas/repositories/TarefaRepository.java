package mensal.gerenciador.de.tarefas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mensal.gerenciador.de.tarefas.models.Tarefa;


public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
	
    List<Tarefa> findByUsuarioId(Long usuarioId);
}
