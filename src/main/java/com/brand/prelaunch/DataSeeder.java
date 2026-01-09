package com.brand.prelaunch;

import com.brand.prelaunch.model.Product;
import com.brand.prelaunch.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;

    public DataSeeder(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (productRepository.count() == 0) {
            List<Product> products = List.of(
                    // FELPE
                    new Product("1", "FELPA_KERNEL_V1", 120, "BESTSELLER",
                            "Non è solo tessuto, è un'armatura. Cotone 450GSM spazzolato per isolamento termico assoluto. Taglio boxy progettato per l'anonimato urbano.",
                            "scanline", 50),

                    new Product("3", "FELPA_FIREWALL_BLK", 130, "NUOVO",
                            "Struttura difensiva contro il freddo. Cappuccio rinforzato a doppio strato. Ricamo 'NO SIGNAL' tono su tono.",
                            "diagonal", 30),

                    // T-SHIRT
                    new Product("2", "T-SHIRT_DAEMON", 45, "LIMITATO",
                            "Esegui il programma in background. Cotone organico trattato con lavaggio enzimatico per un feel 'vissuto' fin dal primo giorno.",
                            "noise", 100),

                    new Product("5", "T-SHIRT_GLITCH_SYS", 50, "POCHI PEZZI",
                            "Errore di sistema intenzionale. Grafica vettoriale ad alta densità che non sbiadisce mai. Vestibilità oversize aggressiva.",
                            "matrix", 80),

                    // PANTALONI
                    new Product("4", "PANTALONI_STACK", 90, "CARGO",
                            "Capacità di carico massima. 6 tasche tattiche con chiusura magnetica. Nylon ripstop resistente agli strappi e all'acqua.",
                            "matrix", 60),

                    new Product("6", "PANTALONI_VOID", 95, "RIPSTOP",
                            "Muoviti nel silenzio. Taglio ergonomico per mobilità totale. Finitura nero opaco che assorbe la luce.",
                            "scanline", 40)
            );

            productRepository.saveAll(products);
            System.out.println(">>> DATA_LOADED: DESCRIZIONI_ITALIANE_HYPE [OK]");
        }
    }
}