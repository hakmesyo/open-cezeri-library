package cezeri.search.shortest_path.jump_point_search;

import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class PixelDraw {
	private final double xMax, yMax;
	private BufferedImage buffIm;
	private WritableRaster wr;
	
	public PixelDraw(float xM, float yM){
		xMax = xM;
		yMax = yM;
		buffIm = new BufferedImage((int)xMax, (int)yMax, BufferedImage.TYPE_INT_RGB);
		wr = buffIm.getRaster();
	}
	
	public void drawPOI(int x, int y, int[] color){
		int[][] adjacents = {{x+1,y},{x-1,y},{x,y+1,},{x,y-1},
							 {x+1,y+1},{x-1,y-1},{x+1,y-1},{x-1,y+1},
							 {x+2,y+1},{x-2,y-1},{x+2,y-1},{x-2,y+1},
							 {x+2,y+2},{x-2,y-2},{x+2,y-2},{x-2,y+2}};
		for (int i=0;i<adjacents.length;i++){
			try{
				drawPixel(adjacents[i][0], adjacents[i][1], color);
			}
			catch (Exception e){
			}
		}
	}
	
	public void drawPixel(float x, float y, int[] color){
		wr.setPixel((int)x,(int)y,color);
	}
	
	public void picPrint(String name){
		try {
			ImageIO.write(buffIm, "png", new File(name+".png"));
		}
		catch (IOException e){
			System.err.println("Image Not Saved.");
		}
	}
	
	public void drawLine(int x, int y, int toX, int toY, int[] color){
		if (y==toY){ //straight lines
			if (x<toX){
				while (x!=toX){
					drawPixel(x,y,color);
					x++;
				}
			}
			else{
				while (x!=toX){
					drawPixel(x,y,color);
					x--;
				}
			}
		}
		else if (x==toX){
			if (y<toY){
				while (y!=toY){
					drawPixel(x,y,color);
					y++;
				}
			}
			else{
				while (y!=toY){
					drawPixel(x,y,color);
					y--;
				}
			}
		}
		else if (x<toX){ //diagonals
			if (y<toY){
				while (x!=toX && y!=toY){
					drawPixel(x,y,color);
					y++;
					x++;
				}
			}
			else{
				while (x!=toX && y!=toY){
					drawPixel(x,y,color);
					y--;
					x++;
				}
			}
		}
		else{
			if (y<toY){
				while (x!=toX && y!=toY){
					drawPixel(x,y,color);
					y++;
					x--;
				}
			}
			else{
				while (x!=toX && y!=toY){
					drawPixel(x,y,color);
					y--;
					x--;
				}
			}
		}
	}
}
