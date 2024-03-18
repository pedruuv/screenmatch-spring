package br.com.pedruuv.screenmatch.main;

import br.com.pedruuv.screenmatch.models.DadosSerie;
import br.com.pedruuv.screenmatch.models.DadosTemporada;
import br.com.pedruuv.screenmatch.services.ConsumoApi;
import br.com.pedruuv.screenmatch.services.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private Scanner scanner = new Scanner(System.in);
    private ConsumoApi api = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=983a7a2b";
    public void exibeMenu(){
        System.out.println("Digite o nome da série para busca:");
        var serie = scanner.nextLine().replace(" ", "+");
        var json = api.obterDados(ENDERECO + serie + API_KEY);

        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<>();

		for (int i = 1; i <= dados.totalTemporadas(); i++){
			json = api.obterDados(ENDERECO + serie + "&season="+ i + API_KEY);
			DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
			temporadas.add(dadosTemporada);
		}
		temporadas.forEach(System.out::println);
    }
}
