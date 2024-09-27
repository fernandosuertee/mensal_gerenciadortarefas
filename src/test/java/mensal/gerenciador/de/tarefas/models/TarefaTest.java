package mensal.gerenciador.de.tarefas.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.time.LocalDate;
import java.util.Set;

import jakarta.validation.ConstraintViolation;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class TarefaTest {

    private final Validator validator;

    public TarefaTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Deve validar campos obrigatórios da Tarefa")
    public void deveValidarCamposObrigatorios() {
        
        Tarefa tarefa = new Tarefa();
        
        Set<ConstraintViolation<Tarefa>> violacoes = validator.validate(tarefa);

        
        assertFalse(violacoes.isEmpty());
        assertEquals(6, violacoes.size());

        for (ConstraintViolation<Tarefa> violacao : violacoes) {
            System.out.println(violacao.getPropertyPath() + ": " + violacao.getMessage());
        }
    }

    @Test
    @DisplayName("Deve criar uma tarefa válida")
    public void deveCriarTarefaValida() {
        
        Usuario usuario = new Usuario("João", "joao@example.com");
        Tarefa tarefa = new Tarefa("Título", "Descrição", LocalDate.now().plusDays(3), usuario, Prioridade.MEDIA, Status.EM_ANDAMENTO);

     
        Set<ConstraintViolation<Tarefa>> violacoes = validator.validate(tarefa);

   
        assertTrue(violacoes.isEmpty());
    }
}