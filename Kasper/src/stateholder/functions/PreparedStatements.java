package stateholder.functions;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.kasper.Boost.Pair;
import parser.TokenCursor;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class PreparedStatements {

    private static Cache<TokenCursor, Pair<String, HashMap<String, Object>>> cache = cache = Caffeine.newBuilder()
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .maximumSize(30)
            .build();

    public static void put (TokenCursor query, Pair<String, HashMap<String, Object>> value) {
        cache.put(query, value);
    }

    public static Object executeCachedInstance (TokenCursor query) {
        var result = cache.getIfPresent(query);
        if (result == null) return false;
        return StoredProcedures.execute(result.first(), result.second());
    }
}
