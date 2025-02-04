package org.xipki.ca.sdk;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 *
 * @author Lijun Liao
 * @since 6.0.0
 */

public class UpdateCertsRequest extends CertsRequest {

  private List<UpdateCertRequestEntry> entries;

  public List<UpdateCertRequestEntry> getEntries() {
    return entries;
  }

  public void setEntries(List<UpdateCertRequestEntry> entries) {
    this.entries = entries;
  }

  public static UpdateCertsRequest decode(byte[] encoded) {
    return JSON.parseObject(encoded, UpdateCertsRequest.class);
  }

}
