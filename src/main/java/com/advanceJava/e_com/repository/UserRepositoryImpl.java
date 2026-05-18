package com.advanceJava.e_com.repository;

import com.advanceJava.e_com.models.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository{
    private final JdbcTemplate jdbc;
    public UserRepositoryImpl (JdbcTemplate jdbc){
        this.jdbc = jdbc;
    }

    private final RowMapper<User> rowMapper = (rs, rowNum) -> {
        User u = new User();
        u.setId(rs.getLong("id"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setRole(rs.getString("role"));
        return u;
    };



    @Override
    public User save(User user) {
        String sql = "INSERT INTO users (username, password, role) VALUES(?, ? , ?)";
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
            return ps;

        }, kh);
        user.setId(kh.getKey().longValue());
        return user;
    }

    @Override
    public Optional<User> findByUserName(String username) {
        List<User> result =
                jdbc.query("SELECT * FROM users WHERE username = ?", rowMapper, username);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }
}
