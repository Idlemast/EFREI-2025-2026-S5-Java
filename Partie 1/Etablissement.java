
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.DAYS;
import java.util.Locale;
import java.util.Scanner;

/**
 *  GRP : William WAN & Hsiao-Wen-Paul LO
 */
public class Etablissement {
    private static final Scanner sc = new Scanner(System.in);
    private static final LocalDateTime tomorrow = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).truncatedTo(MINUTES);

    private String nom;
    private final int limiteClients;
    private final int nombreMaxCreneaux = calculerCreneau(tomorrow.withHour(18).toLocalTime());
    private final int nombreMaxJours = 7;
    private final Client[] clients;
    private final RendezVous[][] planning;

    public Etablissement(String nom, int limiteClients) {
	this.nom = nom;
	this.limiteClients = limiteClients;
	this.clients = new Client[limiteClients];
	//Si les lignes sont des Creneau et que les colonnes sont des Jour, alors c'est un planning
	this.planning = new RendezVous[nombreMaxCreneaux][nombreMaxJours];
    }

    //Structure similaire pour les 2, donc on combine
    //Il faut boucler sur les éléments de la liste
    //C'est juste le if qui va changer (soit nom + téléphone, soit numéro client)
    private Client rechercherCalc(String args, boolean parNumeroClient){
	if (getNombreClients() != 0) {
	    //On ne fait pas for(Client client : clients) car il va boucler sur les null aussi
	    for(int c = 0; c < getNombreClients(); c++) {
		if(parNumeroClient){ //Si numeroClient
		    try {
			if (clients[c].getNumeroClient() == Integer.parseInt(args)) {
			    return clients[c];
			}
		    } catch(NumberFormatException e){ return null; } //retourne null s'il y a un problème
		} else { //Si nom + numeroTelephone
		    //On vérifie les 2 conditions directement
		    if (clients[c].getNom().equalsIgnoreCase(args.split(";")[0]) &&
			    clients[c].getNumeroTelephone().equalsIgnoreCase(args.split(";")[1])) {
			return clients[c];
		    }
		}
	    }
	}
	return null;
    } 
    
    //On convertit les arguments en 1 seul String pour tout passer dans la fonction de calcul
    //Le boolean détermine si args est un numéro client ou pas, ici non donc nom + téléphone
    public Client rechercher(String nom, String numeroTelephone) {
	return rechercherCalc(nom.concat(";").concat(numeroTelephone), false);
    }
    
    public Client rechercher(int numeroClient) {
	return rechercherCalc(Integer.toString(numeroClient), true);
    }

    //Pour éviter le doublon
    private Client ajouterCalc(Client c) {
	//Si le client peut être ajouter
	if (getNombreClients() >= 0 && getNombreClients() < limiteClients) {
	    //On assigne à la dernière position
	    clients[getNombreClients()] = c;
	    //On swap i-1 et i si pas dans l'ordre
	    for (int i = getNombreClients() - 1; i > 0 && clients[i - 1].placerApres(clients[i]); i--) {
		Client tmp = clients[i];
		clients[i] = clients[i - 1];
		clients[i - 1] = tmp;
	    }
	    return c;
	}
	return null;
    }
    
    //Vu que les clients doivent être numérotés automatiquement
    //et que depuisFichierClient() va remplacer toute la table
    //On ne peut pas mettre un auto-increment sur Client
    //C'est pour ça qu'on le fait ici
    public Client ajouter(String nom, String numeroTelephone) {
	return ajouterCalc(new Client(Client.countNumerosClients++, nom, numeroTelephone));
    }

    public Client ajouter(String nom, String numeroTelephone, String email) {
	return ajouterCalc(new Client(Client.countNumerosClients++, nom, numeroTelephone, email));
    }
    
    //Les 2 fonctions prenaient trop de place donc fusion
    private LocalDateTime rechercherCalc(LocalDateTime dt, boolean isDate) {
	//L'input est à -1 pour pouvoir boucler sur les index
	int input = -1;
	if (isDate) { //Si c'est une date
	    System.out.format("[CRENEAUX DISPONIBLES : %s]%n", getJourString(dt));
	    //Soit c'est fermé, soit la date donnée n'est pas dans les 7 prochains jours
	    if (verifierDate(dt.toLocalDate())) { //On vérifie la date
		System.out.format("%s%n%n", estFerme(dt.toLocalDate()) ?
			"Pas de créneau le lundi" : //Si c'est un lundi
			"Date non comprise dans les 7 prochains jours" //Si est différent de la semaine prochaine
		);
		return null;
	    } else {
		//On récupère l'index entre demain (0)
		//et la date donnée pour utiliser plusDays
		int j = getDeltaJours(dt.toLocalDate());
		//Affichage des créneaux en heures
		for (int c = 0; c < planning.length; c++) {
		    System.out.format("[%-2s] %s à %s ",
			    c, //L'index
			    calculerCreneau(c),	//La date du début du créneau en String
			    calculerCreneau(c + 1)  //La date de fin du créneau en String
		    ); 
		    //Est-ce que le créneau est déjà pris ?
		    //On rajoute une petite mention si c'est déjà pris
		    System.out.format(" %s%n", planning[c][j] != null ? "(Créneau déjà pris)" : "");
		}
		//Soit la valeur est hors limite (première fois à -1) soit le créneau est déjà pris
		while (input < 0 || input > planning.length - 1 || planning[input][j] != null) {
		    System.out.format("%s%n", "Veuillez choisir votre créneau :");
		    input = Integer.parseInt(sc.nextLine());
		    //On vérifie comme tout à l'heure et on affiche le message correspondant
		    //On vérifie la limite sinon crash avec != null
		    if (input < 0 || input > planning.length - 1 || planning[input][j] != null) {
			System.out.format("%s%n", input < 0 || input > planning.length - 1 ?
			    "Ce n'est pas dans la sélection" :
			    "Le créneau est déjà pris"
			);
		    }
		}
		//A partir d'ici l'input est valide
		System.out.format("%s%n", "Créneau validé !");
		System.out.format("Votre créneau est le %s de %s à %s%n",
			getJourString(dt), //Pour afficher le jour en texte
			//On affiche les créneaux
			calculerCreneau(input),
			calculerCreneau(input + 1)
		);
		//On se sert de l'input pour générer un LocalTime
		//calculerCreneau() renvoie soit selon l'argument :
		//un int qui correspond au nombre de tranches de 30 minutes depuis 10h
		//un LocalTime qui correspond au nombre à l'heure réel du rendez-vous
		return calculerCreneau(input).atDate(dt.toLocalDate());
	    }
	} else { //Si c'est une heure
	    System.out.format("[CRENEAUX DISPONIBLES : %s]%n", dt.toLocalTime());
	    //Si avant 10h ou après 17h30 (dernier créneau), on retourne null
	    if(dt.getHour() < tomorrow.getHour() || calculerCreneau(dt.toLocalTime()) > planning.length - 1){
		System.out.format("%s%n%n", "L'établissement est fermé à cette heure");
		return null;
	    }
	    //On boucle directement sur les jours
	    for (int j = 0; j < nombreMaxJours; j++) {
		System.out.format("[%-1s] %s ", j, getJourString(dt.plusDays(j)));
		//On vérifie si c'est un lundi et si c'est vide
		System.out.format("%s%n",
		    planning[calculerCreneau(dt.toLocalTime())][j] != null ? "(Déjà pris)" : //Si != null = déjà pris
		    estFerme(tomorrow.toLocalDate().plusDays(j)) ? "(fermé)" : "" //Sinon si c'est fermé, sinon rien
		);
	    }
	    while (input < 0 || input > nombreMaxJours - 1 || //Choisi une valeur correspondant à un jour
		planning[calculerCreneau(dt.toLocalTime())][input] != null || 
		estFerme(dt.toLocalDate().plusDays(input)) //Si c'est fermé
	    ) {
		System.out.format("Veuillez choisir votre créneau : %n");
		try {
		    input = Integer.parseInt(sc.nextLine());
		    //Si rentre un nombre non indexé
		    if (input < 0 || input > nombreMaxJours - 1 ||
			//ou que le créneau est déjà pris
			planning[calculerCreneau(dt.toLocalTime())][input] != null ||
			//ou que c'est fermé
			estFerme(dt.toLocalDate().plusDays(input))
		    ) {
			System.out.format("%s%n",
			    //Si nombre non valide
			    (input < 0 || input > nombreMaxJours - 1) ?
				"Ce n'est pas un créneau valide" :
				//Sinon si est fermé
				estFerme(dt.plusDays(input).toLocalDate()) ?
				    "L'établissement est fermé" :
				    //Sinon le créneau est déjà pris
				    "Le créneau est déjà pris"
			);
		    }
		} catch(NumberFormatException e){
		    System.out.format("%s%n", "Ce n'est pas un créneau valide");
		}
	    }
	    //A partir d'ici l'input est valide
	    System.out.format("%n%s%n", "Créneau validé !");
	    System.out.format("Votre créneau est le %s de %s à %s%n%n",
		getJourString(dt.plusDays(input)),
		calculerCreneau(calculerCreneau(dt.toLocalTime())),
		calculerCreneau(calculerCreneau(dt.toLocalTime()) + 1)
	    );
	    //retourne la date choisie
	    //y ajoute un LocalTime calculé avec un int calculé avec un LocalTime
	    //Pour éviter d'ajouter le temps manuellement
	    return calculerCreneau( 
		calculerCreneau(
		    dt.toLocalTime())
		).atDate(dt.toLocalDate().plusDays(input));
	}
    }
    
    public LocalDateTime rechercher(LocalDate date) {
	//On met une heure qui ne servira pas
	//Pour qu'il soit en LocalDateTime
	return rechercherCalc(date.atTime(tomorrow.getHour(), 0), true);
    }

    public LocalDateTime rechercher(LocalTime time) {
	//On met une date qui ne servira pas
	//Pour qu'il soit en LocalDateTime
	return rechercherCalc(time.atDate(tomorrow.toLocalDate()), false);
    }

    //Les 3 fonctions font la même chose donc on combine
    //Aucune logique juste mettre la date dans le planning
    //Mais faut aussi vérifier que le client est dans la liste
    private RendezVous ajouterCalc(RendezVous rdv, LocalDateTime dt){
	//Si le client n'est pas dans la liste de client
	if(rechercher(rdv.getClient().getNom(), rdv.getClient().getNumeroTelephone()) != null){
	    //On récupère les index et on ajoute
	    int j = getDeltaJours(dt.toLocalDate());
	    int c = calculerCreneau(dt.toLocalTime());
	    planning[c][j] = rdv;
	    return rdv;
	} else { return null; }
    }
    
    //Le pris du rendez-vous est calculé automatiquement
    
    //PrestationExpress
    public RendezVous ajouter(Client c, LocalDateTime dt, Prestation.CategorieVehicule categorieVehicule, boolean besoinNettoyage){
	PrestationExpress prestation = new PrestationExpress(categorieVehicule, besoinNettoyage);
	return ajouterCalc(new RendezVous(c, prestation, prestation.nettoyage()), dt);
    }
    
    //PrestationSale
    public RendezVous ajouter(Client c, LocalDateTime dt, Prestation.CategorieVehicule categorieVehicule){
	PrestationSale prestation = new PrestationSale(categorieVehicule);
	return ajouterCalc(new RendezVous(c, prestation, prestation.nettoyage()), dt);
    }
    
    //PrestationTresSale
    public RendezVous ajouter(Client c, LocalDateTime dt, Prestation.CategorieVehicule categorieVehicule, PrestationTresSale.TypeSalissure typeSalissure){
	PrestationTresSale prestation = new PrestationTresSale(categorieVehicule, typeSalissure);
	return ajouterCalc(new RendezVous(c, prestation, prestation.nettoyage()), dt);
    }
    
    //planifier() juste la partie Client
    public Client planifierClient(){
	String nom, numeroTelephone;
	System.out.format("%s%n", "Rentrez votre nom :");
	nom = sc.nextLine();
	System.out.format("%n%s%n", "Rentrez votre numéro de téléphone :");
	numeroTelephone = sc.nextLine();
	//Si le client est déjà dans la liste, on le récupère sinon on le créé
	return rechercher(nom, numeroTelephone) != null ?
	    rechercher(nom, numeroTelephone) :
	    ajouter(nom, numeroTelephone);
    }
    
    public LocalDateTime planifierCreneau(){
	//Pour boucler si l'input n'est pas bon
	int inputInt = -1;
	//Pour récupérer la date ou l'heure
	String inputStr;
	//On affecte à la valeur d'aujourd'hui pour avoir la LocalDate et le LocalTime
	//Elle n'est pas comprise dans verifierDate() ni dans verifierTime() donc on va boucler là-dessus
	LocalDateTime dt = tomorrow.minusDays(1).minusMinutes(1);
	//-------------------------
	//Booléen pour sélectionner vers quelle méthode on va s'orienter
	while(inputInt < 0 || inputInt > 1){
	    System.out.format("%n%s%n%s%n%s%n", "Comment voulez vous prendre votre rendez-vous ?", "[0] Date", "[1] Heure");
	    try {
		inputInt = Integer.parseInt(sc.nextLine());
		if(inputInt < 0 || inputInt > 1) System.out.format("%s %s%n", inputInt, "n'est pas une valeur valide");
	    } catch(NumberFormatException e) {
		System.out.format("%s %s%n", getErrorString(e), "n'est pas une valeur valide");
	    }
	}
	//------------------------------------
	//Si date (= 0)
	if(inputInt == 0){
	    while(verifierDate(dt.toLocalDate())){
		//Pas de vérification du type de l'input trop chiant à faire (ex : 2025-15-156)
		System.out.format("%n%s%n%s%n", "Veuillez entrer une date souhaitée :", "(dans le format suivant : YYYY-MM-DD et sur les 7 jours qui suivent)");
		inputStr = sc.nextLine();
		try {
		    //Année
		    dt = dt.withYear(Integer.parseInt(inputStr.split("-")[0]))
			//Mois
			.withMonth(Integer.parseInt(inputStr.split("-")[1]))
			//Jour
			.withDayOfMonth(Integer.parseInt(inputStr.split("-")[2]));
		    if(verifierDate(dt.toLocalDate())) System.out.format("%n%s %s%n", dt.toLocalDate(), " n'est pas une date valide");
		} catch(NumberFormatException e){
		    //Pour éviter l'erreur
		    System.out.format("%s %s%n", getErrorString(e), "n'est pas une valeur valide");
		}
	    }
	    System.out.format("%n");
	    dt = rechercher(dt.toLocalDate());
	} else {
	    System.out.println(verifierTime(dt.toLocalTime()));
	    while(verifierTime(dt.toLocalTime())){
		System.out.format("%n%s%n%s%n",
		    "Veuillez entrer une heure souhaitée :",
		    "(dans le format suivant : hh:mm)");
		inputStr = sc.nextLine();
		try {
		    //Heure
		    dt = dt.withHour(Integer.parseInt(inputStr.split(":")[0]))
			//Minute
			.withMinute(Integer.parseInt(inputStr.split(":")[1]));
		    if(verifierTime(dt.toLocalTime())) System.out.format("%n%s %s%n", dt.toLocalTime(), "n'est pas une heure valide");
		} catch(NumberFormatException e){
		    System.out.format("%s %s%n", getErrorString(e), "n'est pas une valeur valide");
		}
	    }
	    System.out.format("%n");
	    dt = rechercher(dt.toLocalTime());
	}
	return dt;
    }
    
    public Prestation.CategorieVehicule planifierCategorieVehicule(){
	int input = -1;
	Prestation.CategorieVehicule categorieVehicule = null;
	while(input < 0 || input > 2){
	    System.out.format("%n%s%n%s%n%s%n%s%n",
		"Veuillez choir votre type de véhicule :",
		"[0] Citadines", "[1] Berlines",
		"[2] Monospaces et 4x4");
	    try {
		input = Integer.parseInt(sc.nextLine());
		if(input < 0 || input > 2){
		    System.out.format("%s%s%n", input, " n'est pas un choix valide");
		}
	    } catch(NumberFormatException e){
		System.out.format("%s %s%n", getErrorString(e), "n'est pas une valeur valide");
	    }
	    categorieVehicule =
		input == 0 ? Prestation.CategorieVehicule.A :
		input == 1 ? Prestation.CategorieVehicule.B : 
		Prestation.CategorieVehicule.C;
	}
	return categorieVehicule;
    }
    
    public void planifier(){
	Client c = planifierClient();
	//Si le nombre de clients max est atteint et que le client n'est pas déjà dans la liste
	if(c == null){
	    System.out.format("%n%s%n%s%n%n",
		"Le nombre maximum de clients pour cet établissement a été atteint",
		"et le client n'est pas sur la liste, donc cela ne va pas être possible");
	} else {
	    LocalDateTime dt = planifierCreneau();
	    Prestation.CategorieVehicule categorieVehicule = planifierCategorieVehicule();
	    RendezVous rdv;
	    int input = -1;
	    while(input < 0 || input > 2){
		System.out.format("%n%s%n%s%n%s%n%s%n",
		    "Veuillez choisir votre type de prestation :",
		    "[0] Prestation Express",
		    "[1] Prestation sur véhicule sale",
		    "[2] Prestation sur véhicule très sale");
		try {
		    input = Integer.parseInt(sc.nextLine());
		    if(input < 0 || input > 2) System.out.format("%s%n", "Votre choix de type de prestation n'est pas valide");
		} catch(NumberFormatException e){
		    System.out.format("%s %s%n", getErrorString(e), "n'est pas une valeur valide");
		}
	    }
	    switch(input){
		case 0, 1 -> {
		    if(input == 1){
			//PrestationSale
			rdv = ajouter(c, dt, categorieVehicule);
		    } else {
			input = -1;
			while(input < 0 || input > 1){
			    System.out.format("%n%s%n%s%n%s%n",
				"Est-ce que votre véhicule à besoin d'être nettoyé ?",
				"[0] Oui",
				"[1] Non");
			    try {
				input = Integer.parseInt(sc.nextLine());
				if(input < 0 || input > 1) System.out.format("%n%s %s%n", input, "n'est pas un choix valide");
			    } catch(NumberFormatException e){
				System.out.format("%s %s%n", getErrorString(e), "n'est pas une valeur valide");
			    }
			}
			//PrestationExpress
			rdv = ajouter(c, dt, categorieVehicule, input == 0);
		    }
		}
		//PrestationTresSale
		default -> {
		    input = -1;
		    while(input < 0 || input > PrestationTresSale.TypeSalissure.values().length - 1){
			System.out.format("%s%n", "Quel est votre problème ?");
			for(int i = 0; i < PrestationTresSale.TypeSalissure.values().length - 1; i++){
			    System.out.format("[%s] %s%n", i, PrestationTresSale.TypeSalissure.values()[i]);
			}
			try {
			    input = Integer.parseInt(sc.nextLine());
			    if(input < 0 || input > PrestationTresSale.TypeSalissure.values().length - 1) System.out.format("%n%s%n", "Votre problème n'est pas valide");
			} catch(NumberFormatException e){
			    System.out.format("%s %s%n", getErrorString(e), "n'est pas une valeur valide");
			}
			
		    }
		    rdv = ajouter(c, dt, categorieVehicule, PrestationTresSale.TypeSalissure.values()[input]);
		}
	    }
	}
	System.out.format("%n");
    }
    
    //NE PAS ENCORE EN COMPTE SI LE CLIENT N'EST PAS DANS LA LISTE CLIENT
    public String afficher(DayOfWeek d){
	String str = "";
	//On va récupérer l'index du jour souhaité
	int index = getJourIndex(d);
	str += String.format("Voici le planning du %s%n", getJourString(tomorrow.plusDays(index)));
	//Si c'est fermé
	if(estFerme(tomorrow.toLocalDate().plusDays(index))){
	    str += String.format("%s%n%n", "L'établiseement est fermé le lundi");
	    return str;
	}
	//Sinon on affiche chaque créneau
	for(int c = 0; c < nombreMaxCreneaux; c++){
	    str += String.format("%-5s-%-5s ", calculerCreneau(c), this.calculerCreneau(c + 1));
	    str += String.format("%s%n", (planning[c][index] != null) ?
		"(" + planning[c][index].getClient().getNom() + ")" : "");
	}
	str += String.format("%n");
	return str;
    }
    
    //On boucle juste sur tout les noms et numéros de téléphone
    public String afficher(String nom, String numeroTelephone){
	String str = "";
	int value = 0;
	str += String.format("%s%n", "Voici les clients qui correspondent au nom ou au numéro de téléphone donnés :");
	str += String.format("Pour le nom (%s) :%n", nom);
	for(Client c : clients){
	    if(c.getNom().equalsIgnoreCase(nom)) value++;
	    str += (c.getNom().equalsIgnoreCase(nom)) ? String.format("- %s (%s, %s%s)%n", c.getNom(), c.getNumeroClient(), c.getNumeroTelephone(), c.getEmail() != null ? ", " + c.getEmail() : "") : "";
	}
	if(value == 0) str += String.format("%s%n", "Aucun client avec ce nom");
	value = 0;
	str += String.format("Pour le numéro de téléphone (%s) :%n", numeroTelephone);
	for(Client c : clients){
	    if(c.getNumeroTelephone().equalsIgnoreCase(numeroTelephone)) value++;
	    str += (c.getNumeroTelephone().equalsIgnoreCase(numeroTelephone)) ? String.format("- %s (%s, %s%s)%n", c.getNom(), c.getNumeroClient(), c.getNumeroTelephone(), c.getEmail() != null ? ", " + c.getEmail() : "") : "";
	}
	if(value == 0) str += String.format("%s%n", "Aucun client avec ce numéro de téléphone");
	str += String.format("%n");
	return str;
    }
    
    //Afficher les rendez-vous selon le numéro client
    public String afficher(int n){
	//Va chercher le client selon le numéro client
	Client client = rechercher(n);
	String str = "";
	str += String.format("Voici les rendez-vous du client n°%s%n", n);
	int nbRDV = 0;
	//Si il y a client, on balaye tout les créneaux, pour vérifier le numéro client
	if(client != null){
	    for (int c = 0; c < nombreMaxCreneaux; c++) {
		for (int j = 0; j < nombreMaxJours; j++) {
		    if(planning[c][j] != null){
			nbRDV++;
			str += planning[c][j];
		    }
		}
	    }
	    str += nbRDV == 0 ? String.format("%s%n", "Ce client n'a aucun rendez-vous'") : "";
	    return str;
	} else {
	    str += String.format("%s%s%n", "Pour cet établissement, il n'y a pas de client n°", n);
	    return str;
	}
    }
    
    
    //-----------------------------------------------
    public void versFichierClients() {
	try {
	    //On écrase le fichier, on exporte tout à chaque fois
	    FileWriter fw = new FileWriter("clients.txt", false);
	    for (Client c : clients) {
		if (c != null) fw.write(c.versFichier());
	    }
	    fw.close();
	    System.out.format("%s%n", "La liste des clients a bien été exportée");
	} catch(IOException e){
	    System.out.format("%s%n", "Le fichier clients.txt n'a pas pu être créé");
	}
    }
    
    //On suppose que le fichier respecte la limite de clients de l'établissement
    public void depuisFichierClient(){
	BufferedReader br;
	try {
	    br = new BufferedReader(new FileReader("clients.txt"));
	    String s, separator = " : ";
	    for(int i = 0; i < getLimiteClients(); i++){
		s = br.readLine();
		//On utilise pas ajouter() car la fonction assigne un
		//numeroClient qui n'est pas celui du fichier
		//C'est donc un import en full, sans ajout manuel
		//Il faut quand même vérifier chaque client
		//Sans email et avec
		if(s.split(separator).length == 3){
		    clients[i] = new Client(
			    Integer.parseInt(s.split(separator)[0]),
			    s.split(separator)[1],
			    s.split(separator)[2]
		    );
		} else{
		    clients[i] = new Client(
			    Integer.parseInt(s.split(separator)[0]),
			    s.split(separator)[1],
			    s.split(separator)[2],
			    s.split(separator)[3]
		    );
		}
	    }
	} catch (IOException e) {
	    System.getLogger(Etablissement.class.getName()).log(System.Logger.Level.ERROR, (String) null, e);
	} catch (NullPointerException e) {
	    //Fin du fichier
	}
    }
    
    public void versFichierRDV(){
	try {
	    FileWriter fw = new FileWriter("rdv.txt", false);
	    for (int c = 0; c < nombreMaxCreneaux; c++) {
		for (int j = 0; j < nombreMaxJours; j++) {
		    if(planning[c][j] != null){
			fw.write(String.format("%s%n", getDateTimeRendezVous(planning[c][j])));
			fw.write(String.format("%s%n", planning[c][j].getClient().getNumeroClient()));
			fw.write(String.format("%s", planning[c][j].versFichier()));
		    }
		}
	    }
	    fw.close();
	} catch (IOException e) {
	    System.out.format("%s%n", "Le fichier n'a pas pu être créé");
	}
    }
    
    //On suppose que le fichier respecte la limite de clients de l'établissement
    public void depuisFichierRDV(){
	BufferedReader br;
	try {
	    br = new BufferedReader(new FileReader("rdv.txt"));
	    //Vu que le fichier contient tout les rendez-vous, 
	    RendezVous[][] planning = new RendezVous[nombreMaxCreneaux][nombreMaxJours];
	    String s, separator = " : ";
	    LocalDateTime dt;
	    Client c;
	    Prestation.CategorieVehicule categorie;
	    boolean stop = false;
	    while(stop == false){
		try {
		    dt = LocalDateTime.parse(br.readLine());
		    //Logiquement, le client est dans la liste des clients
		    c = rechercher(Integer.parseInt(br.readLine()));
		    s = br.readLine();
		    categorie = Prestation.CategorieVehicule.valueOf(s.split(separator)[0]);
		    //PrestationExpress ou PrestationTresSale
		    if(s.split(separator).length == 3){
			try {
			    Integer.valueOf(s.split(separator)[1]);
			    //PrestationTresSale
			    ajouter(c, dt, categorie, PrestationTresSale.TypeSalissure.valueOf("_".concat(s.split(separator)[1])));
			} catch(NumberFormatException e){
			    //PrestationExpress
			    ajouter(c, dt, categorie, Boolean.parseBoolean(s.split(separator)[1]));
			}
		    } else {
			//PrestationSale
			ajouter(c, dt, categorie);
		    }
		} catch(IOException | NullPointerException e) {
		    //Si c'est la fin du fichier
		    stop = !stop;
		}
	    }
	} catch (FileNotFoundException ex) {
	    System.out.format("%s%n", "Le fichier rdv.txt n'existe pas");
	}
    }

    //Retourne le LocaTime correspondant grâce à l'index
    public LocalTime calculerCreneau(int c) {
	return tomorrow.toLocalTime().plusMinutes(c * 30);
    }

    public int calculerCreneau(LocalTime t) {
	int h = t.getHour() - tomorrow.getHour();
	//Donne +1 pour chaque 30min
	//10:01 donnera donc 1 au lieu de 0, pas de retard possible
	int m = t.getMinute() < 30 ?
	    t.getMinute() == 0 ? 0 : 1 :
	    t.getMinute() == 30 ? 1 : 2;
	return h * 2 + m;
    }
    
    //Retourner true si c'est un lundi
    public boolean estFerme(LocalDate date) {
	return date.getDayOfWeek() == DayOfWeek.MONDAY;
    }
    
    //Si c'est un lundi ou que le jour ne correspond pas
    public boolean verifierDate(LocalDate date){
	return estFerme(date) || //Si c'est un lundi
	    date.isBefore(tomorrow.toLocalDate()) || //Si aujourd'hui ou plus tôt
	    date.isAfter(tomorrow.toLocalDate().plusDays(nombreMaxJours)); //Si 8 jours après ou plus tard
    }
    
    //Si <10h ou >17h30
    public boolean verifierTime(LocalTime time){
	return
	    time.getHour() < tomorrow.getHour() || //Si <10h
	    calculerCreneau(time) > nombreMaxCreneaux; //Si >17h30h
    }
    
    public LocalDateTime getDateTimeRendezVous(RendezVous rdv){
	if(getNombreRDV() != 0){
	    for (int c = 0; c < nombreMaxCreneaux; c++) {
		for (int j = 0; j < nombreMaxJours; j++) {
		    //On balaye tout les créneaux pour retrouver le LocalDateTime correspondant
		    if(rdv == planning[c][j]) return tomorrow.plusDays(j).plusMinutes(c * 30);
		}
	    }
	}
	System.out.format("%s%n", "Pas de créneau retrouvé");
	return null;
    }
    
    //Retourne la différence entre 2 jours en int
    public int getDeltaJours(LocalDate date){
	return (int) DAYS.between(tomorrow.toLocalDate(), date);
    }
    
    //Retourne le jour des 7 prochains jours qui correspond
    //au jour demandé
    public LocalDateTime getDateTimeJour(DayOfWeek dw){
	LocalDateTime dt = null;
	for(int i = 0; i < nombreMaxJours; i++){
	    if(tomorrow.plusDays(i).getDayOfWeek() == dw) dt = tomorrow.plusDays(i);
	}
	return dt;
    }
    
    //Renvoie l'index d'un jour donné
    public int getJourIndex(DayOfWeek dw){
	int index = 0;
	for(int i = 0; i < nombreMaxJours; i++){
	    if(tomorrow.plusDays(i).getDayOfWeek() == dw) index = i;
	}
	return index;
    }
    
    //Renvoie le jour sous forme de String en français et majuscules
    public String getJourString(LocalDateTime date){
	return date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.FRANCE).toUpperCase();
    }

    //Pour éviter de créer une variable + incrément, cette fonction calcule directement le nombre de clients 
    public int getNombreClients() {
	int n = 0;
	for (Client c : clients) {
	    if (c != null) n++;
	}
	return n;
    }
    
    //Balaye tout les créneaux pour renvoyer le nombre de créneaux
    public int getNombreRDV() {
	int n = 0;
	for (int c = 0; c < nombreMaxCreneaux; c++) {
	    for (int j = 0; j < nombreMaxJours; j++) {
		if(planning[c][j] != null) n++;
	    }
	}
	return n;
    }

    //On affiche la liste des clients
    public String printClients() {
	String str = "";
	//On affiche tout les clients
	str += String.format("%s%n", "Liste des clients :");
	//Liste vide
	if (getNombreClients() == 0) {
	    str += String.format("%s%n", "vide");
	} else {
	    for (int i = 0; i < getLimiteClients(); i++) {
		//Ignore les null
		if (clients[i] != null) {
		    str += String.format("- %s (%s, %s%s)%n", clients[i].getNom(), clients[i].getNumeroClient(), clients[i].getNumeroTelephone(), clients[i].getEmail() != null ? ", " + clients[i].getEmail() : "");
		}
	    }
	}
	str += String.format("%n");
	return str;
    }

    //On affiche le planning
    public String printPlanning() {
	String str = "";
	//Pour mettre chaque jour (lundi jusqu'à dimanche)
	str += String.format("%-11s", "");
	//On affiche les jours
	for (int jourTexte = 0; jourTexte < nombreMaxJours; jourTexte++) {
	    str += String.format(" | %-20s", getJourString(tomorrow.plusDays(jourTexte)));
	}
	//On boucle sur tout les créneaux
	for (int c = 0; c < nombreMaxCreneaux; c++) {
	    //Pour indiquer le créneau sur chaque ligne
	    str += String.format("%n%-5s-%-5s", Etablissement.this.calculerCreneau(c), Etablissement.this.calculerCreneau(c + 1));
	    for (int j = 0; j < nombreMaxJours; j++) {
		str += (planning[c][j] != null) ?
		    String.format(" | %-20s", planning[c][j].getClient().getNom()) :
		    String.format(" | %-20s", (estFerme(tomorrow.toLocalDate().plusDays(j))) ?
			"-".repeat(20) : ""
		);
	    }
	}
	str += String.format("%n%n");
	return str;
    }
    
    public String getErrorString(Exception e){
	String error = e.getMessage();
	return error.substring(error.indexOf("\"") + 1, error.lastIndexOf("\""));
	
    }
    
    //Divison du print car trop long
    @Override
    public String toString() {
	String str = "";
	str += String.format("%s%s%n%n", "Nom de l'établissement : ", nom);
	str += printClients();
	str += printPlanning();
	return str;
    }
    
    //Getter + Setter
    public String getNom() { return nom; }
    
    //Pas de set pour limiteCLients car un établissement à un nombre fixe de clients possible
    public int getLimiteClients() { return limiteClients; }
    
    //Si l'établissement se fait racheter
    public void setNom(String nom) { this.nom = nom; }
}
