package com.alura.literalura;

import com.alura.literalura.model.*;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LivroRepository;
import com.alura.literalura.service.ConsumoApi;
import com.alura.literalura.service.ConverteDados;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@SpringBootApplication
public class ChallengeLiterAluraApplication implements CommandLineRunner {

    private final String URL_BASE = "https://gutendex.com/books/?search=";
    private final ConsumoApi consumoApi = new ConsumoApi();
    private final ConverteDados conversor = new ConverteDados();
    private final Scanner leitura = new Scanner(System.in);

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private AutorRepository autorRepository;

    public static void main(String[] args) {
        SpringApplication.run(ChallengeLiterAluraApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    \n*** LiterAlura ***
                    Escolha uma opção:
                    1 - Buscar livro pelo título
                    2 - Listar livros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos em um determinado ano
                    5 - Listar livros em um determinado idioma
                    0 - Sair
                    """;
            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarLivroPeloTitulo();
                    break;
                case 2:
                    listarLivrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivosEmAno();
                    break;
                case 5:
                    listarLivrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarLivroPeloTitulo() {
        System.out.println("Digite o nome do livro para busca:");
        var nomeLivro = leitura.nextLine();
        var json = consumoApi.obterDados(URL_BASE + nomeLivro.replace(" ", "+"));
        ResultadosBusca dadosBusca = conversor.obterDados(json, ResultadosBusca.class);

        Optional<DadosLivro> livroBuscado = dadosBusca.livros().stream().findFirst();

        if (livroBuscado.isPresent()) {
            DadosLivro dadosLivro = livroBuscado.get();
            DadosAutor dadosAutor = dadosLivro.autores().stream().findFirst().orElse(null);

            if (dadosAutor != null) {
                Autor autor = autorRepository.findByNomeContainingIgnoreCase(dadosAutor.nome())
                        .orElseGet(() -> {
                            Autor novoAutor = new Autor();
                            novoAutor.setNome(dadosAutor.nome());
                            novoAutor.setAnoNascimento(dadosAutor.anoNascimento());
                            novoAutor.setAnoFalecimento(dadosAutor.anoFalecimento());
                            return autorRepository.save(novoAutor);
                        });

                Livro livro = new Livro();
                livro.setTitulo(dadosLivro.titulo());
                livro.setIdioma(dadosLivro.idiomas().stream().findFirst().orElse(""));
                livro.setDownloads(dadosLivro.downloads());
                livro.setAutor(autor);
                livroRepository.save(livro);

                System.out.println("Livro salvo com sucesso!");
                System.out.println("Título: " + livro.getTitulo());
                System.out.println("Autor: " + autor.getNome());
            }
        } else {
            System.out.println("Livro não encontrado.");
        }
    }

    private void listarLivrosRegistrados() {
        List<Livro> livros = livroRepository.findAll();
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro registrado.");
        } else {
            livros.forEach(l -> System.out.println(
                    "----- LIVRO -----\n" +
                    " Título: " + l.getTitulo() + "\n" +
                    " Autor: " + l.getAutor().getNome() + "\n" +
                    " Idioma: " + l.getIdioma() + "\n" +
                    " Downloads: " + l.getDownloads() + "\n"
            ));
        }
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("Nenhum autor registrado.");
        } else {
            autores.forEach(a -> System.out.println(
                    "----- AUTOR -----\n" +
                    " Nome: " + a.getNome() + "\n" +
                    " Ano de Nascimento: " + a.getAnoNascimento() + "\n" +
                    " Ano de Falecimento: " + a.getAnoFalecimento() + "\n"
            ));
        }
    }

    private void listarAutoresVivosEmAno() {
        System.out.println("Digite o ano para buscar autores vivos:");
        var ano = leitura.nextInt();
        leitura.nextLine();
        List<Autor> autores = autorRepository.findAutoresVivosEmAno(ano);
        if (autores.isEmpty()) {
            System.out.println("Nenhum autor vivo encontrado para o ano de " + ano);
        } else {
            autores.forEach(a -> System.out.println("Autor: " + a.getNome()));
        }
    }

    private void listarLivrosPorIdioma() {
        System.out.println("Digite o idioma para a busca (ex: en, pt, es, fr):");
        var idioma = leitura.nextLine();
        List<Livro> livros = livroRepository.findByIdioma(idioma);
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado para o idioma " + idioma);
        } else {
            livros.forEach(l -> System.out.println(
                    "----- LIVRO -----\n" +
                    " Título: " + l.getTitulo() + "\n" +
                    " Autor: " + l.getAutor().getNome() + "\n"
            ));
        }
    }
}
