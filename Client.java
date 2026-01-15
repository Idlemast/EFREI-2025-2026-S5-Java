/**
 *
 *  GRP : William WAN & Hsiao-Wen-Paul LO
 */
public class Client {
    //On défini le nombre total de clients à 0
    //et ne sera pas incrémenté dans Client mais dans toutes les autres classes
    //qui l'utilisent car on donne le numeroClient lors de l'instanciation
    public static int countNumerosClients = 0;
    private int numeroClient;
    private String nom;
    private String numeroTelephone;
    //Si email non renseigné, valeur par défaut
    private String email = null;
    
    //Pour Client, le numeroClient est spécifié par les classes externes qui instancie Client ici Etablissement
    //Car si Client venait à s'auto-incrémenter, alors le constructeur avec numéro Client de l'énoncé ne sert à rien
    //Et vu que c'est la consigne venait de ajouter() : Client, c'est cohérent
    //Et pour la Partie 2, cela se confirme puisqu'on à les numéros clients qui ne sont pas les mêmes
    public Client(int numeroClient, String nom, String numeroTelephone){
	this.numeroClient = numeroClient;
	this.nom = nom;
	this.numeroTelephone = numeroTelephone;
    }
    
    public Client(int numeroClient, String nom, String numeroTelephone, String email){
	this(numeroClient, nom, numeroTelephone);
	this.email = email;
    }
    
    public boolean placerApres(Client client){
	return this.nom.compareToIgnoreCase(client.getNom()) > 0;
    };
    
    public String versFichier(){
	return String.format(
	    "%s : %s : %s%s%n",
		numeroClient, nom, numeroTelephone,
		//Si l'email est null, on le retourne avec le même affichage, sinon rien
		String.format(email != null ? " : " + email : ""
	    )
	);
    }
    
    @Override
    public String toString(){
	String str = "";
	str += String.format("Numéro du client : %s%n", numeroClient);
	str += String.format("Nom : %s%n", nom);
	str += String.format("Téléphone : %s%n", numeroTelephone);
	//Si on a un email ou non
	str += String.format("Email : %s%n%n", (email != null) ? email : "Non renseigné");
	return str;
    }
    
    //Getter + Setter
    public int getNumeroClient(){ return numeroClient; }
    public String getNom(){ return nom; }
    public String getNumeroTelephone(){ return numeroTelephone; }
    public String getEmail(){ return email; }
    //Ne sert pas pour ce TP
    public void setNumeroTelephone(String numeroTelephone){ this.numeroTelephone = numeroTelephone; }
    //Ne sert pas pour ce TP
    public void setEmail(String email){ this.email = email; }
    
}
