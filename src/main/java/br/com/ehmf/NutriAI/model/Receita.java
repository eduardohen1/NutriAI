package br.com.ehmf.NutriAI.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "receitas")
public class Receita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @ElementCollection
    private List<String> ingredientes;

    @ElementCollection
    private List<String> instrucoes;

    @Column(name = "porcoes", nullable = false)
    private int porcoes;

    //nutricionais
    private double calorias;
    private double proteinas;
    private double gorduras;
    private double carboidratos;

    public Receita(){}
    public Receita(Long id, String titulo, List<String> ingredientes,
                   List<String> instrucoes, int porcoes) {
        this.id = id;
        this.titulo = titulo;
        this.ingredientes = ingredientes;
        this.porcoes = porcoes;
        this.instrucoes = instrucoes;
    }

    public Receita(Long id, String titulo, List<String> ingredientes,
                   List<String> instrucoes, int porcoes,
                   double calorias, double proteinas,
                   double gorduras, double carboidratos) {
        this.id = id;
        this.titulo = titulo;
        this.ingredientes = ingredientes;
        this.porcoes = porcoes;
        this.instrucoes = instrucoes;
        this.calorias = calorias;
        this.proteinas = proteinas;
        this.gorduras = gorduras;
        this.carboidratos = carboidratos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<String> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<String> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public List<String> getInstrucoes() {
        return instrucoes;
    }

    public void setInstrucoes(List<String> instrucoes) {
        this.instrucoes = instrucoes;
    }

    public int getPorcoes() {
        return porcoes;
    }

    public void setPorcoes(int porcoes) {
        this.porcoes = porcoes;
    }

    public double getCalorias() {
        return calorias;
    }

    public void setCalorias(double calorias) {
        this.calorias = calorias;
    }

    public double getProteinas() {
        return proteinas;
    }

    public void setProteinas(double proteinas) {
        this.proteinas = proteinas;
    }

    public double getGorduras() {
        return gorduras;
    }

    public void setGorduras(double gorduras) {
        this.gorduras = gorduras;
    }

    public double getCarboidratos() {
        return carboidratos;
    }

    public void setCarboidratos(double carboidratos) {
        this.carboidratos = carboidratos;
    }
}
