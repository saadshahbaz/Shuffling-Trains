package asignment22;

public class TrainNetwork {
	final int swapFreq = 2;
	TrainLine[] networkLines;

	public TrainNetwork(int nLines) {
		this.networkLines = new TrainLine[nLines];
	}

	public void addLines(TrainLine[] lines) {
		this.networkLines = lines;
	}

	public TrainLine[] getLines() {
		return this.networkLines;
	}

	public void dance() {
		System.out.println("The tracks are moving!");

		for (int i=0; i<this.networkLines.length; i+=1) {
			this.networkLines[i].shuffleLine();
			
		}
	}

	public void undance() {

		for (int i=0; i<this.networkLines.length; i+=1) {
			this.networkLines[i].sortLine();
		}
	}

	public int travel(String startStation, String startLine, String endStation, String endLine) {
		//initializing the current line
		TrainLine curLine;
		try {
			curLine = getLineByName(startLine);
		} catch (LineNotFoundException e) {
			System.out.println("curLine not found!");
			return 0;
		} 
		
		//declaring the curStation
		TrainStation curStation;
		
		//as there an throws Exception in curStation, we have to try to initialize the cursration
		try {
			curStation = curLine.findStation(startStation);
		
		//if the curStation wasn't found, we catch the exception, and return hour as 168
		} catch (StationNotFoundException e) {
			//int hoursCount=168;
			System.out.println("Hour "+168);
			System.out.println("Jumped off after spending a full week on the train. Might as well walk.");
			return 168;
			
		} 
		

		//initializing the hours
		int hoursCount = 0;
		System.out.println("Departing from "+startStation);

		
		//as the first station wouldn't have a previous station so we initialize is as null.
		TrainStation prevStation=null;
		
		//while my curStation and endStation are not equal to inputs
		while(!(curStation.getName() == endStation) && (!curLine.equals(endLine))) {
			
			//this is to check if the passenger has been traveling for more than 167 hours
			if(hoursCount >= 168) {
				System.out.println("Jumped off after spending a full week on the train. Might as well walk.");
				return hoursCount;
			}
			//we initialize a temporary variable to change prevStation later
			TrainStation temp = curStation;

			try {
				//updating the curStation and prevStation
				curStation=curLine.travelOneStation(curStation, prevStation) ;
				prevStation=temp;

			} catch (StationNotFoundException e) {
				hoursCount = 168;
			} 
			
	
			//updating the current line.
			if (!curLine.equals(curStation.getLine())) {
				try {
					curLine = getLineByName(curStation.getLine().getName());
				} catch (LineNotFoundException e) {
					// TODO Auto-generated catch block
					return hoursCount;
				}
			}
			
			//incrementing the hours
			hoursCount++;
			
		

			//prints an update on your current location in the network.
			System.out.println("Traveling on line "+curLine.getName()+":"+curLine.toString());
			System.out.println("Hour "+hoursCount+". Current station: "+curStation.getName()+" on line "+curLine.getName());
			System.out.println("=============================================");
			
			//after every two hours, we shuffle the current line.
			if (hoursCount %2==0) {
				dance();
			}

		}
		// if the station has been reached, we return the hours it took to reach the station.
		System.out.println("Arrived at destination after "+hoursCount+" hours!");
		return hoursCount;
	}


	public TrainLine getLineByName(String lineName) throws LineNotFoundException{
		for (int i=0; i<this.networkLines.length; i+=1) {
			if (this.networkLines[i].getName() == lineName) {
				return this.networkLines[i];
			}
		}
		throw new LineNotFoundException("Line not found");

	}



	//change this


	//prints a plan of the network for you.
	public void printPlan() {
		System.out.println("CURRENT TRAIN NETWORK PLAN");
		System.out.println("----------------------------");
		for(int i=0;i<this.networkLines.length;i++) {
			System.out.println(this.networkLines[i].getName()+":"+this.networkLines[i].toString());
		}
		System.out.println("----------------------------");
	}
	
}

//exception when searching a network for a LineName and not finding any matching Line object.
class LineNotFoundException extends RuntimeException {
	String name;

	public LineNotFoundException(String n) {
		name = n;
	}

	public String toString() {
		return "LineNotFoundException[" + name + "]";
	}
}


