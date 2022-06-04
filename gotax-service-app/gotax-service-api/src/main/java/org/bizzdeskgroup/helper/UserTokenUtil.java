package org.bizzdeskgroup.helper;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import org.bizzdeskgroup.Dto.MPJWTToken;
import org.bizzdeskgroup.Dtos.Query.InternalUserDto;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.io.InputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

/**
 * Utilities for generating a JWT for testing
 */
public class UserTokenUtil {

    public static final String PRIVATE_KEY_PEM = "/privateKey.pem";
    public static long TOKEN_TTL_MS = 8 * 60 * 60 * 1000; // 8 hours

    private UserTokenUtil() {
    }

    public static String generateUserTokenString(InternalUserDto user, MPJWTToken jwtContent) throws Exception {
//    public static Object generateUserTokenString(AuthenticatedUserDto user, MPJWTToken jwtContent) throws Exception {
        String issuer = "http://example.org/auth";
        PrivateKey key = readPrivateKey(PRIVATE_KEY_PEM);

        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
                .type(JOSEObjectType.JWT)
                .keyID(PRIVATE_KEY_PEM)
                .build();
        jwtContent.setAud(PRIVATE_KEY_PEM);
        
        jwtContent.setIss(issuer);  // Must match the expected issues configuration values
        jwtContent.setJti(UUID.randomUUID().toString());

        jwtContent.setUpn(user.email);
//        jwtContent.setSub("####"+user.id +"PaySure####"+user.firstName + user.lastName);
        jwtContent.setSub(user.id +"####PaySureService####"+user.mdaId +"####PaySureService####"+ user.projectId+"####PaySureService####"+ user.mdaOfficeId);

        List<String> userRole = new ArrayList<String>();
        userRole.add(user.role);
        jwtContent.setGroups(userRole);

        jwtContent.setIat(currentTimeInSecs());
        jwtContent.setExp(currentTimeInSecs() + TOKEN_TTL_MS); // 8 hours expiration!

        JWSObject jwsObject = new JWSObject(header, new Payload(jwtContent.toJSONString()));
        // Apply the Signing protection
        JWSSigner signer = new RSASSASigner(key);

        try {
            jwsObject.sign(signer);
        } catch (JOSEException e) {
            e.printStackTrace();
        }

        return jwsObject.serialize();
//        return jwsObject;

    }
    
    /**
     * Read a PEM encoded private key from the classpath
     *
     * @param pemResName - key file resource name
     * @return PrivateKey
     * @throws Exception on decode failure
     */
    public static PrivateKey readPrivateKey(String pemResName) throws Exception {
        InputStream contentIS = UserTokenUtil.class.getResourceAsStream(pemResName);
        byte[] tmp = new byte[4096];
        int length = contentIS.read(tmp);
        return decodePrivateKey(new String(tmp, 0, length));
    }

    /**
     * Read a PEM encoded public key from the classpath
     *
     * @param pemResName - key file resource name
     * @return PublicKey
     * @throws Exception on decode failure
     */
    public static PublicKey readPublicKey(String pemResName) throws Exception {
        InputStream contentIS = UserTokenUtil.class.getResourceAsStream(pemResName);
        byte[] tmp = new byte[4096];
        int length = contentIS.read(tmp);
        return decodePublicKey(new String(tmp, 0, length));
    }

    /**
     * Generate a new RSA keypair.
     *
     * @param keySize - the size of the key
     * @return KeyPair
     * @throws NoSuchAlgorithmException on failure to load RSA key generator
     */
    public static KeyPair generateKeyPair(int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator.genKeyPair();
    }

    /**
     * Decode a PEM encoded private key string to an RSA PrivateKey
     *
     * @param pemEncoded - PEM string for private key
     * @return PrivateKey
     * @throws Exception on decode failure
     */
    public static PrivateKey decodePrivateKey(String pemEncoded) throws Exception {
        pemEncoded = removeBeginEnd(pemEncoded);
        byte[] pkcs8EncodedBytes = Base64.getDecoder().decode(pemEncoded);

        // extract the private key

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8EncodedBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(keySpec);
    }

    /**
     * Decode a PEM encoded public key string to an RSA PublicKey
     *
     * @param pemEncoded - PEM string for private key
     * @return PublicKey
     * @throws Exception on decode failure
     */
    public static PublicKey decodePublicKey(String pemEncoded) throws Exception {
        pemEncoded = removeBeginEnd(pemEncoded);
        byte[] encodedBytes = Base64.getDecoder().decode(pemEncoded);

        X509EncodedKeySpec spec = new X509EncodedKeySpec(encodedBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    private static String removeBeginEnd(String pem) {
        pem = pem.replaceAll("-----BEGIN (.*)-----", "");
        pem = pem.replaceAll("-----END (.*)----", "");
        pem = pem.replaceAll("\r\n", "");
        pem = pem.replaceAll("\n", "");
        return pem.trim();
    }

    /**
     * @return the current time in seconds since epoch
     */
    public static long currentTimeInSecs() {
        long currentTimeMS = System.currentTimeMillis();
        return currentTimeMS;
    }

    /**
     * Enums to indicate which claims should be set to invalid values for testing failure modes
     */
    public enum InvalidClaims {
        ISSUER, // Set an invalid issuer
        EXP,    // Set an invalid expiration
        SIGNER, // Sign the token with the incorrect private key
        ALG, // Sign the token with the correct private key, but HS
    }
//    public static int ExtractTokenUserId(JsonWebToken jwt){
//
//        String[] createdByToken = jwt.getSubject().split("PaySure####");
//        return Integer.parseInt(createdByToken[0].replace("####", ""));
//    }
    public static String[] ExtractTokenUserMdaId(JsonWebToken jwt){

        return jwt.getSubject().split("####PaySureService####");
    }
  
}