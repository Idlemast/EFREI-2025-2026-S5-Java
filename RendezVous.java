/**
 *
 *  GRP : William WAN & Hsiao-Wen-Paul LO
 */
public class RendezVous {
    //Le client et la prestation ne changeront probablement pas
    private final Client client;
    private final Prestation prestation;
    private double prix;
    
    public RendezVous(Client client, Prestation prestation, double prix){
	this.client = client;
	this.prestation = prestation;
	this.prix = prix;
    }
    
    @Override
    public String toString(){
	String str = "";
	str += String.format("[CLIENT]%n");
	str += client;
	str += String.format("[PRESTATION]%n");
	str += prestation;
	str += String.format("[INFORMATIONS]%n");
	str += String.format("Prix de la prestation : %s€%n", prix);
	str += String.format("%n" + "-".repeat(30) + "%n");
	return str;
    }
    
    //Au format demandé (" : ") pour mettre dans le fichier
    //On prend le versFichier() de la prestation et on y ajoute le prix
    //Le client n'est pas indiqué ici
    public String versFichier(){
	return String.format("%s : %s%n", prestation.versFichier(), prix);
    }
    
    //Getter + Setter
    public Client getClient(){ return client; }
    public Prestation getPrestation(){ return prestation; }
    public double getPrix(){ return prix; }
    public void setPrix(double prix){ this.prix = prix; }
    
}
