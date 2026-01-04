package br.com.feedbacks.repositories;

import br.com.feedbacks.entities.AvaliacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface AvaliacaoRepository extends JpaRepository<AvaliacaoEntity, String>  {

    List<AvaliacaoEntity> findByDataCriacaoBetweenOrderByDataCriacao(LocalDateTime inicio, LocalDateTime fim);
}
