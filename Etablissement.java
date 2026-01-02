
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
 *
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

    public Client rechercher(String nom, String numeroTelephone) {
	if (getNombreClients() != 0) {
	    for (Client client : clients) {
		if (client.getNom().equalsIgnoreCase(nom) && client.getNumeroTelephone().equalsIgnoreCase(numeroTelephone)) {
		    return client;
		}
	    }
	}
	return null;
    }

    //Pour éviter le doublon
    private Client ajouterClientCalcul(Client c) {
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
	return ajouterClientCalcul(new Client(getNombreClients() + 1, nom, numeroTelephone));
    }

    public Client ajouter(String nom, String numeroTelephone, String email) {
	return ajouterClientCalcul(new Client(getNombreClients() + 1, nom, numeroTelephone, email));
    }
    
    //Les 2 fonctions prenaient trop de place donc fusion
    private LocalDateTime rechercherLocalDateTimeCalcul(LocalDateTime dt, boolean isDate) {
	//Est-ce qu'on connaît déjà la date et l'heure
	int input = -1;
	if (isDate) {
	    System.out.format("[CRENEAUX DISPONIBLES : %s]%n", dt.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.FRANCE).toUpperCase());
	    //Soit c'est fermé, soit la date donnée n'est pas dans les 7 prochains jours
	    //On compare Date + 1 à Date + 1 car on ne veut pas la date d'aujour
	    if (checkDate(dt.toLocalDate())) {
		System.out.format(estFerme(dt.toLocalDate()) ? "Pas de créneau le lundi%n" : "Date non comprise dans les 7 prochains jours%n");
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
		    System.out.format("Veuillez choisir votre créneau : %n");
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
		System.out.format("Créneau validé !%n");
		System.out.format("Votre créneau est le %s de %s à %s%n",
			dt.toLocalDate().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.FRANCE).toUpperCase(),
			calculCreneau(input), calculCreneau(input + 1)
		);
		return calculCreneau(input).atDate(dt.toLocalDate());
	    }
	} else {
	    System.out.format("[CRENEAUX DISPONIBLES : %s]%n", dt.toLocalTime().truncatedTo(MINUTES));
	    //Si avant 8h ou après 17h30 (dernier créneau)
	    if(dt.getHour() < tomorrow.getHour() || calculCreneau(dt.toLocalTime()) > planning.length - 1){
		System.out.format("%s%n", "L'établissement est fermé à cette heure");
		return null;
	    }
	    //On boucle directement sur les jours
	    for (int j = 0; j < nombreMaxJours; j++) {
		System.out.format("[%-1s] %s ", j, tomorrow.plusDays(j).getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.FRANCE).toUpperCase());
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
			    dt.plusDays(input).getDayOfWeek() == DayOfWeek.MONDAY ?
				"L'établissement est fermé" :
				"Le créneau est déjà pris"
		    );
		}
	    }
	    //A partir d'ici l'input est valide
	    System.out.format("Créneau validé !%n");
	    System.out.format("Votre créneau est le %s de %s à %s%n",
		    dt.plusDays(input).toLocalDate().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.FRANCE).toUpperCase(),
		    calculCreneau(calculCreneau(dt.toLocalTime().truncatedTo(MINUTES))), calculCreneau(calculCreneau(dt.toLocalTime()) + 1)
	    );
	    System.out.format("%s%n", dt.plusDays(input));
	    return calculCreneau(calculCreneau(dt.toLocalTime().truncatedTo(MINUTES))).atDate(dt.toLocalDate().plusDays(input));
	}
    }
    
    public LocalDateTime rechercher(LocalDate date) {
	//On met une heure qui ne servira pas
	//Pour qu'il soit en LocalDateTime
	return rechercherLocalDateTimeCalcul(date.atTime(tomorrow.getHour(), 0), true);
    }

    public LocalDateTime rechercher(LocalTime time) {
	//On met une date qui ne servira pas
	//Pour qu'il soit en LocalDateTime
	return rechercherLocalDateTimeCalcul(time.atDate(tomorrow.toLocalDate()), false);
    }

//    --------------------------------
//    
//    A remplir 4c
//  
    public RendezVous ajouter(Client c, LocalDateTime dt, Prestation.CategorieVehicule categorieVehicule, boolean besoinNettoyage){
	return null;
    }
    
    public RendezVous ajouter(Client c, LocalDateTime dt, Prestation.CategorieVehicule categorieVehicule){
	return null;
    }
    
    public RendezVous ajouter(Client c, LocalDateTime dt, Prestation.CategorieVehicule categorieVehicule, PrestationTresSale.TypeSalissure typeSalissure){
	return null;
    }
//
//    
//    --------------------------------
    
    public Client planifierClient(){
	String nom, numeroTelephone;
	System.out.format("Rentrez votre nom : %n");
	nom = sc.nextLine();
	System.out.format("Rentrez votre numéro de téléphone : %n");
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
	    System.out.format("Comment voulez vous prendre votre rendez-vous ? %n[0] Date %n[1] Heure %n");
	    inputInt = Integer.parseInt(sc.nextLine());
	    if(inputInt < 0 || inputInt > 1){
		System.out.format("%s n'est pas une valeur valide%n", inputInt);
	    }
	}
	//------------------------------------
	//Si date (= 0)
	if(inputInt == 0){
	    while(checkDate(dt.toLocalDate())){
		//Pas de vérification du type de l'input trop chiant à faire (ex : 2025-15-156)
		System.out.format("Veuillez entrer une date souhaitée : %n(dans le format suivant : YYYY-MM-DD et sur les 7 jours qui suivent) %n");
		inputStr = sc.nextLine();
		dt = dt.withYear(Integer.parseInt(inputStr.split("-")[0])).withMonth(Integer.parseInt(inputStr.split("-")[1])).withDayOfMonth(Integer.parseInt(inputStr.split("-")[2]));
		if(checkDate(dt.toLocalDate())){
		    System.out.format("%s n'est pas une date valide%n", dt.toLocalDate());
		}
	    }
	    dt = rechercher(dt.toLocalDate());
	} else {
	    System.out.println(checkTime(dt.toLocalTime()));
	    while(checkTime(dt.toLocalTime())){
		//Pas de vérification du type de l'input trop chiant à faire (ex : 23:61)
		System.out.format("Veuillez entrer une heure souhaitée : %n(dans le format suivant : hh:mm) %n");
		inputStr = sc.nextLine();
		dt = dt.withHour(Integer.parseInt(inputStr.split(":")[0])).withMinute(Integer.parseInt(inputStr.split(":")[1]));
		if(checkTime(dt.toLocalTime())){
		    System.out.format("%s n'est pas une heure valide%n", dt.toLocalTime().truncatedTo(MINUTES));
		}
	    }
	    dt = rechercher(dt.toLocalTime());
	    
	}
	return dt;
    }
    
    public Prestation.CategorieVehicule planifierCategorieVehicule(){
	int input;
	Prestation.CategorieVehicule categorieVehicule = null;
	while(categorieVehicule == null){
	    System.out.format("Veuillez choir votre type de véhicule :%n[0] Citadines%n[1] Berlines%n[2] Monospaces et 4x4%n");
	    input = Integer.parseInt(sc.nextLine());
	    categorieVehicule =
		input == 0 ? Prestation.CategorieVehicule.A :
		input == 1 ? Prestation.CategorieVehicule.B : 
		input == 2 ? Prestation.CategorieVehicule.C :
		null;
	    if(categorieVehicule == null){
		System.out.format("Votre choix de type de véhicule n'est pas valide%n");
	    }
	}
	return categorieVehicule;
    }
    
    public RendezVous planifierRendezVous(Client c, LocalDateTime dt, Prestation.CategorieVehicule categorieVehicule){
	int input = -1;
	while(input < 0 || input > 2){
	    System.out.format("Veuillez choir votre type de prestation :%n[0] Prestation Express%n[1] Prestation sur véhicule sale%n[2] Prestation sur véhicule très sale%n");
	    input = Integer.parseInt(sc.nextLine());
	    if(input < 0 || input > 2){
		System.out.format("Votre choix de type de prestation n'est pas valide%n");
	    }
	}
	switch(input){
	    case 0, 1 -> {
		if(input == 1){
		    //PrestationSale
		    return ajouter(c, dt, categorieVehicule);
		} else {
		    input = -1;
		    while(input < 0 || input > 1){
			System.out.format("Est-ce que votre véhicule à besoin d'être nettoyé ?%n[0] Oui%n[1] Non%n");
			input = Integer.parseInt(sc.nextLine());
			if(input < 0 || input > 1){
			    System.out.format("Votre choix n'est pas valide%n");
			}
		    }
		    //PrestationExpress
		    return ajouter(c, dt, categorieVehicule, input == 0);
		}
	    }
	    //PrestationTresSale
	    default -> {
		System.out.format("Quel est votre problème ?%n");
		for(int i = 0; i < PrestationTresSale.TypeSalissure.values().length; i++){
		    System.out.format("[%s] %s%n", i, PrestationTresSale.TypeSalissure.values()[i]);
		}
		
	    }
	}
	return null;
    }
    
    //A finir 
    public void planifier(){
	Client c = planifierClient();
	LocalDateTime dt = planifierCreneau();
	Prestation.CategorieVehicule categorieVehicule = planifierCategorieVehicule();
	
    }

    //On affiche la liste des clients
    public String printClients() {
	String str = "";
	//On affiche tout les clients
	str += String.format("%nListe des clients :%n");
	//Liste vide
	if (getNombreClients() == 0) {
	    str += String.format("vide%n");
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
	str += String.format("%n%-11s", "");
	//On affiche les jours
	for (int j = 0; j < planning[0].length; j++) {
	    str += String.format(" | %-20s", tomorrow.plusDays(j).getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.FRANCE));
	}
	//On boucle sur tout les créneaux
	for (int c = 0; c < planning.length; c++) {
	    //Pour indiquer le créneau sur chaque ligne
	    str += String.format("%n%-5s-%-5s", calculCreneau(c), calculCreneau(c + 1));
	    for (int j = 0; j < planning[c].length; j++) {
		str += (planning[c][j] != null) ?
		    String.format(" | %-20s", planning[c][j].getClient().getNom()) :
		    String.format(" | %-20s", (estFerme(tomorrow.toLocalDate().plusDays(j))) ?
			"-".repeat(20) : ""
		);
	    }
	}
	return str;
    }
    
    //-----------------------------------------------
    //------------PARTIE 2 : [2] afficher()----------
    //A verifier
    public String afficher(DayOfWeek d){
	String str = "";
	int index = 0;
	for(int j = 0; j < planning[0].length; j++){
	    if(tomorrow.plusDays(j).getDayOfWeek() == d){ index = j; }
	}
	str += String.format("Voici le planning du %s%n", tomorrow.plusDays(index).getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.FRANCE));
	if(estFerme(tomorrow.toLocalDate().plusDays(index))){
	    str += "L'établiseement est fermé le lundi";
	    return str;
	}
	for(int c = 0; c < planning.length; c++){
	    str += String.format("%n%-5s-%-5s ", calculCreneau(c), calculCreneau(c + 1));
	    str += (planning[c][index] != null) ?
		String.format("%s%n", "(" + planning[c][index].getClient().getNom()) + ")" : "";
	}
	return str;
    }
    
    //A verifier
    String afficher(String nom, String numeroTelephone){
	String str = "";
	int value = 0;
	str += String.format("Voici les clients qui correspondent au nom ou au numéro de téléphone donnés:%n");
	str += String.format("Pour le nom (%s) :%n", nom);
	for(Client c : clients){
	    if(c.getNom().equalsIgnoreCase(nom)) value++;
	    str += (c.getNom().equalsIgnoreCase(nom)) ? String.format("- %s (%s, %s%s)%n", c.getNom(), c.getNumeroClient(), c.getNumeroTelephone(), c.getEmail() != null ? ", " + c.getEmail() : "") : "";
	}
	str += value == 0 ? String.format("Aucun client avec ce nom%n") : "";
	value = 0;
	str += String.format("%nPour le numéro de téléphone (%s) :%n", numeroTelephone);
	for(Client c : clients){
	    if(c.getNumeroTelephone().equalsIgnoreCase(numeroTelephone)) value++;
	    str += (c.getNumeroTelephone().equalsIgnoreCase(numeroTelephone)) ? String.format("- %s (%s, %s%s)%n", c.getNom(), c.getNumeroClient(), c.getNumeroTelephone(), c.getEmail() != null ? ", " + c.getEmail() : "") : "";
	}
	str += value == 0 ? String.format("Aucun client avec ce numéro de téléphone%n") : "";
	return str;
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
    
    public int getDeltaJours(LocalDate date){
	return (int) DAYS.between(tomorrow.toLocalDate(), date);
    }

    //Divison du print car trop long
    @Override
    public String toString() {
	String str = "";
	str += String.format("%nNom de l'établissement : %s%n", nom);
	str += printClients();
	str += printPlanning();
	return str;
    }

    //Getter + Setter
    public String getNom() { return nom; }

    //Pas de set pour limiteCLients car un établissement à un nombre fixe de clients possible
    public int getLimiteClients() { return limiteClients; }

    //Pas de set pour clients car géré par d'autres méthodes
    public Client[] getClients() { return clients; }

    public int getNombreClients() {
	int n = 0;
	for (Client c : clients) {
	    if (c != null) n++;
	}
	return n;
    }

    //Pareil pour planning
    public RendezVous[][] getPlanning() { return planning; }

    //Si l'établissement se fait racheter
    public void setNom(String nom) { this.nom = nom; }
}
