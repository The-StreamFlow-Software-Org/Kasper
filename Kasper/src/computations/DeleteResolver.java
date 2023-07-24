package computations;

import com.kasper.commons.datastructures.*;

public class DeleteResolver {

    public static void delete (KasperObject object) {
        String id = ProtectedUtils.getID(object);
        if (object.parent() instanceof KasperList)
        object.parent().toList().remove(Integer.parseInt(id));
        else if (object.parent() instanceof KasperMap) object.parent().toMap().remove(id);
        removeFromReferences(object);
        removeAllChildren(object);
    }

    protected static void removeAllChildren(KasperObject o){
        if (o instanceof KasperMap) {
            for (var x : o.toMap().entrySet()) {
                dissolveParentChildRelationship(x.getValue().parent(), x.getKey());
                removeAllChildren(x.getValue());
            }
            o.toMap().clear();
            var list = o.toList();
            int i = 0;
            for (var x : list) {
                dissolveParentChildRelationship(x.parent(), i++);
                removeAllChildren(x);
            }
            list.clear();
        } else if (o instanceof KasperPrimitive) {
            removeFromReferences(o);
            ProtectedUtils.setData(o, null);
        }
    }


    // dissolves the reference (pointer) link from Parent -> Child, not necessarily
    // Child -> Parent.
    protected static void dissolveParentChildRelationship(KasperObject parent, Object key) {
        if (parent instanceof KasperMap) {
            parent.toMap().remove((String)key);
        } else if (parent instanceof KasperList) {
            parent.toList().remove((int)key);
        }
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
                // this does not work anymore
                key.toList().remove(index);
            }
        }
    }

}
