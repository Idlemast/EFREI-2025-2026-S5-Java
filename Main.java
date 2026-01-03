
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

/**
 *
 * @author William
 */
public class Main {
    
    public static void main(String[] args) {
	String line = String.format("%n" + "#".repeat(30) + "%n");
	
	System.out.println(line + "[1a] Classe Client");
	Client client1 = new Client(1, "William", "06 66 66 66 66");
	Client client2 = new Client(2, "Paul", "06 22 22 22 22", "hsiao-wen-paul.lo@efrei.net");
	System.out.println("Sans email : ");
	System.out.println(client1);
	System.out.println("Avec email : ");
	System.out.println(client2);
	
	System.out.println(line + "[1b] Classe Prestation");
	PrestationExpress prestationExpress1 = new PrestationExpress(Prestation.CategorieVehicule.A, true);
	PrestationExpress prestationExpress2 = new PrestationExpress(Prestation.CategorieVehicule.C, false);
	PrestationSale prestationSale1 = new PrestationSale(Prestation.CategorieVehicule.A);
	PrestationTresSale prestationTresSale1 = new PrestationTresSale(Prestation.CategorieVehicule.B, PrestationTresSale.TypeSalissure.A);
	PrestationTresSale prestationTresSale2 = new PrestationTresSale(Prestation.CategorieVehicule.C, PrestationTresSale.TypeSalissure.C);
	System.out.println("Prestation Express avec Nettoyage : ");
	System.out.println(prestationExpress1);
	System.out.println("Prestation Express sans Nettoyage : ");
	System.out.println(prestationExpress2);
	System.out.println("Prestation Véhicule sale : ");
	System.out.println(prestationSale1);
	System.out.println("Prestation Véhicule très sale (Salissure 1) : ");
	System.out.println(prestationTresSale1);
	System.out.println("Prestation Véhicule très sale (Salissure 2) : ");
	System.out.println(prestationTresSale2);
	
	System.out.format(line + "[1c] Classe RendezVous%n");
	RendezVous rendezVous1 = new RendezVous(client1, prestationExpress1, 99);
	RendezVous rendezVous2 = new RendezVous(client2, prestationSale1, 110);
	System.out.format("Rendez-vous 1 : %n");
	System.out.println(rendezVous1);
	System.out.format("Rendez-vous 2 : %n");
	System.out.println(rendezVous2);
	
	System.out.format(line + "[2] Classe Etablissement%n");
	Etablissement etablissement1 = new Etablissement("Château de Versailles", 3);
	System.out.format("Etablissement 1 (3 client max) : %n");
	System.out.println(etablissement1);
	
	System.out.format(line + "[3a] Classe Etablissement, rechercher() : Client%n");
	System.out.format("Rechercher un client (devrait retourner null puisqu'il n'y pas encore ajouter()) : %n");
	System.out.println("Recherche de Giorno Giovanna : " + etablissement1.rechercher("Giorno Giovanna", "07 77 77 77 77"));
	
	System.out.format(line + "[3b] Classe Client, placerApres() : boolean%n");
	System.out.format("Trier 2 clients sur le nom pour voir qui est plus loin dans l'alphabet : %n");
	System.out.format("(si les 2 ont la même valeur, renvoie aussi false)%n");
	System.out.format("%s vs %s%n", client1.getNom(), client2.getNom());
	System.out.format("Est-ce que %s doit être placé après %s : %s%n", client1.getNom(), client2.getNom(), client1.placerApres(client2));
	System.out.format("Est-ce que %s doit être placé après %s : %s%n", client2.getNom(), client1.getNom(), client2.placerApres(client1));
	
	System.out.format(line + "[3c] Classe Etablissement, ajouter() : Client%n");
	System.out.format("Ajouter un Client dans Etablissement, mais est-il dejà instantié ? %n");
	System.out.format("On suppose que non, et que c'est l'établissement qui va créer les clients%n");
	System.out.format("On va ajouter un client Giorno Giovanna : %n");
	etablissement1.ajouter("Giorno Giovanna", "07 77 77 77 77");
	System.out.println(etablissement1.printClients());
	System.out.format("Giorno Giovanna a bien été ajouté%n");
	System.out.format("On va ajouter un autre client Bruno Bucciarati : %n");
	etablissement1.ajouter("Bruno Bucciarati", "07 88 88 88 88", "bruno.bucciarati@jojo.jp");
	System.out.println(etablissement1.printClients());
	System.out.format("Bucciarati a bien été ajouté et Giorno a été placé après%n");
	System.out.format("Et si on ajoute encore 2 clients donc 4 sur 3 maximum : %n");
	etablissement1.ajouter("Dio Brando", "07 99 99 99 99");
	System.out.println(etablissement1.printClients());
	System.out.format("Ajout du client 4/3 (normalement null) : %s%n", etablissement1.ajouter("Narancia Ghirga", "07 55 55 55 55"));
	
	System.out.format(line + "[4a] Classe Prestation, prelavage(), lavage(), sechage(), nettoyage() : double%n");
	System.out.format("Calculs des trucs selon la catégorie du véhicule et le type de prestation %n");
	System.out.format("Prestation Express avec lavage d'un véhicule catégorie A (=65) : %s%n", prestationExpress1.nettoyage());
	System.out.format("Prestation d'un véhicule sale de catégorie A (=65) : %s%n", prestationSale1.nettoyage());
	System.out.format("Prestation d'un véhicule très sale de catégorie B (=78) : %s%n", prestationTresSale1.nettoyage());
	System.out.format("Prestation d'un véhicule très sale de catégorie C (=94,75) : %s%n", prestationTresSale2.nettoyage());
	
	System.out.format(line + "[4b] Classe Etablissement, rechercher : LocalDateTime%n");
	System.out.format("On affiche tous les créneaux disponibles et on laisse l'utilisateur sélectionner%n");
//	etablissement1.rechercher(LocalDate.now().plusDays(3));
	etablissement1.rechercher(LocalTime.of(18, 0));
	
	//Ne marche pas, je comprend pas la logique de la question
	System.out.format(line + "[4c] Classe Etablissement, ajouter : RendezVous%n");
//	System.out.format("On affiche tous les créneaux disponibles et on laisse l'utilisateur sélectionner%n");
//	etablissement1.ajouter(client1, LocalDateTime.now().plusHours(5).plusDays(6), PrestationExpress.CategorieVehicule.A, true);

	etablissement1.planifier();


//	Etablissement etab = new Etablissement("Test", 1000);
//	Client c1 = etab.ajouter("Alice", "0101");
//	Client c2 = etab.ajouter("Bob", "0102");
//	Client c3 = etab.ajouter("Charlie", "0103");
//
//	System.out.println(c1.getNumeroClient()); 
//	System.out.println(c2.getNumeroClient()); 
//	System.out.println(c3.getNumeroClient());
    }
}
