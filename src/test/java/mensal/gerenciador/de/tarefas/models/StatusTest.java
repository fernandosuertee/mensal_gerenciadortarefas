package mensal.gerenciador.de.tarefas.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class StatusTest {

    @Test
    @DisplayName("Deve conter os valores corretos de Status")
    public void deveConterValoresCorretos() {
        
        Status[] statusList = Status.values();

        
        assertEquals(3, statusList.length);
        assertEquals(Status.NAO_INICIADA, Status.valueOf("NAO_INICIADA"));
        assertEquals(Status.EM_ANDAMENTO, Status.valueOf("EM_ANDAMENTO"));
        assertEquals(Status.CONCLUIDA, Status.valueOf("CONCLUIDA"));
    }
}