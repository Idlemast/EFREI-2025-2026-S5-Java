
/**
 *
 *  GRP : William WAN & Hsiao-Wen-Paul LO
 */
public class PrestationSale extends Prestation {
    
    public PrestationSale(CategorieVehicule categorieVehicule){
	super(categorieVehicule);
    }
    
    // Prélavage et nettoyage intérieur obligatoires
    //Contrairement à PrestationExpress
    @Override
    protected double nettoyage(){
        return prelavage() + super.nettoyage() + nettoyageInterieur();
    }

    
    
    @Override
    public String versFichier(){
	return String.format("%s", categorieVehicule.name());
    }
    
    @Override
    public String toString(){
	String str = super.toString();
	str += String.format("%s%n%n", "Prestation véhicule sale");
	return str;
    }
}
