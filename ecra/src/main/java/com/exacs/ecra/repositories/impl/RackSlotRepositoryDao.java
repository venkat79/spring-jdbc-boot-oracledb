package com.exacs.ecra.repositories.impl;


import com.exacs.ecra.entities.enums.RackSlotColNames;
import com.exacs.ecra.entities.model.Rack;
import com.exacs.ecra.entities.model.RackSlot;
import com.exacs.ecra.entities.model.VirtualMachine;
import com.exacs.ecra.repositories.inf.RackRepository;
import com.exacs.ecra.repositories.inf.RackSlotRepository;
import com.exacs.ecra.repositories.inf.VirtualMachineRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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


@Repository
public class RackSlotRepositoryDao implements RackSlotRepository  {

    private static final Logger _logger = LoggerFactory.getLogger(RackSlotRepositoryDao.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RackRepository rackRepository;

    @Autowired
    @Lazy
    private VirtualMachineRepository virtualMachineRepository;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    // Table names
    private static final String RACK_SLOT = "rack_slot";

    // Named parameters
    private static final String RACK_ID = "rackId";
    private static final String RACK_SLOT_IDS= "rackSlotIds";
    private static final String RACK_SLOT_ID = "rackSlotId";

    // SQLs
    private static final String GET_ALL_CLUSTERS_RACK_ID =
            "select "+RackSlotColNames.id+", "+RackSlotColNames.name+", "+RackSlotColNames.exadata_rack_id+" from "+RACK_SLOT+" " +
                    "where "+RackSlotColNames.exadata_rack_id+" = :"+RACK_ID+"";
    private static final String GET_CLUSTER_CLUSTER_ID =
            "select "+RackSlotColNames.id+", "+RackSlotColNames.name+", "+RackSlotColNames.exadata_rack_id+" from "+RACK_SLOT+" where "+RackSlotColNames.id+" in (:"+RACK_SLOT_IDS+")";
    private static final String GET_CLUSTER_CLUSTER_ID_RACK_ID =
            "select "+RackSlotColNames.id+", "+RackSlotColNames.name+", "+RackSlotColNames.exadata_rack_id+" from "+RACK_SLOT+" " +
                    "where "+RackSlotColNames.id+" = :"+RACK_SLOT_ID+" and "+RackSlotColNames.exadata_rack_id+"= :"+RACK_ID+"";


    // SQL - Delete
    private static final String DELETE_SQL_BY_RACK_SLOT_IDS = "DELETE FROM "+RACK_SLOT+" where "+RackSlotColNames.id+" in (:"+RACK_SLOT_IDS+")";
    private static final String DELETE_SQL_BY_RACK_ID = "DELETE FROM "+RACK_SLOT+" where "+RackSlotColNames.exadata_rack_id+" = :"+RACK_ID+"";

    @Override
    public RackSlot create(RackSlot rackSlot) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert
                .withTableName(RACK_SLOT)
                .usingGeneratedKeyColumns(RackSlotColNames.id.name());

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(RackSlotColNames.name.name(), rackSlot.getName())
                .addValue(RackSlotColNames.exadata_rack_id.name(), rackSlot.getRack().getId());
        Number number = simpleJdbcInsert.executeAndReturnKey(params);
        rackSlot.setId(number.longValue());

        return rackSlot;
    }

    @Override
    public List<RackSlot> findAllClusters(long rackId, boolean lazy) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue(RACK_ID, rackId);
        return (getClusters(parameters, GET_ALL_CLUSTERS_RACK_ID, lazy));
    }

    @Override
    public RackSlot findClusterByRackIdAndClusterId(long rackId, long rackSlotId, boolean lazy) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue(RACK_SLOT_ID, rackSlotId);
        parameters.addValue(RACK_ID, rackId);
        return getCluster(parameters, GET_CLUSTER_CLUSTER_ID_RACK_ID, lazy);
    }

    @Override
    public RackSlot findClusterById(long rackSlotId, boolean lazy) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue(RACK_SLOT_IDS, Collections.singletonList(rackSlotId));
        return getCluster(parameters, GET_CLUSTER_CLUSTER_ID, lazy);

    }

    @Override
    public List<RackSlot> findClusterByIds(List<Long> rackSlotIds, boolean lazy) {
        if (CollectionUtils.isEmpty(rackSlotIds)) {
            return null;
        } else {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue(RACK_SLOT_IDS, rackSlotIds);
            return (getClusters(parameters, GET_CLUSTER_CLUSTER_ID, lazy));
        }
    }

    private List<RackSlot> getClusters(MapSqlParameterSource parameters, String query, boolean lazy) {

        List<RackSlot> rackSlotList;
        if (lazy) {
            rackSlotList = (List<RackSlot>) namedParameterJdbcTemplate.query(query,
                    parameters, new RackSlotLazyMapper());
        } else {
            rackSlotList = (List<RackSlot>) namedParameterJdbcTemplate.query(query,
                    parameters, new RackSlotEagerExtractor());
        }


        return rackSlotList;
    }

    private RackSlot getCluster(MapSqlParameterSource parameters, String query, boolean lazy) {

        RackSlot rackSlot;
        List<RackSlot> rackSlotList;

        if (lazy) {
            rackSlotList = (List<RackSlot>) namedParameterJdbcTemplate.query(query,
                    parameters, new RackSlotLazyMapper());
        } else {
            rackSlotList  = (List<RackSlot>) namedParameterJdbcTemplate.query(query,
                    parameters, new RackSlotRepositoryDao.RackSlotEagerExtractor());
        }


        if (CollectionUtils.isEmpty(rackSlotList)) {
            rackSlot = null;
        } else {
            rackSlot = rackSlotList.get(0);
        }


        return rackSlot;
    }

    @Override
    public void deleteAllClusters(long rackId) {

        Rack rack = rackRepository.findRackById(rackId, false);
        if (rack != null) {
            Map namedParameters = Collections.singletonMap(RACK_ID, rackId);
            namedParameterJdbcTemplate.update(DELETE_SQL_BY_RACK_ID, namedParameters);
        }

    }

    @Override
    public void deleteCluster(long rackSlotId) {

        _logger.debug("RackSlot Id is {}", rackSlotId);
        RackSlot rackSlot = findClusterById(rackSlotId, false);
        _logger.debug("RackSlot is : {}", rackSlot.toString());

        if (rackSlot != null) {
                Map namedParameters = Collections.singletonMap(RACK_SLOT_IDS, rackSlotId);
                namedParameterJdbcTemplate.update(DELETE_SQL_BY_RACK_SLOT_IDS, namedParameters);
        }

    }

    // Mapper to map the table entries to java object
    // Lazy work
    private class RackSlotLazyMapper implements ResultSetExtractor {

        @Override
        public List<RackSlot> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<RackSlot> rackSlotList = new ArrayList();
            while (rs.next()) {
                RackSlot rackSlot  = new RackSlot();
                rackSlot.setId(rs.getLong(RackSlotColNames.id.name()));
                rackSlot.setName(rs.getString(RackSlotColNames.name.name()));
                rackSlotList.add(rackSlot);
            }

            return rackSlotList;

        }

    }

    // Mapper to map dependent objects as well
    // DEPENDS on join query we construct (or) java based retrieval
    // Eager fetch
    private class RackSlotEagerExtractor implements ResultSetExtractor {
        @Override
        public List<RackSlot> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            Map<Long,RackSlot> rackSlotMap = new HashMap();

            RackSlot rackSlot;
            Long rackId = null;
            while (resultSet.next()) {
                long rackSlotId = resultSet.getLong(RackSlotColNames.id.name());
                rackId = resultSet.getLong(RackSlotColNames.exadata_rack_id.name());
                String rackSlotName = resultSet.getString(RackSlotColNames.name.name());

                rackSlot = new RackSlot(rackSlotId, rackSlotName);
                rackSlotMap.put(rackSlot.getId(), rackSlot);
            }

            // Fetch rack from RackRepository (eager)
            Rack rack = rackRepository.findRackById(rackId, false);
            if (rack != null) {
                rackSlotMap.entrySet().stream().forEach(mapEntry -> mapEntry.getValue().setRack(rack));
                // Can be done using left join but for sake of differentiation, showing this as java based
                rackSlotMap.entrySet().stream().forEach(mapEntry -> {
                    List<VirtualMachine> virtualMachineList = virtualMachineRepository.findVMsForCluster(mapEntry.getKey(), true);
                    mapEntry.getValue().setVirtualMachinesList(new ArrayList());

                    if (!CollectionUtils.isEmpty(virtualMachineList)) {
                        mapEntry.getValue().getVirtualMachinesList().addAll(virtualMachineList);
                    }

                });
            }


            // Immutable array list of RackSlot objects
            return new ArrayList(rackSlotMap.values());

        }
    }
}
