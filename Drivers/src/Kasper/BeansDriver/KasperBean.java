package Kasper.BeansDriver;

import KasperCommons.Exceptions.KasperException;

public class KasperBean {
    private String serverLocation;
    private String user;
    private String password;

    public KasperBean(String serverLocation, String user, String password) throws KasperException {
        this.serverLocation = serverLocation;
        this.user = user;
        this.password = password;
        
    }


}