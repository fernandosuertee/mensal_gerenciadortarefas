package mensal.gerenciador.de.tarefas.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import mensal.gerenciador.de.tarefas.models.Usuario;
import mensal.gerenciador.de.tarefas.repositories.UsuarioRepository;


@SpringBootTest
public class UsuarioControllerTest {
	

    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @AfterEach
    public void tearDown() {
        usuarioRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve obter todos os usuários")
    public void deveObterTodosUsuarios() throws Exception {
        
        Usuario usuario = new Usuario("João", "joao@gmail.com");
        usuarioRepository.save(usuario);

        
        mockMvc.perform(MockMvcRequestBuilders.get("/api/usuario")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("João"));
    }

    @Test
    @DisplayName("Deve criar um novo usuário")
    public void deveCriarNovoUsuario() throws Exception {
        
        Usuario usuario = new Usuario("Maria", "maria@gmail.com");

        
        mockMvc.perform(MockMvcRequestBuilders.post("/api/usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Maria"));
    }

    @Test
    @DisplayName("Deve atualizar um usuário existente")
    public void deveAtualizarUsuario() throws Exception {
        
        Usuario usuario = new Usuario("Carlos", "carlos@example.com");
        usuarioRepository.save(usuario);

        Usuario novosDados = new Usuario("Carlos Silva", "carlos.silva@gmail.com");

        
        mockMvc.perform(MockMvcRequestBuilders.put("/api/usuario/" + usuario.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novosDados)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Carlos Silva"));
    }

    @Test
    @DisplayName("Deve deletar um usuário existente")
    public void deveDeletarUsuario() throws Exception {
        
        Usuario usuario = new Usuario("Ana", "ana@gmail.com");
        usuarioRepository.save(usuario);

        
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/usuario/" + usuario.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuário deletado com sucesso."));
    }
    
    
    @Test
    @DisplayName("Deve retornar erro ao atualizar usuário com dados idênticos")
    public void deveRetornarErroAoAtualizarUsuarioComDadosIdenticos() throws Exception {
         
        Usuario usuario = new Usuario("Carlos", "carlos@example.com");
        usuarioRepository.save(usuario);

        Usuario detalhesUsuario = new Usuario("Carlos", "carlos@example.com");

        
        mockMvc.perform(put("/api/usuario/" + usuario.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(detalhesUsuario)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Os dados são idênticos aos já cadastrados."));
    }
    
    
    @Test
    @DisplayName("Deve retornar 404 ao buscar usuário com ID inexistente")
    public void deveRetornar404AoBuscarUsuarioInexistente() throws Exception {
        
        mockMvc.perform(get("/api/usuario/9999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Usuário não encontrado ou cadastrado."));
    } 
    
    
    
    


  


}