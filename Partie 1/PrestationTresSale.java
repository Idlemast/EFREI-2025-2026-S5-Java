/**
 *
 *  GRP : William WAN & Hsiao-Wen-Paul LO
 */
public class PrestationTresSale extends Prestation {
    //On créé une sous-classe TypeSalissure qui agit
    //comme un sélecteur entre différents choix
    public static enum TypeSalissure {
	//Sous la forme _numéro car permet de récupérer le numéro après
        _1("Tâches de nourriture"),
        _2("Tâches de boue"),
        _3("Tâches de transpiration"),
	_4("Tâches de graisse");

        private final String label;

        TypeSalissure(String label) { this.label = label; }

        @Override
        public String toString() { return label; }
    }
    //Et on l'utilise en tant que propriété
    private TypeSalissure typeSalissure;
    
    public PrestationTresSale(CategorieVehicule categorieVehicule, TypeSalissure typeSalissure){
	super(categorieVehicule);
	this.typeSalissure = typeSalissure;
    }
    
    private double surcout() {
        switch (typeSalissure) {
            case _1: return 5.0;
            case _2: return 3.0;
            case _3: return 4.0;
            case _4: return 8.0;
            default: return 2.0;
        }
    }
    
    @Override
    protected double prelavage() {
        return super.prelavage() + surcout();
    }
    
    @Override
    protected double lavage() {
        return super.lavage() + surcout();
    }
    
    @Override
    protected double nettoyage(){
	return prelavage() + lavage() + sechage() + nettoyageInterieur();
    }
    
    @Override
    public String versFichier(){
	return String.format("%s : %s", categorieVehicule.name(), typeSalissure.name().substring(1));
    }
    
    @Override
    public String toString(){
	String str = super.toString();
	str += String.format("%s%s%n%n", "Problème : ", typeSalissure);
	return str;
    }
    
    //Getter + Setter
    public TypeSalissure getTypeSalissure(){ return typeSalissure; }
    public void setTypeSalissure(TypeSalissure typeSalissure){ this.typeSalissure = typeSalissure; }
    
}
