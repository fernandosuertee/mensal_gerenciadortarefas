package mensal.gerenciador.de.tarefas.controllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

import jakarta.validation.Valid;
import mensal.gerenciador.de.tarefas.models.HistoricoDeAlteracao;
import mensal.gerenciador.de.tarefas.models.Prioridade;
import mensal.gerenciador.de.tarefas.models.Status;
import mensal.gerenciador.de.tarefas.models.Tarefa;
import mensal.gerenciador.de.tarefas.models.Usuario;
import mensal.gerenciador.de.tarefas.repositories.TarefaRepository;
import mensal.gerenciador.de.tarefas.repositories.UsuarioRepository;
import mensal.gerenciador.de.tarefas.services.HistoricoDeAlteracaoService;
import mensal.gerenciador.de.tarefas.services.NotificacaoService;
import mensal.gerenciador.de.tarefas.services.TarefaService;


@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {

	
    @Autowired
    private TarefaService tarefaService;
    
    
    @Autowired
    private HistoricoDeAlteracaoService historicoDeAlteracaoService;
    
    
    @Autowired
    private TarefaRepository tarefaRepository;
    
    @Autowired
    private NotificacaoService notificacaoService;

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @PostMapping("/enviarTesteEmail")
    public ResponseEntity<String> enviarEmailTeste() {
    	
        try {
        	
            notificacaoService.enviarEmail("fernandosuertemiranda@gmail.com", "Teste de Email", "Este é um email de teste.");
            
            return ResponseEntity.ok("Email enviado com sucesso");
        } catch (Exception e) {
        	
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Falha ao enviar email: " + e.getMessage());
        }
    }
    
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

    
    @GetMapping("/relatorio")
    public ResponseEntity<Map<Status, List<Tarefa>>> relatorioTarefas() {
        
        List<Tarefa> todasTarefas = tarefaRepository.findAll();

        
        Map<Status, List<Tarefa>> tarefasPorStatus = todasTarefas.stream()
                .collect(Collectors.groupingBy(Tarefa::getStatus));

        
        return ResponseEntity.ok(tarefasPorStatus);
    }
    
    
    @GetMapping("/relatorio/prioridade")
    
    public ResponseEntity<Map<Prioridade, List<Tarefa>>> relatorioTarefasPorPrioridade() {
        
        List<Tarefa> todasTarefas = tarefaRepository.findAll();

        
        Map<Prioridade, List<Tarefa>> tarefasPorPrioridade = todasTarefas.stream()
                .collect(Collectors.groupingBy(Tarefa::getPrioridade));

        
        return ResponseEntity.ok(tarefasPorPrioridade);
    }
    
    @PostMapping
    public ResponseEntity<?> criarTarefa(@Valid @RequestBody Tarefa novosDados) {
        
        if (novosDados.getUsuario() == null || novosDados.getUsuario().getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: Usuário não fornecido.");
        }

        
        Usuario usuario = usuarioRepository.findById(novosDados.getUsuario().getId()).orElse(null);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro: Usuário não encontrado.");
        }

        
        novosDados.setUsuario(usuario);

        
        Tarefa tarefaSalva = tarefaService.salvar(novosDados);

        
        String descricaoCriacao = "Tarefa criada: " + tarefaSalva.getTitulo();
        historicoDeAlteracaoService.salvar(new HistoricoDeAlteracao(tarefaSalva.getId(), "tarefa", "criado", descricaoCriacao));

        
        if (usuario.getEmail() != null && !usuario.getEmail().isEmpty()) {
            try {
                String assunto = "Nova tarefa criada: " + tarefaSalva.getTitulo();
                String mensagem = "Uma nova tarefa foi criada com o título: " + tarefaSalva.getTitulo() +
                                  "\nDescrição: " + tarefaSalva.getDescricao() +
                                  "\nData de vencimento: " + tarefaSalva.getDataVencimento();

                notificacaoService.enviarEmail(usuario.getEmail(), assunto, mensagem);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao enviar e-mail: " + e.getMessage());
            }
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(tarefaSalva);
    }




    
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarTarefa(@PathVariable Long id, @Validated @RequestBody Tarefa novosDados) {
        
        Tarefa tarefaExistente = tarefaService.encontrarPorId(id);

        if (tarefaExistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa não encontrada");
        }

        
        tarefaExistente.setTitulo(novosDados.getTitulo());
        tarefaExistente.setDescricao(novosDados.getDescricao());
        tarefaExistente.setDataVencimento(novosDados.getDataVencimento());
        tarefaExistente.setPrioridade(novosDados.getPrioridade());
        tarefaExistente.setStatus(novosDados.getStatus());

        
        if (novosDados.getUsuario() != null) {
            tarefaExistente.setUsuario(novosDados.getUsuario());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário é obrigatório.");
        }

        
        Tarefa tarefaAtualizada = tarefaService.atualizar(tarefaExistente, novosDados);

        
        String descricaoAtualizacao = "Tarefa atualizada: " + tarefaAtualizada.getTitulo();
        historicoDeAlteracaoService.salvar(new HistoricoDeAlteracao(tarefaAtualizada.getId(), "tarefa", "atualizado", descricaoAtualizacao));

        return ResponseEntity.ok(tarefaAtualizada);
    }
    
   
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletarTarefa(@PathVariable Long id) {

	    Tarefa tarefa = tarefaService.encontrarPorId(id);

	    if (tarefa != null) {
	        String alteracao = "Tarefa deletada: " + tarefa.toString();
	        historicoDeAlteracaoService.salvar(new HistoricoDeAlteracao(tarefa.getId(), "tarefa", "deletado", alteracao));
	        
	        
	        tarefaService.deletarPorId(id);

	        return ResponseEntity.ok("Tarefa deletada com sucesso.");
	    }

	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa não encontrada ou inválida.");
	}

}
