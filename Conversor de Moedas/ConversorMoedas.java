import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class ConversorMoedas {

    // Método para realizar a requisição à API e retornar a taxa de câmbio
    public static double obterTaxaCambio(String de, String para) throws Exception {
        String chave = "chave"; // Sua chave de API
        String endereco = "https://v6.exchangerate-api.com/v6/" + chave + "/latest/USD"; // Requisição única com base no USD

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endereco))
                .build();
        HttpResponse<String> response = client
                .send(request, HttpResponse.BodyHandlers.ofString());

        String resposta = response.body();
        Gson gson = new Gson();
        JsonObject json = JsonParser.parseString(resposta).getAsJsonObject();
        JsonObject moedas = json.getAsJsonObject("conversion_rates"); // Todas as taxas de câmbio com base no USD

        // Obter a taxa de câmbio de 'de' e 'para' em relação ao USD
        double taxaPara = moedas.get(para).getAsDouble(); // Ex: USD para BRL
        double taxaDe = moedas.get(de).getAsDouble();   // Ex: USD para EUR

        // A taxa de conversão direta de 'de' para 'para' é:
        double taxaCambio = taxaPara * taxaDe;

        return taxaCambio; // Retorna a taxa de conversão
    }

    // Menu de opções
    public static void exibirMenu() {
        System.out.println("Escolha a conversão de moeda desejada:");
        System.out.println("1. USD para EUR");
        System.out.println("2. EUR para USD");
        System.out.println("3. BRL para USD");
        System.out.println("4. USD para BRL");
        System.out.println("5. BRL para EUR");
        System.out.println("6. EUR para BRL");
        System.out.println("7. Sair");
    }

    // Método principal
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

        while (continuar) {
            exibirMenu();
            int opcao = scanner.nextInt();

            if (opcao == 7) {
                System.out.println("Encerrando o programa...");
                continuar = false;
                break;
            }

            System.out.println("Digite o valor que deseja converter:");
            double valor = scanner.nextDouble();

            String de = "";
            String para = "";

            switch (opcao) {
                case 1:
                    de = "USD";
                    para = "EUR";
                    break;
                case 2:
                    de = "EUR";
                    para = "USD";
                    break;
                case 3:
                    de = "BRL";
                    para = "USD";
                    break;
                case 4:
                    de = "USD";
                    para = "BRL";
                    break;
                case 5:
                    de = "BRL";
                    para = "EUR";
                    break;
                case 6:
                    de = "EUR";
                    para = "BRL";
                    break;
                default:
                    System.out.println("Opção inválida.");
                    continue;
            }

            try {
                // Obter a taxa de câmbio da API
                double taxa = obterTaxaCambio(de, para);
                double valorConvertido = valor * taxa;
                System.out.printf("Valor convertido: %.2f %s\n", valorConvertido, para);
            } catch (Exception e) {
                System.out.println("Erro ao acessar a API de conversão: " + e.getMessage());
            }
        }

        scanner.close();
    }
}
