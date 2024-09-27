package mensal.gerenciador.de.tarefas.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;



@Entity
@Table(name = "historico_alteracao")
public class HistoricoDeAlteracao {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	private Long entidadeId; 
	
    private String entidade;
    
    private String operacao;
    
    @Column(length = 1000)
    private String descricao; 
    
    private LocalDateTime dataAlteracao; 

    public HistoricoDeAlteracao() {}

    public HistoricoDeAlteracao(Long entidadeId, String entidade, String operacao, String descricao) {
        this.entidadeId = entidadeId;
        this.entidade = entidade;
        this.operacao = operacao;
        this.descricao = descricao;
        this.dataAlteracao = LocalDateTime.now();
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEntidadeId() {
		return entidadeId;
	}

	public void setEntidadeId(Long entidadeId) {
		this.entidadeId = entidadeId;
	}

	public String getEntidade() {
		return entidade;
	}

	public void setEntidade(String entidade) {
		this.entidade = entidade;
	}

	public String getOperacao() {
		return operacao;
	}

	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public LocalDateTime getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(LocalDateTime dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}
    
    
    

}
