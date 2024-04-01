package br.com.pedruuv.screenmatch.main;

import br.com.pedruuv.screenmatch.models.DadosEpisodio;
import br.com.pedruuv.screenmatch.models.DadosSerie;
import br.com.pedruuv.screenmatch.models.DadosTemporada;
import br.com.pedruuv.screenmatch.models.Episodio;
import br.com.pedruuv.screenmatch.services.ConsumoApi;
import br.com.pedruuv.screenmatch.services.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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

        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        List<Episodio> episodios = temporadas.stream().flatMap(t -> t.episodios().stream()
                .map(d -> new Episodio(t.numeroTemporada(), d))).collect(Collectors.toList());

        episodios.forEach(System.out::println);


        System.out.println("A partir de que ano você quer ver os episódios?");
        var ano = scanner.nextInt();
        scanner.nextLine();

        LocalDate dataBusca =LocalDate.of(ano, 1, 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        episodios.stream().filter(d -> d.getDataLancamento() != null
                && d.getDataLancamento().isAfter(dataBusca)).forEach(e -> System.out.println("Temporada: "
                + e.getTemporada() + " Episódio: " + e.getTitulo() + " Data Lançamento: " + e.getDataLancamento()
                .format(formatter)));


        System.out.println("Digite um trecho do título do episódio");
        var trechoTitulo = scanner.nextLine();
        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTitulo().contains(trechoTitulo))
                .findFirst();

        if(episodioBuscado.isPresent()){
            System.out.println("Episódio encontrado!");
            System.out.println("Temporada: " + episodioBuscado.get().getTemporada());
        } else {
            System.out.println("Episódio não encontrado!");
        }

        Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));

        System.out.println(avaliacoesPorTemporada);
    }
}
