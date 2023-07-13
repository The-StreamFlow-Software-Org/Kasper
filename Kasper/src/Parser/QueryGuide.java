package Parser;

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

    B. SET
    1. Setting a value

    ---------------
    SET {"list":["one","two","three"], "string":"string type} IN "dbUsers.entity" AS
    "sample"
    ---------------
 */