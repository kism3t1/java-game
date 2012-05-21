import java.awt.image.BufferedImage;
import java.io.Serializable;


public class Animation extends Halja implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7115057005618802362L;
	private BufferedImage img = null;
	private int totalFrames = 0;		//total frames contained in skin
	private int frameWidth = 0;			//width of each frame (in pixels)
	private int frameHeight = 0;		//height of each frame (in pixels)
	private int frameDelay = 0;			//amount of frames to process before moving to the next frame
	private int framesDelayed = 0;	//keeps track of how many frames have been delayed
	
	private int currentFrame = -1;		//current frame within skin (start at -1, next calculation will return 0)
	
	public Animation(BufferedImage img, int frameWidth, int frameHeight, int frameDelay){	//constructor for entity
		this.img = img;
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		
		totalFrames = (img.getWidth() / frameWidth) - 1; //must -1 as first frame is 0
	}
	
	public BufferedImage nextFrame(){
		if(framesDelayed == frameDelay){
			if(currentFrame == totalFrames){	//calculate next frame
				currentFrame = 0;
			}else{
				currentFrame += 1;
			}
			
			framesDelayed = 0;	//reset framesDelayed counter
			
			return img.getSubimage(currentFrame * frameWidth, 0, frameWidth, frameHeight); //get current frame to return
		}else{
			framesDelayed += 1;
		}
		return null;
	}
	
	public BufferedImage previousFrame(){
		if(framesDelayed == frameDelay){
			if(currentFrame == 0){	//calculate next frame
				currentFrame = totalFrames;
			}else{
				currentFrame -= 1;
			}
			
			framesDelayed = 0;	//reset framesDelayed counter
			
			return img.getSubimage(currentFrame * frameWidth, 0, frameWidth, frameHeight); //get current frame to return
		}else{
			framesDelayed += 1;
		}
		return null;
	}
	
	public int getWidth(){
		return frameWidth;
	}
	
	public int getHeight(){
		return frameHeight;
	}
	
	public int getFrameDelay(){
		return frameDelay;
	}
	
	public BufferedImage getImage(){
		return img;
	}
	
	public void setImage(BufferedImage img){
		this.img = img;
	}

}
