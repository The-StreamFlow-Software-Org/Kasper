package parser;

/*
    This contains the details on how a query should look like in Kasper.

    First, let's start with the CREATE operations.

    A. CREATE
    1. creating a node:

    ---------------
    USING none
    CREATE NODE "dbUsers";
    ---------------

    2. creating a collection:
    To specify which node our collection will be in, we can use the IN
    statement as a context identifier.

    ---------------
    CREATE COLLECTION "entity" IN "dbUsers";
    ---------------

    ---------------
    CREATE COMMITMENT "goes to" IN "dbUsers.entity.Anna";
    ---------------

    3. creating a relationship
    A many relationship means that this entity can hold more than one entity.

    ---------------
    CREATE RELATIONSHIP "friends" IN "dbUsers.entity.Anna";
    ---------------

    B. Setting a value

    ---------------
    SET {"list":["one","two","three"], "string":"string type} IN "dbUsers.entity" AS
    "sample"
    ---------------

    Ca. GETTING a value

    ---------------
    GET "dbUsers.entity";
    ---------------

    Cb. GETTING a value, including the relationships

    ---------------
    GET "dbusers.entity" @INCLUDE("friends")

    or you can even query nested relationships with:

    GET "dbusers.entity" @INCLUDE("matches");
    ---------------

    Cc. GETTING a value, including the relationships of their relationships.

    ---------------
    Get "dbusers.entity"

 */