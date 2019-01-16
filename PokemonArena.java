/**
Victoria Ren
main class with main method
handles user interaction, flow of game
Here in this class, everything concerning the battle itself and how the battle flows is defined
This is where the game magic happens

 */
import java.util.*;
import java.io.*;
public class PokemonArena {
	//arraylists: allPokes contains all of the pokes, chosenPokes contains the users four chosen Pokes, enemyPokes contains all pokes- chosen pokes
	private static ArrayList<Pokemon> allpokes;
	private static ArrayList<Pokemon> chosenPokes;
	private static ArrayList<Pokemon>enemyPokes;

	private static Pokemon goodPoke, badPoke; //there will always be a good pokemon vs a bad pokemon
	private static int action= 0; //similar to red/black in checkerboard so if the action is 1 then attack, if 2, retreat; 3 = pass
	
	private static int turn= 1; //
	private static int enemy=0; // for taking turns to attack
	private static int good=1;  //
	private static int choice= 1; //which attack the good pokemon decides to use
	private static int numAtt; //which attack bad poke uses
	
	private static String trainer=""; //global variable for the user's name just for some user interaction
	
	public static void main(String [] args){
		allpokes= new ArrayList<Pokemon>(); //redefine so previous data is refreshed
		chosenPokes= new ArrayList<Pokemon>();
		enemyPokes= new ArrayList<Pokemon>();
		//call on each of the methods needed for the program to run smoothly in the order it should
		intro(); //cute little intro
		loadPokes();
		displayPokes();
		choosePokes();
		setEnemyPokes();
		
		System.out.println("PRESS ENTER TO CONTINUE");
		Scanner kb= new Scanner(System.in);
		kb.nextLine();
		
		//the while loop keeps the game running, battles will continue to happen until one side (good vs bad) is gone and defeated
		while(enemyPokes.size()>0 && chosenPokes.size()>0){ 
			battle();
		}
		
		if(enemyPokes.size()==0){ //once all the enemy pokes are gone and defeated
			declareWinner(); //game ends, good guys win
		}
		
		if(chosenPokes.size()==0){ //if good guys all defeated
			motivateDefeatedTrainer(); 
		}
	}
	
	//this function is simple, just a sweet little hello to make user interaction cooler
	public static void intro(){
		System.out.println("Enter your trainer name: \n");
		Scanner kb= new Scanner(System.in);
		trainer = kb.nextLine();
		System.out.println("Why hello " + trainer +"! In our world the land is filled with all sorts of marvelous and interesting creatures we call Pokemon!");
		System.out.println("In this simulation of engaging in various Pokemon battles, you'll be dealing with 28 unique Pokemons, four of which you can");
		System.out.println("recruit to fight for you! Good luck and have fun " + trainer +"!");
		
		System.out.println("PRESS ENTER TO CONTINUE"); //style points
		kb.nextLine();
	}
	
	//this function loads all of the data with regards to each pokemon
	public static void loadPokes(){
		try{
			Scanner inFile= new Scanner(new BufferedReader(new FileReader ("pokemon.txt"))); //open the file
			inFile.nextLine(); // dump that 28th line
			while(inFile.hasNextLine()){
				// if the file has another line keep going
				allpokes.add( new Pokemon(inFile.nextLine())); //add to allPokes to get a list with all pokes
				
			}
			inFile.close(); //done
		}
		catch(IOException ex){ //just in case something didn't work
			System.out.println("Victoria, did you forget your data file???");
		}
	}
	
	//this function creates an awesome and very nice looking table showing the stats of each pokemon so the user can see
	//stats include the name, type, resistence, weakness, hp
	public static void displayPokes(){
		//loads the pokemon table with full stats on the pokemon for the user
		System.out.println("\t\t\t\t\t\t\t********          PICK YOUR FOUR POKEMON          ********\t\n"); //fancy

		for(int i=0; i<allpokes.size(); i++){ //more fancy formatting 
			System.out.println("========================================================================================================================");
			
			if((i+1)<10){ //this is for numbering off each pokemon, since 1 and 11 have different numbers of digits all the formatting is ruined by that one extra space
			//style points
				System.out.print((i+1)+".  ");	
			}
		
			if((i+1)>=10){
				System.out.print((i+1)+". ");
			}
			System.out.println(allpokes.get(i)); //print those stats
		}
		System.out.println("========================================================================================================================");	
	}		
	
	//the user can choose four out of 28 Pokemons to be on their team and fight
	public static void choosePokes(){
		System.out.println("Enter four choices " + trainer + ": \n");

		for(int i = 0; i<4;i++){
			System.out.println("Enter pokemon number "+(i+1)+": ");
			Scanner kb= new Scanner(System.in);
			int pokenum= kb.nextInt(); //which poke they choose
			if(chosenPokes.contains(allpokes.get(pokenum-1))){ //they can only choose a pokemon once, so you cannot have four Gyrados 
				System.out.println("You already chose that pokemon. Please choose another: ");
				i-=1; //go back a step
			}
			else{
				chosenPokes.add(allpokes.get(pokenum-1));//list starts at 1
			}
			
		}
	}
	
	//sets apart the user's chosen pokes from the remaining pokemon that weren't chosen as ENEMIES
	public static void setEnemyPokes(){
		for(int i=0; i<allpokes.size(); i++){
			if(chosenPokes.contains(allpokes.get(i))){
				continue; //if chosen, ignore it
			}
			else{
				enemyPokes.add(allpokes.get(i));
			}
		}
		Collections.shuffle(enemyPokes);//battles are randomized!!

	}	
	
	//this function is the whole battle between a goodpoke and a badpoke, CRUCIAL THIS IS THE GAME BASICALLY
	//based on a random turn creator, if the pokemon has hp, and exists then the good poke and badpoke will take turns battling eachother
	public static void battle(){ 
		badPoke=enemyPokes.get(0); //take the first element in badPokes, enemyPokes is shuffled so each time it would be different
		
		System.out.println("\nA wild "+badPoke.getName()+" has appeared!");	//style points	
		pickBattlePoke(); //calls the function so user can choose which poke to fight 
		int turn = (int)(Math.random()*2); //turns are randomized, good or bad can go first
		
		while(chosenPokes.size()>0 && badPoke.getHp()>0){ //while good and bad exists alive
			if(turn==0 && badPoke.getStun()==false){//enemy goes
				badTurn(); //function called for the bad turn to happen
				
			}
			else{
				if(goodPoke.getStun()==false){ //if they are stunned they can't attack
					chooseAction(); //poke gets to choose what to do
				}
			
			goodTurn(); //function for what happens in a good turn
				
			}
			if(goodPoke.getHp()<=0 && chosenPokes.size()>0){
				ripGoodPoke(); //if it faints
			}
			turn = 1-turn; //rotate turns
			
			if(badPoke.getDisable()== true){ //function defined in pokemon class, if badpoke is disabled
				badPoke.fixDisable();// must fix the disable, so the battle is not always affected, disable only lasts until the battle is over
			}
			if(goodPoke.getDisable()==true){ //same as above
				goodPoke.fixDisable();
			}
			restoreEnergy();	//each turn the energy is restored by 10
		}
		restoreHp(); //after each round, the hp is restored by 20
		
		
		if(badPoke.getHp()<=0){ //if badpoke has fainted
			enemyPokes.remove(0); //REMOVE FROM ENEMY POKES, spot[0] is now a new poke to fight
			System.out.println("Nice job "+ trainer +"!");
		}
	}
	
	//once a goodpoke is defeated, take it out of chosen pokes list, and choose a new goodPoke
	public static void ripGoodPoke(){
		if(goodPoke.getHp()<=0){
			System.out.println("Well done trainer! Your "+goodPoke.getName()+" has done well.");
			chosenPokes.remove(goodPoke); //remove it, it's done
			if(chosenPokes.size()>0){ //if there are still goodPokes
				System.out.println("Choose another pokemon to take "+goodPoke.getName()+"'s place!");
				pickBattlePoke(); //choose a new pokemon
			}
			
		}
	}
	
	//user chooses which of their pokemons they want to use, the chosen poke 
	//becomes the goodPoke at the time 
	public static void pickBattlePoke(){
		//display available pokes
		System.out.println("\nYour Pokemons:\n"); 
		for(int i=0; i<chosenPokes.size(); i++){
			System.out.println("Pokemon "+(i+1)+" : "+chosenPokes.get(i));
		}
		System.out.println("\nChoose one of your pokemons to fight with:\n");
		Scanner kb= new Scanner(System.in);
		int choicePoke= kb.nextInt();
		//says __name__ i choose you!
		System.out.println((chosenPokes.get(choicePoke-1)).getName()+", I choose you!");
		goodPoke= chosenPokes.get(choicePoke-1); //choice of poke becomes goodPoke	
	}
	
	//a poke can attack, pass, or retreat. This function allows the user to choose an action
	public static void chooseAction(){
		System.out.println("Your turn: Choose an action:\n1 = ATTACK\n2 = RETREAT\n3 = PASS");
		Scanner kb= new Scanner(System.in);
		action= kb.nextInt();
		//action is a global variable, so the goodPoke's choice of action
		//can be used in other functions based on what they choose
		if(action== 1){
			chooseAttack(); //good guys get to choose what attack to use, refers to function
			
		}
		if(action ==2){
			System.out.println("Your pokemon RETREATED from the battle");
		}
		if(action== 3){
			System.out.println("Your pokemon PASSES it's turn");
		}	
	}
	
	//displays all the chosen good poke's attacks, and allows the goodPoke to choose which one to use
	public static void chooseAttack(){
		System.out.println(goodPoke.getName()+" , choose which attack to use:");

		for( int i= 0; i<goodPoke.getNumAtt(); i++){ //outputs what attacks the pokemon has available to use
			System.out.println((i+1)+": "+goodPoke.toAttack(i)); //there's a function similar to toString() in Pokemon
		}

		Scanner kb= new Scanner(System.in);
		choice= kb.nextInt(); //defined as a global variable, to be used in battle
			
	}
	
	//function allows goodpoke to retreat, so they step down and the user can choose another poke from chosenPoke to fight
	public static void retreat(Pokemon p){
		System.out.println("Choose another pokemon to take the place of "+goodPoke.getName()+" : ");
		pickBattlePoke(); //pick new battlepoke
		turn=enemy; //enemy's turn
	}
	
	//this function goes over the bad Poke's turn, it chooses a random attack belonging to the enemy, and if the enemy
	//has enough energy, it attacks
	public static void badTurn(){
		int numAtt = (int)(Math.random()*badPoke.getNumAtt()); //generates random attack based on how many they have
		
		if(badPoke.getEnergy()>= badPoke.getECost(numAtt) && badPoke.getAttackSpecial(numAtt)!=("stun")){ //if they are stunned they cannot attack
			badPoke.attack(badPoke,goodPoke,numAtt); //attack function defined in Pokemon class, it takes away hp from bad and energy from good 
		}
		
		if(badPoke.getDisable()==true){ //if disabled fix it, since they can only be disabled once
			badPoke.fixDisable();
		}
		if(badPoke.getStun()== true){ //if badpoke is stunned, automatically pass the bad poke's turn
			pass(badPoke);
			badPoke.fixStun(); //they are no longer stunned
		}
	}
	
	//similar to badTurn, the function goes over what can happen in a goodPoke's turn, based on what action they chose in chooseAction()
	public static void goodTurn(){
		if(action==1){ //if attack, action is a global variable based on choice of action in chooseAction()
		
			goodPoke.attack(goodPoke,badPoke,(choice-1)); //attack, choice refers to the global variable in chooseAttack()
		}
		if(action==2){
			retreat(goodPoke); //function defined
		}	
		if(action==3){
			pass(goodPoke);
		}
		
		if(goodPoke.getStun()== true){
			pass(goodPoke);
			goodPoke.fixStun(); //they are no longer stunned
		}
		if(goodPoke.getDisable()==true){ //undisable
			goodPoke.fixDisable();
		}
	}
	
	//function passes a pokemon's turn, so they don't do anything and the competitor goes again
	public static void pass(Pokemon p){ 
		if(turn==good){ 
			turn -=1; //skips the turn
		}
		else{
			turn+=1; //skips the turn
		}
		
	}
	
	//function restores 20hp to all the chosenPokes
	public static void restoreHp(){
		for(int i=0; i<chosenPokes.size();i++){
			chosenPokes.get(i).heal(); //heal is defined in Pokemon class, restore that hp
		}
	}
	
	//function restores 10 energy to ALL POKEMONS ATTACKING
	public static void restoreEnergy(){ //everybody gets +10 energy!
		for(int i=0; i<chosenPokes.size();i++){
			chosenPokes.get(i).energize();
		}
		badPoke.energize(); //energize defined in Pokemon
	}
	
	//if the user loses before all the enemy pokes are defeated, function outputs "there there it's okay" and game over
	public static void motivateDefeatedTrainer(){
		System.out.println("Great effort "+ trainer +"! You and your four Pokemon managed to defeat "+(24- enemyPokes.size())+ " enemy Pokemons! ");
		System.out.println("Oh well maybe next time :)");
	}
	
	//if user WINS and beats all enemy pokes, call this function which declares them as trainer supreme	
	public static void declareWinner(){
		System.out.println("********************************************************************************");
		System.out.println("CONGRATULATIONS "+ trainer + " YOU HAVE EARNED THE TITLE OF......................\n ***TRAINER SUPREME***");
		System.out.println("********************************************************************************");
	}	
	
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ THE FINAL BRACKET THAT ENDS IT ALL	

}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~