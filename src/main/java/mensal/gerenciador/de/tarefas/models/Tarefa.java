package mensal.gerenciador.de.tarefas.models;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;



@Entity
@Table
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Título é obrigatório")
    private String titulo;

    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;

    
    @FutureOrPresent(message = "A data de vencimento deve ser no presente ou no futuro")
    @NotNull(message = "Data de vencimento é obrigatória")
    private LocalDate dataVencimento;

    
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Prioridade é obrigatória")
    private Prioridade prioridade;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status é obrigatório")
    private Status status;
    
    
    @NotNull(message = "Usuário é obrigatório")
	@ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnoreProperties("tarefas")
    private Usuario usuario;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public Prioridade getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(Prioridade prioridade) {
		this.prioridade = prioridade;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}


     
    public Tarefa() {
    }

    public Tarefa(String titulo, String descricao, @NotNull(message = "Data de vencimento é obrigatória") LocalDate dataVencimento, Usuario usuario, Prioridade prioridade, Status status) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataVencimento = dataVencimento;
        this.usuario = usuario;
        this.prioridade = prioridade;
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "Tarefa{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", descricao='" + descricao + '\'' +
                ", dataVencimento=" + dataVencimento +
                ", prioridade=" + prioridade +
                ", status=" + status +
                ", usuario=" + (usuario != null ? usuario.getNome() : "Sem usuário") +
                '}';
    }

}