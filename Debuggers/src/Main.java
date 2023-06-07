import Kasper.BeansDriver.AbstractReference;
import Kasper.BeansDriver.CollectionReference;
import Kasper.BeansDriver.KasperBean;
import KasperCommons.Authenticator.KasperAccessAuthenticator;
import KasperCommons.DataStructures.KasperList;
import KasperCommons.DataStructures.KasperMap;
import KasperCommons.DataStructures.KasperObject;
import KasperCommons.Exceptions.KasperException;
import KasperCommons.Parser.KasperWriter;


public class Main {
    public static void main(String[] args) throws KasperException {
        KasperBean bean = new KasperBean("localhost", "root", "");
        System.out.println(bean.useNode("mynode").useCollection("collection"));

    }
}