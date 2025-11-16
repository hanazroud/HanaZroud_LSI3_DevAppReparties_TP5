package corbaClient;

import corbaBanque.IBanqueRemote;
import corbaBanque.IBanqueRemoteHelper;
import corbaBanque.Compte;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Properties;

public class BanqueClient {
    public static void main(String[] args) {
        try {
            // a. Récupérer les propriétés de l'annuaire JNDI
            Properties props = new Properties();
            props.load(BanqueClient.class.getResourceAsStream("/jndi.properties"));
            Context ctx = new InitialContext(props);

            // b. Récupérer la référence de l'objet distant
            Object objRef = ctx.lookup("Banque"); // Nom de l'objet dans le serveur

            // c. Convertir la référence vers le type IBanqueRemote pour créer le Stub
            IBanqueRemote banque = IBanqueRemoteHelper.narrow((org.omg.CORBA.Object) objRef);

            // d. Appeler à distance les méthodes du service bancaire
            // Créer un compte
            Compte cpte1 = new Compte();
            cpte1.code = 1;
            cpte1.solde = 1000;
            banque.creerCompte(cpte1);

            // Verser de l'argent
            banque.verser(500, 1);

            // Retirer de l'argent
            banque.retirer(200, 1);

            // Consulter un compte
            Compte c = banque.getCompte(1);
            System.out.println("Compte consulté : code=" + c.code + ", solde=" + c.solde);

            // Consulter tous les comptes
            Compte[] comptes = banque.getComptes();
            System.out.println("Liste de tous les comptes :");
            for (Compte compte : comptes) {
                System.out.println("code=" + compte.code + ", solde=" + compte.solde);
            }

            // Conversion Euro -> Dinar
            double dt = banque.conversion(100);
            System.out.println("100 € = " + dt + " DT");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}