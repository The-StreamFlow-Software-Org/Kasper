package Computations;

import KasperCommons.DataStructures.KasperList;
import KasperCommons.DataStructures.KasperMap;
import KasperCommons.DataStructures.KasperObject;
import KasperCommons.DataStructures.ProtectedUtils;

public class DeleteResolver {

    public static void delete (KasperObject object) {
        String id = ProtectedUtils.getID(object);
        if (object.parent() instanceof KasperList)
        object.parent().toList().remove(Integer.parseInt(id));
        else if (object.parent() instanceof KasperMap) object.parent().toMap().remove(id);
        removeFromReferences(object);
    }


    // removes this object from all references
    protected static void removeFromReferences (KasperObject object) {
        assert ProtectedUtils.getReferenceIterable(object) != null;
        if (ProtectedUtils.getReferences(object) == null) return;
        for (var x : ProtectedUtils.getReferenceIterable(object)) {
            // REMEMBER TO CHECK THIS AGAIN IN THE INSTANCE OF ADDING KasperRelations
            var key = x.getKey();
            if (key instanceof KasperMap map) {
                map.toMap().remove(x.getValue());
            } else {
                int index = Integer.parseInt(x.getValue());
                key.toList().remove(index);
            }
        }
    }

}
