import Exceptions.KasperException;

public class Kasper {
    private String serverLocation;
    private String user;
    private String password;

    public Kasper (String serverLocation, String user, String password) throws KasperException {
        this.serverLocation = serverLocation;
        this.user = user;
        this.password = password;
        
    }


}