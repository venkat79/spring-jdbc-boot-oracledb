package com.exacs.ecra.repositories.impl;

import com.exacs.ecra.entities.enums.RackColNames;
import com.exacs.ecra.entities.enums.RackSlotColNames;
import com.exacs.ecra.entities.enums.RackType;
import com.exacs.ecra.entities.model.ComputeNode;
import com.exacs.ecra.entities.model.Rack;

import com.exacs.ecra.repositories.inf.RackRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class RackRepositoryDao implements RackRepository {

    private static final Logger _logger = LoggerFactory.getLogger(RackRepositoryDao.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    

    // Table names
    private static final String RACK = "rack";
    private static final String COMPUTE_NODE = "compute_node";

    // Named parameters
    private static final String RACK_IDS = "rackIds";
    private static final String COMPUTE_NODE_IDS = "computeNodeIds";


    // SQLs - Fetch
    private static final String FETCH_SQL_BY_ID_COMPLETE = "SELECT r.id AS \"rack_id\", " +
            "r.name AS \"rack_name\", " +
            "r.rack_type, \n" +
            "cn.id AS \"compute_node_id\", cn.name " +
            "AS \"compute_node_name\" FROM "+RACK+" r LEFT JOIN compute_node cn " +
            "ON r.id = cn.EXADATA_RACK_ID where r.id in (:"+RACK_IDS+")";
    private static final String FETCH_SQL_COMPLETE =
            "SELECT r.id AS \"rack_id\", r.name AS \"rack_name\", r.rack_type, \n" +
                    "cn.id AS \"compute_node_id\", cn.name AS \"compute_node_name\" " +
                    "FROM rack r LEFT JOIN "+COMPUTE_NODE+" cn ON r.id = cn.EXADATA_RACK_ID";

    // SQLs - Delete
    private static final String DELETE_SQL_BY_RACK_IDS = "DELETE FROM "+RACK+" where "+RackColNames.id+" in (:"+RACK_IDS+")";
    private static final String DELETE_SQL_BY_COMPUTE_NODE_IDS = "DELETE FROM "+COMPUTE_NODE+" where "+RackColNames.id+" in (:"+COMPUTE_NODE_IDS+")";


    @Override
    public void deleteAllRacks() {
        List<Rack> rackList = findAll(false);
        if (!CollectionUtils.isEmpty(rackList)) {
            List<Long> rackIds = rackList.stream().map(x -> x.getId()).collect(Collectors.toList());
            delete(rackIds);
        }
    }

    @Override
    public void delete(List<Long> rackIds) {

        // Get the information -- delete all relationships and delete the main stuff
        List<Rack> rackList = findRackByIds(rackIds, false);

        if (!CollectionUtils.isEmpty(rackList)) {
            List<Long> computeNodeIds = new ArrayList();
            if (!CollectionUtils.isEmpty(rackList)) {
                for (Rack rack: rackList) {
                    Set<ComputeNode> computeNodeList = rack.getComputeNodeList();
                    computeNodeIds.addAll(computeNodeList.stream().map(x -> x.getId()).collect(Collectors.toList()));
                }

                if (!CollectionUtils.isEmpty(computeNodeIds)) {
                    Map namedParameters = Collections.singletonMap(COMPUTE_NODE_IDS, computeNodeIds);
                    namedParameterJdbcTemplate.update(DELETE_SQL_BY_COMPUTE_NODE_IDS, namedParameters);
                }

                Map namedParameters = Collections.singletonMap(RACK_IDS, rackIds);
                namedParameterJdbcTemplate.update(DELETE_SQL_BY_RACK_IDS, namedParameters);

            }
        }

    }

    @Override
    public void delete(long rackId) {
        delete(Collections.singletonList(rackId));
    }

    @Override
    public Rack create(Rack rack) {

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert
                .withTableName(RACK)
                .usingGeneratedKeyColumns(RackColNames.id.name());

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", rack.getName())
                .addValue("rack_type", rack.getRackType().name());
        Number number = simpleJdbcInsert.executeAndReturnKey(params);
        rack.setId(number.longValue());

        // Insert for compute nodes
        if (!CollectionUtils.isEmpty(rack.getComputeNodeList())) {

            for (ComputeNode computeNode : rack.getComputeNodeList()) {

                // Different elegant way to insert
                simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
                simpleJdbcInsert
                        .withTableName(COMPUTE_NODE)
                        .usingGeneratedKeyColumns(RackSlotColNames.id.name());

                params = new MapSqlParameterSource()
                        .addValue(RackSlotColNames.name.name(), computeNode.getName())
                        .addValue(RackSlotColNames.exadata_rack_id.name(), rack.getId());
                number = simpleJdbcInsert.executeAndReturnKey(params);
                computeNode.setId(number.longValue());

                rack.getComputeNodeList().add(computeNode);

            }

        }

        return rack;
    }

    @Override
    public List<Rack> findAll(boolean lazy) {
        if (lazy) {
            return (List<Rack>) jdbcTemplate.query(FETCH_SQL_COMPLETE, new RackLazyMapper());
        } else {
            return (List<Rack>) jdbcTemplate.query(FETCH_SQL_COMPLETE, new RackEagerExtractor());
        }

    }

    @Override
    public List<Rack> findRackByIds(List<Long> rackIds, boolean lazy) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue(RACK_IDS, rackIds);
        List<Rack> rackList;
        if (lazy) {
            rackList = (List<Rack>) namedParameterJdbcTemplate.query(FETCH_SQL_BY_ID_COMPLETE,
                    parameters, new RackLazyMapper());
        } else {
            rackList = (List<Rack>) namedParameterJdbcTemplate.query(FETCH_SQL_BY_ID_COMPLETE,
                    parameters, new RackEagerExtractor());
        }

        return rackList;

    }

    @Override
    public Rack findRackById(long rackId, boolean lazy) {

       List<Rack> rackList = findRackByIds(Collections.singletonList(rackId), lazy);
       if (CollectionUtils.isEmpty(rackList)) {
           return null;
       } else {
           return rackList.size() > 0 ? rackList.get(0) : null;
       }

    }

    // Mapper to map the table entries to java object
    // Lazy work
    private class RackLazyMapper implements ResultSetExtractor {

        @Override
        public List<Rack> extractData(ResultSet rs) throws SQLException,DataAccessException {
            List<Rack> rackList = new ArrayList();
            while (rs.next()) {
                Rack rack = new Rack();
                rack.setId(rs.getLong(RackColNames.id.name()));
                rack.setName(rs.getString(RackColNames.name.name()));
                rack.setRackType(RackType.valueOf(rs.getString(RackColNames.rack_type.name())));
                rackList.add(rack);
            }

            return rackList;
        }

    }

    // Mapper to map dependent objects as well
    // DEPENDS on join query we construct
    // Eager fetch
    private class RackEagerExtractor implements ResultSetExtractor {
        @Override
        public List<Rack> extractData(ResultSet resultSet) throws SQLException,DataAccessException {
            Map<Long,Rack> rackMap = new HashMap();

            Rack rack = null;
            while (resultSet.next()) {
                long rackId = resultSet.getLong("rack_id");

                if (rackMap.get(rackId) == null) {
                    String rackName = resultSet.getString("rack_name");
                    RackType rackType = RackType.valueOf(resultSet.getString("rack_type"));
                    rack = new Rack(rackId, rackName, rackType);
                    rackMap.put(rack.getId(), rack);
                }

                Long computeNodeId = resultSet.getLong("compute_node_id");
                if (computeNodeId != null) {
                    String computeNodeName = resultSet.getString("compute_node_name");
                    ComputeNode computeNode = new ComputeNode(computeNodeId, computeNodeName);
                    computeNode.setRack(rack);
                    rack.getComputeNodeList().add(computeNode);
                }

            }

            // Immutable array list of Rack objects
            return new ArrayList(rackMap.values());

        }
    }


}
