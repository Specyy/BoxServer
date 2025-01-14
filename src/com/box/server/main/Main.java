package com.box.server.main;

import java.net.ServerSocket;

@SuppressWarnings({ "static-access" })
public final class Main {
	private ServerSocket s;
	private boolean running = false;
	private static Main instance;
	private static int port;

	public Main(String[] args) throws BoxServerException {
		instance = this;
		if (args == null) {
			throw new BoxServerException("Run argument error. Format: port=port");
		}
		if (args.length <= 0) {
			throw new BoxServerException("Run argument error. Format: port=port");
		}
		if (!(args[0].split("=")[0].equalsIgnoreCase("port"))) {
			throw new BoxServerException("Run argument error. Format: port=port");
		}
		try {
			this.port = Integer.parseInt(args[0].split("=")[1]);
		} catch (IndexOutOfBoundsException e) {
			throw new BoxServerException("Run argument error. Format: port=port");
		}
		BoxClientThreadManager manager = new BoxClientThreadManager(this.port);
		manager.setRunning(true);
		manager.start();
	}

	public static void main(String[] args) {
		try {
			new Main(args);
		} catch (BoxServerException e) {
			e.printStackTrace();
		}
	}

	public static final Main instance() {
		return instance;
	}

}
