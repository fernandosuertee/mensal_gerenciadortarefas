package mensal.gerenciador.de.tarefas.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mensal.gerenciador.de.tarefas.models.Usuario;
import mensal.gerenciador.de.tarefas.services.UsuarioService;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

	
    @Autowired
    private UsuarioService usuarioService;

    
    @GetMapping
    public ResponseEntity<?> obterTodosUsuarios() {
        List<Usuario> usuarios = usuarioService.encontrarTodos();
        if (usuarios.isEmpty()) {
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum usuário cadastrado ou encontrado.");
        }
        return ResponseEntity.ok(usuarios);
    }
    
    
    @GetMapping("/{id}")
    public ResponseEntity<?> obterUsuarioPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.encontrarPorId(id);
        if (usuario == null) {
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado ou cadastrado.");
        }
        return ResponseEntity.ok(usuario);
    }

    
    @PostMapping
    public ResponseEntity<Usuario> criarUsuario(@Validated @RequestBody Usuario usuario) {
        Usuario usuarioSalvo = usuarioService.salvar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
    }

    
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarUsuario(@PathVariable Long id, @Validated @RequestBody Usuario detalhesUsuario) {
        try {
            Usuario usuario = usuarioService.encontrarPorId(id);

            if (usuario != null) {
                
                if (usuario.getNome().equals(detalhesUsuario.getNome()) &&
                    usuario.getEmail().equals(detalhesUsuario.getEmail())) {
                    
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Os dados são idênticos aos já cadastrados.");
                }

                usuario.setNome(detalhesUsuario.getNome());
                usuario.setEmail(detalhesUsuario.getEmail());

                Usuario usuarioAtualizado = usuarioService.salvar(usuario);

                return ResponseEntity.ok(usuarioAtualizado);
            }

            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        } catch (Exception e) {
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar o usuário.");
        }
    }


    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarUsuario(@PathVariable Long id) {
    	
        if (usuarioService.encontrarPorId(id) != null) {
        	
            usuarioService.deletarPorId(id);
            
            return ResponseEntity.ok("Usuário deletado com sucesso.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado ou invalido.");
    }
}