package org.xipki.ca.sdk;

import com.alibaba.fastjson.JSON;

/**
 *
 * @author Lijun Liao
 * @since 6.0.0
 */

public class TransactionIdRequest extends SdkRequest {

  private String tid;

  public String getTid() {
    return tid;
  }

  public void setTid(String tid) {
    this.tid = tid;
  }

  public static TransactionIdRequest decode(byte[] encoded) {
    return JSON.parseObject(encoded, TransactionIdRequest.class);
  }

}
