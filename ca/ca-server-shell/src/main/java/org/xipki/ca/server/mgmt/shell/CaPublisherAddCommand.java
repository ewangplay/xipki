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

package org.xipki.ca.server.mgmt.shell;

import java.util.List;

import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.Option;

@Command(scope = "ca", name = "capub-add", description="Add publisher to CA")
public class CaPublisherAddCommand extends CaCommand {
	@Option(name = "-ca",
            description = "Required. CA name",
            required = true)
    protected String           caName;

	@Option(name = "-publisher",
		required = true, multiValued = true, description = "Required. Publisher name. Multivalued")
	protected List<String>     publisherNames;

    @Override
    protected Object doExecute() throws Exception {
    	for(String publisherName : publisherNames)
    	{
    		caManager.addPublisherToCA(publisherName, caName);
    	}
    	return null;
    }
}
