
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

    //Vu que nous devons prévoir les 7 jours suivants, autant mettre sur static à 8h
    private static final LocalDateTime now = LocalDateTime.now().withHour(8).withMinute(0).truncatedTo(MINUTES);

    private String nom;
    private final int limiteClients;
    private final int nombreMaxCreneaux = 20;
    private final int nombreMaxJours = 7;
    private Client[] clients;
    //On suppose 9 créneaux (8h à 18h avec pause d'une heure) tout les jours
    private RendezVous[][] planning;

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
		if (client.getNom().equalsIgnoreCase(nom) && client.getNom().equalsIgnoreCase(numeroTelephone)) {
		    return client;
		}
	    }
	}
	return null;
    }

    //Pour éviter le doublon
    private Client ajouterCalcul(Client client) {
	//Au moins 1 client
	if (getNombreClients() > 0) {
	    //On assigne à la dernière position
	    clients[getNombreClients()] = client;
	    //On swap i-1 et i si pas dans l'ordre
	    for (int i = getNombreClients() - 1; i > 0 && clients[i - 1].placerApres(clients[i]); i--) {
		Client temp = clients[i];
		clients[i] = clients[i - 1];
		clients[i - 1] = temp;
	    }
	} else {
	    //Si il n'y a pas de client
	    clients[0] = client;
	}
	return client;
    }

    public Client ajouter(String nom, String numeroTelephone) {
	if (getNombreClients() >= limiteClients) {
	    return null;
	}
	return ajouterCalcul(new Client(getNombreClients() + 1, nom, numeroTelephone));
    }

    public Client ajouter(String nom, String numeroTelephone, String email) {
	if (getNombreClients() >= limiteClients) {
	    return null;
	}
	return ajouterCalcul(new Client(getNombreClients() + 1, nom, numeroTelephone, email));
    }

    //Retourner true si c'est un lundi
    public boolean estFerme(LocalDate date) {
	return date.getDayOfWeek() == DayOfWeek.MONDAY;
    }

    //Les 2 fonctions prenaient trop de place donc fusion
    public LocalDateTime rechercherCalcul(LocalDateTime dt, boolean isDate, boolean besoinDemander) {
	//Est-ce qu'on connaît déjà la date et l'heure
	if(besoinDemander){
	    Scanner sc = new Scanner(System.in);
	    int input = -1;
	    if (isDate) {
		System.out.format("[CRENEAUX DISPONIBLES : %s]%n", dt.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.FRANCE).toUpperCase());
		//Soit c'est fermé, soit la date donnée n'est pas dans les 7 prochains jours
		//On compare Date + 1 à Date + 1 car on ne veut pas la date d'aujour
		if (estFerme(dt.toLocalDate()) || dt.toLocalDate().plusDays(1).isBefore(now.toLocalDate().plusDays(1)) || dt.toLocalDate().isAfter(now.toLocalDate().plusDays(7))) {
		    System.out.format(estFerme(dt.toLocalDate()) ? "Pas de créneau le lundi%n" : "Date non comprise dans les 7 prochains jours%n");
		    return null;
		} else {
		    int j = (int) DAYS.between(now, dt);
		    //Affichage des créneaux en heures
		    for (int c = 0; c < planning.length; c++) {
			System.out.format("[%-2s] %s à %s ", c, calculCreneau(c), calculCreneau(c + 1));
			//Est-ce que le créneau est déjà pris ?
			System.out.format(" %s%n", planning[c][j] != null ? "(Créneau déjà pris)" : "");
		    }
		    //Soit la valeur est hors limite (première fois à -1) soit le créneau est déjà pris
		    while (input < 0 || input > planning.length - 1 || planning[input][j] != null) {
			System.out.format("Veuillez choisir votre créneau : %n");
			input = sc.nextInt();
			//On vérifie comme tout à l'heure et on affiche le message correspondant
			//On vérifie la limite sinon crash avec != null
			if (input < 0 || input > planning.length - 1 || planning[input][j] != null) {
			    System.out.format("%s%n", input < 0 || input > planning.length - 1 ? "Ce n'est pas dans la sélection" : "Le créneau est déjà pris");
			}
		    }
		    //A partir d'ici l'input est valide
		    System.out.format("Créneau validé !%n");
		    System.out.format("Votre créneau est le %s de %s à %s",
			    dt.toLocalDate().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.FRANCE).toUpperCase(),
			    calculCreneau(input), calculCreneau(input + 1)
		    );
		    return calculCreneau(input).atDate(dt.toLocalDate());
		}
	    } else {
		System.out.format("[CRENEAUX DISPONIBLES : %s]%n", dt.toLocalTime().truncatedTo(MINUTES));
		//Si avant 8h ou après 17h30 (dernier créneau)
		if(dt.getHour() < now.getHour() || calculCreneau(dt.toLocalTime()) > planning.length - 1){
		    System.out.format("%s%n", "L'établissement est fermé à cette heure");
		    return null;
		}
		//On boucle directement sur les jours
		for (int j = 0; j < planning[0].length; j++) {
		    System.out.format("[%-1s] %s ", j, now.plusDays(j + 1).getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.FRANCE).toUpperCase());
		    //On vérifie si c'est un lundi et si c'est c'est vide
		    if (planning[calculCreneau(dt.toLocalTime())][j] != null || estFerme(now.toLocalDate().plusDays(j + 1))) {
			System.out.format("%s%n", estFerme(now.toLocalDate().plusDays(j + 1)) ? "(fermé)" : "(Déjà pris)");
		    } else {
			System.out.format("%n");
		    }
		}
		while (input < 0 || input > planning[0].length - 1 || planning[calculCreneau(dt.toLocalTime())][input] != null || dt.plusDays(input + 1).getDayOfWeek() == DayOfWeek.MONDAY) {
		    System.out.format("Veuillez choisir votre créneau : %n");
		    input = sc.nextInt();
		    if (input < 0 || input > planning[0].length - 1 || planning[calculCreneau(dt.toLocalTime())][input] != null || dt.plusDays(input + 1).getDayOfWeek() == DayOfWeek.MONDAY) {
			if (input < 0 || input > planning[0].length - 1) {
			    System.out.format("%s%n", "Ce n'est pas un créneau valide");
			} else {
			    System.out.format("%s%n", dt.plusDays(input + 1).getDayOfWeek() == DayOfWeek.MONDAY ? "L'établissement est fermé" : "Le créneau est déjà pris");
			}
		    }
		}
		//A partir d'ici l'input est valide
		System.out.format("Créneau validé !%n");
		System.out.format("Votre créneau est le %s de %s à %s",
			dt.plusDays(input + 1).toLocalDate().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.FRANCE).toUpperCase(),
			dt.toLocalTime(), calculCreneau(calculCreneau(dt.toLocalTime()) + 1)
		);
		return dt.plusDays(input + 1);
	    }
	} else {
	    //On initialise les valeurs en index qu'on va chercher dans le planning
	    int c = calculCreneau(dt.toLocalTime());
	    int j = (int) DAYS.between(now, dt);
	    System.out.format("%s%n", dt.toString());
	    //Si valeur créneau invalide, si lundi, si déjà pris
	    if(c < 0 || j < 0 || dt.getDayOfWeek() == DayOfWeek.MONDAY || planning[c][j] != null){
		return null;
	    }
	    return dt;
	}
    }

    public LocalDateTime rechercher(LocalDateTime date) {
	//Ici le true ne sert pas, c'est le false qui va bypass toute la validation
	//Le premier booléen sert pour le cas où on a besoin de demander 
	return rechercherCalcul(date, true, false);
    }
    
    public LocalDateTime rechercher(LocalDate date) {
	//On met une heure qui ne servira pas
	//Pour qu'il soit en LocalDateTime
	return rechercherCalcul(date.atTime(8, 0), true, true);
    }

    public LocalDateTime rechercher(LocalTime time) {
	//On met une date qui ne servira pas
	//Pour qu'il soit en LocalDateTime
	return rechercherCalcul(time.atDate(now.toLocalDate()), false, true);
    }

//    --------------------------------
//    
//    A remplir 4c
    //PrestationExpress
    public RendezVous ajouter(Client c, LocalDateTime dt, Prestation.CategorieVehicule categorieVehicule, boolean besoinNettoyage) {
	Client client = rechercher(c.getNom(), c.getNumeroTelephone());
	PrestationExpress prestation = new PrestationExpress(categorieVehicule, besoinNettoyage);
	RendezVous rdv;
	if(client == null){
	    ajouter(c.getNom(), c.getNumeroTelephone());
	}
	if(rechercher(dt) == null){
	    System.out.format("%s%n", "Créneau non disponible");
	    rdv = null;
	    return rdv;
	} else {
	    rdv = new RendezVous(c, prestation, 100);
	    
	    return rdv;
	}
    }
    
    //PrestationSale
    public RendezVous ajouter(Client c, LocalDateTime dt, Prestation.CategorieVehicule categorieVehicule) {
	return null;
    }
    
    //PrestatioonTresSale
    public RendezVous ajouter(Client c, LocalDateTime dt, Prestation.CategorieVehicule categorieVehicule, PrestationTresSale.TypeSalissure typeSalissure) {
	return null;
    }

//    
//    --------------------------------
    public LocalTime calculCreneau(int c) {
	return now.toLocalTime().plusMinutes(c * 30);
    }

    public int calculCreneau(LocalTime t) {
	int h = t.getHour() - now.getHour();
	int m;
	if (t.getMinute() < 30) {
	    m = t.getMinute() == 0 ? 0 : 1;
	} else {
	    m = t.getMinute() == 30 ? 1 : 2;
	}
	return h * 2 + m;
    }

    //On affiche la liste des clients
    public String printClients() {
	String str = "";
	//On affiche tout les clients
	str += String.format("%nListe des clients : ");
	//Liste vide
	if (getNombreClients() == 0) {
	    str += String.format("vide%n");
	} else {
	    for (int i = 0; i < getLimiteClients(); i++) {
		//Ignore les null
		if (clients[i] != null) {
		    str += String.format("%n- %s (%s, %s", clients[i].getNom(), clients[i].getNumeroClient(), clients[i].getNumeroTelephone());
		    //Si le client a un email
		    if (clients[i].getEmail() != null) {
			str += String.format(", %s", clients[i].getEmail());
		    }
		    str += String.format(")");
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
	    str += String.format(" | %-20s", now.plusDays(j + 1).getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.FRANCE));
	}

	//On boucle sur tout les créneaux
	for (int c = 0; c < planning.length; c++) {
	    //Pour indiquer le créneau sur chaque ligne
	    str += String.format("%n%-5s-%-5s", calculCreneau(c), calculCreneau(c + 1));
	    for (int j = 0; j < planning[c].length; j++) {
		if (planning[c][j] != null) {
		    str += String.format(" | %-20s", planning[c][j].getClient().getNom());
		} else {
		    if (!estFerme(now.toLocalDate().plusDays(j + 1))) {
			str += String.format(" | %-20s", "");
		    } else {
			str += String.format(" | %-20s", "-".repeat(20));
		    }
		}

	    }
	}
	return str;
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
    public String getNom() {
	return nom;
    }

    //Pas de set pour limiteCLients car un établissement à un nombre fixe de clients possible
    public int getLimiteClients() {
	return limiteClients;
    }

    //Pas de set pour clients car géré par d'autres méthodes
    public Client[] getClients() {
	return clients;
    }

    public int getNombreClients() {
	int nombre = 0;
	for (Client client : clients) {
	    if (client != null) {
		nombre += 1;
	    }
	}
	return nombre;
    }

    //Pareil pour planning
    public RendezVous[][] getPlanning() {
	return planning;
    }

    //Si l'établissement se fait racheter
    public void setNom(String nom) {
	this.nom = nom;
    }

}
