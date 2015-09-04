package org.schlocknet.kbdb.ws.requests;

import lombok.Getter;
import lombok.Setter;
import org.schlocknet.kbdb.model.JsonBase;

/**
 *
 * @author Ryan
 */
public class UserEmailRequest extends JsonBase {
    private @Getter @Setter String emailAddress;
}
