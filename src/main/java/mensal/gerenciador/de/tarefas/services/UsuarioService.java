package mensal.gerenciador.de.tarefas.services;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mensal.gerenciador.de.tarefas.models.Usuario;
import mensal.gerenciador.de.tarefas.repositories.UsuarioRepository;


@Service
public class UsuarioService {

	
    @Autowired
    private UsuarioRepository usuarioRepository;

    
    public List<Usuario> encontrarTodos() {
        return usuarioRepository.findAll(); 
    }

    public Usuario encontrarPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario salvar(Usuario usuario) {
        
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());
        if (usuarioExistente.isPresent()) {
            throw new IllegalArgumentException("E-mail já cadastrado. Tente com outro e-mail.");
        }
        return usuarioRepository.save(usuario);
    }


    public void deletarPorId(Long id) {
        usuarioRepository.deleteById(id);
    }
}