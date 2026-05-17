package com.advanceJava.e_com.repository;

import com.advanceJava.e_com.models.ReturnRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class ReturnRequestRepositoryImpl implements ReturnRepository {

    private final JdbcTemplate jdbc;
    public ReturnRequestRepositoryImpl(JdbcTemplate jdbc){
        this.jdbc = jdbc;
    }

    private final RowMapper<ReturnRequest> rowMapper = (rs, rn)->{

            ReturnRequest r = new ReturnRequest();
            r.setId(rs.getLong("id"));
            r.setOrderId(rs.getLong("order_id"));
            r.setReason(rs.getString("reason"));
            r.setStatus(rs.getString("status"));
            r.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            return r;
    };

    @Override
    public ReturnRequest save(ReturnRequest r) {
        String sql = "INSERT INTO return_requests (order_id, reason, status) VALUES (?,?,?)";
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, r.getOrderId());
            ps.setString(2, r.getReason());
            ps.setString(3, r.getStatus());
            return ps;
        }, kh);
        r.setId(kh.getKey().longValue());
        return r;
    }

    @Override
    public Optional<ReturnRequest> findById(Long id) {
        List<ReturnRequest> result = jdbc.query("SELECT * FROM return_requests WHERE id=?", rowMapper, id);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public List<ReturnRequest> findAll() {
        return jdbc.query("SELECT * FROM return_requests ORDER BY created_at DESC", rowMapper);
    }



    @Override
    public List<ReturnRequest> findByOrderId(Long orderId) {
        return jdbc.query("SELECT * FROM return_requests WHERE order_id=?", rowMapper, orderId);
    }

    @Override
    public void updateStatus(Long id, String status) {
        jdbc.update("UPDATE return_requests SET status=? WHERE id=?", status, id);
    }
}
