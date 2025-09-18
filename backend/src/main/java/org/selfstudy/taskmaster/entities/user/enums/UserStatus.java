package org.selfstudy.taskmaster.entities.user.enums;

public enum UserStatus {

    ACTIVE("Active User", true),
    INACTIVE("Inactive User", true),
    SUSPENDED("Suspended User", false),
    PENDING("Pendung User", false),
    DELETED("Deleted User", false);

    private final String description;
    private final boolean canLogin;

    UserStatus(String description, boolean canLogin) {
        this.description = description;
        this.canLogin    = canLogin;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCanLogin() {
        return canLogin;
    }

}
