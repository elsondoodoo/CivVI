package civgame;

import java.util.*;

public class City {
    private String name;
    private Player owner;
    private Hexagon location;
    private int population = 10;
    private int level = 1;
    private int health = 50;
    private double populationMultiplier = 1.0;
    private double goldMultiplier = 1.0;
    private double cultureMultiplier = 1.0;
    private double scienceMultiplier = 1.0;
    private boolean hasMonument = false;
    private boolean hasMarket = false;
    private boolean hasLibrary = false;
    private Queue<TroopTraining> trainingQueue = new LinkedList<>();
    
    public City(Player owner, Hexagon location) {
        this.owner = owner;
        this.location = location;
        this.name = owner.getName() + "'s City " + (owner.getCities().size() + 1);
    }
    
    public String getName() {
        return name;
    }
    
    public Player getOwner() {
        return owner;
    }
    
    public void setOwner(Player owner) {
        this.owner = owner;
    }
    
    public Hexagon getLocation() {
        return location;
    }
    
    public int getHealth() {
        return health;
    }
    
    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) {
            health = 0;
        }
    }
    
    public void resetHealth() {
        health = 50;
    }
    
    public void update(Map map) {
        // Calculate rice production from surrounding grassland
        int riceProduction = 0;
        List<Hexagon> surroundingTiles = map.getHexagonsInRange(location, 3);
        
        for (Hexagon hex : surroundingTiles) {
            if (hex.getTerrain() == TerrainType.GRASSLAND) {
                riceProduction += hex.getTerrain().getResourceYield();
            }
        }
        
        // Grow population
        int populationGrowth = (int)(riceProduction * populationMultiplier);
        population += populationGrowth;
        
        // Collect resources
        int woodProduction = 0;
        int stoneProduction = 0;
        
        for (Hexagon hex : surroundingTiles) {
            if (hex.getTerrain() == TerrainType.FOREST) {
                woodProduction += hex.getTerrain().getResourceYield();
            } else if (hex.getTerrain() == TerrainType.MOUNTAIN) {
                stoneProduction += hex.getTerrain().getResourceYield();
            }
        }
        
        owner.addResources(riceProduction, woodProduction, stoneProduction);
        
        // Check for level upgrade
        if (population >= level * 100) {
            level++;
            System.out.println(name + " upgraded to level " + level + "!");
        }
    }
    
    public int getGoldProduction() {
        return (int)((population / 2) * goldMultiplier);
    }
    
    public int getCultureProduction() {
        return (int)((population / 50) * cultureMultiplier);
    }
    
    public int getScienceProduction() {
        return (int)((population / 50) * scienceMultiplier);
    }
    
    public void buildMonument() {
        hasMonument = true;
        populationMultiplier *= 1.2;
        cultureMultiplier *= 1.5;
    }
    
    public void buildMarket() {
        hasMarket = true;
        populationMultiplier *= 1.2;
        goldMultiplier *= 1.5;
    }
    
    public void buildLibrary() {
        hasLibrary = true;
        populationMultiplier *= 1.2;
        scienceMultiplier *= 1.5;
    }
    
    public void queueTroop(TroopType type) {
        int turnsRequired;
        switch (type) {
            case SCOUT:
                turnsRequired = Math.max(1, 3 - level);
                break;
            case SOLDIER:
                turnsRequired = Math.max(1, 5 - level);
                break;
            case ARCHER:
                turnsRequired = Math.max(1, 7 - level);
                break;
            default:
                turnsRequired = 1;
        }
        
        trainingQueue.add(new TroopTraining(type, turnsRequired));
    }
    
    public Troop updateTraining() {
        if (!trainingQueue.isEmpty()) {
            TroopTraining training = trainingQueue.peek();
            training.turnsRemaining--;
            
            if (training.turnsRemaining <= 0) {
                trainingQueue.poll();
                
                // Create the troop
                switch (training.type) {
                    case SCOUT:
                        return new Scout(owner, location);
                    case SOLDIER:
                        return new Soldier(owner, location);
                    case ARCHER:
                        return new Archer(owner, location);
                    default:
                        return null;
                }
            }
        }
        return null;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" (Level ").append(level).append("): ");
        sb.append(population).append(" population, ").append(health).append(" health");
        sb.append("\nBuildings: ");
        if (hasMonument) sb.append("Monument ");
        if (hasMarket) sb.append("Market ");
        if (hasLibrary) sb.append("Library ");
        if (!hasMonument && !hasMarket && !hasLibrary) sb.append("None");
        
        return sb.toString();
    }
    
    private class TroopTraining {
        TroopType type;
        int turnsRemaining;
        
        TroopTraining(TroopType type, int turnsRemaining) {
            this.type = type;
            this.turnsRemaining = turnsRemaining;
        }
    }
}