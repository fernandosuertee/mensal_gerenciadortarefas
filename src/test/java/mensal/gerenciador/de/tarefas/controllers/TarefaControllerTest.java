package mensal.gerenciador.de.tarefas.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import mensal.gerenciador.de.tarefas.models.Prioridade;
import mensal.gerenciador.de.tarefas.models.Status;
import mensal.gerenciador.de.tarefas.models.Tarefa;
import mensal.gerenciador.de.tarefas.models.Usuario;
import mensal.gerenciador.de.tarefas.repositories.TarefaRepository;
import mensal.gerenciador.de.tarefas.repositories.UsuarioRepository;


@SpringBootTest
public class TarefaControllerTest {


    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @AfterEach
    public void tearDown() {
        tarefaRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve obter todas as tarefas")
    public void deveObterTodasAsTarefas() throws Exception {
        
        Usuario usuario = new Usuario("Teste", "teste@example.com");
        usuarioRepository.save(usuario);

        Tarefa tarefa = new Tarefa("Título", "Descrição", LocalDate.now().plusDays(5), usuario, Prioridade.MEDIA, Status.NAO_INICIADA);
        tarefaRepository.save(tarefa);

       
        mockMvc.perform(get("/api/tarefas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Título"));
    }

    @Test
    @DisplayName("Deve criar uma nova tarefa")
    public void deveCriarNovaTarefa() throws Exception {
        
        Usuario usuario = new Usuario("Teste", "teste@example.com");
        usuarioRepository.save(usuario);

        Tarefa tarefa = new Tarefa("Nova Tarefa", "Descrição da nova tarefa", LocalDate.now().plusDays(5), usuario, Prioridade.ALTA, Status.NAO_INICIADA);

       
        mockMvc.perform(post("/api/tarefas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tarefa)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Nova Tarefa"));
    }

    @Test
    @DisplayName("Deve retornar erro ao criar tarefa sem usuário")
    public void deveRetornarErroAoCriarTarefaSemUsuario() throws Exception {
        
        Usuario usuario = new Usuario(); 
        Tarefa tarefa = new Tarefa("Nova Tarefa", "Descrição da nova tarefa", LocalDate.now().plusDays(5), usuario, Prioridade.ALTA, Status.NAO_INICIADA);

        
        mockMvc.perform(post("/api/tarefas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tarefa)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Erro: Usuário não fornecido."));
    }

    @Test
    @DisplayName("Deve atualizar uma tarefa existente")
    public void deveAtualizarTarefa() throws Exception {
        
        Usuario usuario = new Usuario("Teste", "teste@example.com");
        usuarioRepository.save(usuario);

        Tarefa tarefa = new Tarefa("Tarefa Antiga", "Descrição antiga", LocalDate.now().plusDays(5), usuario, Prioridade.MEDIA, Status.NAO_INICIADA);
        tarefaRepository.save(tarefa);

        Tarefa novosDados = new Tarefa("Tarefa Atualizada", "Descrição atualizada", LocalDate.now().plusDays(10), usuario, Prioridade.ALTA, Status.EM_ANDAMENTO);

        
        mockMvc.perform(put("/api/tarefas/" + tarefa.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novosDados)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Tarefa Atualizada"));
    }

    @Test
    @DisplayName("Deve deletar uma tarefa existente")
    public void deveDeletarTarefa() throws Exception {
        
        Usuario usuario = new Usuario("Teste", "teste@example.com");
        usuarioRepository.save(usuario);

        Tarefa tarefa = new Tarefa("Tarefa para deletar", "Descrição", LocalDate.now().plusDays(5), usuario, Prioridade.BAIXA, Status.NAO_INICIADA);
        tarefaRepository.save(tarefa);

        
        mockMvc.perform(delete("/api/tarefas/" + tarefa.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Tarefa deletada com sucesso."));
    }
    
    @Test
    @DisplayName("Deve retornar 404 ao buscar tarefa com ID inexistente")
    public void deveRetornar404AoBuscarTarefaInexistente() throws Exception {
        
        mockMvc.perform(get("/api/tarefas/9999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Tarefa com o ID 9999 não encontrada."));
    }
    
    @Test
    @DisplayName("Deve retornar 404 ao deletar tarefa com ID inexistente")
    public void deveRetornar404AoDeletarTarefaInexistente() throws Exception {
       
        mockMvc.perform(delete("/api/tarefas/9999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Tarefa não encontrada ou inválida."));
    }
    
    
    @Test
    @DisplayName("Deve retornar 404 ao atualizar tarefa com ID inexistente")
    public void deveRetornar404AoAtualizarTarefaInexistente() throws Exception {
    
        Usuario usuario = new Usuario("Teste", "teste@example.com");
        usuarioRepository.save(usuario);

        Tarefa novosDados = new Tarefa("Tarefa Atualizada", "Descrição atualizada", LocalDate.now().plusDays(10), usuario, Prioridade.ALTA, Status.EM_ANDAMENTO);

        
        mockMvc.perform(put("/api/tarefas/9999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novosDados)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Tarefa não encontrada"));
    } 
    
    
    @Test
    @DisplayName("Deve retornar relatório vazio quando não há tarefas")
    public void deveRetornarRelatorioVazioQuandoNaoHaTarefas() throws Exception {
        
        mockMvc.perform(get("/api/tarefas/relatorio"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
    
    
    @Test
    @DisplayName("Deve retornar relatório de tarefas por status")
    public void deveRetornarRelatorioDeTarefasPorStatus() throws Exception {
        
        Usuario usuario = new Usuario("Teste", "teste@example.com");
        usuarioRepository.save(usuario);

        Tarefa tarefa1 = new Tarefa("Tarefa 1", "Descrição 1", LocalDate.now().plusDays(5), usuario, Prioridade.MEDIA, Status.NAO_INICIADA);
        Tarefa tarefa2 = new Tarefa("Tarefa 2", "Descrição 2", LocalDate.now().plusDays(5), usuario, Prioridade.ALTA, Status.EM_ANDAMENTO);
        Tarefa tarefa3 = new Tarefa("Tarefa 3", "Descrição 3", LocalDate.now().plusDays(5), usuario, Prioridade.BAIXA, Status.CONCLUIDA);

        tarefaRepository.save(tarefa1);
        tarefaRepository.save(tarefa2);
        tarefaRepository.save(tarefa3);

       
        mockMvc.perform(get("/api/tarefas/relatorio"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.NAO_INICIADA").isArray())
                .andExpect(jsonPath("$.EM_ANDAMENTO").isArray())
                .andExpect(jsonPath("$.CONCLUIDA").isArray());
    } 
    
    
    @Test
    @DisplayName("Deve retornar erro ao criar tarefa com data de vencimento no passado")
    public void deveRetornarErroAoCriarTarefaComDataPassada() throws Exception {
        
        Usuario usuario = new Usuario("Teste", "teste@example.com");
        usuarioRepository.save(usuario);

        Tarefa tarefa = new Tarefa("Nova Tarefa", "Descrição da nova tarefa", LocalDate.now().minusDays(1), usuario, Prioridade.ALTA, Status.NAO_INICIADA);

        
        mockMvc.perform(post("/api/tarefas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tarefa)))
                .andExpect(status().isBadRequest());
       
    }
}