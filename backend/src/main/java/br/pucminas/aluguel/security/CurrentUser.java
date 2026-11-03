package br.pucminas.aluguel.security;

import br.pucminas.aluguel.model.Papel;
import org.springframework.security.oauth2.jwt.Jwt;

public record CurrentUser(Long id, Papel papel) {
    public static CurrentUser from(Jwt jwt) {
        return new CurrentUser(((Number) jwt.getClaim("uid")).longValue(),
                Papel.valueOf(jwt.getClaimAsString("papel")));
    }
}
