package br.com.ehmf.NutriAI.service;

import br.com.ehmf.NutriAI.model.Receita;
import br.com.ehmf.NutriAI.repository.ReceitaRepository;
import org.springframework.stereotype.Service;

@Service
public class ReceitaService {

    private final ReceitaRepository receitaRepository;

    public ReceitaService(ReceitaRepository receitaRepository) {
        this.receitaRepository = receitaRepository;
    }

    public Receita save(Receita receita) {
        //validar argumentos se necessario
        try{
            if(receita.getTitulo() == null || receita.getTitulo().isEmpty()) {
                throw new IllegalArgumentException("Nome da receita nao pode ser vazio");
            }
            if(receita.getIngredientes() == null || receita.getIngredientes().isEmpty()) {
                throw new IllegalArgumentException("Ingredientes da receita nao podem ser vazios");
            }
            if(receita.getInstrucoes() == null || receita.getInstrucoes().isEmpty()) {
                throw new IllegalArgumentException("Modo de preparo da receita nao pode ser vazio");
            }
            if(receita.getPorcoes() < 1) {
                throw new IllegalArgumentException("Porcoes da receita devem ser maiores que zero");
            }
            return receitaRepository.save(receita);
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao salvar receita: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar receita: " + e.getMessage());
        }

    }

    //buscar por id
    public Receita findById(Long id) {
        return receitaRepository.findById(id).orElse(null);
    }

    //deletar por id
    public void deleteById(Long id) {
        receitaRepository.deleteById(id);
    }

    //buscar todas as receitas
    public java.util.List<Receita> findAll() {
        return receitaRepository.findAll();
    }

    //atualizar receita
    public Receita update(Long id, Receita updatedReceita) {
        return receitaRepository.findById(id).map(receita -> {
            receita.setTitulo(updatedReceita.getTitulo());
            receita.setIngredientes(updatedReceita.getIngredientes());
            receita.setInstrucoes(updatedReceita.getInstrucoes());
            receita.setPorcoes(updatedReceita.getPorcoes());
            return receitaRepository.save(receita);
        }).orElse(null);
    }
}
