package br.com.ehmf.NutriAI.service;

import br.com.ehmf.NutriAI.model.Receita;
import br.com.ehmf.NutriAI.repository.IngredientesRepository;
import br.com.ehmf.NutriAI.service.interfaces.NutricionalAIService;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class NutricionalService {

    private final IngredientesRepository ingredientesRepository;
    private final NutricionalAIService nutricionalAIService;

    public NutricionalService(
            IngredientesRepository ingredientesRepository,
            @Value("${langchain4j.open-ai.chat-model.api-key:}") String apiKey,
            @Value("${langchain4j.open-ai.chat-model-name:gpt-4o-2024-08-06}") String modelName
    ){
        this.ingredientesRepository = ingredientesRepository;
        if(apiKey == null || apiKey.isEmpty() || apiKey.equals("${OPENAI_API_KEY}")){
            this.nutricionalAIService = ingredients ->
                "{\"calorias\": 1, \"carboidratos\": 1, \"proteinas\": 1, \"gorduras\": 1}";
        } else {
            ChatLanguageModel chatLanguageModel = OpenAiChatModel.builder()
                    .apiKey(apiKey)
                    .modelName(modelName)
                    .build();
            this.nutricionalAIService = AiServices.builder(NutricionalAIService.class)
                    .chatLanguageModel(chatLanguageModel)
                    .build();
        }

    }

    //Calcular valores nutricionais por receita
    public void calcularNutricionalPorReceita(Receita receita){
        Map<String, Double> totalNutricional = new HashMap<>();
        totalNutricional.put("calorias", 0.0);
        totalNutricional.put("carboidratos", 0.0);
        totalNutricional.put("proteinas", 0.0);
        totalNutricional.put("gorduras", 0.0);

        List<String> ingredientes = receita.getIngredientes();
        for(String ingredienteText : ingredientes) {
            //Extrair o nome do ingrediente e quantidade
            Map<String, Object> ingredienteInfo = parseIngrediente(ingredienteText);
            String ingredienteNome = (String) ingredienteInfo.get("nome");
            double quantidade = (double) ingredienteInfo.get("quantidade");

            //Buscar ingrediente no banco de dados
            var ingredienteOpt = ingredientesRepository.findByNomeContainingIgnoreCase(ingredienteNome);
            if(ingredienteOpt.isPresent()){
                //se tiver o dado no BD
                var ingrediente = ingredienteOpt.get();
                double fator = quantidade / 100.0;
                totalNutricional.put("calorias", totalNutricional.get("calorias") +
                        ingrediente.getCalorias() * fator);
                totalNutricional.put("carboidratos", totalNutricional.get("carboidratos") +
                        ingrediente.getCarboidratos() * fator);
                totalNutricional.put("proteinas", totalNutricional.get("proteinas") +
                        ingrediente.getProteinas() * fator);
                totalNutricional.put("gorduras", totalNutricional.get("gorduras") +
                        ingrediente.getGorduras() * fator);
            }else {
                //IA
                try{
                    //se o ingrediente não for encontrado no DB, usar
                    // IA para estimar o valor nutricional
                    String ingredientesStr = String.join(", ", ingredientes);
                    String aiResponse = nutricionalAIService.calcularNutricional(ingredientesStr);

                    //Parsear a resposta JSON da IA
                    Pattern pattern = Pattern.compile("\"(calorias|carboidratos|proteinas|gorduras)\"\\s*:\\s*(\\d+(\\.\\d+)?)");
                    Matcher matcher = pattern.matcher(aiResponse);

                    while(matcher.find()){
                        String key = matcher.group(1);
                        double value = Double.parseDouble(matcher.group(2));
                        totalNutricional.put(key, totalNutricional.get(key) + value);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        //atualizar a receita com os valores nutricionais totais
        receita.setCalorias(totalNutricional.get("calorias"));
        receita.setCarboidratos(totalNutricional.get("carboidratos"));
        receita.setProteinas(totalNutricional.get("proteinas"));
        receita.setGorduras(totalNutricional.get("gorduras"));
    }

    private Map<String, Object> parseIngrediente(String ingredienteStr){
        Map<String, Object> resultado = new HashMap<>();
        Pattern pattern = Pattern.compile("(\\d+)\\s*g\\s+de\\s+(.+)");
        Matcher matcher = pattern.matcher(ingredienteStr);
        if(matcher.find()){
            double quantidade = Double.parseDouble(matcher.group(1));
            String nome = matcher.group(2).trim();
            resultado.put("quantidade", quantidade);
            resultado.put("nome", nome);
        } else {
            //se não conseguir parsear, assumir 100g
            resultado.put("quantidade", 100.0);
            resultado.put("nome", ingredienteStr.trim());
        }
        return resultado;
    }

}
