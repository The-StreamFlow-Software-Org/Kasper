package Kasper.BeansDriver.Listeners;

import KasperCommons.Exceptions.KasperException;

public interface OnFailure {

    public void doEvent(KasperException exception);
}
