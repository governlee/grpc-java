/*
 * Copyright 2015, gRPC Authors All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.grpc.netty;

/**
 * Utility class for determining support for Jetty TLS ALPN/NPN.
 */
final class JettyTlsUtil {
  private JettyTlsUtil() {
  }

  private static Throwable jettyAlpnUnavailabilityCause;
  private static Throwable jettyNpnUnavailabilityCause;

  /**
   * Indicates whether or not the Jetty ALPN jar is installed in the boot classloader.
   */
  static synchronized boolean isJettyAlpnConfigured() {
    try {
      Class.forName("org.eclipse.jetty.alpn.ALPN", true, null);
      return true;
    } catch (ClassNotFoundException e) {
      jettyAlpnUnavailabilityCause = e;
      return false;
    }
  }

  static synchronized Throwable getJettyAlpnUnavailabilityCause() {
    // This case should be unlikely
    if (jettyAlpnUnavailabilityCause == null) {
      boolean discard = isJettyAlpnConfigured();
    }
    return jettyAlpnUnavailabilityCause;
  }

  /**
   * Indicates whether or not the Jetty NPN jar is installed in the boot classloader.
   */
  static synchronized boolean isJettyNpnConfigured() {
    try {
      Class.forName("org.eclipse.jetty.npn.NextProtoNego", true, null);
      return true;
    } catch (ClassNotFoundException e) {
      jettyNpnUnavailabilityCause = e;
      return false;
    }
  }

  static synchronized Throwable getJettyNpnUnavailabilityCause() {
    // This case should be unlikely
    if (jettyNpnUnavailabilityCause == null) {
      boolean discard = isJettyNpnConfigured();
    }
    return jettyNpnUnavailabilityCause;
  }
}