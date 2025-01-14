package com.box.server.main;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class BoxClientThreadManager extends Thread {
	private static final List<BoxClientThread> boxClients = new ArrayList<BoxClientThread>();
	private ServerSocket s;
	private final int port;
	private boolean running = false;

	BoxClientThreadManager(int port) {
		this.port = port;
	}

	private void createServer() throws IOException {
		try {
			this.s = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("[BoxServer] Could not create server.");
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		try {
			createServer();
		} catch (IOException e1) {
			e1.printStackTrace();
		}	running = true;
		while (running) {
			this.handleConnections();
		}
	}

	private void handleConnections() {
		// Socket s = this.s.accept();
		// dis = new DataInputStream(s.getInputStream());
		// dos = new DataOutputStream(s.getOutputStream());

		try {
			System.out.println(
					"[BoxServer] Waiting for clients...");
			Socket so = this.s.accept();
			System.out.println(
					"[BoxServer] Accepted client " + so.getInetAddress().getHostAddress() + ":" + so.getLocalPort());
			BoxClientThread boxClient = new BoxClientThread(this, so);
			if (!boxClients.contains(boxClient)) {
				boxClient.setRunning(true);
				boxClients.add(boxClient);
				boxClient.start();
			}
		} catch (Exception e) {
			System.out.println("[BoxServer] Could not connect to socket.");
			e.printStackTrace();
		}

	}

	List<BoxClientThread> boxClients() {
		return boxClients;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
}
