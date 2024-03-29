/*
 * Copyright 2017 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.netflix.spinnaker.clouddriver.kubernetes.validator;

import com.google.common.base.Strings;
import com.netflix.spinnaker.clouddriver.deploy.ValidationErrors;
import com.netflix.spinnaker.clouddriver.kubernetes.description.manifest.KubernetesManifest;
import com.netflix.spinnaker.clouddriver.kubernetes.security.KubernetesCredentials;
import com.netflix.spinnaker.clouddriver.security.AccountCredentials;
import com.netflix.spinnaker.clouddriver.security.AccountCredentialsProvider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KubernetesValidationUtil {
  private static final Logger log = LoggerFactory.getLogger(KubernetesValidationUtil.class);
  private final String context;
  private final ValidationErrors errors;

  public KubernetesValidationUtil(String context, ValidationErrors errors) {
    this.context = context;
    this.errors = errors;
  }

  private String joinAttributeChain(String... attributes) {
    List<String> chain = new ArrayList<>();
    chain.add(context);
    Collections.addAll(chain, attributes);
    return String.join(".", chain);
  }

  public void reject(String errorName, String... attributes) {
    String field = joinAttributeChain(attributes);
    String error = joinAttributeChain(field, errorName);
    errors.reject(field, error);
  }

  public boolean validateNotEmpty(String attribute, Object value) {
    if (value == null) {
      reject("empty", attribute);
      return false;
    }

    return true;
  }

  public boolean validateCredentials(
      AccountCredentialsProvider provider, String accountName, KubernetesManifest manifest) {
    String namespace = manifest.getNamespace();
    return validateCredentials(provider, accountName, namespace);
  }

  public boolean validateCredentials(
      AccountCredentialsProvider provider, String accountName, String namespace) {
    log.info("Validating credentials for {} {}", accountName, namespace);
    if (Strings.isNullOrEmpty(accountName)) {
      reject("empty", "account");
      return false;
    }

    if (Strings.isNullOrEmpty(namespace)) {
      return true;
    }

    AccountCredentials<?> credentials = provider.getCredentials(accountName);
    if (credentials == null) {
      reject("notFound", "account");
      return false;
    }

    if (!(credentials.getCredentials() instanceof KubernetesCredentials)) {
      reject("wrongVersion", "account");
      return false;
    }

    return validateNamespace(namespace, (KubernetesCredentials) credentials.getCredentials());
  }

  protected boolean validateNamespace(String namespace, KubernetesCredentials credentials) {
    final List<String> configuredNamespaces = credentials.getNamespaces();
    if (configuredNamespaces != null
        && !configuredNamespaces.isEmpty()
        && !configuredNamespaces.contains(namespace)) {
      reject("wrongNamespace", namespace);
      return false;
    }

    final List<String> omitNamespaces = credentials.getOmitNamespaces();
    if (omitNamespaces != null && omitNamespaces.contains(namespace)) {
      reject("omittedNamespace", namespace);
      return false;
    }
    return true;
  }
}
