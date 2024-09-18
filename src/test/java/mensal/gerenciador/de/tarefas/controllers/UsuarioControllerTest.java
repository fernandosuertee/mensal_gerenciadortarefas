package mensal.gerenciador.de.tarefas.controllers;

import mensal.gerenciador.de.tarefas.models.Usuario;
import mensal.gerenciador.de.tarefas.services.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
public class UsuarioControllerTest {

	
    @Autowired
    private WebApplicationContext contextoAplicacao; 

    private MockMvc simuladorMvc;

    
    @MockBean
    private UsuarioService usuarioServico; 

    
    @BeforeEach
    void configurar() {
        simuladorMvc = MockMvcBuilders.webAppContextSetup(contextoAplicacao).build(); // Configuração manual do MockMvc
    }

    
    @Test
    void deveObterTodosUsuariosComSucesso() throws Exception {
        Usuario usuario = new Usuario("João", "joao@example.com");
        Mockito.when(usuarioServico.encontrarTodos()).thenReturn(List.of(usuario));

        simuladorMvc.perform(get("/api/usuario"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'nome':'João','email':'joao@example.com'}]"));
    }

    
    @Test
    void deveRetornarNotFoundQuandoNaoHouverUsuarios() throws Exception {
        Mockito.when(usuarioServico.encontrarTodos()).thenReturn(Collections.emptyList());

        simuladorMvc.perform(get("/api/usuario"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Nenhum usuário cadastrado ou encontrado."));
    }

    
    @Test
    void deveObterUsuarioPorIdComSucesso() throws Exception {
        Usuario usuario = new Usuario("João", "joao@example.com");
        Mockito.when(usuarioServico.encontrarPorId(anyLong())).thenReturn(usuario);

        simuladorMvc.perform(get("/api/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{'nome':'João','email':'joao@example.com'}"));
    }

    
    @Test
    void deveRetornarNotFoundQuandoUsuarioNaoExistir() throws Exception {
        Mockito.when(usuarioServico.encontrarPorId(anyLong())).thenReturn(null);

        simuladorMvc.perform(get("/api/usuario/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Usuário não encontrado ou cadastrado."));
    }

    
    @Test
    void deveCriarUsuarioComSucesso() throws Exception {
        Usuario usuario = new Usuario("João", "joao@example.com");
        Mockito.when(usuarioServico.salvar(any(Usuario.class))).thenReturn(usuario);

        simuladorMvc.perform(post("/api/usuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"João\",\"email\":\"joao@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().json("{'nome':'João','email':'joao@example.com'}"));
    }

    
    @Test
    void deveAtualizarUsuarioComSucesso() throws Exception {
        Usuario usuarioExistente = new Usuario("João", "joao@example.com");
        Usuario usuarioAtualizado = new Usuario("João Silva", "joao.silva@example.com");

        Mockito.when(usuarioServico.encontrarPorId(anyLong())).thenReturn(usuarioExistente);
        Mockito.when(usuarioServico.salvar(any(Usuario.class))).thenReturn(usuarioAtualizado);

        simuladorMvc.perform(put("/api/usuario/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"João Silva\",\"email\":\"joao.silva@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{'nome':'João Silva','email':'joao.silva@example.com'}"));
    }

    
    @Test
    void deveRetornarNotFoundQuandoTentarAtualizarUsuarioInexistente() throws Exception {
        Mockito.when(usuarioServico.encontrarPorId(anyLong())).thenReturn(null);

        simuladorMvc.perform(put("/api/usuario/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"João Silva\",\"email\":\"joao.silva@example.com\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Usuário não encontrado."));
    }

    
    @Test
    void deveDeletarUsuarioComSucesso() throws Exception {
        Usuario usuario = new Usuario("João", "joao@example.com");
        Mockito.when(usuarioServico.encontrarPorId(anyLong())).thenReturn(usuario);
        Mockito.doNothing().when(usuarioServico).deletarPorId(anyLong());

        simuladorMvc.perform(delete("/api/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuário deletado com sucesso."));
    }

    
    @Test
    void deveRetornarNotFoundAoTentarDeletarUsuarioInexistente() throws Exception {
        Mockito.when(usuarioServico.encontrarPorId(anyLong())).thenReturn(null);

        simuladorMvc.perform(delete("/api/usuario/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Usuário não encontrado ou invalido."));
    }
}
