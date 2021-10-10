package safeElevator;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SafeElevator {    

    //variables
    Scanner intel = new Scanner(System.in);
    ArrayList<Integer> listOfFloors; //unique to each floors. Some passangers enter the same floors. Store it as one.
    
    boolean isDoorOpen = false;
    
    final int maxPass = 10; //maxPass = maximum passengers
    final int maxFloors = 10; // maxFloors = maximum floors
    final int minPass = 1; //minPass = minimum passengers per elevator ride
    final int minFloors = 1; //minFloors = lowest floor in the building is first floor
    
    
    int curF = 1; //curF = Base floor for elevator autosets to the first(minimum) floor)
    int destF = 0; //destF = destination floor constant, before passenger enters floor number
    int passF = 0; //passenger floor asks for passengers floor 
    int passNum = 0; //number of passengers entered by passenger
        
    int[] destination_lists = new int[maxFloors]; 
    
    Timer t = new Timer();  
    TimerTask tt = new TimerTask() {  
    @Override  
    public void run() {  
       
    };  
};  
          
    void askPassenger(){
        isDoorOpen = false;
        println("Welcome to the Elevator! \n Enjoy your ride" );
        println("Doors opening... Come in!");
        
        long start = System.currentTimeMillis();
        isDoorOpen = true;
        print(curF + "F | How many passengers?: ");
        passNum = intel.nextInt();
        if(passNum < minPass || passNum > maxPass){
            println("Error! Allowed number of passengers is [1-10].");
            askPassenger();
        } else {
            listOfFloors = new ArrayList<>();
            for(int d = 0; d < passNum; d++){                
                int floor = askPassengerFloor(d);                    
                if(!listOfFloors.contains(floor)) listOfFloors.add(floor);
                }
            }              
        println("Doors closing...");
         
        long finish = System.currentTimeMillis();
        long timeElapsed = finish-start;
        println("Time taken " + timeElapsed);
            isDoorOpen = false;
            initialize_elevator(); //calls the elevator function
    }
    
    //ask each passenger for the floor they want to go to
    int askPassengerFloor(int id){
        boolean isValidEntry = false;
        int floor = 0;
        while(!isValidEntry){
            print("Passenger number: " + (id+1) + "\nEnter your destination floor: ");
            floor = intel.nextInt();
            if(floor < 0 || floor > maxFloors){
                println("Error! The floor you have entered does not exist. Valid floors[1-10]");
            } if (floor == 0){              
             t.scheduleAtFixedRate(tt, 500, 500);
            System.out.println("Elevator stopped at floor " + curF + " Doors open...");
            System.exit(0);
            } 
            else if(floor == curF){
                println("Error! You are already in the " +   curF   +  "Floor. ");
                        
            }
               else{
                destination_lists[floor-1]++;
                isValidEntry = true;
            }
        }
        return floor;
    }

   void initialize_elevator(){   
       long start = System.currentTimeMillis();
        for(int d = 0; d < listOfFloors.size(); d++){
            int shortest = findShortest();
            println("Next destination: " + shortest + " Floor| (" + destination_lists[shortest-1] + ") Passengers");
             
            while(curF < shortest) {
                up();
            }
           
            while(curF > shortest){
                down();
            }
            while(destination_lists[shortest-1] > 0){
                println("Unloading(" + destination_lists[shortest-1]-- + ") Passengers  at " +   curF   + "  Floor");
                
            }
            
        }        
        askPassenger();
        long finish = System.currentTimeMillis();
        long timeElapsed = finish-start;
        println("Time taken " + timeElapsed);
    }
    void up(){
        println(curF++ + "F| Going up...");
        
    }
    void down(){
        println(curF-- + "F | Going down...");
    }
    
    void println(Object o) {
        System.out.println(o);
    }
    void print(Object o) {
        System.out.print(o);
    }     
    //this algorithm solves for the shortest distance the elevator takes with all selected floors by the passengers. The shortest path = highest priority.
    int findShortest(){
        int shortest = Math.abs(curF - listOfFloors.get(0));
        int id = 0;
        for(int d = 1; d < listOfFloors.size(); d++){
            if(shortest > Math.abs(curF - listOfFloors.get(d))){
                shortest = Math.abs(curF - listOfFloors.get(d));
                id = d;
            }
        }
        shortest = listOfFloors.get(id);
        listOfFloors.set(id, 100);
        return shortest;
    }

    
}


