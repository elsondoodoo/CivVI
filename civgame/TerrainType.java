package civgame;

public enum TerrainType {
    GRASSLAND(1, "Rice", 1),
    FOREST(2, "Wood", 1),
    MOUNTAIN(2, "Stone", 1);
    
    private int movementCost;
    private String resource;
    private int resourceYield;
    
    TerrainType(int movementCost, String resource, int resourceYield) {
        this.movementCost = movementCost;
        this.resource = resource;
        this.resourceYield = resourceYield;
    }
    
    public int getMovementCost() {
        return movementCost;
    }
    
    public String getResource() {
        return resource;
    }
    
    public int getResourceYield() {
        return resourceYield;
    }
}