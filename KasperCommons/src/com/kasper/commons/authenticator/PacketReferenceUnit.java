package com.kasper.commons.authenticator;

import java.util.Map;


public class PacketReferenceUnit {

    /*
    1 -> set
    2 -> get
    3 -> create-node
    4 -> create-collection
    5 -> exists
    6 -> has-property
    7 -> has-property-equal-to
    8 -> delete
    9 -> update
    10 -> full-text-search
    11 -> auth-simply
    12 -> auth-uri
    13 -> response-ok
    14 -> response-error
     */
    public int header; // determines the method

    /*
    Args type:
    { 'path' : contains the path }
    { 'uri' : contains the URI }
    { 'match-path' : contains the path to match in 'has-property' methods }
     */
    public Map<String, String> args;

    /*
    Special type of args. Optional.
    Holds results or request data.
     */
    public String data;

    /*
    Exception data
    0 -> Exception
    1 -> KasperException
    2 -> KasperObjectAlreadyExistsException
    3 -> NotIterableException
    4 -> NoSuchKasperObjectException
     */
    public int exception;
    public String exceptionMessage;



}
