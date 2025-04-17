package civgame;

public abstract class Troop {
    protected Player owner;
    protected Hexagon location;
    protected int health;
    protected int damage;
    protected int range;
    protected int movement;
    protected int movementLeft;
    protected TroopType type;
    
    public Troop(Player owner, Hexagon location) {
        this.owner = owner;
        this.location = location;
        this.movementLeft = movement;
    }
    
    public Player getOwner() {
        return owner;
    }
    
    public Hexagon getLocation() {
        return location;
    }
    
    public int getHealth() {
        return health;
    }
    
    public int getDamage() {
        return damage;
    }
    
    public int getRange() {
        return range;
    }
    
    public TroopType getType() {
        return type;
    }
    
    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) {
            health = 0;
        }
    }
    
    public boolean canMoveTo(Hexagon target) {
        int distance = location.getDistance(target);
        int cost = target.getMovementCost();
        
        return distance * cost <= movementLeft;
    }
    
    public void moveTo(Hexagon target) {
        int distance = location.getDistance(target);
        int cost = target.getMovementCost();
        
        movementLeft -= distance * cost;
        location = target;
    }
    
    public void resetMovement() {
        movementLeft = movement;
    }
}