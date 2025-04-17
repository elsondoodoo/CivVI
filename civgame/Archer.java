package civgame;

public class Archer extends Troop {
    public Archer(Player owner, Hexagon location) {
        super(owner, location);
        this.health = 5;
        this.damage = 3;
        this.range = 2;
        this.movement = 2;
        this.movementLeft = movement;
        this.type = TroopType.ARCHER;
    }
}