
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 *
 * @author William
 */
public class Main {
    
    public static void main(String[] args) {
	String line = String.format("#".repeat(30) + "%n");
	
	System.out.format(line + "PARTIE 1 [1a] Classe Client%n%n");
	System.out.format("%s%n%n", "On va créer 2 clients, un sans email et l'autre avec");
	Client client1 = new Client(1, "William", "06 66 66 66 66");
	Client client2 = new Client(2, "Paul", "06 22 22 22 22", "hsiao-wen-paul.lo@efrei.net");
	System.out.format("%s%n%n%s", "Sans email : ", client1);
	System.out.format("%s%n%n%s", "Avec email : ", client2);
	
	System.out.format(line + "PARTIE 1 [1b] Classe Prestation%n%n");
	System.out.format("%s%n%n", "On va créer des prestations de toute sorte");
	PrestationExpress prestationExpress1 = new PrestationExpress(Prestation.CategorieVehicule.A, true);
	PrestationExpress prestationExpress2 = new PrestationExpress(Prestation.CategorieVehicule.C, false);
	PrestationSale prestationSale1 = new PrestationSale(Prestation.CategorieVehicule.A);
	PrestationTresSale prestationTresSale1 = new PrestationTresSale(Prestation.CategorieVehicule.B, PrestationTresSale.TypeSalissure.A);
	PrestationTresSale prestationTresSale2 = new PrestationTresSale(Prestation.CategorieVehicule.C, PrestationTresSale.TypeSalissure.C);
	System.out.format("%s%n%n%s", "Prestation Express avec Nettoyage :", prestationExpress1);
	System.out.format("%s%n%n%s", "Prestation Express sans Nettoyage :", prestationExpress2);
	System.out.format("%s%n%n%s", "Prestation Véhicule sale :", prestationSale1);
	System.out.format("%s%n%n%s", "Prestation Véhicule très sale (Salissure 1) :", prestationTresSale1);
	System.out.format("%s%n%n%s", "Prestation Véhicule très sale (Salissure 2) :", prestationTresSale2);
	
	System.out.format(line + "PARTIE 1 [1c] Classe RendezVous%n%n");
	System.out.format("%s%n%n", "On va créer des quelques rendez-vosu et les afficher");
	RendezVous rendezVous1 = new RendezVous(client1, prestationExpress1, 99);
	RendezVous rendezVous2 = new RendezVous(client2, prestationSale1, 110);
	System.out.format("%s%n%n%s", "Rendez-vous 1 : ", rendezVous1);
	System.out.format("%s%n%n%s", "Rendez-vous 2 : ", rendezVous2);
	
	System.out.format(line + "PARTIE 1 [02] Classe Etablissement%n%n");
	System.out.format("%s%n%n", "On va créer un établissement et les afficher");
	Etablissement etablissement1 = new Etablissement("EFREI", 3);
	System.out.format("%s%n%n%s", "Etablissement 1 (3 client max) : ", etablissement1);
	
	System.out.format(line + "PARTIE 1 [3a] Classe Etablissement, rechercher() : Client%n%n");
	System.out.format("On va rechercher un client (devrait retourner null puisqu'il n'y pas encore ajouter()) : %n");
	System.out.format("%s%s%n%n", "Recherche de Giorno Giovanna : ", etablissement1.rechercher("Giorno Giovanna", "07 77 77 77 77"));
	
	System.out.format(line + "PARTIE 1 [3b] Classe Client, placerApres() : boolean%n%n");
	System.out.format("Trier 2 clients sur le nom pour voir qui est plus loin dans l'alphabet : %n");
	System.out.format("(si les 2 ont la même valeur, renvoie aussi false)%n%n");
	System.out.format("%s vs %s%n", client1.getNom(), client2.getNom());
	System.out.format("Est-ce que %s doit être placé après %s : %s%n", client1.getNom(), client2.getNom(), client1.placerApres(client2));
	System.out.format("Est-ce que %s doit être placé après %s : %s%n%n", client2.getNom(), client1.getNom(), client2.placerApres(client1));
	
	System.out.format(line + "PARTIE 1 [3c] Classe Client, ajouter() : Client%n%n");
	System.out.format("Ajouter un Client dans Etablissement, mais est-il dejà instantié ? %n");
	System.out.format("On suppose que non, et que c'est l'établissement qui va créer les clients%n");
	System.out.format("On va ajouter un client Giorno Giovanna : %n%n");
	etablissement1.ajouter("Giorno Giovanna", "07 77 77 77 77");
	System.out.format(etablissement1.printClients());
	System.out.format("Giorno Giovanna a bien été ajouté%n");
	System.out.format("On va ajouter un autre client Bruno Bucciarati : %n%n");
	etablissement1.ajouter("Bruno Bucciarati", "07 88 88 88 88", "bruno.bucciarati@jojo.jp");
	System.out.format(etablissement1.printClients());
	System.out.format("Bucciarati a bien été ajouté et Giorno a été placé après%n");
	System.out.format("Et si on ajoute encore 2 clients donc 4 sur 3 maximum : %n%n");
	etablissement1.ajouter("Dio Brando", "07 99 99 99 99");
	System.out.format(etablissement1.printClients());
	System.out.format("Ajout du client 4/3 (normalement null) : %s%n%n", etablissement1.ajouter("Narancia Ghirga", "07 55 55 55 55"));
	
	System.out.format(line + "PARTIE 1 [4a] Classe Prestation, prelavage(), lavage(), sechage(), nettoyage() : double%n%n");
	System.out.format("Calculs des prestations selon la catégorie du véhicule et le type de prestation %n");
	System.out.format("Prestation Express avec lavage d'un véhicule catégorie A (=65) : %s%n", prestationExpress1.nettoyage());
	System.out.format("Prestation d'un véhicule sale de catégorie A (=65) : %s%n", prestationSale1.nettoyage());
	System.out.format("Prestation d'un véhicule très sale de catégorie B (=78) : %s%n", prestationTresSale1.nettoyage());
	System.out.format("Prestation d'un véhicule très sale de catégorie C (=94,75) : %s%n%n", prestationTresSale2.nettoyage());
	
	System.out.format(line + "PARTIE 1 [4b] Classe Etablissement, rechercher() : LocalDateTime%n%n");
	System.out.format("%s%n%s%n%n", "On affiche tous les créneaux disponibles et on laisse l'utilisateur sélectionner", "Commentaires à décommenter pour essayer");
	//Retirer le commentaire pour tester rechercher() avec une LocalDate
	//Ici la LocalDate est fixée à +3 jours par rapport à aujourd'hui, libre de modifier
	//etablissement1.rechercher(LocalDate.now().plusDays(3));
	//Retirer le commentaire pour tester rechercher() avec un LocalTime
	//Ici le LocalTime est fixée à 18h (fermé), libre de modifier
	etablissement1.rechercher(LocalTime.of(18, 0));
	
	System.out.format(line + "PARTIE 1 [4c] Classe Etablissement, ajouter() : RendezVous%n%n");
	System.out.format("%s", etablissement1.printPlanning());
	System.out.format("%s%n%s%n%n", "Pour l'instant il n'y a aucun rendez-vous ajouté au planning, donc ajoutons un rendez-vous", "Ajoutons un créneau dans 6 jours à 10h :");
	RendezVous rdv1 = etablissement1.ajouter(client1, LocalDateTime.now().plusDays(6).withHour(10).withMinute(0), PrestationExpress.CategorieVehicule.A, true);
	System.out.format("%s", etablissement1.printPlanning());
	System.out.format("%s%n%s%n%n", "L'établissement est complet donc cela n'est pris en compte", "Mais si nous prenons Giorno Giovanna qui lui est dans la liste des clients");
	etablissement1.ajouter(etablissement1.rechercher("Giorno Giovanna", "07 77 77 77 77"), LocalDateTime.now().plusDays(6).withHour(10).withMinute(0), PrestationExpress.CategorieVehicule.A, true);
	System.out.format("%s", etablissement1.printPlanning());
	
	
	System.out.format(line + "PARTIE 2 [01] Classe Etablissement, planifier : void%n%n");
	//Retirer le commentaire pour lancer la fonction
	//Attention : l'établissement à 3 clients maximum (déjà atteint) donc à changer
	//etablissement1.planifier();

	System.out.format(line + "PARTIE 2 [02] Classe Etablissement, afficher : String%n%n");
	System.out.format("%s%n%s%n%n", "On va d'abord tester l'affichage des rendez-vous sur un jour donné", "Lundi étant fermé, il affiche que c'est fermé");
	System.out.format(etablissement1.afficher(DayOfWeek.MONDAY));
	System.out.format(etablissement1.afficher(etablissement1.getDateRendezVous(rdv1).getDayOfWeek()));
	System.out.format("%s%n%n", "Pour le reste ça semble bon");
	System.out.format("%s%n%s%n%s%n%n", "Ensuite pour les clients selon le nom ou le numéro client", "Nous allons prendre Giorno Giovanna pour exemple", "Pour rappel : Giorno Giovanna (07 77 77 77 77)");
	System.out.format("%s%n%n%s", "Avec une erreur sur le nom (Jonathan Joestar au lieu de Giorno Giovanna)", etablissement1.afficher("Jonathan Joestar", "07 77 77 77 77"));
	System.out.format("%s%n%n%s", "Avec une erreur sur le numéro de téléphone (06 77 77 77 77 au lieu de 07 77 77 77 77)", etablissement1.afficher("Giorno Giovanna", "06 77 77 77 77"));
	System.out.format("%s%n%n", "C'est correct");
//	System.out.format("%s%n%n", "Nous allons maintenant vérifier que ");
	
	
    }
}