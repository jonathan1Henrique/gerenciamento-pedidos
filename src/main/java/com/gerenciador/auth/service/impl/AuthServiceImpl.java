package com.gerenciador.auth.service.impl;

import com.gerenciador.auth.UsuarioMapper;
import com.gerenciador.auth.entity.Usuario;
import com.gerenciador.auth.jwt.JwtService;
import com.gerenciador.auth.record.LoginResponseRecord;
import com.gerenciador.auth.record.LoginResquetRecord;
import com.gerenciador.auth.record.RegisterRequestRecord;
import com.gerenciador.auth.record.UsuarioLogadoRecord;
import com.gerenciador.auth.repository.UsuarioRepository;
import com.gerenciador.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final UsuarioMapper  mapper;

    @Override
    public void register(RegisterRequestRecord request) {
        Usuario usuario = mapper.toUsuario(request);
        usuario.setPassword(passwordEncoder.encode(request.password()));
        usuarioRepository.save(usuario);
    }

    @Override
    public LoginResponseRecord login(LoginResquetRecord request) {

        Usuario usuario = usuarioRepository.findByUsername(request.username())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        if (!passwordEncoder.matches(request.password(), usuario.getPassword())) {
            throw new BadCredentialsException("Senha inválida");
        }

        return new LoginResponseRecord(jwtService.generateToken(usuario));
    }

    public UsuarioLogadoRecord getUsuarioLogado() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
           if(userDetails.getUsername() != null ){
               return UsuarioLogadoRecord.builder()
                       .username(userDetails.getUsername())
                       .role(userDetails.getAuthorities().iterator().next().getAuthority())
                       .build();
           }

        }

        throw new RuntimeException("Usuário não autenticado");
    }
}
