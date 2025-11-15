package br.com.ehmf.NutriAI.controller;

import br.com.ehmf.NutriAI.model.Receita;
import br.com.ehmf.NutriAI.service.ReceitaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/receitas")
public class ReceitaController {

    public final ReceitaService receitaService;

    public ReceitaController(ReceitaService receitaService) {
        this.receitaService = receitaService;
    }

    @GetMapping
    public ResponseEntity<List<Receita>> obterTodasReceitas() {
        List<Receita> receitas = receitaService.findAll();
        if (receitas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(receitas);
    }

    @PostMapping
    public ResponseEntity<Receita> salvarReceita(@RequestBody Receita receita) {
        Receita receitaSalva = receitaService.save(receita);
        return ResponseEntity.ok(receitaSalva);
    }

    @GetMapping("/{id}/calcula-nutricional")
    public ResponseEntity<Receita> calcularInformacoesNutricionais(@PathVariable Long id) {
        return receitaService.calcularNutricionalPorReceita(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
