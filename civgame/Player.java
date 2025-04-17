package civgame;

import java.util.*;

public class Player {
    private String name;
    private int id;
    private List<City> cities = new ArrayList<>();
    private List<Troop> troops = new ArrayList<>();
    private int rice = 100;
    private int wood = 100;
    private int stone = 100;
    private int gold = 100;
    private int culture = 0;
    private int science = 0;
    
    public Player(String name, int id) {
        this.name = name;
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public int getId() {
        return id;
    }
    
    public List<City> getCities() {
        return cities;
    }
    
    public void addCity(City city) {
        cities.add(city);
    }
    
    public void removeCity(City city) {
        cities.remove(city);
    }
    
    public List<Troop> getTroops() {
        return troops;
    }
    
    public void addTroop(Troop troop) {
        troops.add(troop);
    }
    
    public void removeTroop(Troop troop) {
        troops.remove(troop);
    }
    
    public int getRice() {
        return rice;
    }
    
    public int getWood() {
        return wood;
    }
    
    public int getStone() {
        return stone;
    }
    
    public int getGold() {
        return gold;
    }
    
    public int getCulture() {
        return culture;
    }
    
    public int getScience() {
        return science;
    }
    
    public void useResources(int rice, int wood, int stone) {
        this.rice -= rice;
        this.wood -= wood;
        this.stone -= stone;
    }
    
    public void useGold(int gold) {
        this.gold -= gold;
    }
    
    public void collectResources() {
        for (City city : cities) {
            gold += city.getGoldProduction();
            culture += city.getCultureProduction();
            science += city.getScienceProduction();
        }
    }
    
    public void addResources(int rice, int wood, int stone) {
        this.rice += rice;
        this.wood += wood;
        this.stone += stone;
    }
    
    public void updateTroopTraining() {
        for (City city : cities) {
            Troop trainedTroop = city.updateTraining();
            if (trainedTroop != null) {
                troops.add(trainedTroop);
            }
        }
    }
}