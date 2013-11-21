package uk.org.dulwich.mydulwich.ntlm;

import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeFactory;
import org.apache.http.impl.auth.NTLMScheme;
import org.apache.http.params.HttpParams;

public class NTLMSchemeFactory implements AuthSchemeFactory {

        @Override
        public AuthScheme newInstance(HttpParams arg0) {
                
                return new NTLMScheme(new JCIFSEngine());
        }

}