package KasperCommons.DataStructures;

import java.util.ArrayDeque;
import java.util.Deque;

public class LocalPathCrawler {



    public static void crawlPaths (KasperObject o) {
        var t = new Thread(()->asyncCrawl(o));
        t.start();
    }

    public static void finalPathSetter (KasperObject object, String id){
        object.finalPath = id;
    }





    protected static void asyncCrawl(KasperObject o) {
        Deque<KasperObject> stack = new ArrayDeque<>(); // Stack to hold objects to process
        stack.push(o);

        while (!stack.isEmpty()) {
            KasperObject current = stack.pop();


            if (!(current instanceof KasperString)) {
                if (current instanceof KasperMap) {
                    for (var x : current.toMap().entrySet()) {
                        if (!x.getValue().id.isEmpty()) return;
                        var val = x.getValue();
                        val.id = x.getKey();
                        if (val.inquirers!=null) {
                            // interrupt all thread listeners
                            for (var thread : val.inquirers) {
                                thread.interrupt();
                            }
                        }
                        stack.push(x.getValue()); // Add to stack for further processing
                    }
                } else if (current instanceof KasperList) {
                    int currentIdx = 0;
                    for (var x : current.toList()) {
                        if (!x.id.isEmpty()) return;
                        x.id = Integer.toString(currentIdx);
                        if (x.inquirers != null) {
                            for (var thread : x.inquirers) {
                                thread.interrupt();
                            }
                        }
                        currentIdx++;
                        stack.push(x); // Add to stack for further processing
                    }
                }
            }
        }
    }


}


