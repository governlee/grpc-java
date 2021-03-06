/*
 * Copyright 2018, gRPC Authors All rights reserved.
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

package io.grpc.services;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import io.grpc.ConnectivityState;
import io.grpc.internal.Channelz;
import io.grpc.internal.Channelz.ChannelStats;
import io.grpc.internal.Channelz.Security;
import io.grpc.internal.Channelz.ServerStats;
import io.grpc.internal.Channelz.SocketOptions;
import io.grpc.internal.Channelz.SocketStats;
import io.grpc.internal.Channelz.TransportStats;
import io.grpc.internal.Instrumented;
import io.grpc.internal.LogId;
import io.grpc.internal.WithLogId;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Collections;

/**
 * Test class definitions that will be used in the proto utils test as well as
 * channelz service test.
 */
final class ChannelzTestHelper {

  static final class TestSocket implements Instrumented<SocketStats> {
    private final LogId id = LogId.allocate("socket");
    TransportStats transportStats = new TransportStats(
        /*streamsStarted=*/ 1,
        /*lastLocalStreamCreatedTimeNanos=*/ 2,
        /*lastRemoteStreamCreatedTimeNanos=*/ 3,
        /*streamsSucceeded=*/ 4,
        /*streamsFailed=*/ 5,
        /*messagesSent=*/ 6,
        /*messagesReceived=*/ 7,
        /*keepAlivesSent=*/ 8,
        /*lastMessageSentTimeNanos=*/ 9,
        /*lastMessageReceivedTimeNanos=*/ 10,
        /*localFlowControlWindow=*/ 11,
        /*remoteFlowControlWindow=*/ 12);
    SocketAddress local = new InetSocketAddress("10.0.0.1", 1000);
    SocketAddress remote = new InetSocketAddress("10.0.0.2", 1000);
    Channelz.SocketOptions socketOptions = new Channelz.SocketOptions.Builder().build();

    @Override
    public ListenableFuture<SocketStats> getStats() {
      SettableFuture<SocketStats> ret = SettableFuture.create();
      ret.set(
          new SocketStats(
              transportStats,
              local,
              remote,
              socketOptions,
              new Security()));
      return ret;
    }

    @Override
    public LogId getLogId() {
      return id;
    }
  }

  static final class TestListenSocket implements Instrumented<SocketStats> {
    private final LogId id = LogId.allocate("listensocket");
    SocketAddress listenAddress = new InetSocketAddress("10.0.0.1", 1234);

    @Override
    public ListenableFuture<SocketStats> getStats() {
      SettableFuture<SocketStats> ret = SettableFuture.create();
      ret.set(
          new SocketStats(
              /*data=*/ null,
              listenAddress,
              /*remoteAddress=*/ null,
              new SocketOptions.Builder().build(),
              /*security=*/ null));
      return ret;
    }

    @Override
    public LogId getLogId() {
      return id;
    }
  }

  static final class TestServer implements Instrumented<ServerStats> {
    private final LogId id = LogId.allocate("server");
    ServerStats serverStats = new ServerStats(
        /*callsStarted=*/ 1,
        /*callsSucceeded=*/ 2,
        /*callsFailed=*/ 3,
        /*lastCallStartedMillis=*/ 4,
        Collections.<Instrumented<SocketStats>>emptyList());

    @Override
    public ListenableFuture<ServerStats> getStats() {
      SettableFuture<ServerStats> ret = SettableFuture.create();
      ret.set(serverStats);
      return ret;
    }

    @Override
    public LogId getLogId() {
      return id;
    }
  }

  static final class TestChannel implements Instrumented<ChannelStats> {
    private final LogId id = LogId.allocate("channel-or-subchannel");

    ChannelStats stats = new ChannelStats(
      /*target=*/ "sometarget",
      /*state=*/ ConnectivityState.READY,
      /*callsStarted=*/ 1,
      /*callsSucceeded=*/ 2,
      /*callsFailed=*/ 3,
      /*lastCallStartedMillis=*/ 4,
      /*subchannels=*/ Collections.<WithLogId>emptyList(),
      /*sockets=*/ Collections.<WithLogId>emptyList());

    @Override
    public ListenableFuture<ChannelStats> getStats() {
      SettableFuture<ChannelStats> ret = SettableFuture.create();
      ret.set(stats);
      return ret;
    }

    @Override
    public LogId getLogId() {
      return id;
    }
  }
}
