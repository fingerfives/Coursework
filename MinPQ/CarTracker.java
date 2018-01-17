/**
 * An application that allows efficient lookup and sorting of data
 * using a Minimum Priority Queue, Binary Search Tree and Quick Sort
 * @author Joshua Massey
 * 
 */
import java.util.*;
import java.io.*;

public class CarTracker{

  public static Scanner input = new Scanner(System.in);
  
  //a collection of all cars, sorted by VIN number
  public static Key[] listings = new Key[10];
  public static Car[] cars = new Car[10];
  public static int numCars = 0;

  public static MinPQ pricePQ = new MinPQ("price");
  public static MinPQ milesPQ = new MinPQ("miles");



  public static void main(String[] args){

    int choice = 0;

    while(choice != 8){
    System.out.println("Would you like to:\n1) Add a car\n2) Update a car\n3) Remove a car from consideration" +
    "\n4) Retrieve the lowest price car\n5) Retreive the lowest mileage car\n6) Retrieve the lowest priced car by make and model\n" +
    "7) Retrieve the lowest mileage car by make and model");
    choice = input.nextInt();

    while(choice >= 7 && choice == 0){
      System.out.println("Would you like to:\n1) Add a car\n2) Update a listing\n3) Remove a car from consideration" +
      "\n4) Retrieve the lowest priced car\n5) Retreive the lowest mileage car\n6) Retrieve the lowest priced car by make and model\n" +
      "7) Retrieve the lowest mileage car by make and model");
     choice = input.nextInt();
    }

    if (listings.length == numCars) {
      listings = Arrays.copyOf(listings, numCars * 2);
    }



    if (choice == 1) {
      Car addedCar = addCar();
      int index;
      index = pricePQ.insert(addedCar);
      listings[numCars].setPIndex(index);
      index = milesPQ.insert(addedCar);
      listings[numCars].setMIndex(index);
      
      //Keeping the array sorted now allows for searching in logarithmic time
      quickSort(listings, 0, numCars);

    } else if (choice == 2) {
      updateCar();

    } else if (choice == 3){

      System.out.println("What is the vehicle's VIN: ");
      String vinnum = input.next();
      //removeCar(vinnum);
    }
      else if (choice == 6){

      System.out.println("What is the vehicle's make: ");
      String make = input.next();
      System.out.println("What is the vehicle's model: ");
      String model = input.next();
      Car lw = pricePQ.getMin(make, model);

      System.out.println("The lowest price vehicle is a:\n");
      System.out.println(lw.getMake() + " " + lw.getModel() + "\nPrice: $" + lw.getPrice() +
          "\nMiles: " + lw.getMiles() + " miles\nColor: " + lw.getColor());

    } else if (choice == 7){

      System.out.println("What is the vehicle's make: ");
      String make = input.next();
      System.out.println("What is the vehicle's model: ");
      String model = input.next();
      Car lw = milesPQ.getMin(make, model);

      System.out.println("The lowest price vehicle is a:\n");
      System.out.println(lw.getMake() + " " + lw.getModel() + "\nPrice: $" + lw.getPrice() +
          "\nMiles: " + lw.getMiles() + "miles\nColor: " + lw.getColor());

    } else if(choice == 4){
      Car p = pricePQ.getMin();
      System.out.println("The lowest price vehicle is a:\n");
      System.out.println(p.getMake() + " " + p.getModel() + "\nPrice: $" + p.getPrice() +
        "\nMiles: " + p.getMiles() + " miles\nColor: " + p.getColor());
    } else if (choice == 5){
      Car m = milesPQ.getMin();
      System.out.println("The lowest price vehicle is a:\n");
      System.out.println(m.getMake() + " " + m.getModel() + "\nPrice: $" + m.getPrice() +
        "\nMiles: " + m.getMiles() + " miles\nColor: " + m.getColor());
    }

  }

  }

/**
 * Collects data for the Car object
 * @return a new Car object
 */
  public static Car addCar(){

    Key k = new Key();

    Car newCar = new Car();
    System.out.println("Enter the car's VIN number:");
    String vin = input.next();
    k.setVIN(vin);
    newCar.setVIN(vin);
    System.out.println("Enter the vehicle's make:");
    String mk = input.next();
    System.out.println("Enter the vehicle's model:");
    String md = input.next();
    newCar.setMake_Model(mk, md);
    System.out.println("Name the price of your " + md + ":");
    double mn = input.nextDouble();
    newCar.setPrice(mn);
    System.out.println("How many miles are on your " + md + ":");
    double ml = input.nextDouble();
    newCar.setMiles(ml);
    System.out.println("What is the " + md + "'s color:");
    String cl = input.next();
    //the user is able to enter integer values ( should not be a thing )
    //implement a method
    newCar.setColor(cl);
    numCars++;
    listings[numCars] = k;
    return newCar;

  }

  /**
   * Updates the data associated with a specific Car object
   */
  public static void updateCar(){
    System.out.println("Enter the car's VIN:");
    String vin = input.next();
    Car c = null;
    int pqIndex = 0;

    int bsIndex = binarySearchKey(listings, vin, 0, numCars);

    if (bsIndex != -1) {

        System.out.println("What would you like to update:\n1) Price\n2) Mileage\n3) Color");
        int choice = input.nextInt();

        if (choice == 1) {
            System.out.println("What is the new price of the vehicle:");
            double p = input.nextDouble();
            Key k = listings[bsIndex];
            pqIndex = k.getPIndex();
            c = pricePQ.getCar(pqIndex);
            c.setPrice(p);

          } else if (choice == 2) {
            System.out.println("Enter the vehicle's updated mileage:");
            double m = input.nextDouble();
            bsIndex = binarySearchKey(listings, vin, 0, numCars);
            Key k = listings[bsIndex];
            pqIndex = k.getMIndex();
            c = milesPQ.getCar(pqIndex);
            c.setMiles(m);
              
          } else if (choice == 3) {
            System.out.println("What is the vehicle's new color: ");
            String cl = input.next();
            c.setColor(cl);
          }
    } else {
      System.out.println("A vehicle with that VIN does not exist.");
    }

  //int newIndex = milesPQ.update(c);
  //listings[pqIndex].setMIndex(newIndex);
  //newIndex = pricePQ.update(c);
  //listings[pqIndex].setPIndex(newIndex);
}


/*public static void remove(String vin){

  int index = binarySearchKey(listings, vinnum, 0, numCars);
  if (bsIndex != -1) {

    Key k = listings[bsIndex];
    int pqIndex = k.getPIndex();
    pricePQ.remove(pqIndex);
    pqIndex = k.getMIndex();
    milesPQ.remove(pqIndex);
    numCars--;
  } else {
    System.out.println("A vehicle with that VIN does not exist.");
  }*/








/**
 * Keeps the array of Key objects sorted by VIN number
 * @param a an array of Key objects
 * @param start the index in which sorting begins
 * @param end the index indicating the end of the array
 */
public static void quickSort(Key[] a, int start, int end){

  int i = start;
  int j = end;

  if (j - i >= 1){

    String pivot = a[i].getVIN();

    while (j > i){

      while (a[i].getVIN().compareTo(pivot) <= 0 && i < end && j > i){
        i++;
      }

      while (a[j].getVIN().compareTo(pivot) >= 0 && j > start && j >= i){
        j--;
      }

      if (j > i)
      swap(a, i, j);
    }

    swap(a, start, j);
    quickSort(a, start, j - 1);
    quickSort(a, j + 1, end);
  }
}

/**
 * Swaps two items in the array
 * @param a an array of Key objects
 * @param i the smaller of the two indices
 * @param j the larger of the two indices
 */
public static void swap(Key[] a, int i, int j){
  Key temp = a[i];
  a[i] = a[j];
  a[j] = temp;
}





/**
 * Performs a binary search on the Car PQ
 * @param cars an array of Car objects
 * @param value the Car's price
 * @param min the minimum index to begin searching
 * @param max the maximum index to begin searching
 * @param type indicates whether the PQ is sorted by price or miles
 * @return the index of the matching Car object within the PQ
 */
public static int binarySearchPQ(Car[] cars, double value, int min, int max, String type) {

    if (min > max) {
        return -1;
    }

    if(type.equals("price")){
      int mid = (max + min) / 2;
      double midPrice = cars[mid].getPrice();
      if (midPrice == (value)) {
          return mid;
      } else if(midPrice  > value) {
          return binarySearchPQ(cars, value, min, mid - 1, type);
      } else {
          return binarySearchPQ(cars, value, mid + 1, max, type);
      }
    } else {
      int mid = (max + min) / 2;
      double midMiles = cars[mid].getMiles();
      if (midMiles == (value)) {
          return mid;
      } else if(midMiles > value) {
          return binarySearchPQ(cars, value, min, mid - 1, type);
      } else {
          return binarySearchPQ(cars, value, mid + 1, max, type);
      }
    }
}


/**
 * Performs a binary search on the array of Key objects
 * @param vins an array of Key objects
 * @param value the VIN we are searching for
 * @param min the minimum index to search
 * @param max the maximum index to search
 * @return the index of the matching VIN value within the array
 */
public static int binarySearchKey(Key[] vins, String value, int min, int max) {

    if (min > max) {
        return -1;
    }


    int mid = (max + min) / 2;

    if (vins[mid].getVIN().equals(value)) {
        return mid;
    } else if(vins[mid].getVIN().compareTo(value) > 0) {
        return binarySearchKey(vins, value, min, mid - 1);
    } else {
        return binarySearchKey(vins, value, mid + 1, max);
    }
}


}




/**
 * Each Key object contains data for a single Car object
 * @author Joshua Massey
 */
class Key{

  private static String vin;
  private static int pindex;
  private static int mindex;

  /**
   * Sets the VIN value from the related Car object
   * @param v the VIN value
   */
  public static void setVIN(String v){
    vin = v;
  }

  /**
   * Sets the index of the Car object within the Price PQ
   * @param i the index of the Car in the Price PQ
   */
  public static void setPIndex(int i){
    pindex = i;
  }

  /**
   * Sets the index of the Car object within the Miles PQ
   * @param i the index of the Car in the Miles PQ
   */
  public static void setMIndex(int i){
    pindex = i;
  }

  /**
   * Sends the VIN value back to the client
   * @return the Car's VIN value
   */
  public static String getVIN(){
    return vin;
  }

  /**
   * Sends the Price PQ index back to the client
   * @return the index of the Car within the Price PQ
   */
  public static int getPIndex(){
    return pindex;
  }

  /**
   * Sends the Miles PQ index back to the client
   * @return the index of the Car within the Miles PQ
   */
  public static int getMIndex(){
    return mindex;
  }
}









































/**
 * The Minimum PQ used to store and sort Cars
 * @author Joshua Massey
 */
class MinPQ{
  private static int capacity;
  private static int n = 0;
  private static Car[] carPQ;
  private static String type;

  /**
  *initializes a new PQ of size 10
  *@param type indicates whether we should sort by miles or price
  */
  MinPQ(String type){
    carPQ = new Car[10];
    this.type = type;
    capacity = 10;
  }

/**
 * Inserts a new Car object into the PQ, resizing if necessary
 * @param c a Car Object
 * @return the Car's location within the PQ
 */
  public static int insert(Car c){
    if(capacity == carPQ.length - 1) carPQ = Arrays.copyOf(carPQ, capacity * 2);
    //there is nothing at index 0 of any PQ
    capacity += 1;
    carPQ[n] = c;
    n += 1;
    int index = swim(n);
    return index;
  }

/**
 * looks up a Car at a specific index
 * @param index the location of the Car within the PQ
 * @return the Car object
 */
  public static Car getCar(int index){
    return carPQ[index];
  }

  /**
  * Updates the Car's index after data in it's Key object is mutated
  * @param c the car with updated information
  * @return the Car's updated index location
  */
  public static int update(Car c){
    carPQ[0] = c;
    int q = sink(0);
    swim2(n);
    return q;
  }

/**
 * Gets the first element in the PQ
 * @return the Car object with the lowest value
 */
  public static Car getMin(){
    return carPQ[1];
  }


  /**
   * Gets the first Car matching the search requirements
   * @param make the make of the Car (i.e. Toyota)
   * @param model the model of the Car (i.e. Corolla)
   * @return the first Car matching the search requirements
   */
  public static Car getMin(String make, String model){
    Car c = null;

    for (int i = 0; i < n; i++) {
      if (carPQ[i].getMake().equals(make) && carPQ[i].getModel().equals(model)) {
        return carPQ[i];
      }
    }


    return c;
  }


  /*public static void remove(int index){
    carPQ[index] = null;
    carPQ[0] =  carPQ
    int newIndex = sink(0);
    n--;
    carPQ
  }*/


  public static int swim(int n){
    while(n > 1 && less(n/2, n)){
      exch(n/2, n);
      n = n/2;
    }
    return n;
  }


  public static void swim2(int n){
    while(n > 1 && less(n/2, n)){
      exch(n/2, n);
      n = n/2;
    }
  }


  public static int sink(int n){
    while(2*n < capacity){
      int j = 2 * n;
      if(j < capacity && less(j, j+1)) j = j+1;
      if(less(j,n)) break;
      exch(n,j);
      n = j;
    }
    return n;
  }

  public static boolean less(int i, int j){
    if(type.equals("miles")){

      if(carPQ[i].getMiles() < carPQ[j].getMiles()){
        return true;
      } else {
        return false;
      }

    } else {

      if(carPQ[i].getPrice() < carPQ[j].getPrice()){
        return true;
      } else {
        return false;
      }
    }

  }


  public static void exch(int i, int j){
      Car v = carPQ[i];
      carPQ[i] = carPQ[j];
      carPQ[j] = v;
  }




}

































/**
 * The Car class that stores information about its make, model, etc
 * @author Joshua Massey
 */
  class Car{

    public static String vin;
    public static String make;
    public static String model;
    public static String color;
    public static double price;
    public static double miles;

    public static void setVIN(String vin_num){
      vin = vin_num;
    }

    public static void setMake_Model(String brand, String kind){
      make = brand;
      model = kind;
    }

    public static void setColor(String iro){
      color = iro;
    }

    public static void setMiles(double distance){
      miles = distance;
    }

    public static void setPrice(double cost){
      price = cost;
    }

    public static String getVIN(){
      return vin;
    }

    public static String getMake(){
      return make;
    }

    public static String getModel(){
      return model;
    }

    public static double getPrice(){
      return price;
    }

    public static double getMiles(){
      return miles;
    }

    public static String getColor(){
      return color;
    }
  }
