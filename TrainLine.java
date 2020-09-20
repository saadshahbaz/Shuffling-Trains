package asignment22;

import java.util.Arrays;
import java.util.Random;


public class TrainLine {

	private TrainStation leftTerminus;
	private TrainStation rightTerminus;
	private String lineName;
	private boolean goingRight;
	public TrainStation[] lineMap;
	public static Random rand;

	public TrainLine(TrainStation leftTerminus, TrainStation rightTerminus, String name, boolean goingRight) {
		this.leftTerminus = leftTerminus;
		this.rightTerminus = rightTerminus;
		this.leftTerminus.setLeftTerminal();
		this.rightTerminus.setRightTerminal();
		this.leftTerminus.setTrainLine(this);
		this.rightTerminus.setTrainLine(this);
		this.lineName = name;
		this.goingRight = goingRight;

		this.lineMap = this.getLineArray();
	}

	public TrainLine(TrainStation[] stationList, String name, boolean goingRight)
	/*
	 * Constructor for TrainStation input: stationList - An array of TrainStation
	 * containing the stations to be placed in the line name - Name of the line
	 * goingRight - boolean indicating the direction of travel
	 */
	{
		TrainStation leftT = stationList[0];
		TrainStation rightT = stationList[stationList.length - 1];

		stationList[0].setRight(stationList[stationList.length - 1]);
		stationList[stationList.length - 1].setLeft(stationList[0]);

		this.leftTerminus = stationList[0];
		this.rightTerminus = stationList[stationList.length - 1];
		this.leftTerminus.setLeftTerminal();
		this.rightTerminus.setRightTerminal();
		this.leftTerminus.setTrainLine(this);
		this.rightTerminus.setTrainLine(this);
		this.lineName = name;
		this.goingRight = goingRight;

		for (int i = 1; i < stationList.length - 1; i++) {
			this.addStation(stationList[i]);
		}

		this.lineMap = this.getLineArray();
	}

	public TrainLine(String[] stationNames, String name,
			boolean goingRight) {/*
									 * Constructor for TrainStation. input: stationNames - An array of String
									 * containing the name of the stations to be placed in the line name - Name of
									 * the line goingRight - boolean indicating the direction of travel
									 */
		TrainStation leftTerminus = new TrainStation(stationNames[0]);
		TrainStation rightTerminus = new TrainStation(stationNames[stationNames.length - 1]);

		leftTerminus.setRight(rightTerminus);
		rightTerminus.setLeft(leftTerminus);

		this.leftTerminus = leftTerminus;
		this.rightTerminus = rightTerminus;
		this.leftTerminus.setLeftTerminal();
		this.rightTerminus.setRightTerminal();
		this.leftTerminus.setTrainLine(this);
		this.rightTerminus.setTrainLine(this);
		this.lineName = name;
		this.goingRight = goingRight;
		for (int i = 1; i < stationNames.length - 1; i++) {
			this.addStation(new TrainStation(stationNames[i]));
		}

		this.lineMap = this.getLineArray();

	}

	// adds a station at the last position before the right terminus
	public void addStation(TrainStation stationToAdd) {
		TrainStation rTer = this.rightTerminus;
		TrainStation beforeTer = rTer.getLeft();
		rTer.setLeft(stationToAdd);
		stationToAdd.setRight(rTer);
		beforeTer.setRight(stationToAdd);
		stationToAdd.setLeft(beforeTer);

		stationToAdd.setTrainLine(this);

		this.lineMap = this.getLineArray();
	}

	public String getName() {
		return this.lineName;
	}

	public int getSize() {

		int counter =1;
		
		TrainStation current = getLeftTerminus();
		while (current.getRight()!=null) {
			current = current.getRight();
			counter +=1;
		}
		return counter;
		//return 0; // change this!
	}

	public void reverseDirection() {
		this.goingRight = !this.goingRight;
	}

	public TrainStation travelOneStation(TrainStation current, TrainStation previous) throws StationNotFoundException{

		TrainStation x = findStation(current.getName());
		if (x == null) {
			throw new StationNotFoundException("Station not found");
		}
		
		else {
			if(current.hasConnection) {
				if (!current.getTransferStation().equals(previous)) {
					TrainStation temp = current;
					current.getTransferLine();
					current = current.getTransferStation();
					//previous = temp;
					return current;
					
				}
				else {
					TrainStation temp = current;
					current = getNext(current);
					//previous = temp;
					return current;
				}
			}
			else {
				TrainStation temp = current;
				current = getNext(current);
				//previous = temp;
				return current;
			}
		}
		
		
	}

	
	public TrainStation getNext(TrainStation station) throws StationNotFoundException {
		if(findStation(station.getName()) == null){
			throw new StationNotFoundException("Station not found");
		}
		else {
			if (this.goingRight) {
				if (station.isRightTerminal()) {
					this.goingRight=false;
					//station.setLeftTerminal();
					return station.getLeft();
				}
				else {
					return station.getRight();
				}
				
			}
			else {
				if (station.isLeftTerminal()) {
					this.goingRight=true;
					//station.setRightTerminal();
					return station.getRight();
				}
				else {
					return station.getLeft();	
				}
			}
		}
	}

	
	public TrainStation findStation(String name) throws StationNotFoundException{
		TrainStation current = getLeftTerminus();
		while (current.getName() != name) {
			if(current.getRight() == null) {
				throw new StationNotFoundException(name + " not Found!");
			}
			else {
				current = current.getRight();
			}
		}
		return current;

		
		
	}
	public void sortLine() {
		
			
			this.lineMap = this.getLineArray();

			int k=0;
			boolean swapMade=true;
			while((k<this.lineMap.length-1) && (swapMade)) {
				swapMade=false;
				k++;
				for (int j=0; j<this.lineMap.length-k; j+=1) {
				if (this.lineMap[j].getName().compareTo(this.lineMap[j+1].getName())>0) {
					
						TrainStation temp=lineMap[j];
						lineMap[j] = lineMap[j+1];
						lineMap[j+1]=temp;
						swapMade=true;
					}
				}
				
			}
			
			this.lineMap[0].setLeft(null);
			this.lineMap[0].setRight(this.lineMap[1]);
			this.lineMap[0].setLeftTerminal();
			this.leftTerminus=this.lineMap[0];
			this.lineMap[0] = this.leftTerminus;
			
			this.lineMap[this.lineMap.length-1].setLeft(this.lineMap[this.lineMap.length-2]);
			this.lineMap[this.lineMap.length-1].setRightTerminal();
			this.rightTerminus= this.lineMap[this.lineMap.length-1];
			this.lineMap[this.lineMap.length-1] = this.rightTerminus;
			this.lineMap[this.lineMap.length-1].setRight(null);
			
			for (int i=1; i<this.lineMap.length-1; i+=1) {
				this.lineMap[i].setRight(this.lineMap[i+1]);
				this.lineMap[i].setLeft(this.lineMap[i-1]);
				this.lineMap[i] = this.lineMap[i-1].getRight();
				this.lineMap[i].setNonTerminal();
			}
			
			
			
			
		
			
		}
		
		
	

	public TrainStation[] getLineArray() {

		TrainStation[] tr = new TrainStation[getSize()];
		tr[0]=this.leftTerminus;
		//tr[0].setRight(tr[1]);
		for (int i=1; i<getSize()-1; i+=1) {
			tr[i] = tr[i-1].getRight();
			
		}
		//tr[getSize()-1].setLeft(tr[getSize()-2]);
		tr[getSize()-1]=this.rightTerminus;
		return tr; // change this
	}

	private TrainStation[] shuffleArray(TrainStation[] array) {
		Random rand = new Random();
		rand.setSeed(11);

		for (int i = 0; i < array.length; i++) {
			int randomIndexToSwap = rand.nextInt(array.length);
			TrainStation temp = array[randomIndexToSwap];
			array[randomIndexToSwap] = array[i];
			array[i] = temp;
		}
		this.lineMap = array;
		return array;
	}

	public void shuffleLine() {

		// you are given a shuffled array of trainStations to start with
		TrainStation[] lineArray = this.getLineArray();
		TrainStation[] shuffledArray = shuffleArray(lineArray);
		
		//as the line has now been shuffled, all the nodes are messed up so updating the nodes
		shuffledArray[0].setLeft(null); //setting the head.prev to null;
		shuffledArray[0].setRight(this.lineMap[1]); //setting head.next to the right node;
		shuffledArray[0].setNonTerminal(); //it could be a right terminal so we unassign it first
		shuffledArray[0].setLeftTerminal(); //reassigning the the head to head;
		this.leftTerminus=shuffledArray[0]; 
		//this.lineMap[0] = this.leftTerminus;
		
		
		for (int i=1; i<shuffledArray.length-1; i+=1) { //for every node
			shuffledArray[i].setRight(shuffledArray[i+1]); //setting the node.next pointer
			shuffledArray[i].setLeft(shuffledArray[i-1]); //setting the node.prev pointer
			//this.lineMap[i] = this.lineMap[i-1].getRight();
			shuffledArray[i].setNonTerminal(); //setting it to be non terminal
		}
		
		//setting tail.prev
		shuffledArray[shuffledArray.length-1].setLeft(shuffledArray[shuffledArray.length-2]);
		//unassigning it it
		shuffledArray[shuffledArray.length-1].setNonTerminal();
		//reassigning it
		shuffledArray[shuffledArray.length-1].setRightTerminal();
		//setting it to its correct node.
		this.rightTerminus= shuffledArray[shuffledArray.length-1];
		//setting the tail.next pointer to null
		shuffledArray[shuffledArray.length-1].setRight(null);
		
		//re-assigning the lineMap
		this.lineMap = shuffledArray;
	
		//getLineArray();
		
		}
	
		

	public String toString() {
		TrainStation[] lineArr = this.getLineArray();
		String[] nameArr = new String[lineArr.length];
		for (int i = 0; i < lineArr.length; i++) {
			nameArr[i] = lineArr[i].getName();
		}
		return Arrays.deepToString(nameArr);
	}

	public boolean equals(TrainLine line2) {

		// check for equality of each station
		TrainStation current = this.leftTerminus;
		TrainStation curr2 = line2.leftTerminus;

		
		try {
			while (current != null) {
				if (!current.equals(curr2))
					return false;
				else {
					current = current.getRight();
					curr2 = curr2.getRight();
				}
			}

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public TrainStation getLeftTerminus() {
		return this.leftTerminus;
	}

	public TrainStation getRightTerminus() {
		return this.rightTerminus;
	}

	

}


//Exception for when searching a line for a station and not finding any station of the right name.
class StationNotFoundException extends RuntimeException {
	String name;

	public StationNotFoundException(String n) {
		name = n;
	}

	public String toString() {
		return "StationNotFoundException[" + name + "]";
	}
}




