package br.pucminas.aluguel.config;

import br.pucminas.aluguel.model.Automovel;
import br.pucminas.aluguel.model.TipoProprietario;
import br.pucminas.aluguel.repository.AutomovelRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    ApplicationRunner seedAutomoveis(AutomovelRepository repo) {
        return args -> {
            if (repo.count() > 0) return;
            Object[][] dados = {
                {"MAT-0001", 2023, "Fiat", "Argo", "RTM1A23"},
                {"MAT-0002", 2024, "Volkswagen", "Polo", "QWE2B34"},
                {"MAT-0003", 2022, "Chevrolet", "Onix", "KJH3C45"},
                {"MAT-0004", 2024, "Toyota", "Corolla", "BRA4D56"},
            };
            for (Object[] d : dados) {
                Automovel a = new Automovel();
                a.setMatricula((String) d[0]); a.setAno((Integer) d[1]); a.setMarca((String) d[2]);
                a.setModelo((String) d[3]); a.setPlaca((String) d[4]);
                a.setTipoProprietario(TipoProprietario.EMPRESA);
                repo.save(a);
            }
        };
    }
}
