package Kasper.BeansDriver.Listeners;

import KasperCommons.DataStructures.KasperObject;
import KasperCommons.Exceptions.KasperException;
import KasperCommons.Network.NetworkPackage;

public class QueryListener {
    private NetworkPackage pack;
    KasperObject result;


    public QueryListener(NetworkPackage networkPackage) {
        this.pack = networkPackage;

    }

    // TODO: implement a method that constructs data from a parsed XML


    public QueryListener addOnSuccessListener(OnSuccess success){
        success.doEvent(result); // TODO: implement result
        return this;
    }

    // TODO: Figure out how to handle success, failure, and results
    public QueryListener addOnFailureListener(OnFailure failure) {
        failure.doEvent(new KasperException());
        return this;
    }





}
