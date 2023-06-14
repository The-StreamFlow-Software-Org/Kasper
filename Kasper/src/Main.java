import DataStructures.KasperCollection;
import KasperCommons.Authenticator.KasperAccessAuthenticator;

import KasperCommons.DataStructures.KasperMap;
import KasperCommons.Parser.PathParser;
import Persistence.InstantiatorService;
import Persistence.Outstream;
import Server.SuperClass.KasperGlobalMap;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        int size = 100000;
        new KasperAccessAuthenticator("kasper.util.key");
        InstantiatorService.start();
        System.out.println("Finished processing " + size + " keys!");
        System.out.println("Found you, " + KasperGlobalMap.findWithPath("f1.prof.subject").toStr());
        InstantiatorService.close();





    }
}