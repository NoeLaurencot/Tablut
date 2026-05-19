package model;

public enum Team {
    SWEDISH,
    MUSCOVITES;

    // get the opposite team
    public Team oppositeTeam() {
        if (this == Team.SWEDISH)
            return MUSCOVITES;
        else {
            return SWEDISH;
        }
    }
}
