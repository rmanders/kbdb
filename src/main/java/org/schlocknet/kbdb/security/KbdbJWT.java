package org.schlocknet.kbdb.security;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import lombok.Getter;
import lombok.Setter;
import java.util.Base64;
import static org.schlocknet.kbdb.config.Constants.CHAR_ENCODING;

/**
 *
 * @author Ryan
 */
public class KbdbJWT implements Serializable {
    
    private @Getter @Setter KbdbJWTHeader head = new KbdbJWTHeader();
    private @Getter @Setter KbdbJWTPayload payload = null;
    private @Getter @Setter byte[] signature;

    //<editor-fold defaultstate="collapsed" desc="encodeHeaderAndPayload">
    /**
     * Encodes the header and payload into base64
     *
     * @return
     * @throws java.io.UnsupportedEncodingException
     */
    public String encodeHeaderAndPayload() throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        sb
                .append(Base64.getEncoder().encode(
                        head.toJson().getBytes(CHAR_ENCODING)))
                .append(".")
                .append(Base64.getEncoder().encode(
                        payload.toJson().getBytes(CHAR_ENCODING)));
        return sb.toString();
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="encode">
    /**
     * Encodes the whole JWT into the proper base64 JWT format
     * @return
     * @throws UnsupportedEncodingException
     */
    public String encode() throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        sb
                .append(encodeHeaderAndPayload())
                .append(".")
                .append(Base64.getEncoder().encode(signature));
        return sb.toString();
    }
    //</editor-fold>
}
