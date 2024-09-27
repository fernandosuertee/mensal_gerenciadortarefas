package mensal.gerenciador.de.tarefas.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class PrioridadeTest {

    @Test
    @DisplayName("Deve conter os valores corretos de Prioridade")
    public void deveConterValoresCorretos() {
        
        Prioridade[] prioridades = Prioridade.values();

        
        assertEquals(3, prioridades.length);
        assertEquals(Prioridade.BAIXA, Prioridade.valueOf("BAIXA"));
        assertEquals(Prioridade.MEDIA, Prioridade.valueOf("MEDIA"));
        assertEquals(Prioridade.ALTA, Prioridade.valueOf("ALTA"));
    }
}