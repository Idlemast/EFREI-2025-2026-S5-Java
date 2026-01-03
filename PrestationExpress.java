/**
 *
 * @author William
 */
public class PrestationExpress extends Prestation {
    private boolean besoinNettoyage;
    
    public PrestationExpress(CategorieVehicule categorieVehicule, boolean besoinNettoyage){
	super(categorieVehicule);
	this.besoinNettoyage = besoinNettoyage;
    }
    
    @Override
    protected double nettoyage(){
	return besoinNettoyage ? super.nettoyage() + nettoyageInterieur() : super.nettoyage();
    }
    
    @Override
    public String toString(){
	String str = super.toString();
	str += String.format("%s%n%n", besoinNettoyage ? "Besoin de nettoyage" : "Pas besoin de nettoyage");
	return str;
    }
    
    //Getter + Setter
    public boolean getBesoinNettoyage(){ return besoinNettoyage; }
    public void setBesoinNettoyage(boolean besoinNettoyage){ this.besoinNettoyage = besoinNettoyage; }
    
}
