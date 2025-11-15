package br.com.ehmf.NutriAI.repository;

import br.com.ehmf.NutriAI.model.Ingredientes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientesRepository extends JpaRepository<Ingredientes, Long> {
}
