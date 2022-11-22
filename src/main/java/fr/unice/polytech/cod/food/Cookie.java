package fr.unice.polytech.cod.food;

import fr.unice.polytech.cod.Saleable;
import fr.unice.polytech.cod.food.ingredient.*;
import fr.unice.polytech.cod.store.Store;

import java.util.*;

public class Cookie implements Saleable {
    protected final String name;
    protected final Dough dough;
    protected final Flavour flavour;
    protected List<Topping> toppingList; // TODO : Maybe topping should be represented as a set
    protected  Mix mix;
    protected  Cooking cooking;
    private int preparationTime; // en minutes

    public Cookie(String cookieName, Dough dough, Flavour flavour, List<Topping> toppingList, Mix mix, Cooking cooking, int preparationTime) {
        this.name = cookieName;
        this.dough = dough;
        this.flavour = flavour;
        this.toppingList = toppingList;
        this.mix = mix;
        this.cooking = cooking;
        this.preparationTime = preparationTime;
    }

    public Cookie(Cookie cookie) {
        name = cookie.getName();
        dough = new Dough(cookie.getDough());
        flavour = new Flavour(cookie.getFlavour());
        toppingList =  new ArrayList<>();
        cookie.getToppingList().forEach(topping -> toppingList.add(new Topping(topping)));

        mix = cookie.getMix();
        cooking = cookie.getCooking();
        preparationTime = cookie.getPreparationTime();
    }

    public Cooking getCooking() {
        return this.cooking;
    }

    private Mix getMix() {
        return this.mix;
    }

    /**
     * Give the set of all ingredients needed to produce one cookie base on its recipe.
     *
     * @return The set of all ingredients needed.
     */
    public Set<Ingredient> getIngredients() {
        Set<Ingredient> ingredients = new HashSet<>();

        if (dough != null) ingredients.add(dough);
        if (flavour != null) ingredients.add(flavour);
        ingredients.addAll(toppingList);
        return ingredients;
    }

    public ArrayList<Ingredient> getIngredientsList() {
       return new ArrayList<>(getIngredients().stream().toList());
    }

    @Override
    public String getName() {
        return name;
    }

    public Dough getDough() {
        return dough;
    }

    public Flavour getFlavour() {
        return flavour;
    }

    public List<Topping> getToppingList() {
        return toppingList;
    }

    @Override
    public double getPriceByStore(Store store) {
        Map<Ingredient, Double> taxes = store.getTaxes();
        double price = dough.getQuantity() * dough.getPrice() + taxes.get(dough);
        if(flavour != null) price += flavour.getQuantity() * flavour.getPrice() + taxes.get(flavour);
        if(!toppingList.isEmpty()) {
            for(Topping topping : toppingList)
                price += topping.getQuantity() * topping.getPrice() +taxes.get(topping);
        }
        return price;
    }

    @Override
    public double getPriceHT(){
        double price = dough.getQuantity() * dough.getPrice();
        if(!toppingList.isEmpty()) {
            for(Topping topping : toppingList)
                price += topping.getQuantity() * topping.getPrice();
        }
        return price;
    }

    @Override
    public String toString() {
        return "Cookie{" +
                "cookieName='" + name + '\'' +
                ", dough=" + dough +
                ", flavour=" + flavour +
                ", toppingList=" + toppingList +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (object == this)
            return true;

        // Check for the instance
        if (!(object instanceof Cookie c))
            return false;

        // Check for the name equality
        return c.name.equals(name);
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public void setCooking(Cooking cooking) {
        this.cooking = cooking;
    }
}
