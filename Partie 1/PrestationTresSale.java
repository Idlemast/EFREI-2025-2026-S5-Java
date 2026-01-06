/**
 *
 * @author William
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
    
    //a faire
    @Override
    protected double nettoyage(){
	return super.nettoyage() + nettoyageInterieur();
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
