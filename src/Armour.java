/*
 * Armour class
 * 
 * Set amount of damage certain armour can take from specific elements
 */


public class Armour {
	int takeDamageBase = 0;
	float takeDamageFire = 0.0f;
	float takeDamageFrost = 0.0f;
	float takeDamageLightning = 0.0f;
	float takeDamageEthert = 0.0f;
	float takeDamageMayth = 0.0f;
	float takeDamageBes = 0.0f;
	int health = 0; //maybe change to float?
	
	
	public Armour(int takeDamageBase, float takeDamageFire, float takeDamageIce,
			float takeDamageLightning, float takeDamageEthert, float takeDamageMayth,
			float takeDamageBes, int health) {
		this.takeDamageBase = takeDamageBase;
		this.takeDamageFire = takeDamageFire;
		this.takeDamageFrost = takeDamageIce;
		this.takeDamageLightning = takeDamageLightning;
		this.takeDamageEthert = takeDamageEthert;
		this.takeDamageMayth = takeDamageMayth;
		this.takeDamageBes = takeDamageBes;
		this.health = health;
	}
	
}
