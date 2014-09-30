/*
 * Copyright 2005-2014 Red Hat, Inc.
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */
package org.hornetq.core.server.cluster.ha;

import org.hornetq.api.config.HornetQDefaultConfiguration;
import org.hornetq.core.server.impl.HornetQServerImpl;
import org.hornetq.core.server.impl.LiveActivation;
import org.hornetq.core.server.impl.SharedNothingLiveActivation;

import java.util.Map;

public class ReplicatedPolicy implements HAPolicy<LiveActivation>
{
   private boolean checkForLiveServer = HornetQDefaultConfiguration.isDefaultCheckForLiveServer();

   private boolean allowAutoFailBack = HornetQDefaultConfiguration.isDefaultAllowAutoFailback();

   private long failbackDelay = HornetQDefaultConfiguration.getDefaultFailbackDelay();

   private String groupName = null;

   private String clusterName;

   /*
   * this is used as the policy when the server is started after a failover
   * */
   private ReplicaPolicy replicaPolicy;


   public ReplicatedPolicy()
   {
   }

   public ReplicatedPolicy(boolean checkForLiveServer, boolean allowAutoFailBack, long failbackDelay, String groupName, String clusterName)
   {
      this.checkForLiveServer = checkForLiveServer;
      this.allowAutoFailBack = allowAutoFailBack;
      this.failbackDelay = failbackDelay;
      this.groupName = groupName;
      this.clusterName = clusterName;
      /*
      * we create this with sensible defaults in case we start after a failover
      * */
      replicaPolicy = new ReplicaPolicy(clusterName, -1, groupName, this);
   }

   public ReplicatedPolicy(boolean checkForLiveServer, boolean allowAutoFailBack, long failbackDelay, String groupName, String clusterName, ReplicaPolicy replicaPolicy)
   {
      this.checkForLiveServer = checkForLiveServer;
      this.allowAutoFailBack = allowAutoFailBack;
      this.failbackDelay = failbackDelay;
      this.groupName = groupName;
      this.replicaPolicy = replicaPolicy;
   }

   public boolean isCheckForLiveServer()
   {
      return checkForLiveServer;
   }

   public void setCheckForLiveServer(boolean checkForLiveServer)
   {
      this.checkForLiveServer = checkForLiveServer;
   }

   public boolean isAllowAutoFailBack()
   {
      return allowAutoFailBack;
   }

   public void setAllowAutoFailBack(boolean allowAutoFailBack)
   {
      this.allowAutoFailBack = allowAutoFailBack;
   }

   public long getFailbackDelay()
   {
      return failbackDelay;
   }

   public void setFailbackDelay(long failbackDelay)
   {
      this.failbackDelay = failbackDelay;
   }

   public String getClusterName()
   {
      return clusterName;
   }

   public void setClusterName(String clusterName)
   {
      this.clusterName = clusterName;
   }

   public ReplicaPolicy getReplicaPolicy()
   {
      return replicaPolicy;
   }

   public void setReplicaPolicy(ReplicaPolicy replicaPolicy)
   {
      this.replicaPolicy = replicaPolicy;
   }

   /*
   * these 2 methods are the same, leaving both as the second is correct but the first is needed until more refactoring is done
   * */
   public String getBackupGroupName()
   {
      return groupName;
   }

   public String getGroupName()
   {
      return groupName;
   }

   @Override
   public String getScaleDownGroupName()
   {
      return null;
   }

   public void setGroupName(String groupName)
   {
      this.groupName = groupName;
   }

   @Override
   public boolean isSharedStore()
   {
      return false;
   }

   @Override
   public boolean isBackup()
   {
      return false;
   }

   @Override
   public boolean canScaleDown()
   {
      return false;
   }

   @Override
   public String getScaleDownClustername()
   {
      return null;
   }

   @Override
   public LiveActivation createActivation(HornetQServerImpl server, boolean wasLive, Map<String, Object> activationParams, HornetQServerImpl.ShutdownOnCriticalErrorListener shutdownOnCriticalIO)
   {
      return new SharedNothingLiveActivation(server, this);
   }
}
