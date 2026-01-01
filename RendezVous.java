/**
 *
 * @author William
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
	str += String.format("-".repeat(30));
	str += String.format("%n[CLIENT]");
	str += client;
	str += String.format("%n[PRESTATION]");
	str += prestation;
	str += String.format("%n[INFORMATIONS]%n");
	str += String.format("Prix de la prestation : %s$", prix);
	return str;
    }
    
    //Getter + Setter
    public Client getClient(){ return client; }
    public Prestation getPrestation(){ return prestation; }
    public double getPrix(){ return prix; }
    public void setPrix(double prix){ this.prix = prix; }
    
}
