package bmtDataGen;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;


public class GenRsaInsertDB {

	private static final int COMMIT_COUNT = 100;
	
	private static final int KEY_SIZE = 2048;
	
	private static final String[] BANK_CODE = {"002", "003", "004", "005", "007", "010", "020", "021", "023", "027", "031", "032", "034", "035", "037", "039", "089", "090"};
	private static final String[] STATE_CODE = {"Seoul", "Pusan", "Jeju", "Daegu", "Incheon", "Seoul", "Daejeon", "Gyeonggi", "Gyeongnam", "Gyeongbuk", "Jeonnam", "Jeonbuk", "Gangwon", "Chungnam", "Chungbuk", "Gwanju", "Seoul", "Seoul"};

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		if (args.length != 2) {
			System.err.println("2 parameter needed");
			System.exit(9);
		}
		
		long totalCnt = insertDB(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		
		System.out.println("Good job~! Generated " + totalCnt + " rows.");
	}

	private static ArrayList<String> createRsa() throws NoSuchAlgorithmException, NoSuchProviderException, IOException {
		Security.addProvider(new BouncyCastleProvider());

		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");
		generator.initialize(KEY_SIZE);

		KeyPair keyPair = generator.generateKeyPair();

		RSAPrivateKey priv = (RSAPrivateKey) keyPair.getPrivate();
		RSAPublicKey pub = (RSAPublicKey) keyPair.getPublic();

		PemObject privPemObject = new PemObject("RSA PRIVATE KEY", priv.getEncoded());
		PemObject pubPemObject = new PemObject("RSA PUBLIC KEY", pub.getEncoded());

		ArrayList<String> keyList = new ArrayList<String>();
		
		String privPemStr = convertPemString(privPemObject);
		privPemStr = privPemStr.replaceAll("\\n", "");
		privPemStr = privPemStr.replaceAll("\\r", "");
		
		String pubPemStr = convertPemString(pubPemObject);
		pubPemStr = pubPemStr.replaceAll("\\n", "");
		pubPemStr = pubPemStr.replaceAll("\\r", "");
		
		keyList.add(privPemStr);
		keyList.add(pubPemStr);

		return keyList;
	}

	private static String convertPemString(PemObject pemObj) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		PemWriter pemWriter = new PemWriter(new OutputStreamWriter(bos));
		pemWriter.writeObject(pemObj);
		pemWriter.close();
		return new String(bos.toByteArray(),"UTF-8");
	}

	private static CertModel genData(long seedCount) throws Exception {
		
		int index = (int) (seedCount % 18);

		CertModel cert = new CertModel();
		cert.setSeq(seedCount);
		cert.setId(BANK_CODE[index] + fullFormat(seedCount, '0', 12));
		cert.setJuminNum(fullFormat(seedCount, '7', 13));
		cert.setCountry("KR");
		cert.setState(STATE_CODE[index]);
		cert.setLocation("");
		cert.setOrganization("SK");
		cert.setOrganizationUnit("Blockchain group");
		cert.setCompanyName("SKBC Co.");
		cert.setSubjectName("Test");
		cert.setHashMsg(getHash(cert.getJuminNum()));
		cert.setSignMsg(getSign(cert.getHashMsg()));
		cert.setSignMsg4Login(getSign(cert.getHashMsg() + cert.getId()));
		
		ArrayList<String> keyList = createRsa();
		
		cert.setPrivPem(keyList.get(0));
		cert.setPubPem(keyList.get(1));

		return cert;
	}
	
	private static String getHash(String originalString) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(originalString.getBytes(StandardCharsets.UTF_8));
		return new String(Hex.encode(hash));
	}
	
	private static String getSign(String originalString) throws Exception {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

	    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA", "BC");

	    keyGen.initialize(512, new SecureRandom());

	    KeyPair keyPair = keyGen.generateKeyPair();
//	    Signature signature = Signature.getInstance("SHA1withRSA", "BC");
	    Signature signature = Signature.getInstance("SHA256withRSA", "BC");

	    signature.initSign(keyPair.getPrivate(), new SecureRandom());

	    byte[] message = originalString.getBytes();
	    signature.update(message);

	    byte[] sigBytes = signature.sign();
	    
	    return new String(Hex.encode(sigBytes));
	}
	
	private static String fullFormat(long value, char c, int minCount) {
		String s = "" + value;
		int maxLen = minCount - s.length();
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<maxLen; i++) {
			sb.append(c);
		}
		return sb.toString() + s;
	}

	private static long insertDB(int startNum, int endNum) throws Exception {
		Connection con = null; 
		Statement stmt = null;
		long totalCnt = 0;
		long cnt = 0;
		try { 
			Class.forName("org.hsqldb.jdbc.JDBCDriver"); 
			con = DriverManager.getConnection( "jdbc:hsqldb:hsql://localhost/xdb", "SA", ""); 
			stmt = con.createStatement();
			for (int i=startNum; i<=endNum; i++) {
				CertModel cert = genData(i);
				stmt.executeUpdate(cert.getInsertSql());
				totalCnt++;
				if (cnt == COMMIT_COUNT) {
					con.commit(); 
					cnt = 0;
				} else {
					cnt++;
				}
			}
			con.commit(); 
			
			return totalCnt;
		} catch (Exception e) { 
			e.printStackTrace(System.out); 
			throw new Exception(e.getMessage());
		}
	}

}
