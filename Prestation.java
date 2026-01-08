/**
 *
 *  GRP : William WAN & Hsiao-Wen-Paul LO
 */
public abstract class Prestation {
    //On créé une sous-classe CategorieVehicule qui
    //agit comme un sélecteur entre différents choix définis
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
    protected CategorieVehicule categorieVehicule;
    
    public Prestation(CategorieVehicule categorieVehicule){
	this.categorieVehicule = categorieVehicule;
    }
    
    //On calcule les prix selon la catégorie du véhicule
    //En protected car on veut que les classes filles puissent aussi accéder 
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
    
    protected double nettoyage() {
        return lavage() + sechage();
    }
    
    protected double nettoyageInterieur() {
        return categorieVehicule == CategorieVehicule.C ? 40 : 30;
    }
    
    //Pour dire que chaque Prestation doit avoir une méthode versFichier()
    public abstract String versFichier();
    
    @Override
    public String toString() {
        return String.format("%s%s%n", "Catégorie du véhicule : ", categorieVehicule);
    }
    
    public CategorieVehicule getCategorieVehicule(){ return categorieVehicule; }
}
