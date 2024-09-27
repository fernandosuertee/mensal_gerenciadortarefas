package mensal.gerenciador.de.tarefas.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mensal.gerenciador.de.tarefas.models.HistoricoDeAlteracao;
import mensal.gerenciador.de.tarefas.repositories.HistoricoDeAlteracaoRepository;

@Service
public class HistoricoDeAlteracaoService {

    @Autowired
    private HistoricoDeAlteracaoRepository historicoDeAlteracaoRepository;

    public List<HistoricoDeAlteracao> encontrarTodos() {
        return historicoDeAlteracaoRepository.findAll();
    }

    public List<HistoricoDeAlteracao> encontrarPorEntidadeId(Long entidadeId) {
    	
        return historicoDeAlteracaoRepository.findByEntidadeId(entidadeId);
    }

    public HistoricoDeAlteracao salvar(HistoricoDeAlteracao historicoDeAlteracao) {
    	
        return historicoDeAlteracaoRepository.save(historicoDeAlteracao);
    }
}