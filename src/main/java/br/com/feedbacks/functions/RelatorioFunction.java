package br.com.feedbacks.functions;

import br.com.feedbacks.entities.AvaliacaoEntity;
import br.com.feedbacks.repositories.AvaliacaoRepository;
import br.com.feedbacks.services.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Supplier;

@Component
@AllArgsConstructor
public class RelatorioFunction {

    private final EmailService emailService;

    private final AvaliacaoRepository avaliacaoRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private static final String SUBJECT = "Relatório de Avaliações Recebidas (%s e %s)";

    private static final String BODY =
            "Média das notas: %s\n\nNotas recebidas no período:\n\n%s";


    private static final String LINE = "Data: %s | Descrição: %s | Nota: %d\n";


    @Bean
    public Supplier<String> gerarRelatorio() {
        return () -> {
            StringBuilder sb = new StringBuilder();

            LocalDateTime dataFinal = LocalDateTime.now();
            LocalDateTime dataInicial = dataFinal.minusDays(7);

            var avaliacoes = avaliacaoRepository.findByDataCriacaoBetweenOrderByDataCriacao(dataInicial, dataFinal);

            var mediaNotas = getMediaNotas(avaliacoes);

            var body = formatBody(avaliacoes, sb, mediaNotas);

            emailService.sendSimpleEmail(
                    String.format(SUBJECT, formatter.format(dataInicial), formatter.format(dataFinal)),
                    body);

            return "Processado";
        };
    }

    private String formatBody(List<AvaliacaoEntity> avaliacoes, StringBuilder sb, BigDecimal mediaNotas) {
        avaliacoes.forEach(avaliacao ->
                sb.append(String.format(LINE,
                        formatter.format(avaliacao.getDataCriacao()),
                        avaliacao.getDescricao(),
                        avaliacao.getNota())));

        return String.format(BODY, mediaNotas, sb.toString());
    }

    private BigDecimal getMediaNotas(List<AvaliacaoEntity> avaliacoes) {
        return new BigDecimal(avaliacoes.stream()
                .mapToLong(AvaliacaoEntity::getNota)
                .reduce(Long::sum)
                .orElse(0L) / avaliacoes.size())
                .setScale(2, RoundingMode.HALF_EVEN);
    }
}
