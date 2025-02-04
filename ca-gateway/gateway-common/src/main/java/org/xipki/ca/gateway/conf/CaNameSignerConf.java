package org.xipki.ca.gateway.conf;

import java.util.List;

/**
 *
 * @author Lijun Liao
 * @since 6.0.0
 */

public class CaNameSignerConf {

  private List<String> names;

  private SignerConf signer;

  public List<String> getNames() {
    return names;
  }

  public void setNames(List<String> names) {
    this.names = names;
  }

  public SignerConf getSigner() {
    return signer;
  }

  public void setSigner(SignerConf signer) {
    this.signer = signer;
  }
}
