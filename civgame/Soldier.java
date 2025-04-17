package civgame;

public class Soldier extends Troop {
    public Soldier(Player owner, Hexagon location) {
        super(owner, location);
        this.health = 10;
        this.damage = 5;
        this.range = 1;
        this.movement = 3;
        this.movementLeft = movement;
        this.type = TroopType.SOLDIER;
    }
}