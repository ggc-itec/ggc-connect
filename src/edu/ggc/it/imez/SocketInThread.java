package edu.ggc.it.imez;

public class SocketInThread extends Thread  {
	private NetWorkIO netWorkIO;
	
	public SocketInThread(NetWorkIO NetWorkIO) {
		this.netWorkIO = netWorkIO; 
	}

	@Override
	public void run() {
		super.run();
		
		
	}

}
