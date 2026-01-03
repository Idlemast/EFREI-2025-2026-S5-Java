/**
 *
 * @author William
 */
public abstract class Prestation {
    //On créé une sous-classe CategorieVehicule qui agit comme un sélecteur entre différents choix
    public static enum CategorieVehicule {
        A("Citadines"),
        B("Berlines"),
        C("Monospaces et 4x4");

        private final String label;

        CategorieVehicule(String label) { this.label = label; }

        @Override
        public String toString() { return label; }
    }
    //Et on l'utilise en tant que propriété
    private CategorieVehicule categorieVehicule;
    
    public Prestation(CategorieVehicule categorieVehicule){
	this.categorieVehicule = categorieVehicule;
    }
    
    protected double prelavage(){
	double prix = 5;
        prix = switch (categorieVehicule) {
            case B -> prix * 1.5;
            case C -> prix * 1.75;
	    default -> prix;
        };
	return prix;
    }
    
    protected double lavage(){
	double prix = 20;
        prix = switch (categorieVehicule) {
            case B -> prix * 1.5;
            case C -> prix * 1.75;
	    default -> prix;
        };
	return prix;
    }
    
    protected double sechage(){
	double prix = 10;
        prix = switch (categorieVehicule) {
            case B -> prix * 1.05;
            case C -> prix * 1.1;
	    default -> prix;
        };
	return prix;
    }
    
    protected double nettoyage(){
	return prelavage() + lavage() + sechage();
    }
    
    protected double nettoyageInterieur() {
        return categorieVehicule == CategorieVehicule.C ? 40 : 30;
    }
    
    @Override
    public String toString() {
        return String.format("Catégorie du véhicule : %s%n", categorieVehicule);
    }
    
    public CategorieVehicule getCategorieVehicule(){ return categorieVehicule; }
}
