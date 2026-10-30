import java.io.Serializable;

//Cet objet sert à échanger les données entre le client et le serveur.
// Le client envoie les deux nombres et l'opération à effectuer.
// Le serveur fait le calcul et renvoie le résultat ou un message d'erreur.

public class Operation implements Serializable {

    private static final long serialVersionUID = 1L;

    private double operande1;
    private double operande2;
    private char operateur;      // + - * /
    private double resultat;
    private String erreur;       // null si tout s'est bien passé

    public Operation(double operande1, char operateur, double operande2) {
        this.operande1 = operande1;
        this.operateur = operateur;
        this.operande2 = operande2;
    }

    public double getOperande1() { return operande1; }
    public double getOperande2() { return operande2; }
    public char getOperateur()   { return operateur; }
    public double getResultat()  { return resultat; }
    public String getErreur()    { return erreur; }

    public void setResultat(double resultat) { this.resultat = resultat; }
    public void setErreur(String erreur)     { this.erreur = erreur; }

    @Override
    public String toString() {
        if (erreur != null) {
            return operande1 + " " + operateur + " " + operande2 + " -> ERREUR : " + erreur;
        }
        return operande1 + " " + operateur + " " + operande2 + " = " + resultat;
    }
}