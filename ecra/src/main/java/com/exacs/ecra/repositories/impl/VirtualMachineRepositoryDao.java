package com.exacs.ecra.repositories.impl;

import com.exacs.ecra.entities.enums.VirtualMachineColNames;
import com.exacs.ecra.entities.model.ComputeNode;
import com.exacs.ecra.entities.model.RackSlot;
import com.exacs.ecra.entities.model.VirtualMachine;
import com.exacs.ecra.repositories.inf.RackSlotRepository;
import com.exacs.ecra.repositories.inf.VirtualMachineRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class VirtualMachineRepositoryDao implements VirtualMachineRepository {

    private static final Logger _logger = LoggerFactory.getLogger(VirtualMachineRepositoryDao.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private RackSlotRepository rackSlotRepository;

    // Table names
    private static final String VIRTUAL_MACHINE = "virtual_machine";

    // Named parameters
    private static final String VIRTUAL_MACHINE_IDS = "virtualMachineIds";
    private static final String RACK_SLOT_ID = "rackSlotId";
    private static final String COMPUTE_NODE_ID = "computeNodeId";
    private static final String VIRTUAL_MACHINE_ID = "virtualMachineId";

    // SQLs
    private static final String FETCH_SQL_BY_ID =
            "select * from "+VIRTUAL_MACHINE+" where "+ VirtualMachineColNames.id+" in (:"+VIRTUAL_MACHINE_IDS+")";
    private static final String FETCH_SQL_BY_RACK_SLOT_ID =
            "select * from "+VIRTUAL_MACHINE+" where "+ VirtualMachineColNames.rack_slot_id+" =:"+RACK_SLOT_ID+"";
    private static final String FETCH_SQL_BY_RACK_SLOT_ID_NODEID =
            "select * from "+VIRTUAL_MACHINE+" where "+ VirtualMachineColNames.rack_slot_id+" =:"+RACK_SLOT_ID+" and "+VirtualMachineColNames.compute_node_id+" =:"+COMPUTE_NODE_ID+"";


    // Delete SQLs
    private static final String DELETE_VM_BY_ID =
            "delete from "+VIRTUAL_MACHINE+" where id = :virtualMachineId";

    @Override
    public VirtualMachine createVM(VirtualMachine virtualMachine) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert
                .withTableName(VIRTUAL_MACHINE)
                .usingGeneratedKeyColumns(VirtualMachineColNames.id.name());

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(VirtualMachineColNames.name.name(), virtualMachine.getName())
                .addValue(VirtualMachineColNames.compute_node_id.name(), virtualMachine.getComputeNode().getId())
                .addValue(VirtualMachineColNames.rack_slot_id.name(), virtualMachine.getRackSlot().getId());
        Number number = simpleJdbcInsert.executeAndReturnKey(params);
        virtualMachine.setId(number.longValue());

        return virtualMachine;
    }

    @Override
    public void deleteVM(long id) {
        Map namedParameters = Collections.singletonMap(VIRTUAL_MACHINE_ID, id);
        namedParameterJdbcTemplate.update(DELETE_VM_BY_ID, namedParameters);
    }

    @Override
    public VirtualMachine findById(long id, boolean lazy) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue(VIRTUAL_MACHINE_IDS, id);
        return getVM(parameters, FETCH_SQL_BY_ID, lazy);
    }

    @Override
    public List<VirtualMachine> findVMsForCluster(long rackSlotId, boolean lazy) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue(RACK_SLOT_ID, rackSlotId);
        return getVMS(parameters, FETCH_SQL_BY_RACK_SLOT_ID, lazy);
    }

    @Override
    public VirtualMachine findVMForClusterAndNode(long rackSlotId, long nodeId, boolean lazy) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue(RACK_SLOT_ID, rackSlotId);
        parameters.addValue(COMPUTE_NODE_ID, nodeId);
        return getVM(parameters, FETCH_SQL_BY_RACK_SLOT_ID_NODEID, lazy);
    }

    private List<VirtualMachine> getVMS(MapSqlParameterSource parameters, String query, boolean lazy) {

        List<VirtualMachine> virtualMachinesList;
        if (lazy) {
            virtualMachinesList = (List<VirtualMachine>) namedParameterJdbcTemplate.query(query,
                    parameters, new VirtualMachineLazyMapper());
        } else {
            virtualMachinesList = (List<VirtualMachine>) namedParameterJdbcTemplate.query(query,
                    parameters, new VirtualMachineEagerExtractor());
        }


        return virtualMachinesList;
    }

    private VirtualMachine getVM(MapSqlParameterSource parameters, String query, boolean lazy) {

        VirtualMachine virtualMachine;
        List<VirtualMachine> virtualMachineList;
        if (lazy) {
            virtualMachineList = (List<VirtualMachine>) namedParameterJdbcTemplate.query(query, parameters, new VirtualMachineLazyMapper());
        } else {
            virtualMachineList  = (List<VirtualMachine>) namedParameterJdbcTemplate.query(query,
                    parameters, new VirtualMachineEagerExtractor());
        }

        if (CollectionUtils.isEmpty(virtualMachineList)) {
            virtualMachine = null;
        } else {
            virtualMachine = virtualMachineList.get(0);
        }


        return virtualMachine;
    }

    // Mapper to map the table entries to java object
    // Lazy work -- will not get relationships by default
    private class VirtualMachineLazyMapper implements ResultSetExtractor {

        @Override
        public List<VirtualMachine> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<VirtualMachine> virtualMachineList = new ArrayList();
            while (rs.next()) {
                VirtualMachine virtualMachine = new VirtualMachine(rs.getLong(VirtualMachineColNames.id.name()),
                        rs.getString(VirtualMachineColNames.name.name()));
                virtualMachineList.add(virtualMachine);
            }

            _logger.debug("Virtual machines list : {}", virtualMachineList.toString());

            return virtualMachineList;
        }

    }

    // Mapper to map dependent objects as well
    // DEPENDS on join query we construct (or) java based filtering
    // Eager fetch
    private class VirtualMachineEagerExtractor implements ResultSetExtractor {
        @Override
        public List<VirtualMachine> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            Map<Long,Long> vmComputeNodeMap = new HashMap();
            Map<Long,Long> vmClusterMap = new HashMap();
            Map<Long,VirtualMachine> virtualMachineMap = new HashMap();

            VirtualMachine virtualMachine;

            while (resultSet.next()) {
                long id = resultSet.getLong(VirtualMachineColNames.id.name());
                long computeNodeId = resultSet.getLong(VirtualMachineColNames.compute_node_id.name());
                long clusterId = resultSet.getLong(VirtualMachineColNames.rack_slot_id.name());
                String name = resultSet.getString(VirtualMachineColNames.name.name());

                virtualMachine = new VirtualMachine(id, name);
                vmComputeNodeMap.put(id, computeNodeId);
                vmClusterMap.put(id, clusterId);
                virtualMachineMap.put(id, virtualMachine);

            }

            // Set computeNode, rackSlot
            List<RackSlot> rackSlotList = rackSlotRepository.findClusterByIds(new ArrayList(vmClusterMap.values()), false);
            if (!CollectionUtils.isEmpty(rackSlotList)) {
                final Map<Long,RackSlot> rackSlotMap =
                        rackSlotList.stream().collect(Collectors.toMap(RackSlot::getId, Function.identity()));
                virtualMachineMap.entrySet().stream()
                        .forEach(e -> {
                            VirtualMachine vm = e.getValue();
                            RackSlot rs = rackSlotMap.get(vmClusterMap.get(vm.getId()));
                            vm.setRackSlot(rs);
                            long computeNodeId = vmComputeNodeMap.get(vm.getId());
                            ComputeNode computeNode = rs.getRack().getComputeNodeList().stream()
                                                    .filter(cn -> cn.getId() == computeNodeId).findFirst()
                                                    .orElse(null);
                            vm.setComputeNode(computeNode);
                        });
            }

            // Immutable array list of VirtualMachine objects
            return new ArrayList(virtualMachineMap.values());

        }
    }
}