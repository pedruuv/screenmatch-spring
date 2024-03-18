package br.com.pedruuv.screenmatch;

import br.com.pedruuv.screenmatch.models.DadosSerie;
import br.com.pedruuv.screenmatch.services.ConsumoApi;
import br.com.pedruuv.screenmatch.services.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Hello World to Spring!");
		var api = new ConsumoApi();
		var json = api.obterDados("https://www.omdbapi.com/?t=the+mentalist&apikey=983a7a2b");
		System.out.println(json);

		var conversor = new ConverteDados();
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		System.out.println(dados);
	}
}
