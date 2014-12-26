package com.mygdx.game;

import com.mygdx.game.Objects.*;
import com.mygdx.game.Util.Constants;

import java.util.ArrayList;
import java.util.List;

public class SpatialHashGrid
{
    public static final SpatialHashGrid instance = new SpatialHashGrid();

    List<GameObject>[] dynamicCells;
    List<GameObject>[] staticCells;
    private int cellsPerRow;
    private int cellsPerCol;
    private int numCells;
    private float cellSize;
    int[] cellIds = new int[4];
    List<GameObject> foundObjects;

    private SpatialHashGrid(){}

    @SuppressWarnings("unchecked")
    public void initialize()
    {
        this.cellSize = Constants.CELL_SIZE;
        this.cellsPerRow = (int) Math.ceil(Constants.GRID_WIDTH / cellSize);
        this.cellsPerCol = (int) Math.ceil(Constants.GRID_HEIGHT / cellSize);
        numCells = cellsPerRow * cellsPerCol;
        dynamicCells = new List[numCells];
        staticCells = new List[numCells];
        for(int i = 0; i < numCells; i++)
        {
            dynamicCells[i] = new ArrayList<GameObject>(100);
            staticCells[i] = new ArrayList<GameObject>(100);
        }
        foundObjects = new ArrayList<GameObject>(100);

        update(Game.instance.objects);
    }
    public void update(List<GameObject> objects)
    {
        for(GameObject object : objects)
        {
            if(object instanceof StaticObject)
                insertStaticObject(object);
            else if(object instanceof DynamicObject)
                insertDynamicObject(object);
        }
    }
    public void insertStaticObject(GameObject obj)
    {
        int[] cellIds = getCellIds(obj);
        int i = 0;
        int cellId = -1;
        while(i <= 3 && (cellId = cellIds[i++]) != -1)
        {
            staticCells[cellId].add(obj);
        }
    }
    public void insertDynamicObject(GameObject obj)
    {
        int[] cellIds = getCellIds(obj);
        int i = 0;
        int cellId = -1;
        while(i <= 3 && (cellId = cellIds[i++]) != -1)
        {
            dynamicCells[cellId].add(obj);
        }
    }
    public void removeObject(GameObject obj)             // nulls the passed object
    {
       /* int[] cellIds = getCellIds(obj);              //Some balls done get destroyed
        int i = 0;
        int cellId = -1;
        while(i <= 3 && (cellId = cellIds[i++]) != -1)
        {
            dynamicCells[cellId].remove(obj);
            staticCells[cellId].remove(obj);
        }*/
        /*if(obj instanceof Ball)
        {
            for(int i = 0; i < numCells; i++)
                dynamicCells[i].remove(obj);
        }*/
        if(obj instanceof Cube)
        {
            for(int i = 0; i < numCells; i++)
                staticCells[i].remove(obj);
        }
    }
    public void clearDynamicCells(GameObject obj)        // nulls the whole list
    {
        for(List<GameObject> dynamicCell : dynamicCells)
        {
            dynamicCell.clear();
        }
    }
    public List<GameObject> getPotentialColliders(GameObject obj)
    {
        foundObjects.clear();
        int[] cellIds = getCellIds(obj);
        int i = 0;
        int cellId = -1;
        while(i <= 3 && (cellId = cellIds[i++]) != -1)
        {
            int len = dynamicCells[cellId].size();
            for(int j = 0; j < len; j++)
            {
                GameObject collider = dynamicCells[cellId].get(j);
                if(!foundObjects.contains(collider))
                    foundObjects.add(collider);

            }
            len = staticCells[cellId].size();
            for(int j = 0; j < len; j++)
            {
                GameObject collider = staticCells[cellId].get(j);
                if(!foundObjects.contains(collider))
                    foundObjects.add(collider);
            }
        }
        return foundObjects;
    }
    public int[] getCellIds(GameObject obj)
    {
        int x1 = (int) Math.floor(obj.getX() / cellSize);
        int y1 = (int) Math.floor(obj.getY() / cellSize);
        int x2 = (int) Math.floor((obj.getX() + obj.getWidth()) / cellSize);
        int y2 = (int) Math.floor((obj.getY() + obj.getHeight()) / cellSize);

        if(x1 == x2 && y1 == y2)
        {
            if(x1 >= 0 && x1 < cellsPerRow && y1 >= 0 && y1 < cellsPerCol)
                cellIds[0] = x1 + y1 * cellsPerRow;
            else
                cellIds[0] = -1;
            cellIds[1] = -1;
            cellIds[2] = -1;
            cellIds[3] = -1;
        }
        else if(x1 == x2)
        {
            int i = 0;
            if(x1 >= 0 && x1 < cellsPerRow)
            {
                if(y1 >= 0 && y1 < cellsPerCol)
                    cellIds[i++] = x1 + y1 * cellsPerRow;
                if(y2 >= 0 && y2 < cellsPerCol)
                    cellIds[i++] = x1 + y2 * cellsPerRow;
            }
            while(i <= 3) cellIds[i++] = -1;
        }
        else if(y1 == y2)
        {
            int i = 0;
            if(y1 >= 0 && y1 < cellsPerCol)
            {
                if(x1 >= 0 && x1 < cellsPerRow)
                    cellIds[i++] = x1 + y1 * cellsPerRow;
                if(x2 >= 0 && x2 < cellsPerRow)
                    cellIds[i++] = x2 + y1 * cellsPerRow;
            }
            while(i <= 3) cellIds[i++] = -1;
        }
        else
        {
            int i = 0;
            int y1CellsPerRow = y1 * cellsPerRow;
            int y2CellsPerRow = y2 * cellsPerRow;
            if(x1 >= 0 && x1 < cellsPerRow && y1 >= 0 && y1 < cellsPerCol)
                cellIds[i++] = x1 + y1CellsPerRow;
            if(x2 >= 0 && x2 < cellsPerRow && y1 >= 0 && y1 < cellsPerCol)
                cellIds[i++] = x2 + y1CellsPerRow;
            if(x2 >= 0 && x2 < cellsPerRow && y2 >= 0 && y2 < cellsPerCol)
                cellIds[i++] = x2 + y2CellsPerRow;
            if(x1 >= 0 && x1 < cellsPerRow && y2 >= 0 && y2 < cellsPerCol)
                cellIds[i++] = x1 + y2CellsPerRow;
            while(i <= 3) cellIds[i++] = -1;
        }
        return cellIds;
    }
}

