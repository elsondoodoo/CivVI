package civgame;

import java.util.*;

public class Game {
    private int currentTurn = 1;
    private final int MAX_TURNS = 30;
    private List<Player> players;
    private Map map;
    private Player currentPlayer;
    private int currentPlayerIndex = 0;
    private GameState gameState = GameState.RUNNING;
    
    public Game(int numPlayers, int mapWidth, int mapHeight) {
        // Initialize map
        this.map = new Map(mapWidth, mapHeight);
        
        // Initialize players
        this.players = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            Player player = new Player("Player " + (i + 1), i);
            players.add(player);
            
            // Place initial city for each player
            int x = (i + 1) * mapWidth / (numPlayers + 1);
            int y = (i + 1) * mapHeight / (numPlayers + 1);
            City city = new City(player, map.getHexagon(x, y));
            player.addCity(city);
        }
        
        currentPlayer = players.get(0);
    }
    
    public void start() {
        Scanner scanner = new Scanner(System.in);
        
        while (gameState == GameState.RUNNING) {
            System.out.println("Turn " + currentTurn + ", " + currentPlayer.getName() + "'s turn");
            displayGameState();
            
            // Process player's turn
            processTurn(scanner);
            
            // Check for game end conditions
            if (currentTurn > MAX_TURNS || checkVictoryConditions()) {
                gameState = GameState.FINISHED;
            }
            
            // Move to next player or next turn
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            currentPlayer = players.get(currentPlayerIndex);
            
            if (currentPlayerIndex == 0) {
                currentTurn++;
                updateGameState();
            }
        }
        
        announceWinner();
    }
    
    private void displayGameState() {
        // Display current game state (map, resources, cities, etc.)
        System.out.println("Map:");
        map.displayMap();
        
        System.out.println("\nCities:");
        for (City city : currentPlayer.getCities()) {
            System.out.println(city);
        }
        
        System.out.println("\nResources:");
        System.out.println("Rice: " + currentPlayer.getRice());
        System.out.println("Wood: " + currentPlayer.getWood());
        System.out.println("Stone: " + currentPlayer.getStone());
        System.out.println("Gold: " + currentPlayer.getGold());
        System.out.println("Culture: " + currentPlayer.getCulture());
        System.out.println("Science: " + currentPlayer.getScience());
    }
    
    private void processTurn(Scanner scanner) {
        boolean turnEnded = false;
        
        while (!turnEnded) {
            System.out.println("\nActions:");
            System.out.println("1. Build/Upgrade City");
            System.out.println("2. Train Troops");
            System.out.println("3. Move Troops");
            System.out.println("4. End Turn");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    handleCityActions(scanner);
                    break;
                case 2:
                    handleTroopTraining(scanner);
                    break;
                case 3:
                    handleTroopMovement(scanner);
                    break;
                case 4:
                    turnEnded = true;
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }
    
    private void handleCityActions(Scanner scanner) {
        // Display cities
        List<City> cities = currentPlayer.getCities();
        for (int i = 0; i < cities.size(); i++) {
            System.out.println((i+1) + ". " + cities.get(i).getName());
        }
        
        System.out.println("Select city (0 to cancel):");
        int cityIndex = scanner.nextInt() - 1;
        scanner.nextLine(); // Consume newline
        
        if (cityIndex >= 0 && cityIndex < cities.size()) {
            City selectedCity = cities.get(cityIndex);
            
            System.out.println("\nCity Actions:");
            System.out.println("1. Build Monument (200 stone, 50 wood)");
            System.out.println("2. Build Market (100 wood, 200 rice)");
            System.out.println("3. Build Library (100 wood, 50 rice, 100 stone)");
            System.out.println("4. Cancel");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    if (currentPlayer.getStone() >= 200 && currentPlayer.getWood() >= 50) {
                        selectedCity.buildMonument();
                        currentPlayer.useResources(0, 50, 200);
                        System.out.println("Monument built!");
                    } else {
                        System.out.println("Not enough resources!");
                    }
                    break;
                case 2:
                    if (currentPlayer.getWood() >= 100 && currentPlayer.getRice() >= 200) {
                        selectedCity.buildMarket();
                        currentPlayer.useResources(200, 100, 0);
                        System.out.println("Market built!");
                    } else {
                        System.out.println("Not enough resources!");
                    }
                    break;
                case 3:
                    if (currentPlayer.getWood() >= 100 && currentPlayer.getRice() >= 50 && currentPlayer.getStone() >= 100) {
                        selectedCity.buildLibrary();
                        currentPlayer.useResources(50, 100, 100);
                        System.out.println("Library built!");
                    } else {
                        System.out.println("Not enough resources!");
                    }
                    break;
                case 4:
                    // Cancel
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }
    
    private void handleTroopTraining(Scanner scanner) {
        // Display cities
        List<City> cities = currentPlayer.getCities();
        for (int i = 0; i < cities.size(); i++) {
            System.out.println((i+1) + ". " + cities.get(i).getName());
        }
        
        System.out.println("Select city (0 to cancel):");
        int cityIndex = scanner.nextInt() - 1;
        scanner.nextLine(); // Consume newline
        
        if (cityIndex >= 0 && cityIndex < cities.size()) {
            City selectedCity = cities.get(cityIndex);
            
            System.out.println("\nTroop Types:");
            System.out.println("1. Scout (20 gold)");
            System.out.println("2. Soldier (50 gold)");
            System.out.println("3. Archer (70 gold)");
            System.out.println("4. Cancel");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            boolean instant = false;
            if (choice >= 1 && choice <= 3) {
                System.out.println("Train instantly with gold? (y/n)");
                instant = scanner.nextLine().trim().equalsIgnoreCase("y");
            }
            
            switch (choice) {
                case 1:
                    if (instant) {
                        if (currentPlayer.getGold() >= 20) {
                            currentPlayer.useGold(20);
                            Troop scout = new Scout(currentPlayer, selectedCity.getLocation());
                            currentPlayer.addTroop(scout);
                            System.out.println("Scout trained!");
                        } else {
                            System.out.println("Not enough gold!");
                        }
                    } else {
                        selectedCity.queueTroop(TroopType.SCOUT);
                        System.out.println("Scout queued for training!");
                    }
                    break;
                case 2:
                    if (instant) {
                        if (currentPlayer.getGold() >= 50) {
                            currentPlayer.useGold(50);
                            Troop soldier = new Soldier(currentPlayer, selectedCity.getLocation());
                            currentPlayer.addTroop(soldier);
                            System.out.println("Soldier trained!");
                        } else {
                            System.out.println("Not enough gold!");
                        }
                    } else {
                        selectedCity.queueTroop(TroopType.SOLDIER);
                        System.out.println("Soldier queued for training!");
                    }
                    break;
                case 3:
                    if (instant) {
                        if (currentPlayer.getGold() >= 70) {
                            currentPlayer.useGold(70);
                            Troop archer = new Archer(currentPlayer, selectedCity.getLocation());
                            currentPlayer.addTroop(archer);
                            System.out.println("Archer trained!");
                        } else {
                            System.out.println("Not enough gold!");
                        }
                    } else {
                        selectedCity.queueTroop(TroopType.ARCHER);
                        System.out.println("Archer queued for training!");
                    }
                    break;
                case 4:
                    // Cancel
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }
    
    private void handleTroopMovement(Scanner scanner) {
        // Display troops
        List<Troop> troops = currentPlayer.getTroops();
        for (int i = 0; i < troops.size(); i++) {
            Troop troop = troops.get(i);
            System.out.println((i+1) + ". " + troop.getType() + " at (" + 
                               troop.getLocation().getX() + "," + troop.getLocation().getY() + ")");
        }
        
        System.out.println("Select troop (0 to cancel):");
        int troopIndex = scanner.nextInt() - 1;
        scanner.nextLine(); // Consume newline
        
        if (troopIndex >= 0 && troopIndex < troops.size()) {
            Troop selectedTroop = troops.get(troopIndex);
            
            System.out.println("Enter target x coordinate:");
            int x = scanner.nextInt();
            System.out.println("Enter target y coordinate:");
            int y = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            if (map.isValidCoordinate(x, y)) {
                Hexagon target = map.getHexagon(x, y);
                if (selectedTroop.canMoveTo(target)) {
                    selectedTroop.moveTo(target);
                    System.out.println("Moved troop to (" + x + "," + y + ")");
                    
                    // Check if there are enemy units or cities at the destination
                    checkForCombat(selectedTroop);
                } else {
                    System.out.println("Cannot move to that location (out of range or impassable)");
                }
            } else {
                System.out.println("Invalid coordinates");
            }
        }
    }
    
    private void checkForCombat(Troop troop) {
        Hexagon location = troop.getLocation();
        
        // Check for enemy troops at the location
        for (Player player : players) {
            if (player != currentPlayer) {
                for (Troop enemyTroop : player.getTroops()) {
                    if (enemyTroop.getLocation() == location) {
                        // Combat!
                        resolveCombat(troop, enemyTroop);
                        return;
                    }
                }
            }
        }
        
        // Check for enemy cities at the location
        for (Player player : players) {
            if (player != currentPlayer) {
                for (City city : player.getCities()) {
                    if (city.getLocation() == location) {
                        // Attack city!
                        attackCity(troop, city);
                        return;
                    }
                }
            }
        }
    }
    
    private void resolveCombat(Troop attacker, Troop defender) {
        System.out.println(attacker.getType() + " attacks " + defender.getType() + "!");
        
        // Apply damage
        defender.takeDamage(attacker.getDamage());
        System.out.println(defender.getType() + " has " + defender.getHealth() + " health left");
        
        // Check if defender is defeated
        if (defender.getHealth() <= 0) {
            System.out.println(defender.getType() + " is defeated!");
            defender.getOwner().removeTroop(defender);
        } else {
            // Counter-attack if in range
            if (attacker.getLocation().getDistance(defender.getLocation()) <= defender.getRange()) {
                System.out.println(defender.getType() + " counter-attacks!");
                attacker.takeDamage(defender.getDamage());
                System.out.println(attacker.getType() + " has " + attacker.getHealth() + " health left");
                
                if (attacker.getHealth() <= 0) {
                    System.out.println(attacker.getType() + " is defeated!");
                    attacker.getOwner().removeTroop(attacker);
                }
            }
        }
    }
    
    private void attackCity(Troop attacker, City city) {
        System.out.println(attacker.getType() + " attacks " + city.getName() + "!");
        
        city.takeDamage(attacker.getDamage());
        System.out.println(city.getName() + " has " + city.getHealth() + " health left");
        
        if (city.getHealth() <= 0) {
            System.out.println(city.getName() + " is captured!");
            Player oldOwner = city.getOwner();
            oldOwner.removeCity(city);
            
            // Check if player has been eliminated
            if (oldOwner.getCities().isEmpty()) {
                System.out.println(oldOwner.getName() + " has been eliminated!");
                players.remove(oldOwner);
                if (currentPlayerIndex > players.indexOf(currentPlayer)) {
                    currentPlayerIndex--;
                }
            }
            
            // Transfer city ownership
            city.resetHealth();
            city.setOwner(currentPlayer);
            currentPlayer.addCity(city);
        }
    }
    
    private void updateGameState() {
        // Update all cities (collect resources, grow population, etc.)
        for (Player player : players) {
            for (City city : player.getCities()) {
                city.update(map);
            }
            
            // Train troops in queue
            player.updateTroopTraining();
            
            // Update player resources
            player.collectResources();
        }
    }
    
    private boolean checkVictoryConditions() {
        // Condition 1: Only one player remains
        if (players.size() == 1) {
            return true;
        }
        
        // Condition 2: Max turns reached (checked in main game loop)
        return false;
    }
    
    private void announceWinner() {
        System.out.println("\nGame Over!");
        
        if (players.size() == 1) {
            System.out.println(players.get(0).getName() + " wins by conquest!");
        } else if (currentTurn > MAX_TURNS) {
            // Find player with most culture and science
            Player culturalWinner = players.get(0);
            Player scientificWinner = players.get(0);
            
            for (Player player : players) {
                if (player.getCulture() > culturalWinner.getCulture()) {
                    culturalWinner = player;
                }
                if (player.getScience() > scientificWinner.getScience()) {
                    scientificWinner = player;
                }
            }
            
            System.out.println("Cultural Victory: " + culturalWinner.getName() + " with " + culturalWinner.getCulture() + " culture");
            System.out.println("Scientific Victory: " + scientificWinner.getName() + " with " + scientificWinner.getScience() + " science");
        }
    }
    
    private enum GameState {
        RUNNING, FINISHED
    }
}