package com.example.finalproject.Helper;

import android.content.Context;
import android.widget.Toast;

import com.example.finalproject.Domain.Foods;

import java.util.ArrayList;

public class ManagmentCart {
    private Context context;
    private TinyDB tinyDB;

    // Constructor for ManagementCart class
    public ManagmentCart(Context context) {
        this.context = context;
        this.tinyDB = new TinyDB(context);
    }

    /*
     * This method handles inserting a food item into the preferences of Firebase
     * @param item: Foods object
     */
    public void insertFood(Foods item) {
        ArrayList<Foods> listpop = getListCart();
        boolean existAlready = false;
        int n = 0;

        for (int i = 0; i < listpop.size(); i++) {
            if (listpop.get(i).getTitle().equals(item.getTitle())) {
                existAlready = true;
                n = i;
                break;
            }
        }

        if (existAlready) {

            listpop.get(n).setNumberInCart(item.getNumberInCart());
        } else {

            listpop.add(item);
        }
        tinyDB.putListObject("CartList", listpop);
        Toast.makeText(context, "Added to your cart", Toast.LENGTH_SHORT).show();
    }

    // Method to get the list of items from preferences
    public ArrayList<Foods> getListCart() {
        return tinyDB.getListObject("CartList");
    }

    // Method to calculate the total fee of items in the cart
    public Double getTotalFee() {
        ArrayList<Foods> listItem = getListCart();
        double fee = 0;

        // Loop through the items in the cart and calculate the total fee
        for (int i = 0; i < listItem.size(); i++) {
            fee = fee + (listItem.get(i).getPrice() * listItem.get(i).getNumberInCart());
        }

        return fee;
    }

    /*
     * Method to decrease the quantity of an item in the cart
     * @param listItem: List of items in the cart
     * @param position: Position of the item to be decreased
     * @param changeNumberItemsListener: Listener for handling changes in the number of items
     */
    public void minusNumberItem(ArrayList<Foods> listItem, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        if (listItem.get(position).getNumberInCart() == 1) {
            // If the quantity is 1, remove the item from the list
            listItem.remove(position);
        } else {
            // If the quantity is more than 1, decrease the quantity by 1
            listItem.get(position).setNumberInCart(listItem.get(position).getNumberInCart() - 1);
        }

        // Update the preferences with the modified list
        tinyDB.putListObject("CartList", listItem);

        // Notify the listener that the number of items has changed
        changeNumberItemsListener.change();
    }

    /*
     * Method to increase the quantity of an item in the cart
     * @param listItem: List of items in the cart
     * @param position: Position of the item to be increased
     * @param changeNumberItemsListener: Listener for handling changes in the number of items
     */
    public void plusNumberItem(ArrayList<Foods> listItem, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        // Increase the quantity of the item at the specified position by 1
        listItem.get(position).setNumberInCart(listItem.get(position).getNumberInCart() + 1);

        // Update the preferences with the modified list
        tinyDB.putListObject("CartList", listItem);

        // Notify the listener that the number of items has changed
        changeNumberItemsListener.change();
    }
    public void clearCart(){
        tinyDB.remove("CartList");
    }
}
