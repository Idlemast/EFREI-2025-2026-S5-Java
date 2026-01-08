
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;

/**
GRP : William WAN & Hsiao-Wen-Paul LO
 */

public class Main {
    
    public static void main(String[] args) {
        String line = String.format(" ".repeat(70));
	String line2 = String.format("%n"+"=".repeat(170)+"%n"+"=".repeat(170)+"%n");
        String line3 = String.format("#".repeat(120)+"%n");
        
	System.out.println(line2);
	System.out.format(line+"PARTIE 1 [1a] Classe Client");
        System.out.println(line2);
        
	System.out.format("%s%n%n", "On va créer 2 clients, un sans email et l'autre avec");
	Client client1 = new Client(Client.countNumerosClients++, "William", "06 66 66 66 66");
	Client client2 = new Client(Client.countNumerosClients++, "Paul", "06 22 22 22 22", "hsiao-wen-paul.lo@efrei.net");
	System.out.format("%s%n%n%s", "Sans email : ", client1);
	System.out.format("%s%n%n%s", "Avec email : ", client2);
	
        System.out.println(line2);
	System.out.format(line+ "PARTIE 1 [1b] Classe Prestation");
        System.out.println(line2);
        
	System.out.format("%s%n%n", "On va créer des prestations de toute sorte");
	PrestationExpress prestationExpress1 = new PrestationExpress(Prestation.CategorieVehicule.A, true);
	PrestationExpress prestationExpress2 = new PrestationExpress(Prestation.CategorieVehicule.C, false);
	PrestationSale prestationSale1 = new PrestationSale(Prestation.CategorieVehicule.A);
	PrestationTresSale prestationTresSale1 = new PrestationTresSale(Prestation.CategorieVehicule.B, PrestationTresSale.TypeSalissure._1);
	PrestationTresSale prestationTresSale2 = new PrestationTresSale(Prestation.CategorieVehicule.C, PrestationTresSale.TypeSalissure._3);
	System.out.format("%s%n%n%s", "Prestation Express avec Nettoyage :", prestationExpress1);
	System.out.format("%s%n%n%s", "Prestation Express sans Nettoyage :", prestationExpress2);
	System.out.format("%s%n%n%s", "Prestation Véhicule sale :", prestationSale1);
	System.out.format("%s%n%n%s", "Prestation Véhicule très sale (Salissure 1) :", prestationTresSale1);
	System.out.format("%s%n%n%s", "Prestation Véhicule très sale (Salissure 2) :", prestationTresSale2);
	
        System.out.println(line2);
	System.out.format(line + "PARTIE 1 [1c] Classe RendezVous");
        System.out.println(line2);
	System.out.format("%s%n%n", "On va créer des quelques rendez-vosu et les afficher");
	RendezVous rendezVous1 = new RendezVous(client1, prestationExpress1, 99);
	RendezVous rendezVous2 = new RendezVous(client2, prestationSale1, 110);
	System.out.format("%s%n%n%s", "Rendez-vous 1 : ", rendezVous1);
	System.out.format("%s%n%n%s", "Rendez-vous 2 : ", rendezVous2);
	
        System.out.println(line2);
	System.out.format(line + "PARTIE 1 [02] Classe Etablissement");
        System.out.println(line2);
        
	System.out.format("%s%n%n", "On va créer un établissement et les afficher");
	Etablissement etablissement1 = new Etablissement("EFREI", 3);
	System.out.format("%s%n%n%s", "Etablissement 1 (3 client max) : ", etablissement1);
        
	System.out.println(line2);
	System.out.format(line + "PARTIE 1 [3a] Classe Etablissement, rechercher() : Client");
        System.out.println(line2);
        
	System.out.format("On va rechercher un client (devrait retourner null puisqu'il n'y pas encore ajouter()) : %n");
	System.out.format("%s%s%n%n", "Recherche de Giorno Giovanna : ", etablissement1.rechercher("Giorno Giovanna", "07 77 77 77 77"));
	
        // test plus tard avec la fonction ajouter() qu'on a fait en bas 
        System.out.format("%s%n", "Maintenant on ajoute Giorno Giovanna dans l'établissement");
        etablissement1.ajouter("Giorno Giovanna", "07 77 77 77 77");
        System.out.format("%s%n", "Test 1 : Rechercher Giorno Giovanna ");
        System.out.format("%s%s%n%n", "Recherche de Giorno Giovanna : ", etablissement1.rechercher("Giorno Giovanna", "07 77 77 77 77"));
        System.out.format("%s%n", "Test 2 : Rechercher Giorna Giovanno ");
        System.out.format("%s%s%n%n", "Recherche de Giorna Giovanno : ", etablissement1.rechercher("Giorna Giovanno", "07 77 77 77 77"));
        System.out.format("%s%n", "Test 3 : Rechercher Giorno Giovanna mais avec un mauvais numéro de téléphone  ");
        System.out.format("%s%s%n%n", "Recherche de Giorna Giovanno : ", etablissement1.rechercher("Giorno Giovanna", "06 66 66 66 66"));
        System.out.format("%s%n", "Test 4 : Rechercher Giorno Giovanna mais en minuscule  ");
        System.out.format("%s%s%n%n", "Recherche de Giorno Giovanna : ", etablissement1.rechercher("giorno giovanna", "07 77 77 77 77"));
        
        
        System.out.println(line2);
	System.out.format(line + "PARTIE 1 [3b] Classe Client, placerApres() : boolean");
        System.out.println(line2);
        
	System.out.format("Trier 2 clients sur le nom pour voir qui est plus loin dans l'alphabet : %n");
	System.out.format("(si les 2 ont la même valeur, renvoie aussi false)%n%n");
	System.out.format("%s vs %s%n", client1.getNom(), client2.getNom());
	System.out.format("Est-ce que %s doit être placé après %s : %s%n", client1.getNom(), client2.getNom(), client1.placerApres(client2));
	System.out.format("Est-ce que %s doit être placé après %s : %s%n%n", client2.getNom(), client1.getNom(), client2.placerApres(client1));
	
        System.out.println(line2);
	System.out.format(line + "PARTIE 1 [3c] Classe Client, ajouter() : Client");
        System.out.println(line2);
        
	System.out.format("Ajouter un Client dans Etablissement, mais est-il dejà instantié ? %n");
	System.out.format("On suppose que non, et que c'est Etablissement qui va créer les clients%n");
	System.out.format("On va ajouter un client Giorno Giovanna : %n%n");
	Client client3 = etablissement1.ajouter("Giorno Giovanna", "07 77 77 77 77");
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
	
        
        
        
        
        
        
        System.out.println(line2);
	System.out.format(line + "PARTIE 1 [4a] Classe Prestation, prelavage(), lavage(), sechage(), nettoyage() : double");
        System.out.println(line2);
        
        System.out.format("Calculs des prestations selon la catégorie du véhicule et le type de prestation%n%n");

        /* ================= PRESTATION EXPRESS ================= */
        PrestationExpress prestaExpress1 = new PrestationExpress(Prestation.CategorieVehicule.A, false);
        PrestationExpress prestaExpress2 = new PrestationExpress(Prestation.CategorieVehicule.B, true);
        PrestationExpress prestaExpress3 = new PrestationExpress(Prestation.CategorieVehicule.B, false);
        PrestationExpress prestaExpress4 = new PrestationExpress(Prestation.CategorieVehicule.C, false);

        System.out.format("Prestation Express sans nettoyage — véhicule catégorie A (=30€) : %.2f€%n",
                prestaExpress1.nettoyage());

        System.out.format("Prestation Express avec nettoyage — véhicule catégorie B (=70.5€) : %.2f€%n",
                prestaExpress2.nettoyage());

        System.out.format("Prestation Express sans nettoyage — véhicule catégorie B (=40.5€) : %.2f€%n",
                prestaExpress3.nettoyage());

        System.out.format("Prestation Express sans nettoyage — véhicule catégorie C (=46€) : %.2f€%n%n",
                prestaExpress4.nettoyage());

        /* ================= PRESTATION SALE ================= */
        PrestationSale prestaSale1 = new PrestationSale(Prestation.CategorieVehicule.A);
        PrestationSale prestaSale2 = new PrestationSale(Prestation.CategorieVehicule.B);
        PrestationSale prestaSale3 = new PrestationSale(Prestation.CategorieVehicule.C);

        System.out.format("Prestation d'un véhicule sale — catégorie A (=65€) : %.2f€%n",
                prestaSale1.nettoyage());

        System.out.format("Prestation d'un véhicule sale — catégorie B (=78€) : %.2f€%n",
                prestaSale2.nettoyage());

        System.out.format("Prestation d'un véhicule sale — catégorie C (=94.75€) : %.2f€%n%n",
                prestaSale3.nettoyage());

        /* ================= PRESTATION TRÈS SALE ================= */
        PrestationTresSale prestaTresSale1 = new PrestationTresSale(
                Prestation.CategorieVehicule.A, PrestationTresSale.TypeSalissure._1);
        PrestationTresSale prestaTresSale2 = new PrestationTresSale(
                Prestation.CategorieVehicule.A, PrestationTresSale.TypeSalissure._2);

        PrestationTresSale prestaTresSale3 = new PrestationTresSale(
                Prestation.CategorieVehicule.B, PrestationTresSale.TypeSalissure._1);
        PrestationTresSale prestaTresSale4 = new PrestationTresSale(
                Prestation.CategorieVehicule.B, PrestationTresSale.TypeSalissure._2);

        PrestationTresSale prestaTresSale5 = new PrestationTresSale(
                Prestation.CategorieVehicule.C, PrestationTresSale.TypeSalissure._1);
        PrestationTresSale prestaTresSale6 = new PrestationTresSale(
                Prestation.CategorieVehicule.C, PrestationTresSale.TypeSalissure._2);
        PrestationTresSale prestaTresSale7 = new PrestationTresSale(
                Prestation.CategorieVehicule.C, PrestationTresSale.TypeSalissure._3);

        System.out.format("Prestation d'un véhicule très sale — catégorie A, salissure 1 (=66.98€) : %.2f€%n",
                prestaTresSale1.nettoyage());

        System.out.format("Prestation d'un véhicule très sale — catégorie A, salissure 2 (=68.98€) : %.2f€%n%n",
                prestaTresSale2.nettoyage());

        System.out.format("Prestation d'un véhicule très sale — catégorie B, salissure 1 (=79.98€) : %.2f€%n",
                prestaTresSale3.nettoyage());

        System.out.format("Prestation d'un véhicule très sale — catégorie B, salissure 2 (=81.98€) : %.2f€%n%n",
                prestaTresSale4.nettoyage());

        System.out.format("Prestation d'un véhicule très sale — catégorie C, salissure 1 (=96.73€) : %.2f€%n",
                prestaTresSale5.nettoyage());

        System.out.format("Prestation d'un véhicule très sale — catégorie C, salissure 2 (=98.73€) : %.2f€%n",
                prestaTresSale6.nettoyage());

        System.out.format("Prestation d'un véhicule très sale — catégorie C, salissure 3 (=100.73€) : %.2f€%n",
                prestaTresSale7.nettoyage());       
        
        System.out.println(line2);
	System.out.format(line + "PARTIE 1 [4b] Classe Etablissement, rechercher() : LocalDateTime");
        System.out.println(line2);
        
	System.out.format("%s%n%s%n%n", "On affiche tous les créneaux disponibles et on laisse l'utilisateur sélectionner", "Commentaires à décommenter pour essayer");
	//Retirer le commentaire pour tester rechercher() avec une LocalDate
	//Ici la LocalDate est fixée à +3 jours par rapport à aujourd'hui, libre de modifier
	//etablissement1.rechercher(LocalDate.now().plusDays(3));
	//Retirer le commentaire pour tester rechercher() avec un LocalTime
	//Ici le LocalTime est fixée à 18h (fermé), libre de modifier
	etablissement1.rechercher(LocalTime.of(18, 0));
	
        System.out.println(line2);
	System.out.format(line + "PARTIE 1 [4c] Classe Etablissement, ajouter() : RendezVous");
        System.out.println(line2);
        
	System.out.format("%s", etablissement1.printPlanning());
	System.out.format("%s%n%s%n%n", "Pour l'instant il n'y a aucun rendez-vous ajouté au planning, donc ajoutons un rendez-vous", "Ajoutons un créneau dans 6 jours à 10h :");
	RendezVous rdv1 = etablissement1.ajouter(client1, LocalDateTime.now().plusDays(6).withHour(10).withMinute(0), PrestationExpress.CategorieVehicule.A, true);
	System.out.format("%s", etablissement1.printPlanning());
	System.out.format("%s%n%s%n%n", "L'établissement est complet donc cela n'est pris en compte", "Mais si nous prenons Giorno Giovanna qui lui est dans la liste des clients");
	etablissement1.ajouter(etablissement1.rechercher(client3.getNom(), client3.getNumeroTelephone()), etablissement1.getDateTimeJour(DayOfWeek.FRIDAY).withHour(10).withMinute(0), PrestationExpress.CategorieVehicule.A, true);
	System.out.format("%s", etablissement1.printPlanning());
	
        System.out.format(line3);
	System.out.format(line + "PARTIE 2 [01] Classe Etablissement, planifier : void%n%n");
	//Retirer le commentaire pour lancer la fonction
	//Attention : l'établissement à 3 clients maximum (déjà atteint) donc à changer (etablissememnt1)
	//etablissement1.planifier();

	System.out.format(line + "PARTIE 2 [02] Classe Etablissement, afficher : String%n%n");
	System.out.format("%s%n%s%n%n", "On va d'abord tester l'affichage des rendez-vous sur un jour donné", "Lundi étant fermé, il affiche que c'est fermé");
	System.out.format(etablissement1.afficher(DayOfWeek.MONDAY));
	System.out.format(etablissement1.afficher(etablissement1.getDateTimeRendezVous(rdv1).getDayOfWeek()));
	System.out.format("%s%n%n", "Pour le reste ça semble bon");
	System.out.format("%s%n%s%n%s%n%n", "Ensuite pour les clients selon le nom ou le numéro client", "Nous allons prendre Giorno Giovanna pour exemple", "Pour rappel : Giorno Giovanna (07 77 77 77 77)");
	System.out.format("%s%n%n%s", "Avec une erreur sur le nom (Jonathan Joestar au lieu de Giorno Giovanna)", etablissement1.afficher("Jonathan Joestar", "07 77 77 77 77"));
	System.out.format("%s%n%n%s", "Avec une erreur sur le numéro de téléphone (06 77 77 77 77 au lieu de 07 77 77 77 77)", etablissement1.afficher("Giorno Giovanna", "06 77 77 77 77"));
	System.out.format("%s%n%n", "C'est correct");
	System.out.format("%s%n%n", "Nous allons maintenant vérifier qu'on puisse afficher les clients selon l'id");
	System.out.format("%s%n%n%s%n%n", "Avec un client qui n'est pas dans la liste de clients", etablissement1.afficher(client1.getNumeroClient()));
	System.out.format("%s%n%n%s%n%n", "Avec un client qui est dans la liste de clients", etablissement1.afficher(client3.getNumeroClient()));
	System.out.format("%s", etablissement1.printClients());

	    
	    
	    
	    

	
	
    }
    
    public void menu(){
	Scanner sc = new Scanner(System.in);
	int exit = -1;
	Etablissement etablissement3 = new Etablissement("EFREI 3", 5);
	etablissement3.depuisFichierClient();
	etablissement3.depuisFichierRDV();
	while(exit != 8){
	    exit = -1;
	    if(exit < 0 || exit > 8){
		System.out.format("%s%n", "Que voulez vous faire ?");
		System.out.format("%s%n%s%n%s%n%s%n%s%n%s%n%s%n%s%n%n",
			"[0] Rechercher un client",
			"[1] Ajouter un client",
			"[2] Rechercher un créneau par jour",
			"[3] Rechercher un créneau par heure",
			"[4] Planifier un rendez-vous",
			"[5] Afficher le planning sur un jour donné",
			"[6] Afficher selon le nom ou le numéro de téléphone",
			"[7] Afficher les rendez-vous selon le numéro client",
			"[8] Quitter le programme");
		try {
		    exit = Integer.parseInt(sc.nextLine());
		} catch(NumberFormatException e) {
		    exit = -1;
		}
		if(exit < 0 || exit > 8) System.out.format("%s%n", "La valeur n'est pas valide");
		switch(exit){
		    
		}
	    }
	}
    }
}