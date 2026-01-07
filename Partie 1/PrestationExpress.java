/**
 *
 *  GRP : William WAN & Hsiao-Wen-Paul LO
 */
public class PrestationExpress extends Prestation {
    private boolean besoinNettoyage;
    
    public PrestationExpress(CategorieVehicule categorieVehicule, boolean besoinNettoyage){
	super(categorieVehicule);
	this.besoinNettoyage = besoinNettoyage;
    }
    
    //On override pour si on a besoin d'un nettoyage ou pas
    @Override
    protected double nettoyage(){
	return besoinNettoyage ? super.nettoyage() + nettoyageInterieur() : super.nettoyage();
    }
    
    @Override
    public String versFichier(){
	return String.format("%s : %s", categorieVehicule.name(), besoinNettoyage);
    }
    
    @Override
    public String toString(){
	String str = super.toString();
	str += String.format("%s%n%n", besoinNettoyage ? "Besoin de nettoyage" : "Pas besoin de nettoyage");
	return str;
    }
    
    //Getter + Setter
    public boolean getBesoinNettoyage(){ return besoinNettoyage; }
    
}
