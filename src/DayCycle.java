import java.awt.image.RescaleOp;

//PROOF OF CONCEPT

/*	Need it to after 5 mins runCycledown 50 times to make it night then wait for 5 mins
	and runCycleup 50 times to bring it back into day
	
	I think I need to bring it out of the game loop and maybe use a timer & Thread?.....
*/

public class DayCycle {
	
	private static long dLast;
	private static int x;
	
	static float scaledown = 0.995f;
	static float offsetdown = .5f;
	
	static float scaleup = 1f;
	static float offsetup = 1f;
	
	public static void runCycledown(){
		if (System.currentTimeMillis() - dLast > 500) {
		RescaleOp rescaleOp = new RescaleOp(scaledown, offsetdown, null);
		rescaleOp.filter(JavaGame.tileSkins[0], JavaGame.tileSkins[0]);
		rescaleOp.filter(JavaGame.tileSkins[1], JavaGame.tileSkins[1]);
		rescaleOp.filter(JavaGame.tileSkins[2], JavaGame.tileSkins[2]);
		rescaleOp.filter(JavaGame.tileSkins[3], JavaGame.tileSkins[3]);
		rescaleOp.filter(JavaGame.tileSkins[4], JavaGame.tileSkins[4]);
		rescaleOp.filter(JavaGame.entitySkins[0], JavaGame.entitySkins[0]);
		rescaleOp.filter(JavaGame.enemySkins[0], JavaGame.enemySkins[0]);
		rescaleOp.filter(JavaGame.enemySkins[1], JavaGame.enemySkins[1]);
		rescaleOp.filter(JavaGame.enemySkins[2], JavaGame.enemySkins[2]);
		System.out.println("Sun Setting");
		dLast = System.currentTimeMillis();
			}
	}
		
	
	public static void runCycleup(){
		if (System.currentTimeMillis() - dLast > 500) {
		RescaleOp rescaleOp = new RescaleOp(scaleup, offsetup, null);
		rescaleOp.filter(JavaGame.tileSkins[0], JavaGame.tileSkins[0]);
		rescaleOp.filter(JavaGame.tileSkins[1], JavaGame.tileSkins[1]);
		rescaleOp.filter(JavaGame.tileSkins[2], JavaGame.tileSkins[2]);
		rescaleOp.filter(JavaGame.tileSkins[3], JavaGame.tileSkins[3]);
		rescaleOp.filter(JavaGame.tileSkins[4], JavaGame.tileSkins[4]);
		rescaleOp.filter(JavaGame.entitySkins[0], JavaGame.entitySkins[0]);
		rescaleOp.filter(JavaGame.enemySkins[0], JavaGame.enemySkins[0]);
		rescaleOp.filter(JavaGame.enemySkins[1], JavaGame.enemySkins[1]);
		rescaleOp.filter(JavaGame.enemySkins[2], JavaGame.enemySkins[2]);
		System.out.println("Sun Rising");
		dLast = System.currentTimeMillis();
		}
	}
}
