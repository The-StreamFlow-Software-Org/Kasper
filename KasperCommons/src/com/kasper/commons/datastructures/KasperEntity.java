package com.kasper.commons.datastructures;

public interface KasperEntity {
    KasperObject getObject();
    int getIntType();

    int TYPE_OBJECT = 0;
    int TYPE_EXCEPTION = 1;
    int TYPE_QUERY_OK = 3;

     class QueryOk implements KasperEntity {

         private static QueryOk singleton = null;
         private KasperString ok = new KasperString("query ok!");

         @Override
         public KasperObject getObject() {
             if (singleton == null) singleton = new QueryOk();
             return singleton.ok;
         }

         public static KasperObject storedInstance () {
                if (singleton == null) singleton = new QueryOk();
                return singleton.ok;
         }

         @Override
         public int getIntType() {
             return TYPE_QUERY_OK;
         }
     }
}
