package service;

import corbaBanque.*;
import org.omg.PortableServer.POA;
import java.util.ArrayList;
import java.util.List;

public class BanqueImpl extends IBanqueRemotePOA {
    private POA poa; // si besoin
    private final List<Compte> comptes = new ArrayList<>();

    public BanqueImpl(POA poa) {
        this.poa = poa;
    }

    @Override
    public void creerCompte(Compte cpte) {
        synchronized (comptes) {
            for (Compte c : comptes) {
                if (c.code == cpte.code) {
                    return; // compte déjà existant
                }
            }
            Compte copie = new Compte(cpte.code, cpte.solde);
            comptes.add(copie);
            System.out.println("Compte créé: code=" + cpte.code + ", solde=" + cpte.solde);
        }
    }

    @Override
    public void verser(float mt, int code) {
        synchronized (comptes) {
            for (Compte c : comptes) {
                if (c.code == code) {
                    c.solde += mt;
                    System.out.println("Versement " + mt + " sur compte " + code + ", nouveau solde=" + c.solde);
                    return;
                }
            }
            System.out.println("Verser: compte introuvable: " + code);
        }
    }

    @Override
    public void retirer(float mt, int code) {
        synchronized (comptes) {
            for (Compte c : comptes) {
                if (c.code == code) {
                    if (c.solde >= mt) {
                        c.solde -= mt;
                        System.out.println("Retrait " + mt + " sur compte " + code + ", nouveau solde=" + c.solde);
                    } else {
                        System.out.println("Retrait impossible: solde insuffisant pour compte " + code);
                    }
                    return;
                }
            }
            System.out.println("Retirer: compte introuvable: " + code);
        }
    }

    @Override
    public Compte getCompte(int code) {
        synchronized (comptes) {
            for (Compte c : comptes) {
                if (c.code == code) {
                    return new Compte(c.code, c.solde); // retourne une copie
                }
            }
            return new Compte(-1, 0.0f); // si non trouvé
        }
    }

    @Override
    public Compte[] getComptes() {
        synchronized (comptes) {
            Compte[] arr = new Compte[comptes.size()];
            for (int i = 0; i < comptes.size(); i++) {
                Compte c = comptes.get(i);
                arr[i] = new Compte(c.code, c.solde);
            }
            return arr;
        }
    }

    @Override
    public double conversion(float mt) {
        double taux = 3.3; // taux fixe pour exemple
        return mt * taux;
    }

    // si besoin : setter ORB
    public void setORB(org.omg.CORBA.ORB orb_val) {
        // stocker l'ORB si nécessaire
    }
}
