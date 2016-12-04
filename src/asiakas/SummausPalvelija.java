package asiakas;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SummausPalvelija extends Thread {
	int numero;
	int portti;
	Socket soketti;
	ServerSocket serveri;
	boolean yhdistetty = false;
	int kaikkienSumma = 0;
	ObjectInputStream oIn;
	
	public SummausPalvelija(int i, int portti) throws IOException {
		this.numero = i;
		this.portti = portti;
		serveri = new ServerSocket(portti);
	}
	
	public void run() {
		try {
			soketti = serveri.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}
		yhdistetty = true;
		try {
			oIn = new ObjectInputStream(soketti.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		while(yhdistetty) {
			int luku;
			try {
				luku = oIn.readInt();
				kaikkienSumma = kaikkienSumma + luku;
				suljeSummaaja();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public int annaPortti() {
		return portti;
	}
	public int annakaikkienSumma() {
		return kaikkienSumma;
	}
	
	public void suljeSummaaja() throws IOException {
		soketti.close();
		yhdistetty = false;
	}
}
