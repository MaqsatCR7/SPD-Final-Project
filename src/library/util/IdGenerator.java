package library.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/** Generates readable ids such as BK-001, MB-002, LN-003. */
public final class IdGenerator {
    private static final Map<String, AtomicInteger> COUNTERS = new ConcurrentHashMap<>();

    private IdGenerator() {
    }

    public static String next(String prefix) {
        int n = COUNTERS.computeIfAbsent(prefix, k -> new AtomicInteger()).incrementAndGet();
        return String.format("%s-%03d", prefix, n);
    }
}
