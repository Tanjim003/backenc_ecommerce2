package com.advanceJava.e_com.repository;

import com.advanceJava.e_com.models.Order;
import com.advanceJava.e_com.models.OrderItem;
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
public class OrderRepositoryImpl implements OrderRepository{
    private final JdbcTemplate jdbc;
    public OrderRepositoryImpl(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    private final RowMapper<Order> orderMapper = (rs, rn) -> {
        Order o = new Order();
        o.setId(rs.getLong("id"));
        o.setUserId(rs.getLong("user_id"));
        o.setStatus(rs.getString("status"));
        o.setTotalAmount(rs.getBigDecimal("total_amount"));
        o.setStreet(rs.getString("street"));
        o.setCity(rs.getString("city"));
        o.setPostalCode(rs.getString("postal_code"));
        o.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return o;
    };

    private final RowMapper<OrderItem> itemMapper = (rs, rn) -> {
        OrderItem i = new OrderItem();
        i.setId(rs.getLong("id"));
        i.setOrderId(rs.getLong("order_id"));
        i.setProductId(rs.getLong("product_id"));
        i.setQuantity(rs.getInt("quantity"));
        i.setPriceAtPurchase(rs.getBigDecimal("price_at_purchase"));
        return i;
    };

    @Override
    public Order save(Order o) {
        String sql = "INSERT INTO orders (user_id, status, total_amount, street, city, postal_code) VALUES (?,?,?,?,?,?)";
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, o.getUserId());
            ps.setString(2, o.getStatus());
            ps.setBigDecimal(3, o.getTotalAmount());
            ps.setString(4, o.getStreet());
            ps.setString(5, o.getCity());
            ps.setString(6, o.getPostalCode());
            return ps;
        }, kh);
        o.setId(kh.getKey().longValue());
        return o;
    }

    @Override
    public Optional<Order> findById(Long id) {
        List<Order> r = jdbc.query("SELECT * FROM orders WHERE id=?", orderMapper, id);
        return r.isEmpty() ? Optional.empty() : Optional.of(r.get(0));
    }

    @Override
    public List<Order> findAll() {
        return jdbc.query("SELECT * FROM orders ORDER BY created_at DESC", orderMapper);
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        return jdbc.query("SELECT * FROM orders WHERE user_id=? ORDER BY created_at DESC", orderMapper, userId);
    }

    @Override
    public void updateStatus(Long id, String status) {
        jdbc.update("UPDATE orders SET status=? WHERE id=?", status, id);
    }

    @Override
    public void saveOrderItem(OrderItem item) {
        jdbc.update("INSERT INTO order_items (order_id, product_id, quantity, price_at_purchase) VALUES (?,?,?,?)",
                item.getOrderId(), item.getProductId(), item.getQuantity(), item.getPriceAtPurchase());
    }

    @Override
    public List<OrderItem> findItemsByOrderId(Long orderId) {
        return jdbc.query("SELECT * FROM order_items WHERE order_id=?", itemMapper, orderId);
    }
}
