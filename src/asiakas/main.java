package asiakas;
import palvelin.WorkDistributor;

public class main {

	public static void main(String[] args) throws Exception {
		client.UDPLahetys();
		//client.muodostaYhteys();
		 //WorkDistributor.main(args);
		 client.portinKuuntelija();
		 client.SummaajienMaara();
		 client.TeeSummaajat();
		 client.palvelimenKysymykset();
	}

}