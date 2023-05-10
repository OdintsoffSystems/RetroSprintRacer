package com.odintsofftware.gameapi.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.odintsofftware.retrosprintracer.RetroSprintRacer;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ivan
 */
public class TiledLayer extends Layer {
	
    /**
     * the overall height of the TiledLayer grid
     */
    private int cellHeight; // = 0;
    /**
     * the overall cell width of the TiledLayer grid
     */
    private int cellWidth; // = 0;

    /**
     * The num of rows of the TiledLayer grid.
     */
    private int rows; // = 0;

    /**
     * the num of columns in the TiledLayer grid
     */
    private int columns; // = 0;

    /**
     * int array for storing row and column of cell
     *
     * it contains the tile Index for both static and animated tiles
     */
    private int[][] cellMatrix; // = null;

    /**
     * Source image for tiles
     */
    // package access as it is used by Pixel level Collision
    // detection with a Sprite
    Texture sourceTexture; // = null;

    /**
     * no. of tiles
     */
    private int numberOfTiles; // = 0;

    /**
     * Table to map from animated Index to static Index
     * 0th location is unused.
     * anim --> static Index
     * -1 --> 21
     * -2 --> 34
     * -3 --> 45
     * for now keep 0 the location of the table empty instead of computing
     * -index make index +ve and access this Table.
     *
     */
    private int[] anim_to_static; // = null;

    /**
     * total number of animated tiles. This variable is also used as
     * index in the above table to add new entries to the anim_to_static table.
     * initialized to 1 when table is created.
     */
    private int numOfAnimTiles; // = 0

	private TextureRegion[] textureSplit;
	
	int viewportWidth  = RetroSprintRacer.GAME_WIDTH;
    int viewportHeight = RetroSprintRacer.GAME_HEIGHT;

    public TiledLayer(int columns, int rows, int tileWidth,  int tileHeight, Texture texture) {
    	// IllegalArgumentException will be thrown
    	// in the Layer super-class constructor
            super(columns < 1 || tileWidth < 1 ? -1 : columns * tileWidth,
    	         rows < 1 || tileHeight < 1 ? -1 : rows * tileHeight);

            /* if texture is null texture.getWidth() will throw NullPointerException
            if (((texture.getWidth() % tileWidth) != 0) ||
                ((texture.getHeight() % tileHeight) != 0)) {
                 throw new IllegalArgumentException();
            }*/
            this.columns = columns;
            this.rows = rows;

            cellMatrix = new int[rows][columns];

            int noOfFrames =
                (texture.getWidth() / tileWidth) * (texture.getHeight() / tileHeight);
            // the zero th index is left empty for transparent tile
            // so it is passed in  createStaticSet as noOfFrames + 1
            // Also maintain static indices is true
            // all elements of cellMatrix[][]
            // are set to zero by new, so maintainIndices = true
            createStaticSet(texture, tileWidth, tileHeight, true);		
		
    }

    private void createStaticSet(Texture texture, int tileWidth, int tileHeight, boolean maintainIndices) {

		cellWidth = tileWidth;
		cellHeight = tileHeight;
		sourceTexture = texture;
			
		if (!maintainIndices) {
		  // populate cell matrix, all the indices are 0 to begin with
		  for (rows = 0; rows < cellMatrix.length; rows++) {
		      int totalCols = cellMatrix[rows].length;
		      for (columns = 0; columns < totalCols; columns++) {
		          cellMatrix[rows][columns] = 0;
		      }
		  }
		  // delete animated tiles
		  anim_to_static = null;
		}
		
		textureSplit = TextureRegion.split(sourceTexture, tileWidth, tileHeight)[0];
		
		for (int i = 0; i < textureSplit.length; i++) {
			textureSplit[i].flip(false, true);
		}
		
		numberOfTiles = textureSplit.length;
		
	}
    
    /**
     * Sets the contents of a cell.  <P>
     *
     * The contents may be set to a static tile index, an animated
     * tile index, or it may be left empty (index 0)
     * @param col the column of cell to set
     * @param row the row of cell to set
     * @param tileIndex the index of tile to place in cell
     * @throws IndexOutOfBoundsException if there is no tile with index
     *         <code>tileIndex</code>
     * @throws IndexOutOfBoundsException if <code>row</code> or
     *         <code>col</code> is outside the bounds of the
     *         <code>TiledLayer</code> grid
     * @see #getCell
     * @see #fillCells
     */
    public void setCell(int col, int row, int tileIndex) {

        if (col < 0 || col >= this.columns || row < 0 || row >= this.rows) {
            throw new IndexOutOfBoundsException();
        }

		if (tileIndex > 0) {
	            // do checks for static tile
	            if (tileIndex > numberOfTiles) {
		        throw new IndexOutOfBoundsException();
		    }
		} else if (tileIndex < 0) {	      
	        throw new IndexOutOfBoundsException();
    	}		
	
        cellMatrix[row][col] = tileIndex;

    }

    /**
     * Gets the contents of a cell.  <p>
     *
     * Gets the index of the static or animated tile currently displayed in
     * a cell.  The returned index will be 0 if the cell is empty.
     *
     * @param col the column of cell to check
     * @param row the row of cell to check
     * @return the index of tile in cell
     * @throws IndexOutOfBoundsException if <code>row</code> or
     *         <code>col</code> is outside the bounds of the
     *         <code>TiledLayer</code> grid
     * @see #setCell
     * @see #fillCells
     */
    public int getCell(int col, int row) {
        if (col < 0 || col >= this.columns || row < 0 || row >= this.rows) {
            throw new IndexOutOfBoundsException();
        }
        return cellMatrix[row][col];
    }

    /**
     * Fills a region cells with the specific tile.  The cells may be filled
     * with a static tile index, an animated tile index, or they may be left
     * empty (index <code>0</code>).
     *
     * @param col the column of top-left cell in the region
     * @param row the row of top-left cell in the region
     * @param numCols the number of columns in the region
     * @param numRows the number of rows in the region
     * @param tileIndex the Index of the tile to place in all cells in the
     * specified region
     * @throws IndexOutOfBoundsException if the rectangular region
     *         defined by the parameters extends beyond the bounds of the
     *         <code>TiledLayer</code> grid
     * @throws IllegalArgumentException if <code>numCols</code> is less
     * than zero
     * @throws IllegalArgumentException if <code>numRows</code> is less
     * than zero
     * @throws IndexOutOfBoundsException if there is no tile with
     *         index <code>tileIndex</code>
     * @see #setCell
     * @see #getCell
     */
    public void fillCells(int col, int row, int numCols, int numRows, int tileIndex) {

    	if (numCols < 0 || numRows < 0) {
                throw new IllegalArgumentException();
    	}

            if (col < 0 || col >= this.columns || row < 0 || row >= this.rows ||
    	    col + numCols > this.columns || row + numRows > this.rows) {
                throw new IndexOutOfBoundsException();
            }

    	if (tileIndex > 0) {
                // do checks for static tile
                if (tileIndex >= numberOfTiles ) {
    	        throw new IndexOutOfBoundsException();
    	    }
    	} else if (tileIndex < 0) {
  	            throw new IndexOutOfBoundsException();                
    	}

            for (int rowCount = row; rowCount < row + numRows; rowCount++) {
                for (int columnCount = col;
                         columnCount < col + numCols; columnCount++) {
                    cellMatrix[rowCount][columnCount] = tileIndex;
                }
            }
    }
    
    public void setMultiLineTileSet(String imagePath, int tileWidth, int tileHeight) {
    	/*tiles = new Texture(Gdx.files.internal(imagePath));
    	
		TextureRegion[][] splitTiles = TextureRegion.split(tiles, tileWidth, tileHeight);*/
    }

    public void draw() {
    	// Need to use spriteBatch version
    }
    
    public void draw(SpriteBatch batch) {
    	 if (visible) {
    		    int startColumn = 0;
    		    int endColumn = this.columns;
    		    int startRow = 0;
    		    int endRow = this.rows;
	            //Rectangle gRect = new Rectangle(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
 		       		       		    
    		    int viewportX = 0;
    		    int viewportY = 0;    		   
    		    
    		    // calculate the number of columns left of the clip
    		    int number = (int)((viewportX - this.x) / cellWidth);
    		    if (number > 0) {
    		    	startColumn = number;
    		    }

    		    // calculate the number of columns right of the clip
    		    int endX = (int)(this.x + (this.columns * cellWidth));
    		    int endClipX = viewportX + viewportWidth;
    		    number = (endX - endClipX) / cellWidth;
    		    if (number > 0) {
    		    	endColumn -= number;
    		    }

    		    // calculate the number of rows above the clip
    		    number = (int)((viewportY - this.y) / cellHeight);
    		    if (number > 0) {
    		    	startRow = number;
    		    }

    		    // calculate the number of rows below the clip
    		    int endY = (int)(this.y + (this.rows * cellHeight));
    		    int endClipY = viewportY + viewportHeight;
    		    number = (endY - endClipY) / cellHeight;
    		    if (number > 0) {
    		    	endRow -= number;
    		    }

    		    // paint all visible cells
    		    int tileIndex = 0;

    		    // y-coordinate
    		    float ty  = this.y + (startRow * cellHeight);
    	        for (int row = startRow; row < endRow; row++, ty += cellHeight) {

    		        // reset the x-coordinate at the beginning of every row
    	                // x-coordinate to draw tile into
    		        float tx = this.x + (startColumn * cellWidth);
    		        
    		        for (int column = startColumn; column < endColumn; column++, tx += cellWidth) {

    	                    tileIndex = cellMatrix[row][column];
 
    			    if (tileIndex == 0) { // transparent tile
    			    	continue;
                    } 
    	            
    			    batch.draw(textureSplit[tileIndex - 1], tx, ty);
                }
            }
		}
    }
	
}
