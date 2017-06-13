package com.games.numbercruncher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;

import com.games.common.CommonFunctions;
import com.games.numbercruncher.SolverHandler.CallerHandle;


public class Main implements CallerHandle {
	private static long initTime=0l;
	private static int SOLVED_GRIDS=0;
	private static int numberOfFalse=0;
	private static int numberOfTrue=0;
	private static Object lock=new Object();

	private static final int[] staticGrid=new int[]{
		1,2,3,4,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,7,8,
		0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0
	};
	private  static final int[] staticGrid2=new int[]{
		8,0,0,0,0,0,0,0,0,
		0,0,3,6,0,0,0,0,0,
		0,7,0,0,9,0,2,0,0,
		0,5,0,0,0,7,0,0,0,
		0,0,0,0,4,5,7,0,0,
		0,0,0,1,0,0,0,3,0,
		0,0,1,0,0,0,0,6,8,
		0,0,8,5,0,0,0,1,0,
		0,9,0,0,0,0,4,0,0,
	};
	private static final int[] staticGrid3=new int[]{
		0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,
		8,7,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,4,3,2,1
	};
	private static final int[] staticGrid4=new int[]{
		1,2,3,4,5,6,7,8,9,
		0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,1,
		0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,4,
		0,0,0,0,0,0,0,0,3,
		0,0,0,0,0,0,0,0,2
	};
	private static final int[] staticGrid5=new int[]{
		1,0,3,4,5,6,7,8,0,
		0,2,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0
	};
	private static final int[] staticGrid6=new int[] {1,9,3,4,5,6,7,8,2,
		0,2,0,0,0,0,0,9,3,
		0,0,0,0,0,0,0,6,4,
		9,0,8,0,0,0,0,4,5,
		0,0,7,0,0,0,0,2,6,
		2,0,6,5,0,0,0,1,7,
		3,7,9,0,0,0,6,5,8,
		5,6,2,0,0,0,4,3,1,
		4,0,1,0,0,0,2,7,9};
	private static final int[] staticGrid7=new int[] {
		1,2,3,4,0,0,0,0,0,
		5,6,8,7,0,0,0,0,0,
		9,0,0,2,0,0,0,0,0,
		8,3,5,6,9,0,0,0,0,
		0,0,0,0,0,0,0,0,8,
		0,0,0,0,0,0,0,0,9,
		3,0,0,1,4,2,6,8,7,
		0,1,0,3,5,8,2,9,4,
		2,8,4,9,6,7,5,3,1};
	private static final int[] staticGrid8=new int[]{//try index 0,value 9
		0,0,4,2,0,0,0,0,7,
		0,1,0,0,0,0,0,0,0,
		0,0,0,7,0,6,5,0,0,
		0,0,0,8,0,0,0,9,0,
		0,2,0,9,0,4,0,6,0,
		0,4,0,0,0,2,0,0,0,
		0,0,1,6,0,7,0,0,0,
		0,0,0,0,0,0,0,3,0,
		3,0,0,0,0,5,7,0,2
	};


	public static ArrayList<int[]> solutions;
	private static ArrayList<int[]> testList;
	private static int testTotal;
	public static ArrayList<int[]> readPuzzles(){
		BufferedReader br = null;
		String everything=null;
		int[] currentGrid=new int[81];
		try {
			br = new BufferedReader(new FileReader("C:\\EclipseWorkspace\\GameWorkspace\\GridSolver\\src\\com\\games\\numbercruncher\\sudoku.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			everything = sb.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//		Blog.i(everything);
		return makeListFromString(everything);

	}
	private static ArrayList<int[]> makeListFromString(String everything) {

		int[] puzzle=new int[81];
		ArrayList<int[]> list=new ArrayList<int[]>();
		String[] lines = everything.split(System.getProperty("line.separator"));
		int puzzleNr=0;
		boolean firstLine=true;
		for(String line: lines){
			if(line.substring(0,4).equals("Grid")){
				//				Blog.i(line);
				if(firstLine){
					firstLine=false;
				}
				else{
					list.add(puzzle);
					//					Blog.i(CommonFunctions.arrayToString(puzzle, 9));
				}
				puzzle=new int[81];
				puzzleNr=0;
			}
			else{
				for(char c:line.toCharArray()){
					puzzle[puzzleNr]=Character.getNumericValue(c);
					//				Blog.i(Character.getNumericValue(c));
					puzzleNr++;

				}
			}

		}
		list.add(puzzle);
		return list;
	}
	private static int rowColToIndex(int row,int col){
		return 9*row+col;
	}
	public static void main(String [ ] args)
	{
		Blog.i("main");
		//		new SolverHandler(staticGrid8,rowColToIndex(0, 0),9,new Main()).start();
		//		synchronized(lock){
		//			try {
		//				lock.wait();
		//			} catch (InterruptedException e) {
		//				// TODO Auto-generated catch block
		//				e.printStackTrace();
		//			}
		//		}
		//				ArrayList<int[]> list=readPuzzles();
		ArrayList<int[]> list=readBigPuzzles();
		Blog.i(CommonFunctions.arrayToString(list.get(0),9));
		resetAnswerList();
		testTotal=0;
		initTime=System.currentTimeMillis();
		int cores=Runtime.getRuntime().availableProcessors();
		for(int[] i: list){
			FirstNonZero cell=findFirstZero(i);
			i[cell.index]=0;
			new SolverHandler(i,cell.index,cell.value,new Main()).start();
			if(java.lang.Thread.activeCount()>=15){
				try {
					synchronized(lock){
						lock.wait();
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//			Blog.i(cell.value, 9);
		}
		try {
			Thread.sleep(12000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//		//		new SolverHandler(staticGrid6,73,8,new Main()).start();
	}
	private static ArrayList<int[]> readBigPuzzles() {
		BufferedReader br = null;
		String everything=null;
		int[] currentGrid=new int[81];
		try {
			br = new BufferedReader(new FileReader("C:\\EclipseWorkspace\\GameWorkspace\\GridSolver\\src\\com\\games\\numbercruncher\\sudokubig.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			everything = sb.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Blog.i(everything);
		return makeBigListFromString(everything);

	}
	private static ArrayList<int[]> makeBigListFromString(String everything) {
		int[] puzzle=new int[81];
		ArrayList<int[]> list=new ArrayList<int[]>();
		String[] lines = everything.split(System.getProperty("line.separator"));
		int puzzleNr=0;
		boolean firstLine=true;
		for(String line: lines){
			for(char c:line.toCharArray()){
				puzzle[puzzleNr]=Character.getNumericValue(c);
				//				Blog.i(Character.getNumericValue(c));
				puzzleNr++;
			}
			//			Blog.i(CommonFunctions.arrayToString(puzzle, 9));
			puzzleNr=0;
			list.add(puzzle);
			puzzle=new int[81];
		}
		return list;
	}
	private static void resetAnswerList() {
		testList=new ArrayList<int[]>();
	}
	private static FirstNonZero findFirstZero(int[] i) {
		for(int j=0;j<i.length;j++){
			if(i[j]!=0){
				FirstNonZero n=new FirstNonZero();
				n.index=j;
				n.value=i[j];
				return n;
			}
		}
		return null;
	}
	public static final void resetSolutions(){
		solutions=new ArrayList<int[]>();
	}
	public static final void addSolution(int[] puzzle){
		solutions.add(puzzle);
	}
	public synchronized void onGridSolved(int[] grid, boolean solution, int place, int value) {
		synchronized(Main.class){
			++SOLVED_GRIDS;
			//			int array2d[][] = new int[9][9];
			//
			//			for(int i=0; i<array2d.length;i++)
			//				for(int j=0;j<array2d.length;j++){
			//					Blog.i(i," ",j);
			//					array2d[i][j] = grid[(j*9) + i]; 
			//				}

			//			Blog.i(CommonFunctions.intArToList(grid));
			//			testList.add(grid);
			for(int i=0;i<3;i++){
				testTotal+=grid[i]*Math.pow(10, 2-i);
			}
			//			if(!isValid(array2d)){
			//				throw new IllegalArgumentException("you moron");
			//			}
			//			else{
			////				Blog.i("solution works");
			//			}
			if(solution){
				numberOfTrue++;
			}
			else{
				numberOfFalse++;
			}
			if(SOLVED_GRIDS%1000==0 || SOLVED_GRIDS==49151){
				Blog.i("Time in milliseconds: ",System.currentTimeMillis()-initTime);
				Blog.i("Max solver time in milliseconds: ", SolverHandler.maxTime);
				Blog.i("testTotal: ", testTotal);
				Blog.i("Puzzle had solution: ",solution);
				Blog.i(CommonFunctions.arrayToString(grid, 9));
				Blog.i("SOLVED GRIDS ",SOLVED_GRIDS);
				Blog.i("Number of true puzzles: ",numberOfTrue, " Number of false puzzles: ", numberOfFalse);
				Blog.i("Average solver time in milliseconds: ", 1.0f*(System.currentTimeMillis()-initTime)/SOLVED_GRIDS);
			}

			synchronized(lock){
				lock.notify();
			}
		}
	}
	private boolean isValid(int[][] board) {
		//Check rows and columns 
		for (int i = 0; i < board.length; i++) {
			BitSet bsRow = new BitSet(9);
			BitSet bsColumn = new BitSet(9);
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] == 0 || board[j][i] == 0) continue;
				if (bsRow.get(board[i][j] - 1) || bsColumn.get(board[j][i] - 1))
					return false;
				else {
					bsRow.set(board[i][j] - 1);
					bsColumn.set(board[j][i] - 1);
				}
			}
		}
		//Check within 3 x 3 grid
		for (int rowOffset = 0; rowOffset < 9; rowOffset += 3) {
			for (int columnOffset = 0; columnOffset < 9; columnOffset += 3) {
				BitSet threeByThree = new BitSet(9);
				for (int i = rowOffset; i < rowOffset + 3; i++) {
					for (int j = columnOffset; j < columnOffset + 3; j++) {
						if (board[i][j] == 0) continue;
						if (threeByThree.get(board[i][j] - 1))
							return false;
						else
							threeByThree.set(board[i][j] - 1);
					}
				}  
			}
		}
		return true;}
	static class FirstNonZero{
		public int index;
		public int value;
	}
}
