package library.model;

import library.behavioral.observer.ItemEvent;
import library.behavioral.observer.ItemObserver;
import library.exception.LibraryException;
import library.util.IdGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Library member.
 * BUILDER pattern: the nested {@link Builder} creates a Member step by step
 * (name and e-mail are required, everything else is optional).
 * OBSERVER pattern: a Member is a concrete observer - it gets a notification
 * when an item it reserved becomes available.
 */
public class Member implements ItemObserver {
    private final String id;
    private final String name;
    private final String email;
    private final String phone;
    private final String address;
    private final MembershipType membershipType;
    private final List<String> notifications = new ArrayList<>();

    private Member(Builder builder) {
        this.id = IdGenerator.next("MB");
        this.name = builder.name;
        this.email = builder.email;
        this.phone = builder.phone;
        this.address = builder.address;
        this.membershipType = builder.membershipType;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public MembershipType getMembershipType() {
        return membershipType;
    }

    public List<String> getNotifications() {
        return Collections.unmodifiableList(notifications);
    }

    @Override
    public void update(ItemEvent event) {
        if (event.type() == ItemEvent.Type.AVAILABLE) {
            String message = "'" + event.item().getTitle() + "' is now available!";
            notifications.add(message);
            System.out.println("   [NOTIFY -> " + name + "] " + message);
            event.item().unsubscribe(this); // a reservation is one-shot
        }
    }

    @Override
    public String toString() {
        return id + " " + name + " <" + email + "> " + membershipType;
    }

    // ------------------------------------------------------------------ Builder
    public static class Builder {
        private final String name;
        private final String email;
        private String phone = "-";
        private String address = "-";
        private MembershipType membershipType = MembershipType.STANDARD;

        public Builder(String name, String email) {
            this.name = name;
            this.email = email;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder membershipType(MembershipType membershipType) {
            this.membershipType = membershipType;
            return this;
        }

        public Member build() {
            if (name == null || name.isBlank()) {
                throw new LibraryException("Member name must not be empty");
            }
            if (email == null || !email.contains("@")) {
                throw new LibraryException("Member e-mail is invalid: " + email);
            }
            return new Member(this);
        }
    }
}
