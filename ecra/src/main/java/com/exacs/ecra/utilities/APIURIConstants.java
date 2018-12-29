package com.exacs.ecra.utilities;

public class APIURIConstants {

   public static final String V2_ECRA = "/v2/ecra";

   public static final String V2_ECRA_RACK = V2_ECRA + "/" + "racks";

   public static final String V2_ECRA_RACK_ID = V2_ECRA_RACK + "/" + "{rackId}";

   public static final String V2_ECRA_RACK_ID_CLUSTERS = V2_ECRA_RACK_ID + "/" + "clusters";

   public static final String V2_ECRA_RACK_ID_CLUSTER_ID = V2_ECRA_RACK_ID_CLUSTERS + "/" + "{clusterId}";

   public static final String V2_ECRA_RACK_ID_CLUSTER_ID_NODES_NODE_ID_VMS =
           V2_ECRA_RACK_ID_CLUSTERS + "/" + "{clusterId}" + "/" + "nodes" + "/" + "{nodeId}" + "/" + "vms";

   public static final String V2_ECRA_RACK_ID_CLUSTER_ID_VMS =
           V2_ECRA_RACK_ID_CLUSTERS + "/" + "{clusterId}" + "/" + "vms";

   public static final String V2_ECRA_RACK_ID_CLUSTER_ID_VMS_ID =
           V2_ECRA_RACK_ID_CLUSTER_ID_NODES_NODE_ID_VMS + "/" + "{vmId}";

   public static final String V2_ECRA_RACK_ID_CLUSTER_ID_NODES_NODE_ID =
         V2_ECRA_RACK_ID_CLUSTERS + "/" + "{clusterId}" + "/" + "nodes" + "/" + "{nodeId}";


 }
