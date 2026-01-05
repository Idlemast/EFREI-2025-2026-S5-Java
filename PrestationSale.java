/**
 *
 * @author William
 */
public class PrestationSale extends Prestation {
    
    public PrestationSale(CategorieVehicule categorieVehicule){
	super(categorieVehicule);
    }
    
    //Il faut ajouter le coût du produit, on a pas le choix
    //Contrairement à PrestationExpress
    @Override
    protected double nettoyage(){
	return super.nettoyage() + nettoyageInterieur();
    }
    
    
    @Override
    public String versFichier(){
	return String.format("%s", categorieVehicule.name());
    }
    
    @Override
    public String toString(){
	String str = super.toString();
	str += String.format("%s%n%n", "Besoin de nettoyage");
	return str;
    }
}
