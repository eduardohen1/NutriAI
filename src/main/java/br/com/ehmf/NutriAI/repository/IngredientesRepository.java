package br.com.ehmf.NutriAI.repository;

import br.com.ehmf.NutriAI.model.Ingredientes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IngredientesRepository extends JpaRepository<Ingredientes, Long> {

    Optional<Ingredientes> findByNomeContainingIgnoreCase(String nome); //pesquisar por nome

}
