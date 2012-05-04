import java.awt.image.RescaleOp;

//PROOF OF CONCEPT

// Need to get the scale and offsets right

public class DayCycle {
	
	private static long dLast;
	private static int x;
	
	static float scaledown = 0.9f;
	static float offsetdown = 0.1f;
	
	static float scaleup = 1.3f;
	static float offsetup = 0.9f;
	
	public static void runSunset(){
			if (System.currentTimeMillis() - dLast > 500) {
		RescaleOp rescaleOp = new RescaleOp(scaledown, offsetdown, null);
		rescaleOp.filter(JavaGame.tileSkins[0], JavaGame.tileSkins[0]);
		rescaleOp.filter(JavaGame.tileSkins[1], JavaGame.tileSkins[1]);
		rescaleOp.filter(JavaGame.tileSkins[2], JavaGame.tileSkins[2]);
		rescaleOp.filter(JavaGame.tileSkins[3], JavaGame.tileSkins[3]);
		rescaleOp.filter(JavaGame.entitySkins[0], JavaGame.entitySkins[0]);
		rescaleOp.filter(JavaGame.enemySkins[0], JavaGame.enemySkins[0]);
		rescaleOp.filter(JavaGame.enemySkins[1], JavaGame.enemySkins[1]);
		rescaleOp.filter(JavaGame.enemySkins[2], JavaGame.enemySkins[2]);
		System.out.println("Sun Setting");
		dLast = System.currentTimeMillis();
		}
	}
		
	
	public static void runSunrise(){
			if (System.currentTimeMillis() - dLast > 500) {
		RescaleOp rescaleOp = new RescaleOp(scaleup, offsetup, null);
		rescaleOp.filter(JavaGame.tileSkins[0], JavaGame.tileSkins[0]);
		rescaleOp.filter(JavaGame.tileSkins[1], JavaGame.tileSkins[1]);
		rescaleOp.filter(JavaGame.tileSkins[2], JavaGame.tileSkins[2]);
		rescaleOp.filter(JavaGame.tileSkins[3], JavaGame.tileSkins[3]);
		rescaleOp.filter(JavaGame.entitySkins[0], JavaGame.entitySkins[0]);
		rescaleOp.filter(JavaGame.enemySkins[0], JavaGame.enemySkins[0]);
		rescaleOp.filter(JavaGame.enemySkins[1], JavaGame.enemySkins[1]);
		rescaleOp.filter(JavaGame.enemySkins[2], JavaGame.enemySkins[2]);
		System.out.println("Sun Rising");
		dLast = System.currentTimeMillis();
		}
	}
	
	public static void runNight(){
			if (System.currentTimeMillis() - dLast > 500) {
		RescaleOp rescaleOp = new RescaleOp(scaledown, offsetdown, null);
		rescaleOp.filter(JavaGame.tileSkins[0], JavaGame.tileSkins[0]);
		rescaleOp.filter(JavaGame.tileSkins[1], JavaGame.tileSkins[1]);
		rescaleOp.filter(JavaGame.tileSkins[2], JavaGame.tileSkins[2]);
		rescaleOp.filter(JavaGame.tileSkins[3], JavaGame.tileSkins[3]);
		rescaleOp.filter(JavaGame.entitySkins[0], JavaGame.entitySkins[0]);
		rescaleOp.filter(JavaGame.enemySkins[0], JavaGame.enemySkins[0]);
		rescaleOp.filter(JavaGame.enemySkins[1], JavaGame.enemySkins[1]);
		rescaleOp.filter(JavaGame.enemySkins[2], JavaGame.enemySkins[2]);
		System.out.println("Night Time");
		dLast = System.currentTimeMillis();
		}
	}
}
