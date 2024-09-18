package mensal.gerenciador.de.tarefas.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mensal.gerenciador.de.tarefas.models.Tarefa;
import mensal.gerenciador.de.tarefas.repositories.TarefaRepository;


@Service
public class TarefaService {

	
    @Autowired
    private TarefaRepository tarefaRepository;

    
    
    public List<Tarefa> encontrarTodas() {
    	
        return tarefaRepository.findAll();
    }

    public Tarefa encontrarPorId(Long id) {
    	
        return tarefaRepository.findById(id).orElse(null);
    }

    public Tarefa salvar(Tarefa tarefa) {
    	
        return tarefaRepository.save(tarefa);
    }

    public void deletarPorId(Long id) {
    	
        tarefaRepository.deleteById(id);
    }

    public List<Tarefa> encontrarPorUsuarioId(Long usuarioId) {
        return tarefaRepository.findByUsuarioId(usuarioId);
    }
}