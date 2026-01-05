import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.DAYS;
import java.util.Locale;
import java.util.Scanner;

/**
 * @author William
 */
public class Etablissement {
    private static final Scanner sc = new Scanner(System.in);
    private static final LocalDateTime tomorrow = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).truncatedTo(MINUTES);

    private String nom;
    private final int limiteClients;
    private final int nombreMaxCreneaux = calculCreneau(tomorrow.withHour(18).toLocalTime());
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
    //Il faut bloucler sur les éléments de la liste
    //C'est juste le if qui va changer (soit nom + téléphone, soit numéro client)
    private Client rechercherCalc(String args, boolean parNumeroClient){
	if (getNombreClients() != 0) {
	    //On ne fait pas for(Client client : clients) car il va boucler sur les null aussi
	    for(int c = 0; c < getNombreClients(); c++) {
		if(parNumeroClient){
		    if (clients[c].getNumeroClient() == Integer.parseInt(args)) {
			return clients[c];
		    }
		} else {
		    if (clients[c].getNom().equalsIgnoreCase(args.split(";")[0]) && clients[c].getNumeroTelephone().equalsIgnoreCase(args.split(";")[1])) {
			return clients[c];
		    }
		}
	    }
	}
	return null;
    } 
    
    //On convertit les arguments en 1 seul String pour tout passer dans la fonction de calcul
    public Client rechercher(String nom, String numeroTelephone) {
	return rechercherCalc(nom.concat(";").concat(numeroTelephone), false);
    }
    
    public Client rechercher(int numeroClient) {
	return rechercherCalc(Integer.toString(numeroClient), true);
    }

    //Pour éviter le doublon
    private Client ajouterCalc(Client c) {
	//Peut être ajouté
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
    
    public Client ajouter(String nom, String numeroTelephone) {
	return ajouterCalc(new Client(Client.countNumerosClients++, nom, numeroTelephone));
    }

    public Client ajouter(String nom, String numeroTelephone, String email) {
	return ajouterCalc(new Client(Client.countNumerosClients++, nom, numeroTelephone, email));
    }
    
    //Les 2 fonctions prenaient trop de place donc fusion
    private LocalDateTime rechercherCalc(LocalDateTime dt, boolean isDate) {
	//Est-ce qu'on connaît déjà la date et l'heure
	int input = -1;
	if (isDate) {
	    System.out.format("[CRENEAUX DISPONIBLES : %s]%n", getJourTexte(dt));
	    //Soit c'est fermé, soit la date donnée n'est pas dans les 7 prochains jours
	    //On compare Date + 1 à Date + 1 car on ne veut pas la date d'aujour
	    if (checkDate(dt.toLocalDate())) {
		System.out.format("%s%n%n", estFerme(dt.toLocalDate()) ? "Pas de créneau le lundi" : "Date non comprise dans les 7 prochains jours");
		return null;
	    } else {
		int j = getDeltaJours(dt.toLocalDate());
		//Affichage des créneaux en heures
		for (int c = 0; c < planning.length; c++) {
		    System.out.format("[%-2s] %s à %s ", c, calculCreneau(c), calculCreneau(c + 1));
		    //Est-ce que le créneau est déjà pris ?
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
			getJourTexte(dt),
			calculCreneau(input), calculCreneau(input + 1)
		);
		return calculCreneau(input).atDate(dt.toLocalDate());
	    }
	} else {
	    System.out.format("[CRENEAUX DISPONIBLES : %s]%n", dt.toLocalTime());
	    //Si avant 8h ou après 17h30 (dernier créneau)
	    if(dt.getHour() < tomorrow.getHour() || calculCreneau(dt.toLocalTime()) > planning.length - 1){
		System.out.format("%s%n%n", "L'établissement est fermé à cette heure");
		return null;
	    }
	    //On boucle directement sur les jours
	    for (int j = 0; j < nombreMaxJours; j++) {
		System.out.format("[%-1s] %s ", j, getJourTexte(dt.plusDays(j)));
		//On vérifie si c'est un lundi et si c'est vide
		System.out.format("%s%n",
		    planning[calculCreneau(dt.toLocalTime())][j] != null ? "(Déjà pris)" :
		    estFerme(tomorrow.toLocalDate().plusDays(j)) ? "(fermé)" : ""
		);
	    }
	    while (input < 0 || input > nombreMaxJours - 1 || planning[calculCreneau(dt.toLocalTime())][input] != null || estFerme(dt.toLocalDate().plusDays(input))) {
		System.out.format("Veuillez choisir votre créneau : %n");
		input = Integer.parseInt(sc.nextLine());
		if (input < 0 || input > nombreMaxJours - 1 || planning[calculCreneau(dt.toLocalTime())][input] != null || estFerme(dt.toLocalDate().plusDays(input))) {
		    System.out.format("%s%n",
			(input < 0 || input > nombreMaxJours - 1) ?
			    "Ce n'est pas un créneau valide" :
			    estFerme(dt.plusDays(input).toLocalDate()) ?
				"L'établissement est fermé" :
				"Le créneau est déjà pris"
		    );
		}
	    }
	    //A partir d'ici l'input est valide
	    System.out.format("%n%s%n", "Créneau validé !");
	    System.out.format("Votre créneau est le %s de %s à %s%n%n",
		    getJourTexte(dt.plusDays(input)),
		    calculCreneau(calculCreneau(dt.toLocalTime())), calculCreneau(calculCreneau(dt.toLocalTime()) + 1)
	    );
	    return calculCreneau(calculCreneau(dt.toLocalTime())).atDate(dt.toLocalDate().plusDays(input));
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
	if(rechercher(rdv.getClient().getNom(), rdv.getClient().getNumeroTelephone()) != null){
	    int j = getDeltaJours(dt.toLocalDate());
	    int c = calculCreneau(dt.toLocalTime());
	    planning[c][j] = rdv;
	    return rdv;
	} else { return null; }
    }
    
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
    
    public Client planifierClient(){
	String nom, numeroTelephone;
	System.out.format("%s%n", "Rentrez votre nom :");
	nom = sc.nextLine();
	System.out.format("%n%s%n", "Rentrez votre numéro de téléphone :");
	numeroTelephone = sc.nextLine();
	//Si le client est déjà dans la liste, on le récupère sinon on le créé
	return rechercher(nom, numeroTelephone) != null ? rechercher(nom, numeroTelephone) : ajouter(nom, numeroTelephone);
    }
    
    public LocalDateTime planifierCreneau(){
	//Pour boucler si l'input n'est pas bon
	int inputInt = -1;
	//Pour récupérer la date ou l'heure
	String inputStr;
	//On affecte à la valeur d'aujourd'hui pour avoir la LocalDate et le LocalTime
	//Elle n'est pas comprise dans checkDate() ni dans checkTime() donc on va boucler là-dessus
	LocalDateTime dt = tomorrow.minusDays(1).minusMinutes(1);
	//-------------------------
	//Booléen pour sélectionner vers quelle méthode on va s'orienter
	while(inputInt < 0 || inputInt > 1){
	    System.out.format("%n%s%n%s%n%s%n", "Comment voulez vous prendre votre rendez-vous ?", "[0] Date", "[1] Heure");
	    inputInt = Integer.parseInt(sc.nextLine());
	    if(inputInt < 0 || inputInt > 1) System.out.format("%s %s%n", inputInt, "n'est pas une valeur valide");
	}
	//------------------------------------
	//Si date (= 0)
	if(inputInt == 0){
	    while(checkDate(dt.toLocalDate())){
		//Pas de vérification du type de l'input trop chiant à faire (ex : 2025-15-156)
		System.out.format("%n%s%n%s%n", "Veuillez entrer une date souhaitée :", "(dans le format suivant : YYYY-MM-DD et sur les 7 jours qui suivent)");
		inputStr = sc.nextLine();
		dt = dt.withYear(Integer.parseInt(inputStr.split("-")[0])).withMonth(Integer.parseInt(inputStr.split("-")[1])).withDayOfMonth(Integer.parseInt(inputStr.split("-")[2]));
		if(checkDate(dt.toLocalDate())) System.out.format("%n%s %s%n", dt.toLocalDate(), "%s n'est pas une date valide");
	    }
	    System.out.format("%n");
	    dt = rechercher(dt.toLocalDate());
	} else {
	    System.out.println(checkTime(dt.toLocalTime()));
	    while(checkTime(dt.toLocalTime())){
		//Pas de vérification du type de l'input trop chiant à faire (ex : 23:61)
		System.out.format("%n%s%n%s%n", "Veuillez entrer une heure souhaitée :", "(dans le format suivant : hh:mm)");
		inputStr = sc.nextLine();
		dt = dt.withHour(Integer.parseInt(inputStr.split(":")[0])).withMinute(Integer.parseInt(inputStr.split(":")[1]));
		if(checkTime(dt.toLocalTime())) System.out.format("%n%s %s%n", dt.toLocalTime(), "n'est pas une heure valide");
	    }
	    System.out.format("%n");
	    dt = rechercher(dt.toLocalTime());
	}
	return dt;
    }
    
    public Prestation.CategorieVehicule planifierCategorieVehicule(){
	int input;
	Prestation.CategorieVehicule categorieVehicule = null;
	while(categorieVehicule == null){
	    System.out.format("%n%s%n%s%n%s%n%s%n", "Veuillez choir votre type de véhicule :", "[0] Citadines", "[1] Berlines", "[2] Monospaces et 4x4");
	    input = Integer.parseInt(sc.nextLine());
	    categorieVehicule =
		input == 0 ? Prestation.CategorieVehicule.A :
		input == 1 ? Prestation.CategorieVehicule.B : 
		input == 2 ? Prestation.CategorieVehicule.C :
		null;
	    if(categorieVehicule == null){
		System.out.format("%s%n", "Votre choix de type de véhicule n'est pas valide");
	    }
	}
	return categorieVehicule;
    }
    
    //A finir 
    public void planifier(){
	Client c = planifierClient();
	//Si le nombre de clients max est atteint et que le client n'est pas déjà dans la liste
	if(c == null){
	    System.out.format("%n%s%n%s%n%n", "Le nombre maximum de clients pour cet établissement a été atteint", "et le client n'est pas sur la liste, donc cela ne va pas être possible");
	} else {
	    LocalDateTime dt = planifierCreneau();
	    Prestation.CategorieVehicule categorieVehicule = planifierCategorieVehicule();
	    RendezVous rdv;
	    int input = -1;
	    while(input < 0 || input > 2){
		System.out.format("%n%s%n%s%n%s%n%s%n", "Veuillez choisir votre type de prestation :", "[0] Prestation Express", "[1] Prestation sur véhicule sale", "[2] Prestation sur véhicule très sale");
		input = Integer.parseInt(sc.nextLine());
		if(input < 0 || input > 2) System.out.format("%s%n", "Votre choix de type de prestation n'est pas valide");
	    }
	    switch(input){
		case 0, 1 -> {
		    if(input == 1){
			//PrestationSale
			rdv = ajouter(c, dt, categorieVehicule);
		    } else {
			input = -1;
			while(input < 0 || input > 1){
			    System.out.format("%n%s%n%s%n%s%n", "Est-ce que votre véhicule à besoin d'être nettoyé ?", "[0] Oui", "[1] Non");
			    input = Integer.parseInt(sc.nextLine());
			    if(input < 0 || input > 1) System.out.format("%n%s%n", "Votre choix n'est pas valide");
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
			input = Integer.parseInt(sc.nextLine());
			if(input < 0 || input > PrestationTresSale.TypeSalissure.values().length - 1) System.out.format("%n%s%n", "Votre choix n'est pas valide");
		    }
		    rdv = ajouter(c, dt, categorieVehicule, PrestationTresSale.TypeSalissure.values()[input]);
		}
	    }
	}
	System.out.format("%n");
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
	    str += String.format(" | %-20s", getJourTexte(tomorrow.plusDays(jourTexte)));
	}
	//On boucle sur tout les créneaux
	for (int c = 0; c < nombreMaxCreneaux; c++) {
	    //Pour indiquer le créneau sur chaque ligne
	    str += String.format("%n%-5s-%-5s", calculCreneau(c), calculCreneau(c + 1));
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
    
    //-----------------------------------------------
    //------------PARTIE 2 : [2] afficher()----------
    public String afficher(DayOfWeek d){
	String str = "";
	int index = 0;
	for(int j = 0; j < nombreMaxJours; j++){
	    if(tomorrow.plusDays(j).getDayOfWeek() == d){ index = j; }
	}
	str += String.format("Voici le planning du %s%n", getJourTexte(tomorrow.plusDays(index)));
	if(estFerme(tomorrow.toLocalDate().plusDays(index))){
	    str += String.format("%s%n%n", "L'établiseement est fermé le lundi");
	    return str;
	}
	for(int c = 0; c < nombreMaxCreneaux; c++){
	    str += String.format("%-5s-%-5s ", calculCreneau(c), calculCreneau(c + 1));
	    str += String.format("%s%n", (planning[c][index] != null) ?
		"(" + planning[c][index].getClient().getNom() + ")" : "");
	}
	str += String.format("%n");
	return str;
    }
    
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
    
    //A verifier
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
    
    //Retourne le LocaTime correspondant grâce à l'index
    public LocalTime calculCreneau(int c) {
	return tomorrow.toLocalTime().plusMinutes(c * 30);
    }

    public int calculCreneau(LocalTime t) {
	int h = t.getHour() - tomorrow.getHour();
	int m = t.getMinute() < 30 ?
	    t.getMinute() == 0 ? 0 : 1 :
	    t.getMinute() == 30 ? 1 : 2;
	return h * 2 + m;
    }
    
    //Retourner true si c'est un lundi
    public boolean estFerme(LocalDate date) {
	return date.getDayOfWeek() == DayOfWeek.MONDAY;
    }
    
    public boolean checkDate(LocalDate date){
	return estFerme(date) || //Si c'est un lundi
	    date.isBefore(tomorrow.toLocalDate()) || //Si aujourd'hui ou plus tôt
	    date.isAfter(tomorrow.toLocalDate().plusDays(nombreMaxJours)); //Si 8 jours après ou plus tard
    }
    
    public boolean checkTime(LocalTime time){
	return
	    time.getHour() < tomorrow.getHour() || //Si <10h
	    calculCreneau(time) > nombreMaxCreneaux; //Si >18h
    }
    
    public LocalDateTime getDateTimeRendezVous(RendezVous rdv){
	for (int c = 0; c < nombreMaxCreneaux; c++) {
	    for (int j = 0; j < nombreMaxJours; j++) {
		if(planning[c][j] == rdv) return tomorrow.plusDays(j).plusMinutes(c * 30);
	    }
	}
	return null;
    }
    
    public int getDeltaJours(LocalDate date){
	return (int) DAYS.between(tomorrow.toLocalDate(), date);
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
    
    public String getJourTexte(LocalDateTime date){
	return date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.FRANCE).toUpperCase();
    }

    //Getter + Setter
    public String getNom() { return nom; }

    //Pas de set pour limiteCLients car un établissement à un nombre fixe de clients possible
    public int getLimiteClients() { return limiteClients; }

    //Pour éviter de créer une variable + incrément, cette fonction calcule directement le nombre de clients 
    public int getNombreClients() {
	int n = 0;
	for (Client c : clients) {
	    if (c != null) n++;
	}
	return n;
    }
    
    public int getNombreRDV() {
	int n = 0;
	for (int c = 0; c < nombreMaxCreneaux; c++) {
	    for (int j = 0; j < nombreMaxJours; j++) {
		if(planning[c][j] != null) n++;
	    }
	}
	return n;
    }
    
    
    
    public void versFichierClients() throws IOException{
	FileWriter fw = new FileWriter("clients.txt", false);
	for (Client c : clients) {
	    if (c != null) fw.write(c.versFichier());
	}
	fw.close();
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
	} catch (FileNotFoundException ex) {
	    System.getLogger(Etablissement.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
	} catch (IOException ex) {
	    System.getLogger(Etablissement.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
	}
    }
    
    public void versFichierRDV() throws IOException{
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
    }
    
    //On suppose que le fichier respecte la limite de clients de l'établissement
    public void depuisFichierRDV(){
	BufferedReader br;
	try {
	    br = new BufferedReader(new FileReader("rdv.txt"));
	    String s, separator = " : ";
	    LocalDateTime dt;
	    Client c;
	    
	    for(int i = 0; i < getNombreRDV(); i++){
		s = br.readLine();
		
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
	} catch (FileNotFoundException ex) {
	    System.getLogger(Etablissement.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
	} catch (IOException ex) {
	    System.getLogger(Etablissement.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
	}
    }

    //Si l'établissement se fait racheter
    public void setNom(String nom) { this.nom = nom; }
}
