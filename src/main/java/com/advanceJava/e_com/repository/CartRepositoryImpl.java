package com.advanceJava.e_com.repository;

import com.advanceJava.e_com.models.CartItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class CartRepositoryImpl implements CartRepository {
    private final JdbcTemplate jdbc;
    public CartRepositoryImpl(JdbcTemplate jdbc){
        this.jdbc = jdbc;
    }
    private final RowMapper<CartItem> rowMapper = (rs, rowNum) -> {
        CartItem c = new CartItem();
        c.setId(rs.getLong("id"));
        c.setUserId(rs.getLong("user_id"));
        c.setProductId(rs.getLong("product_id"));
        c.setQuantity(rs.getInt("quantity"));
        return c;

    };


    @Override
    public CartItem save(CartItem item) {
        String sql = "INSERT INTO cart_items (user_id, product_id, quantity) VALUES (?,?,?)";
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setLong(1, item.getUserId());
            ps.setLong(2, item.getProductId());
            ps.setInt(3, item.getQuantity());
            return ps;

        }, kh);
        item.setId(kh.getKey().longValue());
        return item;
    }

    @Override
    public List<CartItem> findByUserId(Long userId) {
        return jdbc.query("SELECT * FROM cart_items WHERE user_id=?", rowMapper, userId);
    }

    @Override
    public Optional<CartItem> findByUserIdAndProductId(Long userId, Long productId) {
        List<CartItem> r = jdbc.query("SELECT * FROM cart_items WHERE user_id=? AND product_id=?", rowMapper, userId, productId);
        return r.isEmpty() ? Optional.empty() : Optional.of(r.get(0));
    }

    @Override
    public void updateQuantity(Long id, int quantity) {
        jdbc.update("UPDATE cart_items SET quantity=? WHERE id=?", quantity, id);
    }

    @Override
    public void deleteById(Long id) {
        jdbc.update("DELETE FROM cart_items WHERE id=?", id);
    }

    @Override
    public void clearByUserId(Long userId) {
        jdbc.update("DELETE FROM cart_items WHERE user_id=?", userId);
    }
}
