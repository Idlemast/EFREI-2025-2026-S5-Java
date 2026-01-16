import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 *
 *  GRP : William WAN & Hsiao-Wen-Paul LO
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
        System.out.format("%s%n", "Maintenant on ajoute Jotaro Kujo dans l'établissement");
        etablissement1.ajouter("Jotaro Kujo", "08 77 77 77 77");

        System.out.format("%s%n", "Test 1 : Rechercher Jotaro Kujo ");
        System.out.format("%s%s%n%n", "Recherche de Jotaro Kujo : ", etablissement1.rechercher("Jotaro Kujo", "08 77 77 77 77"));

        System.out.format("%s%n", "Test 2 : Rechercher Jotara Kujo ");
        System.out.format("%s%s%n%n", "Recherche de Jotara Kujo : ", etablissement1.rechercher("Jotara Kujo", "08 77 77 77 77"));

        System.out.format("%s%n", "Test 3 : Rechercher Jotaro Kujo mais avec un mauvais numéro de téléphone");
        System.out.format("%s%s%n%n", "Recherche de Jotaro Kujo : ", etablissement1.rechercher("Jotaro Kujo", "08 66 66 66 66"));
        
        System.out.format("%s%n", "Test 4 : Rechercher Jotaro Kujo mais en minuscule");
        System.out.format("%s%s%n%n", "Recherche de Jotaro Kujo : ", etablissement1.rechercher("jotaro kujo", "08 77 77 77 77"));
 
        System.out.format("Test 5 : Rechercher le client n°2 (devrait exister) | Résultat : %s%n", etablissement1.rechercher(2));

        System.out.format("Test 6 : Rechercher le client n°999 (n'existe pas) | Résultat : %s%n", etablissement1.rechercher(999));
        
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

        // PRESTATION EXPRESS
        PrestationExpress prestaExpress1 = new PrestationExpress(Prestation.CategorieVehicule.A, false);
        PrestationExpress prestaExpress2 = new PrestationExpress(Prestation.CategorieVehicule.B, true);

        System.out.format("Prestation Express sans nettoyage — véhicule catégorie A (=30€) : %.2f€%n",
                prestaExpress1.nettoyage());

        System.out.format("Prestation Express avec nettoyage — véhicule catégorie B (=70.5€) : %.2f€%n",
                prestaExpress2.nettoyage());


        // PRESTATION SALE
        PrestationSale prestaSale1 = new PrestationSale(Prestation.CategorieVehicule.A);
        PrestationSale prestaSale2 = new PrestationSale(Prestation.CategorieVehicule.B);

        System.out.format("Prestation d'un véhicule sale — catégorie A (=65€) : %.2f€%n",
                prestaSale1.nettoyage());

        System.out.format("Prestation d'un véhicule sale — catégorie B (=78€) : %.2f€%n",
                prestaSale2.nettoyage());


        // PRESTATION TRÈS SALE 
        PrestationTresSale prestaTresSale1 = new PrestationTresSale(
                Prestation.CategorieVehicule.A, PrestationTresSale.TypeSalissure._1);
        PrestationTresSale prestaTresSale2 = new PrestationTresSale(
                Prestation.CategorieVehicule.A, PrestationTresSale.TypeSalissure._2);

        PrestationTresSale prestaTresSale3 = new PrestationTresSale(
                Prestation.CategorieVehicule.B, PrestationTresSale.TypeSalissure._1);

        System.out.format("Prestation d'un véhicule très sale — catégorie A, salissure 1 (=66.98€) : %.2f€%n",
                prestaTresSale1.nettoyage());

        System.out.format("Prestation d'un véhicule très sale — catégorie A, salissure 2 (=68.98€) : %.2f€%n%n",
                prestaTresSale2.nettoyage());

        System.out.format("Prestation d'un véhicule très sale — catégorie B, salissure 1 (=79.98€) : %.2f€%n",
                prestaTresSale3.nettoyage());


        
        System.out.println(line2);
	System.out.format(line + "PARTIE 1 [4b] Classe Etablissement, rechercher() : LocalDateTime");
        System.out.println(line2);
        
	System.out.format("%s%n%s%n%n", "On affiche tous les créneaux disponibles et on laisse l'utilisateur sélectionner", "Commentaires à décommenter pour essayer");
	//Retirer le commentaire pour tester rechercher() avec une LocalDate
	//Ici la LocalDate est fixée à +3 jours par rapport à aujourd'hui, libre de modifier
	//etablissement1.rechercher(LocalDate.now().plusDays(3));;
	//etablissement1.rechercher(LocalTime.of(13, 0));
        
	//Retirer le commentaire pour tester rechercher() avec un LocalTime
	//Ici le LocalTime est fixée à 18h (fermé), libre de modifier
	etablissement1.rechercher(LocalTime.of(18, 0));
	// test lundi ( fermé ) 
        LocalDate prochainLundi = etablissement1.getDateTimeJour(DayOfWeek.MONDAY).toLocalDate();
        etablissement1.rechercher(prochainLundi);
        // test + de 7 prochains jours 
        etablissement1.rechercher(LocalDate.now().plusDays(20));
        
        System.out.println(line2);
	System.out.format(line + "PARTIE 1 [4c] Classe Etablissement, ajouter() : RendezVous");
        System.out.println(line2);
        
	System.out.format("%s", etablissement1.printPlanning());
	System.out.format("%s%n%s%n%n", "Pour l'instant il n'y a aucun rendez-vous ajouté au planning, donc ajoutons un rendez-vous", "Ajoutons un créneau dans le vendredi à 10h :");
	etablissement1.ajouter(client1, etablissement1.getDateTimeJour(DayOfWeek.FRIDAY).withHour(10).withMinute(0), PrestationExpress.CategorieVehicule.A, true);
	System.out.format("%s%n%s%n%n", "L'établissement est complet donc cela n'est pris en compte", "Mais si nous prenons Giorno Giovanna qui lui est dans la liste des clients");
	RendezVous rendezVous3 = etablissement1.ajouter(etablissement1.rechercher(client3.getNom(), client3.getNumeroTelephone()), etablissement1.getDateTimeJour(DayOfWeek.FRIDAY).withHour(10).withMinute(0), PrestationExpress.CategorieVehicule.A, true);
	System.out.format("%s", etablissement1.printPlanning());

	
	
	System.out.format(line + "PARTIE 2 [01] Classe Etablissement, planifier : void%n%n");
	System.out.format("%s%n", "Retirer le commentaire pour lancer la fonction");
//	//Attention : l'établissement 1 à 3 clients maximum (déjà atteint) donc on va créer un nouveau
	Etablissement etablissement100 = new Etablissement("EFREI 100", 100);
//	etablissement100.planifier();
//          
        System.out.println(line2);
	System.out.format(line + "PARTIE 2 [02] Classe Etablissement, afficher : String%n%n");
        System.out.println(line2);
	System.out.format("%s%n%s%n%n", "On va d'abord tester l'affichage des rendez-vous sur un jour donné", "Lundi étant fermé, il affiche que c'est fermé");
	System.out.format(etablissement1.afficher(DayOfWeek.MONDAY));
	System.out.format(etablissement1.afficher(DayOfWeek.FRIDAY));
	System.out.format(etablissement1.afficher(etablissement1.getDateTimeRendezVous(rendezVous3).getDayOfWeek()));
	System.out.format("%s%n%n", "Pour le reste ça semble bon");
	System.out.format("%s%n%s%n%s%n%n", "Ensuite pour les clients selon le nom ou le numéro client", "Nous allons prendre Giorno Giovanna pour exemple", "Pour rappel : Giorno Giovanna (07 77 77 77 77)");
	System.out.format("%s%n%n%s", "Avec une erreur sur le nom (Jonathan Joestar au lieu de Giorno Giovanna)", etablissement1.afficher("Jonathan Joestar", "07 77 77 77 77"));
	System.out.format("%s%n%n%s", "Avec une erreur sur le numéro de téléphone (06 77 77 77 77 au lieu de 07 77 77 77 77)", etablissement1.afficher("Giorno Giovanna", "06 77 77 77 77"));
	System.out.format("%s%n%n", "C'est correct");
	System.out.format("%s%n%n", "Nous allons maintenant vérifier qu'on puisse afficher les clients selon l'id");
	System.out.format("%s%n%n%s%n%n", "Avec un client qui n'est pas dans la liste de clients", etablissement1.afficher(client1.getNumeroClient()));
	System.out.format("%s%n%n%s%n%n", "Avec un client qui est dans la liste de clients", etablissement1.afficher(client3.getNumeroClient()));
	System.out.format("%s", etablissement1.printClients());
	
	

	etablissement1.versFichierClients();
	etablissement100.depuisFichierClient();
	System.out.format(etablissement100.printClients());
	etablissement1.ajouter(etablissement1.rechercher(client3.getNom(), client3.getNumeroTelephone()), etablissement1.getDateTimeJour(DayOfWeek.FRIDAY).withHour(12).withMinute(0), PrestationTresSale.CategorieVehicule.B, PrestationTresSale.TypeSalissure._3);
	etablissement1.versFichierRDV();
	etablissement100.depuisFichierRDV();
	System.out.format(etablissement100.printPlanning());
	Etablissement etablissementTest = new Etablissement("Etablissement Test", 100);
	etablissementTest.depuisFichierClient();
	etablissementTest.depuisFichierRDV();
//	etablissementTest.planifier();
	menu();
    } 
   
    public static void menu(){
	Scanner sc = new Scanner(System.in);
	Etablissement etab = new Etablissement("Station-lavage", 1000);
	etab.depuisFichierClient();
	etab.depuisFichierRDV();
	Client c = null;
	LocalDateTime dt = null;
	int input;
	boolean exit = false;
	while(exit != true){
	    input = -1;
	    while(input < 0 || input > 8){
		System.out.format("%s%n", "Que voulez vous faire ?");
		if(c != null) System.out.format("%s%n", "Dernière interaction client : " + c.getNom());
		if(dt != null) System.out.format("%s%n", "Dernier créneau : " + dt);
		System.out.format("%s%n%s%n%s%n%s%n%s%n%s%n%s%n%s%n%s%n",
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
		    input = Integer.parseInt(sc.nextLine());
		    if(input < 0 || input > 8){
			System.out.format("%s%n", "La valeur " + exit + " n'est pas valide");
		    }
		} catch(NumberFormatException e) { System.out.format("%s %s%n", e, "n'est pas une valeur valide"); }
	    }
	    switch(input){
		case 0 -> {
		    //rechercher() - nom + téléphone
		    c = menuRechercherClient(etab);
		}
		case 1 -> {
		    //ajouter() : Client - nom, telephone
		    c = menuAjouterClient(etab);
		}
		case 2 -> {
		    //Rechercher un créneau par jour
		    dt = menuRechercherCreneauDate(etab);
		}
		case 3 -> {
		    //Rechercher un créneau par heure
		    dt = menuRechercherCreneauHeure(etab);
		}
		case 4 -> {
		    //Planifier un rendez-vous
		    menuPlanifier(etab, c, dt);
		    dt = null;
		}
		case 5 -> {
		    //Afficher le planning sur un jour donné
		    menuAfficherPlanning(etab);
		}
		case 6 -> {
		    //Afficher selon le nom ou le numéro de téléphone
		    menuAfficherClient(etab);
		}
		case 7 -> {
		    //Afficher les rendez-vous selon le numéro client
		    menuAfficherRendezVous(etab, c);
		}
		default -> {
		    //Fin du programme
		    exit = true;
		    etab.versFichierClients();
		    etab.versFichierRDV();
		}
	    }
	}
    }
    
    public static Client menuRechercherClient(Etablissement e){
	Scanner sc = new Scanner(System.in);
	String nom, numeroTelephone;
	System.out.format("%s%n", "Donnez un nom :");
	nom = sc.nextLine();
	System.out.format("%s%n", "Donnez un numéro de téléphone :");
	numeroTelephone = sc.nextLine();
	System.out.format("%s%n", e.rechercher(nom, numeroTelephone) != null ?
	    //S'il trouve
	    e.rechercher(nom, numeroTelephone) :
	    //S'il ne trouve pas
	    "Il n'y a pas de client pour ce nom et numéro de téléphone"
	);
	return e.rechercher(nom, numeroTelephone);
    }
    
    public static Client menuAjouterClient(Etablissement etab){
	Scanner sc = new Scanner(System.in);
	String nom, numeroTelephone, email;
	System.out.format("%s%n", "Donnez un nom :");
	nom = sc.nextLine();
	System.out.format("%s%n", "Donnez un numéro de téléphone :");
	numeroTelephone = sc.nextLine();
	System.out.format("%s%n", "Si le client a un email, tapez-le, sinon répondez non :");
	email = sc.nextLine();
	if(etab.getNombreClients() >= 0 && etab.getNombreClients() < etab.getLimiteClients()){
	    if(email.equalsIgnoreCase("non")){
		return etab.ajouter(nom, numeroTelephone);
	    } else {
		return etab.ajouter(nom, numeroTelephone, email);
	    }
	} else {
	    System.out.format("%s%n", "Le client ne peut pas être ajouté, il,n'y a plus de place");
	    return null;
	}
    }
	
    public static LocalDateTime menuRechercherCreneauDate(Etablissement etab){
	Scanner sc = new Scanner(System.in);
	LocalDate date = etab.getDate().minusDays(1);
	String value;
	while(etab.verifierDate(date)){
	    System.out.format("%s%n", "Veuillez entrer une date souhaitée dans le format suivant YYYY-MM-DD et sur les 7 jours qui suivent :");
	    value = sc.nextLine();
	    try {
		//Année
		date = date.withYear(Integer.parseInt(value.split("-")[0]))
		    //Mois
		    .withMonth(Integer.parseInt(value.split("-")[1]))
		    //Jour
		    .withDayOfMonth(Integer.parseInt(value.split("-")[2]));
		if(etab.verifierDate(date)) System.out.format("%s%n", date + " n'est pas une date valide");
	    } catch(NumberFormatException e){
		//Pour éviter l'erreur
		System.out.format("%s%n", etab.getErrorString(e) + " n'est pas une valeur valide");
	    }
	}
	return etab.rechercher(date);
    }
    
    public static LocalDateTime menuRechercherCreneauHeure(Etablissement etab){
	Scanner sc = new Scanner(System.in);
	LocalTime time = null;
	String value;
	while(etab.verifierTime(time)){
	    System.out.format("%n%s","Veuillez entrer une heure souhaitée dans le format suivant HH:MM :");
	    value = sc.nextLine();
	    try {
		//Heure
		time = time.withHour(Integer.parseInt(value.split(":")[0]))
		    //Minute
		    .withMinute(Integer.parseInt(value.split(":")[1]));
		if(etab.verifierTime(time)) System.out.format("%s%n", time + " n'est pas une heure valide");
	    } catch(DateTimeParseException | NumberFormatException e){
		System.out.format("%s%n", etab.getErrorString(e) + " n'est pas une valeur valide");
	    }
	}
	return etab.rechercher(time);
    }
    
    public static void menuPlanifier(Etablissement etab, Client c, LocalDateTime dt){
	Scanner sc = new Scanner(System.in);
	int value = -1;
	System.out.format("ok ");
	//Propose de prendre le dernier client et/ou le dernier créneau
	while((value < 0 || value > 1) && (c != null || dt != null)){
	    System.out.format("ok2 ");
	    if(c != null || dt != null){
		System.out.format("ok3 ");
		//affichage selon ce qu'il y a
		System.out.format("%s%n",
		    (c != null && dt != null) ?
			"Vous avez actuellement un client (" + c.getNom() + ") et un créneau (" + dt + "), voulez-vous utiliser ces paramètres pour la planification ?" :
			(c != null && dt == null) ?
			    "Vous avez actuellement un client (" + c.getNom() + "), voulez-vous utiliser ces paramètres pour la planification ?" :
			    "Vous avez actuellement un créneau (" + dt + "), voulez-vous utiliser ces paramètres pour la planification ?"
		);
		System.out.format("%s%n%s%n", "[0] Oui", "[1] Non");
		try {
		    value = Integer.parseInt(sc.nextLine());
		    if(value < 0 || value > 1){
			System.out.format("%s%n", "La valeur " + value + " n'est pas un choix valide");
		    }
		} catch(NumberFormatException e) { System.out.format("%s%n", e + " n'est pas une valeur valide"); }
	    }
	}
	//Fonction selon si on prend en compte les paramètres
	if(value == 0){
	    System.out.format("%s%n", "Nous allons donc utiliser le client et le créneau pour faire le rendez-vous");
	    etab.planifier(c, dt);
	} else {
	    System.out.format("%s%n", "Nous allons partir de rien pour ce rendez-vous");
	    etab.planifier();
	}
    }
    
    public static void menuAfficherPlanning(Etablissement etab){
	Scanner sc = new Scanner(System.in);
	int value = -1;
	//2 à 7 car on prend les valeurs des jours qui vont de 1 à 7
	while(value < 2 || value > 7){
	    System.out.format("%s%n%s%n%s%n%s%n%s%n%s%n%s%n%s%n", "Choisissez un jour :",
		"[1] Lundi (fermé)",
		"[2] Mardi",
		"[3] Mercredi",
		"[4] Jeudi",
		"[5] Vendredi",
		"[6] Samedi",
		"[7] Dimanche"
	    );
	    try {
		value = Integer.parseInt(sc.nextLine());
		if(value < 2 || value > 7){
		    System.out.format("%s%n", "La valeur " + value + " n'est pas valide");
		}
	    } catch(NumberFormatException e) { System.out.format("%s %s%n", e, "n'est pas une valeur valide"); }
	}
	//La valeur est valide
	System.out.format(etab.afficher(DayOfWeek.of(value)));
    }
    
    public static void menuAfficherClient(Etablissement etab){
	Scanner sc = new Scanner(System.in);
	String nom, numeroTelephone;
	System.out.format("%s%n", "Donnez un nom :");
	nom = sc.nextLine();
	System.out.format("%s%n", "Donnez un numéro de téléphone :");
	numeroTelephone = sc.nextLine();
	System.out.format(etab.afficher(nom, numeroTelephone));
    }
    
    public static void menuAfficherRendezVous(Etablissement etab, Client c){
	Scanner sc = new Scanner(System.in);
	int value = -1;
	//Propose de charger la dernière interaction client
	if(c != null){
	    while(value < 0 || value > 1){
		System.out.format("%s%n%s%n%s%n", "Votre dernière interaction client concernait " + c.getNom() + ", voulez-vous voir les rendez-vous de ce client ? ", "[0] Oui", "[1] Non");
		try {
		    value = Integer.parseInt(sc.nextLine());
		    if(value < 0 || value > 1){
			System.out.format("%s%n", "La valeur " + value + " n'est pas valide");
		    }
		} catch(NumberFormatException e) { System.out.format("%s %s%n", e, "n'est pas une valeur valide"); }
	    }
	    if(value == 0) System.out.format(etab.afficher(c.getNumeroClient()));
	}
	//Si on part d'un numéro précis
	if(value != 0){
	    value = -1;
	    while(value < 0){
		System.out.format("%s%n", "Entrez un numéro client :");
		try {
		    value = Integer.parseInt(sc.nextLine());
		    if(value < 0) System.out.format("%s%n", "La valeur " + value + " n'est pas valide");
		} catch(NumberFormatException e) { System.out.format("%s %s%n", e, "n'est pas une valeur valide"); }
	    }
	    System.out.format(etab.afficher(value));
	}
    }
}