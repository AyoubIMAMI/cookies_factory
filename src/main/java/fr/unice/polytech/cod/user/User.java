package fr.unice.polytech.cod.user;

import fr.unice.polytech.cod.pojo.Item;
import fr.unice.polytech.cod.helper.Display;
import fr.unice.polytech.cod.food.Cookie;
import fr.unice.polytech.cod.order.Bill;
import fr.unice.polytech.cod.order.Order;
import fr.unice.polytech.cod.order.OrderState;
import fr.unice.polytech.cod.schedule.Interval;
import fr.unice.polytech.cod.exceptions.InvalidStoreException;
import fr.unice.polytech.cod.store.Store;
import fr.unice.polytech.cod.pojo.StoreLocation;
import fr.unice.polytech.cod.user.fidelityAccount.Discount;
import fr.unice.polytech.cod.user.fidelityAccount.FidelityAccount;

import java.time.Instant;
import java.util.*;

public class User {
    private final Cart cart;
    private final List<Order> userOrders;
    private final StoreLocation storeLocation;
    private FidelityAccount fidelityAccount;

    public User(Cart cart, StoreLocation storeLocation) {  //TODO enlever du constructeur le cart
        this.cart = cart;
        this.userOrders = new ArrayList<>();
        this.storeLocation = storeLocation;
    }

    /**
     * Return the list of available cookies based on the store
     */
    public List<Cookie> viewCatalog() {
        return cart.getStore().getAvailableCookie();
    }

    /**
     * Add cookies to cart
     *
     * @param cookie The cookie to add to the cart
     * @param quantity The quantity of the selected cookie
     */
    public boolean chooseCookies(Cookie cookie, int quantity) {
        return cart.addToCart(new Item(cookie, quantity));
    }

    public List<Store> viewStoreAvailable() {
        return (storeLocation.getStoreList());
    }

    /**
     * choose the store for his current order
     *
     * @param name
     * @return
     * @throws InvalidStoreException
     */
    public Store selectStore(String name) throws InvalidStoreException {
        Store store = storeLocation.selectStore(name);
        this.cart.setStore(store);
        return (store);
    }

    /**
     * Show all the cookies in our order and give the choice to validate or add/delete more cookies
     */
    public void recapCart() {
        Display.title("Your cart:");
        cart.showCart();
    }

    /**
     * Gets a list of available TimeSlots by Date;
     *
     * @return
     */
    public List<Interval> getAvailableIntervals(int minutesNeeded,int numberOfDaysBeforeTheOrder) {
        return (this.cart.getStore().timeSlotAvailable(minutesNeeded,numberOfDaysBeforeTheOrder));
    }


    public void chooseInterval(Interval interval) {
        interval.reserve();
        this.cart.setInterval(interval);
    }

    /**
     * If the cart is not empty, validate the cart to create an order
     */
    public Bill validateCart() throws Exception {
        Instant time = Instant.now();
        if (!cart.isEmpty() && !cart.isTherePenalty(time))
            return cart.validate(this);
        else
            throw new Exception("Panier vide impossible de le valider");
    }

    public Cart getCart() {
        return cart;
    }

    public void addOrder(Order order) {
        userOrders.add(order);
        if (fidelityAccount != null)
            fidelityAccount.addOrder(order);
    }

    public List<Order> getOrders() {
        return userOrders;
    }

    public Set<Item> getAllItemsFromCart() {
        return cart.getItemSet();
    }

    public Item getItemFromCart(String itemName) throws Exception {
        return cart.findItem(itemName);
    }

    public StoreLocation getStoreLocation() {
        return storeLocation;
    }

    public Store getStore() {
        return (this.cart.getStore());
    }

    public void removeOneItemFromCart(Item item) {
        cart.removeFromCart(item);
    }

    public void subscribeToFidelityAccount(String name, String email, String password) {
        fidelityAccount = new FidelityAccount(name, email, password);
    }

    public Optional<FidelityAccount> getSubscription() {
        if(fidelityAccount == null)
            return Optional.empty();
        return Optional.of(fidelityAccount);
    }

    public boolean hasFidelityAccount() {
        return fidelityAccount != null;
    }

    public void useDiscount(Order order) {
        if(fidelityAccount == null)
            return;
        Optional<Discount> _discount = fidelityAccount.getDiscount();
        if(_discount.isEmpty())
            return;

        Discount discount = _discount.get();
        order.setDiscount(discount);
        fidelityAccount.resetDiscount();
    }

    public boolean cancelOrder(Order order) {
        if (userOrders.contains(order) && order.getOrderState().equals(OrderState.PENDING)) {
            cart.cancelOrder(order);
            return true; //Your order has been canceled
        } else
            return false; //Your order is already in progress. You cannot canceled it
    }

    /**
     * This simulates a sms send to the user
     *
     * @param message The message send to the user.
     */
    public void notify(String message) {
        if (fidelityAccount == null)
            Display.smsNok("Anonymous account.");
        else
            Display.smsOk(fidelityAccount.getName(), message);
    }

    public List<Order> getHistory() throws Exception{
        if(fidelityAccount == null)
            throw new Exception("Your not subscribe to a fidelity account");
        return fidelityAccount.getRetrieveOrder();
    }

    public FidelityAccount getFidelityAccount() {
        return fidelityAccount;
    }

    public void setFidelityAccount(FidelityAccount fidelityAccount) {
        this.fidelityAccount = fidelityAccount;
    }
}
