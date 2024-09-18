package mensal.gerenciador.de.tarefas.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;


@SpringBootTest
public class TarefaTest {

    private Validator validador; 
    
    private Usuario usuario; 
    
    
    @BeforeEach
    void configurar() {
        
        ValidatorFactory fabrica = Validation.buildDefaultValidatorFactory();
        validador = fabrica.getValidator();

        
        usuario = new Usuario("João", "joao@example.com");
    }

    
    @AfterEach
    void limpar() {
        
        validador = null;
        usuario = null;
    }

    
    @Test
    void deveValidarTarefaCorretamente() {
        
        Tarefa tarefa = new Tarefa("Estudar", "Estudar para a prova", LocalDate.now().plusDays(1), usuario);

        
        Set<ConstraintViolation<Tarefa>> violacoes = validador.validate(tarefa);
        assertTrue(violacoes.isEmpty());
    }

    
    @Test
    void deveFalharQuandoTituloForVazio() {
        
        Tarefa tarefa = new Tarefa("", "Estudar para a prova", LocalDate.now().plusDays(1), usuario);

        
        Set<ConstraintViolation<Tarefa>> violacoes = validador.validate(tarefa);
        assertFalse(violacoes.isEmpty());
        assertEquals("Título é obrigatório", violacoes.iterator().next().getMessage());
    }

    
    @Test
    void deveFalharQuandoDataVencimentoForNula() {
        
        Tarefa tarefa = new Tarefa("Estudar", "Estudar para a prova", null, usuario);

        
        Set<ConstraintViolation<Tarefa>> violacoes = validador.validate(tarefa);
        assertFalse(violacoes.isEmpty());
        assertEquals("Data de vencimento é obrigatória", violacoes.iterator().next().getMessage());
    }
}