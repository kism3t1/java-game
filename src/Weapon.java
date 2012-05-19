
public class Weapon {
	
	int damageBase = 0;
	float damageFire = 0.0f;
	float damageFrost = 0.0f;
	float damageLightning = 0.0f;
	float damageEthert = 0.0f;
	float damageMayth = 0.0f;
	float damageBes = 0.0f;
	
	boolean thrown = false;
	
	public Weapon(int damageBase, float damageFire, float damageIce,
			float damageLightning, float damageEthert, float damageMayth,
			float damageBes, boolean thrown) {
		this.damageBase = damageBase;
		this.damageFire = damageFire;
		this.damageFrost = damageIce;
		this.damageLightning = damageLightning;
		this.damageEthert = damageEthert;
		this.damageMayth = damageMayth;
		this.damageBes = damageBes;
		this.thrown = thrown;
	}
	
}
