package civgame;

public class Hexagon {
    private int x;
    private int y;
    private TerrainType terrain;
    
    public Hexagon(int x, int y, TerrainType terrain) {
        this.x = x;
        this.y = y;
        this.terrain = terrain;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public TerrainType getTerrain() {
        return terrain;
    }
    
    public int getMovementCost() {
        return terrain.getMovementCost();
    }
    
    public int getDistance(Hexagon other) {
        // Calculate hex distance (simplified for this implementation)
        return Math.max(Math.abs(x - other.x), Math.abs(y - other.y));
    }
    
    public char getTerrainSymbol() {
        switch (terrain) {
            case GRASSLAND: return 'G';
            case FOREST: return 'F';
            case MOUNTAIN: return 'M';
            default: return '?';
        }
    }
}