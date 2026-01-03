
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author William
 */
public class Client {
    private int numeroClient;
    private String nom;
    private String numeroTelephone;
    //Si email non renseigné, valeur par défaut
    private String email = null;
    
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
	return this.nom.compareToIgnoreCase(client.getNom()) > 0 ? true : false;
    };
    
    @Override
    public String toString(){
	String str = "";
	str += String.format("Numéro du client : %s%n", numeroClient);
	str += String.format("Nom : %s%n", nom);
	str += String.format("Téléphone : %s%n", numeroTelephone);
	str += String.format("Email : %s%n", (email != null) ? email : "Non renseigné");
	return str;
    }
    
    //Getter + Setter
    public int getNumeroClient(){ return numeroClient; }
    public String getNom(){ return nom; }
    public String getNumeroTelephone(){ return numeroTelephone; }
    public String getEmail(){ return email; }
    public void setNumeroTelephone(String numeroTelephone){ this.numeroTelephone = numeroTelephone; }
    public void setEmail(String email){ this.email = email; }
    
}
