/*
 * Copyright 2022 OpsMx, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.spinnaker.clouddriver.cloudrun.deploy.converters;

import com.netflix.spinnaker.clouddriver.cloudrun.CloudrunOperation;
import com.netflix.spinnaker.clouddriver.cloudrun.deploy.description.EnableDisableCloudrunDescription;
import com.netflix.spinnaker.clouddriver.cloudrun.deploy.ops.EnableCloudrunAtomicOperation;
import com.netflix.spinnaker.clouddriver.cloudrun.security.CloudrunNamedAccountCredentials;
import com.netflix.spinnaker.clouddriver.orchestration.AtomicOperation;
import com.netflix.spinnaker.clouddriver.orchestration.AtomicOperations;
import com.netflix.spinnaker.clouddriver.security.AbstractAtomicOperationsCredentialsConverter;
import java.util.Map;
import org.springframework.stereotype.Component;

@CloudrunOperation(AtomicOperations.ENABLE_SERVER_GROUP)
@Component
public class EnableCloudrunAtomicOperationConverter
    extends AbstractAtomicOperationsCredentialsConverter<CloudrunNamedAccountCredentials> {

  public AtomicOperation convertOperation(Map input) {
    return new EnableCloudrunAtomicOperation(convertDescription(input));
  }

  public EnableDisableCloudrunDescription convertDescription(Map input) {
    return CloudrunAtomicOperationConverterHelper.convertDescription(
        input, this, EnableDisableCloudrunDescription.class);
  }
}
