/*
 * Copyright (c) 2019 Schibsted Media Group.
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
package com.netflix.spinnaker.clouddriver.aws.deploy.description;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class DeployCloudFormationDescription extends AbstractAmazonCredentialsDescription {

  private String stackName;
  private String templateBody;
  private String templateURL;
  private String roleARN;
  private Map<String, String> parameters = new HashMap<>();
  private Map<String, String> tags = new HashMap<>();
  private String region;
  private List<String> capabilities = new ArrayList<>();
  private List<String> notificationARNs = new ArrayList<>();

  @JsonProperty("isChangeSet")
  private boolean isChangeSet;

  private String changeSetName;
}
