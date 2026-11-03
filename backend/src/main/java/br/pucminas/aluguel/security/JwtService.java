package br.pucminas.aluguel.security;

import br.pucminas.aluguel.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class JwtService {
    private final JwtEncoder encoder;
    private final long expiracaoHoras;

    public JwtService(JwtEncoder encoder, @Value("${app.jwt.expiracao-horas}") long expiracaoHoras) {
        this.encoder = encoder;
        this.expiracaoHoras = expiracaoHoras;
    }

    public String gerar(Usuario u) {
        Instant agora = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(u.getUsername())
                .issuedAt(agora)
                .expiresAt(agora.plus(expiracaoHoras, ChronoUnit.HOURS))
                .claim("uid", u.getId())
                .claim("papel", u.getPapel().name())
                .build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        return encoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }
}
