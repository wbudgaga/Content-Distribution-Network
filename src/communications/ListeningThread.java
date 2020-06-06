package communications;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// it is a thread class that is responsible for accepting new incoming connections.

public class ListeningThread implements Runnable{
	private ServerSocket 		serverSocket;
	private ConnectionManager 	connectionManager;

	 public ListeningThread(int port, ConnectionManager connectionManager) throws IOException{
		 this.connectionManager = connectionManager;
		 serverSocket = new ServerSocket(port,100);
	 }
	
	 private void listening() throws IOException{	
		 Socket connectionSocket = serverSocket.accept(); 
		 register(connectionSocket);
	 }
	 
	 private void register(Socket connectionSocket) throws IOException{
		 connectionManager.addConnection(connectionSocket);
	 }
	 
	 private void sendStopListeningNotification(){
		 connectionManager.listenerStopNotification();
	 }
	 
	 public void stop(){
		try {
			serverSocket.close();
		} catch (IOException e) {}    
	}
	 
	@Override
	public void run() {
		try{
		// Run forever, accepting and servicing connections
		 while (true) 
			listening();
		}catch(IOException ioE){
			sendStopListeningNotification();
		}
	}
}
