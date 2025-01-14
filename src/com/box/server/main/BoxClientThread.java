package com.box.server.main;

import java.awt.Color;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

class BoxClientThread extends Thread {
	private Socket s;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private boolean running = false;
	private BoxClientThreadManager manager;

	BoxClientThread(BoxClientThreadManager manager, Socket s) {
		this.manager = manager;
		this.s = s;

		try {
			this.ois = new ObjectInputStream(this.s.getInputStream());
			this.oos = new ObjectOutputStream(this.s.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		long now = System.nanoTime();
		final long timePerTick = 1000000000 / 60;
		long delta = 0;
		running = true;
		while (running) {

			try {
				if ((s.isClosed()) || (s.getOutputStream() == null) || (!s.isConnected()) || (s.isInputShutdown())
						|| (s.isOutputShutdown()) || (s.getInputStream() == null) || (s == null)) {
					running = false;
					this.ois.close();
					this.oos.close();
					this.s.close();
					break;
				}
				now = System.nanoTime();
				delta = (now - lastTime);

				if (delta >= timePerTick) {
					lastTime = now;
					handleSocket();
				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
				running = false;
				try {
					this.ois.close();
					this.oos.close();
					this.s.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				break;
			}
		}
	}

	private void handleSocket() throws IOException, ClassNotFoundException {
		// System.out.println("Handling socket");
		try {
			// getObjectOutputStream().flush();
			int av = this.ois.available();
			if (av > 0) {
				System.out.println("Server received data, writing to other clients...");
				double x = this.ois.readDouble();
				double y = this.ois.readDouble();
				Color color = (Color) this.ois.readObject();
				System.out.println("Found: ");
				for (int i = 0; i < manager.boxClients().size(); i++) {
					BoxClientThread client = manager.boxClients().get(i);
					if (client == this) {
						continue;
					}
					client.getObjectOutputStream().writeDouble(x);
					client.getObjectOutputStream().writeDouble(y);
					client.getObjectOutputStream().writeObject(color);
					client.getObjectOutputStream().flush();
				}
			}

		} catch (EOFException | SocketException e) {
			e.printStackTrace();
			running = false;
			this.ois.close();
			this.oos.close();
			this.s.close();
			return;
		}
	}

	public Socket getSocket() {
		return s;
	}

	public void setSocket(Socket s) {
		this.s = s;
	}

	public ObjectInputStream getObjectInputStream() {
		return ois;
	}

	public void setObjectInputStream(ObjectInputStream ois) {
		this.ois = ois;
	}

	public ObjectOutputStream getObjectOutputStream() {
		return oos;
	}

	public void setObjectOutputStream(ObjectOutputStream oos) {
		this.oos = oos;
	}

	public BoxClientThreadManager getBoxClientThreadManager() {
		return manager;
	}

	public void setBoxClientThreadManager(BoxClientThreadManager manager) {
		this.manager = manager;
	}
}
