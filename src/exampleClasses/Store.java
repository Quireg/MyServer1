package exampleClasses;

import java.util.ArrayList;


public class Store {
    private String name;
    private ArrayList<Good> goods;

    public Store() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Good> getGoods() {
        return goods;
    }

    public void setGoods(ArrayList<Good> goods) {
        this.goods = goods;
    }
    public void sortByPrice(){
        for (int i = 0; i < goods.size() - 1; i++) {
            for (int j = i+1; j <  goods.size() ; j++) {
                if(goods.get(i).getPrice() < goods.get(j).getPrice()){
                    Good temp = new Good();
                    temp = goods.get(i);
                    goods.set(i, goods.get(j));
                    goods.set(j, temp);
                }


            }
        }
    }
    public void report(){
        System.out.println("Тип товара  Цена  Вес");
        for (int i = 0; i <goods.size() ; i++) {
            System.out.println(goods.get(i).getName() + " " + goods.get(i).getPrice() + " "
                    + goods.get(i).getWeight());

        }
        System.out.println("---------------------------------");
        int totalPrice = 0;
        for (int i = 0; i <goods.size() ; i++) {
            totalPrice += goods.get(i).getPrice();
        }
        System.out.println("Total price " + totalPrice);
        System.out.println("Total quantity " + goods.size());
        System.out.println("Average price " + (totalPrice/goods.size()));
    }

}
