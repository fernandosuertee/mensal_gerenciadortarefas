package mensal.gerenciador.de.tarefas.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
public class UsuarioTest {

	
    private Validator validador; // Renomeado para português

   
    @BeforeEach
    void configurar() {
        
        ValidatorFactory fabrica = Validation.buildDefaultValidatorFactory();
        validador = fabrica.getValidator();
    }

    
    @AfterEach
    void limpar() {
        
        validador = null;
    }

    
    @Test
    void deveValidarUsuarioCorretamente() {
        
        Usuario usuario = new Usuario("João", "joao@example.com");

        
        Set<ConstraintViolation<Usuario>> violacoes = validador.validate(usuario);
        assertTrue(violacoes.isEmpty());
    }

    
    @Test
    void deveFalharQuandoEmailForInvalido() {
        
        Usuario usuario = new Usuario("João", "email-invalido");

        
        Set<ConstraintViolation<Usuario>> violacoes = validador.validate(usuario);
        assertFalse(violacoes.isEmpty());
        assertEquals("Formato de e-mail inválido", violacoes.iterator().next().getMessage());
    }
}