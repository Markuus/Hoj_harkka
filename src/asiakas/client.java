package asiakas;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import palvelin.WorkDistributor;

public class client {
	static ObjectInputStream oIn;
	static ObjectOutputStream oOut;
	static int maara;
	static SummausPalvelija[] summaajat;
	static SummausPalvelija[] threadit;
	public static void UDPLahetys() { //vieläkin antaa UDP erroreja

		int portti = 3126;
		String nimi = "localhost";
		String s="3000";
		byte[] lahtevaData = new byte[256];
		lahtevaData=s.getBytes();
		try {
			DatagramSocket soketti = new DatagramSocket();
			InetAddress kohdeOsoite = InetAddress.getByName(nimi);
			DatagramPacket paketti = new DatagramPacket(lahtevaData, lahtevaData.length, kohdeOsoite, portti);
			soketti.send(paketti);
		}
		catch (IOException x) {
			x.printStackTrace();
		}
	}
	public static void portinKuuntelija() throws Exception { //errori
		
		Socket soketti = null;
		ServerSocket serveri = new ServerSocket(3000);
		boolean etsiminen = true;
		try {
			soketti = serveri.accept();
			InputStream iS = soketti.getInputStream();
			OutputStream oS = soketti.getOutputStream();
			oOut = new ObjectOutputStream(oS);
			oIn = new ObjectInputStream(iS);
			Thread.sleep(2000);
			//int saapuva = oIn.readInt();
			//System.out.println("Saatu viesti: " + saapuva);
		}
		catch (IOException x) {
			x.printStackTrace();
		}


			//UDPLahetys();
	}
	public static void SummaajienMaara() throws IOException { //4
		maara = oIn.readInt();
		
	}
	public static void TeeSummaajat() throws IOException {
		summaajat = new SummausPalvelija[maara];
		threadit = new SummausPalvelija[maara];
		for(int i=0; i<maara; i++) {
			summaajat[i] = new SummausPalvelija(i, 5000+i);
			threadit[i] = summaajat[i];
			threadit[i].start();
			oOut.writeInt(summaajat[i].annaPortti());
			oOut.flush();
		}
	}
	
	public static void palvelimenKysymykset() throws IOException { //6
		int kysymys = oIn.readInt();
		for(int i=0; i<1; i++) {
		if(kysymys == 0) {
			oOut.writeInt(-1);
			oOut.flush();
			break;
		}else if(kysymys == 1) {
			oOut.writeInt(kokonaisSumma());
			oOut.flush();
			System.out.println("kokonaissumma annettu");
			break;
		}else if(kysymys == 2) {
			oOut.writeInt(suurinSumma());
			oOut.flush();
			System.out.println("suurin summa annettu");
			break;
		}else if(kysymys == 3) {
			//lukujen kokonaismäärä
		}else {
			//tulostaa -1 
		}
		}
	}
	
	public static int kokonaisSumma() {
		int summa = 0;
		for(int i=0; i<summaajat.length; i++) {
			summa = summa + summaajat[i].annakaikkienSumma();
		}
		return summa;
	}
	
	public static int suurinSumma() {
		int suurin = 0;
		int indeksi = 0;
		for(int i=0; i<summaajat.length; i++) {
			if(suurin < summaajat[i].annakaikkienSumma()) {
				suurin = summaajat[i].annakaikkienSumma();
				indeksi = i+1;
			}
		}
		return indeksi;
	}
}