package mensal.gerenciador.de.tarefas.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mensal.gerenciador.de.tarefas.models.Tarefa;
import mensal.gerenciador.de.tarefas.services.TarefaService;


@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {

	
    @Autowired
    private TarefaService tarefaService;

    
    
    @GetMapping
    public ResponseEntity<?> obterTodasTarefas() {
        List<Tarefa> tarefas = tarefaService.encontrarTodas();
        
        if (tarefas.isEmpty()) {
            
            return ResponseEntity.ok("Não há tarefas cadastradas no momento.");
        }
        
        return ResponseEntity.ok(tarefas);
    }

    
    
    @GetMapping("/{id}")
    public ResponseEntity<?> obterTarefaPorId(@PathVariable Long id) {
    	
        Tarefa tarefa = tarefaService.encontrarPorId(id);
        
        if (tarefa != null) {
            return ResponseEntity.ok(tarefa);
        } else {
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa com o ID " + id + " não encontrada.");
        }
    }

    
    
    @PostMapping
    public ResponseEntity<Tarefa> criarTarefa(@Validated @RequestBody Tarefa tarefa) {
    	
        Tarefa tarefaSalva = tarefaService.salvar(tarefa);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(tarefaSalva);
    }

    
    
    @PutMapping("/{id}")
    public ResponseEntity<Tarefa> atualizarTarefa(@PathVariable Long id, @Validated @RequestBody Tarefa detalhesTarefa) {
        
    	Tarefa tarefa = tarefaService.encontrarPorId(id);
        
        if (tarefa != null) {
            tarefa.setTitulo(detalhesTarefa.getTitulo());
            tarefa.setDescricao(detalhesTarefa.getDescricao());
            tarefa.setDataVencimento(detalhesTarefa.getDataVencimento());
            return ResponseEntity.ok(tarefaService.salvar(tarefa));
        }
        return ResponseEntity.notFound().build();
    }

    
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarTarefa(@PathVariable Long id) {
        Tarefa tarefa = tarefaService.encontrarPorId(id);
        if (tarefa != null) {
            tarefaService.deletarPorId(id);
            return ResponseEntity.ok("Tarefa deletada com sucesso.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa não encontrada ou inválida.");
    }
}
