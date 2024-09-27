package mensal.gerenciador.de.tarefas.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import mensal.gerenciador.de.tarefas.models.HistoricoDeAlteracao;
import mensal.gerenciador.de.tarefas.repositories.HistoricoDeAlteracaoRepository;


@SpringBootTest
public class HistoricoAlteracaoControllerTest {


    private MockMvc mockMvc;

    @Autowired
    private HistoricoDeAlteracaoRepository historicoRepository;
    
    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setUp() {
        
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @AfterEach
    public void tearDown() {
        historicoRepository.deleteAll();
    }
    @Test
    @DisplayName("Deve obter todo o histórico de alterações")
    public void deveObterTodoHistorico() throws Exception {
        
        HistoricoDeAlteracao historico = new HistoricoDeAlteracao(1L, "tarefa", "criado", "Tarefa criada");
        historico.setDataAlteracao(LocalDateTime.now());
        historicoRepository.save(historico);

        
        mockMvc.perform(get("/api/historico")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].entidade").value("tarefa"))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("Deve obter histórico por ID da entidade")
    public void deveObterHistoricoPorId() throws Exception {
       
        HistoricoDeAlteracao historico = new HistoricoDeAlteracao(2L, "usuario", "atualizado", "Usuário atualizado");
        historico.setDataAlteracao(LocalDateTime.now());
        historicoRepository.save(historico);

       
        mockMvc.perform(get("/api/historico/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].entidadeId").value(2))
                .andExpect(jsonPath("$[0].entidade").value("usuario"));
    }

    @Test
    @DisplayName("Deve retornar 404 quando não encontrar histórico para o ID fornecido")
    public void deveRetornar404QuandoNaoEncontrarHistoricoPorId() throws Exception {

        mockMvc.perform(get("/api/historico/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Nenhuma alteração encontrada para o ID fornecido."));
    }
}