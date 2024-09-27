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
public class UsuarioTest {

    private final Validator validator;

    public UsuarioTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Deve validar campos obrigatórios do Usuário")
    public void deveValidarCamposObrigatorios() {
        
        Usuario usuario = new Usuario();

        
        Set<ConstraintViolation<Usuario>> violacoes = validator.validate(usuario);

       
        assertFalse(violacoes.isEmpty());
        assertEquals(2, violacoes.size());

        for (ConstraintViolation<Usuario> violacao : violacoes) {
            System.out.println(violacao.getPropertyPath() + ": " + violacao.getMessage());
        }
    }

    
    @Test
    @DisplayName("Deve validar formato de e-mail inválido")
    public void deveValidarFormatoDeEmailInvalido() {
        
        Usuario usuario = new Usuario("João", "email_invalido");

        
        Set<ConstraintViolation<Usuario>> violacoes = validator.validate(usuario);

     
        assertFalse(violacoes.isEmpty());
        assertEquals(1, violacoes.size());

        ConstraintViolation<Usuario> violacao = violacoes.iterator().next();
        assertEquals("Formato de e-mail inválido", violacao.getMessage());
    }

    @Test
    @DisplayName("Deve criar um usuário válido")
    public void deveCriarUsuarioValido() {
    
        Usuario usuario = new Usuario("Maria", "maria@example.com");

       
        Set<ConstraintViolation<Usuario>> violacoes = validator.validate(usuario);

     
        assertTrue(violacoes.isEmpty());
    }
}