package com.advanceJava.e_com.repository;

import com.advanceJava.e_com.models.Product;
import org.springframework.data.relational.core.sql.Update;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepositoryImpl implements ProductRepository{
    private final JdbcTemplate jdbc;
    public ProductRepositoryImpl(JdbcTemplate jdbc){
        this.jdbc = jdbc;
    }

    private final RowMapper<Product> rowMapper = (rs, rowNum) -> {
        Product p = new Product();
        p.setId(rs.getLong("id"));
        p.setName(rs.getString("name"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setStockQuantity(rs.getInt("stock_quantity"));
        p.setImagePath(rs.getString("image_path"));
        return p;
    };


    @Override
    public Product save(Product p) {
        String sql = "INSERT INTO products (name, description, price, stock_quantity, image_path) VALUES (?,?,?,?,?)";
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, p.getName());
            ps.setString(2, p.getDescription());
            ps.setBigDecimal(3, p.getPrice());
            ps.setInt(4, p.getStockQuantity());
            ps.setString(5, p.getImagePath());
            return ps;
        }, kh);
        p.setId(kh.getKey().longValue());
        return p;

    }

    @Override
    public Optional<Product> findById(Long id) {
        List<Product> r = jdbc.query("SELECT * FROM products WHERE id=?", rowMapper, id);
        return r.isEmpty() ? Optional.empty() : Optional.of(r.get(0));

    }

    @Override
    public List<Product> findAll() {
        return jdbc.query("SELECT * FROM products", rowMapper);

    }

    @Override
    public void deleteById(Long id) {
        jdbc.update("DELETE FROM products WHERE id=?", id);
    }

    @Override
    public void updateStock(Long id, int newStock) {
        jdbc.update("UPDATE products SET stock_quantity=? WHERE id=?", newStock, id);

    }
}
