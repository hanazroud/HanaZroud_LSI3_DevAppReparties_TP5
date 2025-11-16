package corbaServer;

import corbaBanque.*;
import service.BanqueImpl;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.CosNaming.*;

public class BanqueServer {
    public static void main(String[] args) {
        try {
            // Initialisation ORB
            ORB orb = ORB.init(args, null);

            // Récupérer RootPOA
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            // Créer implémentation Banque
            BanqueImpl banqueImpl = new BanqueImpl(rootpoa);
            banqueImpl.setORB(orb);

            // Obtenir référence CORBA
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(banqueImpl);
            IBanqueRemote href = IBanqueRemoteHelper.narrow(ref);

            // Enregistrer dans le service de noms
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            NameComponent[] path = ncRef.to_name("Banque");
            ncRef.rebind(path, href);

            System.out.println("Serveur CORBA prêt et en attente...");
            orb.run();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
