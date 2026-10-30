/**
 * Cette variable est partagée entre tous les threads ClientProcess.
 * Elle permet de compter le nombre total d'opérations effectuées par le serveur.
 * Sans synchronisation, plusieurs threads peuvent modifier le compteur
 * en même temps et certaines opérations peuvent être perdues.
 * Le mot-clé synchronized permet à un seul thread à la fois d'exécuter la méthode.
 */

public class CompteurGlobal {

    private static int total = 0;

    // static synchronized : verrou unique sur la classe, partagé par tous les threads
    public static synchronized int incrementer(String origine) {
        total++;
        // Affichage DANS la section critique : valeurs affichées dans l'ordre 1,2,3...
        System.out.println("[Compteur global] " + total
                + " opération(s) traitée(s) (dernière : " + origine + ")");
        return total;
    }

    public static synchronized int getTotal() {
        return total;
    }
}