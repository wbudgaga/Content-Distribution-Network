package node;

import java.io.IOException;
// the class is a thread class that is used to update the weight in in fixed timeInterval time.
// the thread will not started if the timeInterval = 0
public class DiscoverTimer extends Thread{
	private Discovery 	discoveryNode;
	private long 		timeInterval;
	private boolean 	keepRunning;
	public DiscoverTimer(Discovery discoveryNode, long timeInterval){
		this.discoveryNode 	=  	discoveryNode;
		this.timeInterval 	=  	timeInterval;
		if (timeInterval > 0)
			this.keepRunning	=	true;
		else
			this.keepRunning	=	false;
	}
	
	protected void stopRunning(){
		this.keepRunning	=	false;
	}
	
	@Override
	public void run() {
		while (keepRunning){
			try {
				discoveryNode.updateWeights();// calling updateWeights() to update the weights in time interval
				sleep(timeInterval);
				continue;
			} 	catch (InstantiationException e) 	{} 
				catch (IllegalAccessException e) 	{} 
				catch (IOException e) 				{} 
				catch (InterruptedException e) 		{}
				stopRunning();			
		}
	}
}
