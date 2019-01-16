/**
Victoria Ren
Allows PokemonArena to be easier
Class contains all info pertaining to each and every pokemon, and what makes up a pokemon
anything about the pokemon itself is defined in this class

 */

import java.util.*;
import java.io.*;

class Pokemon {
	//global variables and fields
	private int hp,maxHP, numAttacks;
	private int maxEnergy=50; //all pokemons start with 50 energy
	private int energy=50;
	private String  name, type, resistance, weakness;
	private Attack[] attacks; //number of attacks a poke can use


	private int numDisable= 0; //can only get disabled once 
	private boolean stun=false; //automatically starts off as false, only true if stun/disable works
	private boolean disable= false;
	
	//contructor for pokemon, takes in data and information is assigned
	public Pokemon(String data){
		String []stats = data.split(","); //split to separate
		//setting up the info
		name = stats[0];
		hp = Integer.parseInt(stats[1]);
		maxHP = hp;
		type = stats[2];
		resistance = stats[3];
		weakness= stats[4];
		numAttacks = Integer.parseInt(stats[5]);  
		attacks =  new Attack[numAttacks]; //attacks defined earlier on, attack class takes the info in to create it's mini class of information
		for(int i=0; i<numAttacks; i++){
			String aName = stats [6+(i*4)]; //i*4 based on elements,every fourth element would be the aName, eCost, etc.
			int energyCost= Integer.parseInt(stats[7+ (i*4)]);
			int damage = Integer.parseInt(stats[8+(i*4)]);
			String special= stats[9+ (i*4)];
		
			attacks[i] = new Attack (aName, energyCost, damage, special); //a full attack with all the information
		
		}
			
	}
	
	//accesses whether stun (a global variable) is true/false	
	public boolean getStun(){ 
		return stun;
	}	
	
	//returns whether disable is true/false, disable is defined later on in attack function	
	public boolean getDisable(){
		return disable;
	}
		
	//returns a nice neat organization of stats regarding the pokemon, or simply puts in the info in String
	public String toString(){
		return String.format("%-15s~~~      hp:%-3d      ~~~      type:%-10s ~~~     resistance:%-11s~~~    weakness:%-11s",name,hp,type,resistance,weakness);	
	}
	
	//same as toString, returns all info about an attack neatly
	public String toAttack(int a){ //takes in specific attack
		return String.format("Attack name: %-10s\t\t Energy Cost: %-4d\t\t Damage: %-4d\t\t Special: %-10s", attacks[a].aName, attacks[a].energyCost, attacks[a].damage, attacks[a].special);	
		//attacks[a] refers to a spot/index in attacks, and the .aName,.energyCost, etc. are fields of the particular attack
	}
	
	//the following functions all return (public so it's accessible in pokemon arena) information about
	//the poke, or the poke's attack to make accessing information easier
	public int getNumAtt(){
		return numAttacks;
	}
	
	public int getEnergy(){
		return energy;
	}
	
	public String getAtName(int a){
		return attacks[a].aName;
	}
	
	public int getECost(int a){
		return attacks[a].energyCost;
	}
	
	public String getAttackSpecial(int a){
		return attacks[a].special;
	}
	
	public String getName(){
		return name; //only returns the pokemon name
	}
	
	public int getHp(){
		return hp;
	}
	
	//function sets hp to hp + 20 (adds 20 and heals the poke) if hp+ 20 does not exceed the poke's max hp
	public void heal(){
		hp = Math.min(maxHP, hp+20); //takes the min/lesser between the two
	}

	//function sets energy to e+10 (recharges energy by ten) if it doesn't exceed 50 (max energy)
	public void energize(){
		energy= Math.min(maxEnergy, energy+10); //take the min
	}
	
	//undos the stun affect, since stun is not permanent on a pokemon
	public void fixStun(){
		stun= false;
	}
	
	//undos the disable effect, since it is not permanent
	public void fixDisable(){
		disable= false;
	}
	
	//function takes 10 damage off attack of pokemon
	public void disablePoke(Attack a){
		a.damage-=10;
	}
	
	//the attack function manages everything about the attack itself, it subtracts hp from enemy poke based on good poke's attack damage
	//and subtracts attacking poke's energy based on attack energy cost, this function covers everything pertainging to an attack
	public void attack(Pokemon good, Pokemon enemy, int a){ //takes in 3 parameters, the good poke, opposing poke, and which attack is being used
		int numDam=attacks[a].damage; //how much damage is done
		
		boolean valid= true; //checks to see if attack can be done
		if(good.energy>= attacks[a].energyCost){
			valid= true; //must have enough energy or attack can't happen
		}
		else{
			System.out.println(good.name+" does not have enough energy for that attack!");
			valid= false;	
		}
		
		//stun special
		if(attacks[a].special.equals("stun")){
			Random rand= new Random(); //randomly generates true or false
			if(rand.nextBoolean()==false){
				System.out.println("Stun failed");
				
			}
			else{
				enemy.stun= true;
				System.out.println(enemy.name+" is stunned");
			}
		}
		
		//wild card--> 50/50 chance of it working
		if(attacks[a].special.equals("wild card")){
			Random rand = new Random(); //generates either true or false
			if(rand.nextBoolean()== false){ //if false is considered a failure
				numDam=0;
				System.out.println("Wild card failed :(");
				 // the attack doesn't do anything and no damage done
			}
			else{
				System.out.println("The wild card was successful!");
				numDam= attacks[a].damage; //works!
			}
		}
		
		//wild storm
		if(attacks[a].special.equals("wild storm")){
			Random rand= new Random(); //generates true/false
			boolean works= rand.nextBoolean();
			
			if(works= false){
				System.out.println("Wild storm unsuccessful");
				numDam= 0; //nothing happens!
			}
			
			else{
				numDam= attacks[a].damage;
				works= rand.nextBoolean();
				while(works== true){ //keeps running it over and over, so while true is still being generated
					System.out.println("Wild storm!");
					numDam += attacks[a].damage; //keep adding damage based on num of wildstorms 
					works= rand.nextBoolean(); //generate again
					
				}	
			}
			
		}
		
		//recharge special (+20 energy)
		if(attacks[a].special.equals("recharge")){
			Random rand= new Random(); //true/false generator
			Boolean works=rand.nextBoolean();
			if(works==true){
				System.out.println("\nEnergy recharged");
				good.energy= Math.min(maxEnergy, energy+20); //can't go over 50 energy
			}
		}
		
		//disable special makes the enemy's attacks do 10 less damage, pokemon can only be disabled once!
		if(attacks[a].special.equals("disable")){
			Random rand= new Random();
			if(rand.nextBoolean()==false){
				System.out.println("disable failed");
			}
			else{
				System.out.println("disable effect activated");
			}
			//enemy can only be disabled once
			if(enemy.numDisable<1 && disable == true){
				disablePoke(enemy.attacks[a-1]);
				enemy.numDisable=1; 
			}
		}
		
		//resistance/weakness
		if(good.type.equals(enemy.resistance) && valid == true){ 
			numDam/=2;//half damage
			System.out.println(enemy.name+" is resistant to " + good.name +"! Any attack damage is reduced by half.");
		}
		
		if(good.type.equals(enemy.weakness) && valid == true){
			numDam*=2; //double damage
			System.out.println(enemy.name+ " is severely weakened by " + good.name + "! Any attack damage is doubled.");
		}
		
		if(valid == true && stun == false){ //can't attack if stunned, must be valid
			System.out.println(good.name+" used "+ attacks[a].aName + "!\n"); //style
			enemy.hp-= numDam; //subtract hp/energy
			good.energy-= attacks[a].energyCost;
			System.out.println(good.name+" has "+good.energy+" energy remaining!\n");
			if(enemy.hp<=0){ //once it faints
				System.out.println(enemy.name+ " has fainted!");
			}
			if(enemy.hp>0){ //
				System.out.println(enemy.name+ " has "+enemy.hp+" HP remaining\n");
			}
		}
		else{
			valid= false; //no attack done
		}			
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	class Attack{//attack class part of pokemon, has a lot of its own info to it
		private String aName, special;
		private int energyCost, damage;
		
		//constructor for the attack class, with use of this function
		public Attack(String aName, int energyCost, int damage, String special){
			this.aName=aName;
			this.energyCost= energyCost;
			this.damage= damage;
			this.special= special;
		}
	}
}

//makes pokemon arena easier, contains things belonging to the pokemon
// read from file and printout corresponding info about pokemon indicated
