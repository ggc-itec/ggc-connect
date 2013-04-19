package edu.ggc.it.imez;

public class ModelImEz {
	private ControllerImEz controllerImEz;
	private NetWorkIO netWorkIO;
	
	public ModelImEz() {	
	 ControllerImEz controllerImEz	= new ControllerImEz(this);
	 NetWorkIO netWorkIO = new NetWorkIO(this);
	}



}
