package civgame;

import java.util.*;

public class Map {
    private int width;
    private int height;
    private Hexagon[][] grid;
    
    public Map(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Hexagon[width][height];
        
        // Generate random terrain
        Random rand = new Random();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int terrainType = rand.nextInt(3);
                TerrainType type;
                
                switch (terrainType) {
                    case 0:
                        type = TerrainType.GRASSLAND;
                        break;
                    case 1:
                        type = TerrainType.FOREST;
                        break;
                    case 2:
                        type = TerrainType.MOUNTAIN;
                        break;
                    default:
                        type = TerrainType.GRASSLAND;
                }
                
                grid[x][y] = new Hexagon(x, y, type);
            }
        }
    }
    
    public Hexagon getHexagon(int x, int y) {
        if (isValidCoordinate(x, y)) {
            return grid[x][y];
        }
        return null;
    }
    
    public boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
    
    public List<Hexagon> getHexagonsInRange(Hexagon center, int range) {
        List<Hexagon> result = new ArrayList<>();
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (center.getDistance(grid[x][y]) <= range) {
                    result.add(grid[x][y]);
                }
            }
        }
        
        return result;
    }
    
    public void displayMap() {
        for (int y = 0; y < height; y++) {
            // Offset odd rows for hexagonal grid visualization
            if (y % 2 == 1) {
                System.out.print(" ");
            }
            
            for (int x = 0; x < width; x++) {
                System.out.print(grid[x][y].getTerrainSymbol() + " ");
            }
            System.out.println();
        }
    }
}