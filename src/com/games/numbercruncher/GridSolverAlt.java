package com.games.numbercruncher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

public class GridSolverAlt implements Runnable {


	private static final int SUBS[][]={{0,1,2},{3,4,5},{6,7,8}};
	final int[] grid;
	private int testPlace;
	private int testValue;
	boolean solvable;
	int[] solutionGrid;
	private SolverHandler manager;
	/**Contains a bitmask telling which values are taken in the row given by the index*/
	private int[] rowValuesMask=new int[9];
	private int[] colValuesMask=new int[9];
	private int[] subValuesMask=new int[9];
	private boolean test=true;
	private static final int[] bitmasks=new int[]{0x0001,0x0002,0x0004,0x0008,0x0010,0x0020,0x0040,0x0080,0x0100};





	public GridSolverAlt(int[] grid,int place,int value, SolverHandler manager){
//		Blog.i(CommonFunctions.arrayToString(grid, 9));
		if(grid[place]!=0){
			throw new IllegalArgumentException("Attempted to place value in occupied square");
		}
		this.grid=grid.clone();
		this.testPlace=place;
		this.testValue=value;
		this.manager=manager;
		this.initBitMasks();
//		Blog.i("row ",Integer.toBinaryString(rowValuesMask[0]));
//		Blog.i(Integer.toBinaryString(colValuesMask[0]));
//		Blog.i(Integer.toBinaryString(subValuesMask[0]));
//		for(int x=1;x<10;x++){
//			Blog.i("Value: ",x," is valid ",isValid(4,x));
//		}
//
//		for(Integer x:bitmasks){
//			Blog.i(Integer.toBinaryString(x));
//		}
//		Blog.i(Integer.toBinaryString(bitmasks[3]&bitmasks[3]));
	}
	private void initBitMasks() {
		for(int x=0;x<grid.length;x++){
			if(grid[x]!=0){
				setValueTaken(x,grid[x]);
			}
		}
	}
	private static int getSubgrid(int i) {
		return SUBS[getRow(i)/3][getColumn(i)/3];
	}
	private static int getRow(int i) {
		return i/9;
	}
	private static int getColumn(int i) {
		return i-9*(i/9);
	}
	private static int rowColToIndex(int row,int col){
		return 9*row+col;
	}
	private static String indexToString(int index){
		return new StringBuilder().append("Row: ").append(getRow(index)).append(" Column: ").append(getColumn(index)).toString();
	}


	@Override
	public void run() {
		if(!isValid(testPlace,testValue)){
			manager.onSolverFinished(false, this.grid);
			return;
		}
		else{
			grid[testPlace]=testValue;
			setValueTaken(testPlace,testValue);
		}
		List<Cell> cells=getEmptyCells();
		Collections.sort(cells,new CellComparator());
//		Blog.i(cells);
//		Blog.i("Solve grid: ", CommonFunctions.arrayToString(grid, 9));
		
		
		solvable=isSolvable(cells);
		if(test){
			this.manager.onSolverFinished(solvable,this.solutionGrid);
		}
//		Blog.i(solvable);

	}
	private boolean isSolvable(List<Cell> cells) {
		Stack<Cell> stack=new Stack<Cell>();
		CellComparator comp=new CellComparator();
		while(true){
			if(cells.isEmpty()){
				solutionGrid=grid;
				return true;
			}
			Cell cell=cells.get(cells.size()-1);
//			Blog.i("Cell to test: " ,cell);
//			cell.setUnOccupied();
//			Blog.i(cell);
//			Blog.i(cell.value);
			cell.value=cell.getNextValidValue();
//			Blog.i(cell.value);
			if(cell.value!=0){//found valid value
				cell.setOccupied();
				stack.push(cells.remove(cells.size()-1));
				Collections.sort(cells, comp);
			}
			else{
				if(stack.isEmpty()){
					solutionGrid=grid;
					return false;
				}
				cell=stack.pop();
				cell.setUnOccupied();
				cells.add(cell);
//				Collections.sort(cells, comp);
			}
			if(Thread.interrupted()){
				return false;
			}
//			Blog.i(CommonFunctions.arrayToString(grid, 9));

		}
	}
	private List<Cell> getEmptyCells() {
		ArrayList<Cell> pq=new ArrayList<GridSolverAlt.Cell>();
		for(int i=0;i<grid.length;i++){
			if(grid[i]==0){
				pq.add(new Cell(i));
			}
		}
		return pq;
	}
	private void setValueTaken(int index, int value){
		grid[index]=value;
		rowValuesMask[getRow(index)]=rowValuesMask[getRow(index)]|bitmasks[value-1];
		colValuesMask[getColumn(index)]=colValuesMask[getColumn(index)]|bitmasks[value-1];
		subValuesMask[getSubgrid(index)]=subValuesMask[getSubgrid(index)]|bitmasks[value-1];
	}
	private void setValueNotTaken(int index, int value){
		grid[index]=0;
		rowValuesMask[getRow(index)]=rowValuesMask[getRow(index)]&~bitmasks[value-1];
		colValuesMask[getColumn(index)]=colValuesMask[getColumn(index)]&~bitmasks[value-1];
		subValuesMask[getSubgrid(index)]=subValuesMask[getSubgrid(index)]&~bitmasks[value-1];
	}
	private boolean isValid(int index, int value){
//		Blog.i("value:", value, "index: ", index);
		return ((bitmasks[value-1]&rowValuesMask[getRow(index)])==0 && 
				(bitmasks[value-1]&colValuesMask[getColumn(index)])==0 && 
				(bitmasks[value-1]&subValuesMask[getSubgrid(index)])==0);
	}
	private class Cell{
		private int place;
		private int value=0;

		public Cell(int place){
			this.place=place;
		}
		public void setOccupied() {
			setValueTaken(place, value);
		}
		public void setUnOccupied() {
			if(value!=0){
				setValueNotTaken(place, value);
			}
		}
		public int getNextValidValue() {
			while(value<9){
				value++;
				if(isValid(place, value)){
					return value;
				}
			}
			value=0;
			return value;
		}
		private int getSubgrid() {
			return GridSolverAlt.getSubgrid(place);
		}
		private int getRow() {
			return GridSolverAlt.getRow(place);
		}
		private int getColumn() {
			return GridSolverAlt.getColumn(place);
		}
		public int getBusyFactor(){
			int unionmask=rowValuesMask[getRow()]|colValuesMask[getColumn()]|subValuesMask[getSubgrid()];
			return Integer.bitCount(unionmask);
		}
		public String toString(){
			return new StringBuilder().append(indexToString(place)).append(" Value: ").append(value).append(" Busyfactor: ").append(getBusyFactor()).toString();
		}
	}
	private class CellComparator implements Comparator<Cell>{
		@Override
		public int compare(Cell arg0, Cell arg1) {
			//			Blog.i(arg0,arg1);
			return arg0.getBusyFactor()-arg1.getBusyFactor();

		}
	}
}
