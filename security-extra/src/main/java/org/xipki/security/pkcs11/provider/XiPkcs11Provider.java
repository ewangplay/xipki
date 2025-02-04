/*
 *
 * Copyright (c) 2013 - 2020 Lijun Liao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.xipki.security.pkcs11.provider;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.Provider;

/**
 * The XIPKI PKCS#11 Provider class.
 * Supported algorithms:
 *
 * <p>Keystore
 * <ul>
 *   <li><code>PKCS11</code></li>
 * </ul>
 *
 * <p>Signature (RSA)
 * <ul>
 *   <li><code>NONEwithRSA</code></li>
 *   <li><code>SHA1withRSA</code></li>
 *   <li><code>SHA224withRSA</code></li>
 *   <li><code>SHA256withRSA</code></li>
 *   <li><code>SHA384withRSA</code></li>
 *   <li><code>SHA512withRSA</code></li>
 *   <li><code>SHA3-224withRSA</code></li>
 *   <li><code>SHA3-256withRSA</code></li>
 *   <li><code>SHA3-384withRSA</code></li>
 *   <li><code>SHA3-512withRSA</code></li>
 *   <li><code>SHA1withRSAandMGF1</code></li>
 *   <li><code>SHA224withRSAandMGF1</code></li>
 *   <li><code>SHA256withRSAandMGF1</code></li>
 *   <li><code>SHA384withRSAandMGF1</code></li>
 *   <li><code>SHA512withRSAandMGF1</code></li>
 *   <li><code>SHA3-224withRSAandMGF1</code></li>
 *   <li><code>SHA3-256withRSAandMGF1</code></li>
 *   <li><code>SHA3-384withRSAandMGF1</code></li>
 *   <li><code>SHA3-512withRSAandMGF1</code></li>
 * </ul>
 *
 * <p>Signature (DSA)
 * <ul>
 *   <li><code>NONEwithDSA</code></li>
 *   <li><code>SHA1withDSA</code></li>
 *   <li><code>SHA224withDSA</code></li>
 *   <li><code>SHA256withDSA</code></li>
 *   <li><code>SHA384withDSA</code></li>
 *   <li><code>SHA512withDSA</code></li>
 *   <li><code>SHA3-224withDSA</code></li>
 *   <li><code>SHA3-256withDSA</code></li>
 *   <li><code>SHA3-384withDSA</code></li>
 *   <li><code>SHA3-512withDSA</code></li>
 * </ul>
 *
 * <p>Signature (ECDSA)
 * <ul>
 *   <li><code>NONEwithECDSA</code></li>
 *   <li><code>SHA1withECDSA</code></li>
 *   <li><code>SHA224withECDSA</code></li>
 *   <li><code>SHA256withECDSA</code></li>
 *   <li><code>SHA384withECDSA</code></li>
 *   <li><code>SHA512withECDSA</code></li>
 *   <li><code>SHA3-224withECDSA</code></li>
 *   <li><code>SHA3-256withECDSA</code></li>
 *   <li><code>SHA3-384withECDSA</code></li>
 *   <li><code>SHA3-512withECDSA</code></li>
 *
 *   <li><code>NONEwithPlain-ECDSA</code></li>
 *   <li><code>SHA1withPlain-ECDSA</code></li>
 *   <li><code>SHA224withPlain-ECDSA</code></li>
 *   <li><code>SHA256withPlain-ECDSA</code></li>
 *   <li><code>SHA384withPlain-ECDSA</code></li>
 *   <li><code>SHA512withPlain-ECDSA</code></li>
 *
 *   <li><code>SM3withSM2</code></li>
 * </ul>
 *
 * @author Lijun Liao
 * @since 2.0.0
 */

public class XiPkcs11Provider extends Provider {

  @SuppressWarnings("rawtypes")
  private static class MyPrivilegedAction implements PrivilegedAction {

    private final XiPkcs11Provider provider;

    MyPrivilegedAction(XiPkcs11Provider provider) {
      this.provider = provider;
    }

    @Override
    public Object run() {
      provider.put("KeyStore.PKCS11", XiKeyStoreSpi.class.getName());

      provider.put("Signature.NONEwithRSA", P11RSADigestSignatureSpi.NoneRSA.class.getName());
      provider.put("Alg.Alias.Signature.RSAwithNONE", "NONEwithRSA");

      provider.put("Signature.SHA1withRSA", P11RSADigestSignatureSpi.SHA1.class.getName());
      provider.put("Alg.Alias.Signature.RSAwithSHA1", "SHA1withRSA");

      provider.put("Signature.SHA224withRSA", P11RSADigestSignatureSpi.SHA224.class.getName());
      provider.put("Alg.Alias.Signature.RSAwithSHA224", "SHA224withRSA");

      provider.put("Signature.SHA256withRSA", P11RSADigestSignatureSpi.SHA256.class.getName());
      provider.put("Alg.Alias.Signature.RSAwithSHA256", "SHA256withRSA");

      provider.put("Signature.SHA384withRSA", P11RSADigestSignatureSpi.SHA384.class.getName());
      provider.put("Alg.Alias.Signature.RSAwithSHA384", "SHA384withRSA");

      provider.put("Signature.SHA512withRSA", P11RSADigestSignatureSpi.SHA512.class.getName());
      provider.put("Alg.Alias.Signature.RSAwithSHA512", "SHA512withRSA");

      provider.put("Signature.SHA3-224withRSA", P11RSADigestSignatureSpi.SHA3_224.class.getName());
      provider.put("Alg.Alias.Signature.RSAwithSHA3-224", "SHA3-224withRSA");

      provider.put("Signature.SHA3-256withRSA", P11RSADigestSignatureSpi.SHA3_256.class.getName());
      provider.put("Alg.Alias.Signature.RSAwithSHA3-256", "SHA3-256withRSA");

      provider.put("Signature.SHA3-384withRSA", P11RSADigestSignatureSpi.SHA3_384.class.getName());
      provider.put("Alg.Alias.Signature.RSAwithSHA3-384", "SHA3-384withRSA");

      provider.put("Signature.SHA3-512withRSA", P11RSADigestSignatureSpi.SHA3_512.class.getName());
      provider.put("Alg.Alias.Signature.RSAwithSHA3-512", "SHA3-512withRSA");

      provider.put("Signature.NONEwithDSA", P11DSASignatureSpi.NONE.class.getName());
      provider.put("Alg.Alias.Signature.DSAwithNONE", "NONEwithDSA");

      provider.put("Signature.SHA1withDSA", P11DSASignatureSpi.SHA1.class.getName());
      provider.put("Alg.Alias.Signature.DSAwithSHA1", "SHA1withDSA");

      provider.put("Signature.SHA224withDSA", P11DSASignatureSpi.SHA224.class.getName());
      provider.put("Alg.Alias.Signature.DSAwithSHA224", "SHA224withDSA");

      provider.put("Signature.SHA256withDSA", P11DSASignatureSpi.SHA256.class.getName());
      provider.put("Alg.Alias.Signature.DSAwithSHA256", "SHA256withDSA");

      provider.put("Signature.SHA384withDSA", P11DSASignatureSpi.SHA384.class.getName());
      provider.put("Alg.Alias.Signature.DSAwithSHA384", "SHA384withDSA");

      provider.put("Signature.SHA512withDSA", P11DSASignatureSpi.SHA512.class.getName());
      provider.put("Alg.Alias.Signature.DSAwithSHA512", "SHA512withDSA");

      provider.put("Signature.SHA3-224withDSA", P11DSASignatureSpi.SHA3_224.class.getName());
      provider.put("Alg.Alias.Signature.DSAwithSHA3-224", "SHA3-224withDSA");

      provider.put("Signature.SHA3-256withDSA", P11DSASignatureSpi.SHA3_256.class.getName());
      provider.put("Alg.Alias.Signature.DSAwithSHA3-256", "SHA3-256withDSA");

      provider.put("Signature.SHA3-384withDSA", P11DSASignatureSpi.SHA3_384.class.getName());
      provider.put("Alg.Alias.Signature.DSAwithSHA3-384", "SHA3-384withDSA");

      provider.put("Signature.SHA3-512withDSA", P11DSASignatureSpi.SHA3_512.class.getName());
      provider.put("Alg.Alias.Signature.DSAwithSHA3-512", "SHA3-512withDSA");

      provider.put("Signature.NONEwithECDSA", P11ECDSASignatureSpi.NONE.class.getName());
      provider.put("Alg.Alias.Signature.ECDSAwithNONE", "NONEwithECDSA");

      provider.put("Signature.SHA1withECDSA", P11ECDSASignatureSpi.SHA1.class.getName());
      provider.put("Alg.Alias.Signature.ECDSAwithSHA1", "SHA1withECDSA");

      provider.put("Signature.SHA224withECDSA", P11ECDSASignatureSpi.SHA224.class.getName());
      provider.put("Alg.Alias.Signature.ECDSAwithSHA224", "SHA224withECDSA");

      provider.put("Signature.SHA256withECDSA", P11ECDSASignatureSpi.SHA256.class.getName());
      provider.put("Alg.Alias.Signature.ECDSAwithSHA256", "SHA256withECDSA");

      provider.put("Signature.SHA384withECDSA", P11ECDSASignatureSpi.SHA384.class.getName());
      provider.put("Alg.Alias.Signature.ECDSAwithSHA384", "SHA384withECDSA");

      provider.put("Signature.SHA512withECDSA", P11ECDSASignatureSpi.SHA512.class.getName());
      provider.put("Alg.Alias.Signature.ECDSAwithSHA512", "SHA512withECDSA");

      provider.put("Signature.SHA3-224withECDSA", P11ECDSASignatureSpi.SHA3_224.class.getName());
      provider.put("Alg.Alias.Signature.ECDSAwithSHA3-224", "SHA3-224withECDSA");

      provider.put("Signature.SHA3-256withECDSA", P11ECDSASignatureSpi.SHA3_256.class.getName());
      provider.put("Alg.Alias.Signature.ECDSAwithSHA3-256", "SHA3-256withECDSA");

      provider.put("Signature.SHA3-384withECDSA", P11ECDSASignatureSpi.SHA3_384.class.getName());
      provider.put("Alg.Alias.Signature.ECDSAwithSHA3-384", "SHA3-384withECDSA");

      provider.put("Signature.SHA3-512withECDSA", P11ECDSASignatureSpi.SHA3_512.class.getName());
      provider.put("Alg.Alias.Signature.ECDSAwithSHA3-512", "SHA3-512withECDSA");

      provider.put("Signature.NONEwithPlain-ECDSA", P11PlainECDSASignatureSpi.NONE.class.getName());
      provider.put("Alg.Alias.Signature.Plain-ECDSAwithNONE", "NONEwithPlain-ECDSA");
      provider.put("Alg.Alias.Signature.NONEwithPlainECDSA", "NONEwithPlain-ECDSA");
      provider.put("Alg.Alias.Signature.PlainECDSAwithNONE", "NONEwithPlain-ECDSA");

      provider.put("Signature.SHA1withPlain-ECDSA", P11PlainECDSASignatureSpi.SHA1.class.getName());
      provider.put("Alg.Alias.Signature.Plain-ECDSAwithSHA1", "SHA1withPlain-ECDSA");
      provider.put("Alg.Alias.Signature.SHAwithPlainECDSA1", "SHA1withPlain-ECDSA");
      provider.put("Alg.Alias.Signature.PlainECDSAwithSHA1", "SHA1withPlain-ECDSA");

      provider.put("Signature.SHA224withPlain-ECDSA", P11PlainECDSASignatureSpi.SHA224.class.getName());
      provider.put("Alg.Alias.Signature.Plain-ECDSAwithSHA224", "SHA224withPlain-ECDSA");
      provider.put("Alg.Alias.Signature.SHA224withPlainECDSA", "SHA224withPlain-ECDSA");
      provider.put("Alg.Alias.Signature.PlainECDSAwithSHA224", "SHA224withPlain-ECDSA");

      provider.put("Signature.SHA256withPlain-ECDSA", P11PlainECDSASignatureSpi.SHA256.class.getName());
      provider.put("Alg.Alias.Signature.Plain-ECDSAwithSHA256", "SHA256withPlain-ECDSA");
      provider.put("Alg.Alias.Signature.SHA256withPlainECDSA", "SHA256withPlain-ECDSA");
      provider.put("Alg.Alias.Signature.PlainECDSAwithSHA256", "SHA256withPlain-ECDSA");

      provider.put("Signature.SHA384withPlain-ECDSA", P11PlainECDSASignatureSpi.SHA384.class.getName());
      provider.put("Alg.Alias.Signature.Plain-ECDSAwithSHA384", "SHA384withPlain-ECDSA");
      provider.put("Alg.Alias.Signature.SHA384withPlainECDSA", "SHA384withPlain-ECDSA");
      provider.put("Alg.Alias.Signature.PlainECDSAwithSHA384", "SHA384withPlain-ECDSA");

      provider.put("Signature.SHA512withPlain-ECDSA", P11PlainECDSASignatureSpi.SHA512.class.getName());
      provider.put("Alg.Alias.Signature.Plain-ECDSAwithSHA512", "SHA512withPlain-ECDSA");
      provider.put("Alg.Alias.Signature.SHA512withPlainECDSA", "SHA512withPlain-ECDSA");
      provider.put("Alg.Alias.Signature.PlainECDSAwithSHA512", "SHA512withPlain-ECDSA");

      provider.put("Signature.SM3withSM2", P11SM3WithSM2SignatureSpi.class.getName());
      provider.put("Alg.Alias.Signature.SM2withSM3", "SM3withSM2");

      provider.put("Signature.SHA1withRSAandMGF1", P11RSAPSSSignatureSpi.SHA1withRSA.class.getName());
      provider.put("Alg.Alias.Signature.RSAandMGF1withSHA1", "SHA1withRSAandMGF1");

      provider.put("Signature.SHA224withRSAandMGF1", P11RSAPSSSignatureSpi.SHA224withRSA.class.getName());
      provider.put("Alg.Alias.Signature.RSAandMGF1withSHA224", "SHA224withRSAandMGF1");

      provider.put("Signature.SHA256withRSAandMGF1", P11RSAPSSSignatureSpi.SHA256withRSA.class.getName());
      provider.put("Alg.Alias.Signature.RSAandMGF1withSHA256", "SHA256withRSAandMGF1");

      provider.put("Signature.SHA384withRSAandMGF1", P11RSAPSSSignatureSpi.SHA384withRSA.class.getName());
      provider.put("Alg.Alias.Signature.RSAandMGF1withSHA384", "SHA384withRSAandMGF1");

      provider.put("Signature.SHA512withRSAandMGF1", P11RSAPSSSignatureSpi.SHA512withRSA.class.getName());
      provider.put("Alg.Alias.Signature.RSAandMGF1withSHA512", "SHA512withRSAandMGF1");

      provider.put("Signature.SHA3-224withRSAandMGF1", P11RSAPSSSignatureSpi.SHA3_224withRSA.class.getName());
      provider.put("Alg.Alias.Signature.RSAandMGF1withSHA3-224", "SHA3-224withRSAandMGF1");

      provider.put("Signature.SHA3-256withRSAandMGF1", P11RSAPSSSignatureSpi.SHA3_256withRSA.class.getName());
      provider.put("Alg.Alias.Signature.RSAandMGF1withSHA3-256", "SHA3-256withRSAandMGF1");

      provider.put("Signature.SHA3-384withRSAandMGF1", P11RSAPSSSignatureSpi.SHA3_384withRSA.class.getName());
      provider.put("Alg.Alias.Signature.RSAandMGF1withSHA3-384", "SHA3-384withRSAandMGF1");

      provider.put("Signature.SHA3-512withRSAandMGF1", P11RSAPSSSignatureSpi.SHA3_512withRSA.class.getName());
      provider.put("Alg.Alias.Signature.RSAandMGF1withSHA3-512", "SHA3-512withRSAandMGF1");

      return null;
    } // method run

  } // class MyPrivilegedAction

  /**
   * Exactly the name this provider is registered under at
   * <code>java.security.Security</code>: "<code>XIPKI-P11</code>".
   */
  public static final String PROVIDER_NAME = "XIPKI-P11";

  /**
   * Version of this provider as registered at
   * <code>java.security.Security</code>.
   */
  public static final double PROVIDER_VERSION = 1.0;

  /**
   * An informational text giving the name and the version of this provider
   * and also telling about the provided algorithms.
   */
  private static final String PROVIDER_INFO = "XiPKI PKCS#11 JCA/JCE provider";

  private static final long serialVersionUID = 1L;

  @SuppressWarnings("unchecked")
  public XiPkcs11Provider() {
    super(PROVIDER_NAME, PROVIDER_VERSION, PROVIDER_INFO);
    AccessController.doPrivileged(new MyPrivilegedAction(this));
  }

}
