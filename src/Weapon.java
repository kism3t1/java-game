import java.awt.geom.AffineTransform;


public class Weapon extends Halja {
	
	private AffineTransform transform;
	private boolean attacking = false;
	
	private int damageBase = 0;
	private float damageFire = 0.0f;
	private float damageFrost = 0.0f;
	private float damageLightning = 0.0f;
	private float damageEthert = 0.0f;
	private float damageMayth = 0.0f;
	private float damageBes = 0.0f;
	
	private boolean thrown = false;
	
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
	
	public int getDamageBase() {
		return damageBase;
	}

	public float getDamageFire() {
		return damageFire;
	}

	public float getDamageFrost() {
		return damageFrost;
	}

	public float getDamageLightning() {
		return damageLightning;
	}

	public float getDamageEthert() {
		return damageEthert;
	}

	public float getDamageMayth() {
		return damageMayth;
	}

	public float getDamageBes() {
		return damageBes;
	}

	public boolean isThrown() {
		return thrown;
	}
	
	public boolean isAttacking(){
		return attacking;
	}
	
	public AffineTransform getTransform(){
		return transform;
	}
	
	public void attack(int direction){
		transform = new AffineTransform();
		attacking = true;
		if(thrown){
			//TODO add thrown weapon code
		}else{
			
			for(int rotation = 0; rotation < 180; rotation++){
				transform.rotate(Math.toRadians(rotation));
			}
		}
		attacking = false;
	}
	
	private class Attack implements Runnable{
		
		public Attack(){
			transform = new AffineTransform();
		}

		@Override
		public void run() {
			attacking = true;
			if(thrown){
				//TODO add thrown weapon code
			}else{
				
				for(int rotation = 0; rotation < 180; rotation++){
					transform.rotate(Math.toRadians(rotation));
				}
			}
			attacking = false;
		}
	}
	
}
