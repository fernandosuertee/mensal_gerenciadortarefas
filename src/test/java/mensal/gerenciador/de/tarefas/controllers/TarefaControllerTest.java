package mensal.gerenciador.de.tarefas.controllers;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import mensal.gerenciador.de.tarefas.models.Tarefa;
import mensal.gerenciador.de.tarefas.services.TarefaService;

class TarefaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TarefaService tarefaService;

    @InjectMocks
    private TarefaController tarefaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(tarefaController).build();
    }

    @Test
    void testObterTodasTarefas() throws Exception {
        when(tarefaService.encontrarTodas()).thenReturn(List.of(new Tarefa()));

        mockMvc.perform(get("/api/tarefas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testObterTodasTarefasVazia() throws Exception {
        when(tarefaService.encontrarTodas()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/tarefas"))
                .andExpect(status().isOk())
                .andExpect(content().string("Não há tarefas cadastradas no momento."));
    }

    @Test
    void testObterTarefaPorId() throws Exception {
        Tarefa tarefa = new Tarefa();
        tarefa.setId(1L);
        when(tarefaService.encontrarPorId(anyLong())).thenReturn(tarefa);

        mockMvc.perform(get("/api/tarefas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testObterTarefaPorIdNaoEncontrada() throws Exception {
        when(tarefaService.encontrarPorId(anyLong())).thenReturn(null);

        mockMvc.perform(get("/api/tarefas/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Tarefa com o ID 1 não encontrada."));
    }

    void testCriarTarefa() throws Exception {
        // Ajuste o objeto Tarefa de acordo com os campos obrigatórios do modelo
        Tarefa tarefa = new Tarefa();
        tarefa.setId(1L);
        tarefa.setTitulo("Nova Tarefa");
        tarefa.setDescricao("Descrição da tarefa");
        tarefa.setDataVencimento(LocalDate.now().plusDays(7)); // Certifique-se de definir a data se for obrigatória

        when(tarefaService.salvar(any(Tarefa.class))).thenReturn(tarefa);

        // Inclua todos os campos obrigatórios na requisição
        mockMvc.perform(post("/api/tarefas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"titulo\":\"Nova Tarefa\", \"descricao\":\"Descrição da tarefa\", \"dataVencimento\":\"2024-09-25\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.titulo").value("Nova Tarefa"));
    }

    @Test
    void testAtualizarTarefa() throws Exception {
        // Cria uma tarefa mockada existente com os dados obrigatórios preenchidos
        Tarefa tarefaExistente = new Tarefa();
        tarefaExistente.setId(1L);
        tarefaExistente.setTitulo("Tarefa Existente");
        tarefaExistente.setDescricao("Descrição existente");
        tarefaExistente.setDataVencimento(LocalDate.now().plusDays(5)); // Ajuste conforme o campo obrigatório

        // Cria a tarefa atualizada com os dados a serem enviados no teste
        Tarefa tarefaAtualizada = new Tarefa();
        tarefaAtualizada.setId(1L);
        tarefaAtualizada.setTitulo("Tarefa Atualizada");
        tarefaAtualizada.setDescricao("Descrição atualizada");
        tarefaAtualizada.setDataVencimento(LocalDate.now().plusDays(10));

        // Simula o comportamento do serviço
        when(tarefaService.encontrarPorId(anyLong())).thenReturn(tarefaExistente);
        when(tarefaService.salvar(any(Tarefa.class))).thenReturn(tarefaAtualizada);

        // Realiza a requisição de atualização
        mockMvc.perform(put("/api/tarefas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"titulo\":\"Tarefa Atualizada\", \"descricao\":\"Descrição atualizada\", \"dataVencimento\":\"2024-09-27\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Tarefa Atualizada"))
                .andExpect(jsonPath("$.descricao").value("Descrição atualizada"));
    }

    @Test
    void testDeletarTarefa() throws Exception {
        Tarefa tarefa = new Tarefa();
        tarefa.setId(1L);

        when(tarefaService.encontrarPorId(anyLong())).thenReturn(tarefa);

        mockMvc.perform(delete("/api/tarefas/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Tarefa deletada com sucesso."));
    }

    @Test
    void testDeletarTarefaNaoEncontrada() throws Exception {
        when(tarefaService.encontrarPorId(anyLong())).thenReturn(null);

        mockMvc.perform(delete("/api/tarefas/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Tarefa não encontrada ou inválida."));
    }
}