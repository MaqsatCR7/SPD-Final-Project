package library.behavioral.strategy;

import library.model.MembershipType;

import java.util.EnumMap;
import java.util.Map;

/** Chooses the fine strategy for a membership type (no if/else chains - Open/Closed). */
public final class FineStrategyProvider {
    private static final Map<MembershipType, FineStrategy> STRATEGIES = new EnumMap<>(MembershipType.class);

    static {
        STRATEGIES.put(MembershipType.STANDARD, new StandardFineStrategy());
        STRATEGIES.put(MembershipType.STUDENT, new StudentFineStrategy());
        STRATEGIES.put(MembershipType.PREMIUM, new PremiumFineStrategy());
    }

    private FineStrategyProvider() {
    }

    public static FineStrategy forMembership(MembershipType type) {
        return STRATEGIES.get(type);
    }
}
