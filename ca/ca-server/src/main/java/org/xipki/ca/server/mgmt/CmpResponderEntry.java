/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This work is part of XiPKI, owned by Lijun Liao (lijun.liao@gmail.com)
 *
 */

package org.xipki.ca.server.mgmt;

import java.security.cert.X509Certificate;

import org.xipki.ca.server.X509Util;

public class CmpResponderEntry {
	public static final String name = "default";
	private String type;
	private String conf;
	private X509Certificate cert;
	
	public CmpResponderEntry() {
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getConf() {
		return conf;
	}

	public void setConf(String conf) {
		this.conf = conf;
	}

	public X509Certificate getCertificate() {
		return cert;
	}

	public void setCert(X509Certificate cert) {
		this.cert = cert;
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("name: ").append(name).append('\n');
		sb.append("type: ").append(type).append('\n');
		sb.append("conf: ").append(conf).append('\n');
		sb.append("cert: ").append("\n");
		sb.append("\tissuer: ").append(
				X509Util.canonicalizeName(cert.getIssuerX500Principal())).append("\n");
		sb.append("\tserialNumber: ").append(cert.getSerialNumber()).append("\n");
		sb.append("\tsubject: ").append(
				X509Util.canonicalizeName(cert.getSubjectX500Principal()));
		return sb.toString();
	}
	
}
