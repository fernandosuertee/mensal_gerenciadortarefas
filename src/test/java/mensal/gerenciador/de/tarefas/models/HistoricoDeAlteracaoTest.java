package mensal.gerenciador.de.tarefas.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

import jakarta.validation.ConstraintViolation;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class HistoricoDeAlteracaoTest {

    private final Validator validator;

    public HistoricoDeAlteracaoTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Deve criar um histórico de alteração válido")
    public void deveCriarHistoricoValido() {
        
        HistoricoDeAlteracao historico = new HistoricoDeAlteracao(1L, "tarefa", "criado", "Descrição de teste");

        
        Set<ConstraintViolation<HistoricoDeAlteracao>> violacoes = validator.validate(historico);

        
        assertTrue(violacoes.isEmpty());
    }


}