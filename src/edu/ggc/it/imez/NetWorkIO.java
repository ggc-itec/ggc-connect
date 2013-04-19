package edu.ggc.it.imez;

public class NetWorkIO {
	private ModelImEz modelImEz;
	public NetWorkIO(ModelImEz modelImEz) {
		this.modelImEz = modelImEz;
		SocketInThread socketInThread = new SocketInThread(this);
	}

}
