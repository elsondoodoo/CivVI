
## 10. Scout.java

```java
package civgame;

public class Scout extends Troop {
    public Scout(Player owner, Hexagon location) {
        super(owner, location);
        this.health = 10;
        this.damage = 2;
        this.range = 1;
        this.movement = 5;
        this.movementLeft = movement;
        this.type = TroopType.SCOUT;
    }
}
```
