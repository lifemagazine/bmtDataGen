package bmtDataGen;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;

import org.bouncycastle.util.encoders.Hex;


public class TestSingVerify {
	
	public static void main(String[] args) throws Exception {
		doTest();
		System.out.println("#######################");
		doTest();
	}
	
	private static void doTest() throws Exception  {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

	    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA", "BC");

	    keyGen.initialize(512, new SecureRandom());

	    KeyPair keyPair = keyGen.generateKeyPair();
//	    Signature signature = Signature.getInstance("SHA1withRSA", "BC");
	    Signature signature = Signature.getInstance("SHA256withRSA", "BC");

	    signature.initSign(keyPair.getPrivate(), new SecureRandom());

	    byte[] message = "1111117777777".getBytes();
	    signature.update(message);

	    byte[] sigBytes = signature.sign();
	    System.out.println(new String(Hex.encode(sigBytes)));
	    signature.initVerify(keyPair.getPublic());
	    signature.update(message);
	    System.out.println(signature.verify(sigBytes));
	}
}
