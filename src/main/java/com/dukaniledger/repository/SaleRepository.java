package com.dukaniledger.repository;

import com.dukaniledger.entity.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    Page<Sale> findByProduct_Category_Owner_Id(Long ownerId, Pageable pageable);

    Page<Sale> findByProductId(Long productId, Pageable pageable);

    // Every product for this shop, with lifetime revenue/cost/profit.
    // LEFT JOIN so products with zero sales still show up with 0 profit.
    @Query(value = """
            SELECT
                p.id AS productId,
                p.name AS productName,
                COALESCE(SUM(s.quantity), 0) AS quantitySold,
                COALESCE(SUM(s.selling_price * s.quantity), 0) AS totalRevenue,
                COALESCE(SUM(s.buying_price * s.quantity), 0) AS totalCost,
                COALESCE(SUM((s.selling_price - s.buying_price) * s.quantity), 0) AS totalProfit
            FROM products p
            JOIN categories c ON p.category_id = c.id
            LEFT JOIN sales s ON s.product_id = p.id
            WHERE c.owner_id = :ownerId
            GROUP BY p.id, p.name
            ORDER BY totalProfit DESC
            """, nativeQuery = true)
    List<ProductProfitProjection> findProfitByProduct(@Param("ownerId") Long ownerId);

    // Total profit for exactly one calendar day.
    @Query(value = """
            SELECT COALESCE(SUM((s.selling_price - s.buying_price) * s.quantity), 0)
            FROM sales s
            JOIN products p ON s.product_id = p.id
            JOIN categories c ON p.category_id = c.id
            WHERE c.owner_id = :ownerId AND DATE(s.sold_at) = :date
            """, nativeQuery = true)
    BigDecimal findProfitForDate(@Param("ownerId") Long ownerId, @Param("date") LocalDate date);

    // Day-by-day profit between two dates (end exclusive) for trend charts.
    @Query(value = """
            SELECT
                DATE(s.sold_at) AS saleDate,
                COALESCE(SUM((s.selling_price - s.buying_price) * s.quantity), 0) AS totalProfit
            FROM sales s
            JOIN products p ON s.product_id = p.id
            JOIN categories c ON p.category_id = c.id
            WHERE c.owner_id = :ownerId
              AND s.sold_at >= :start
              AND s.sold_at < :end
            GROUP BY DATE(s.sold_at)
            ORDER BY saleDate
            """, nativeQuery = true)
    List<DailyProfitProjection> findDailyProfitBetween(
            @Param("ownerId") Long ownerId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    // Total profit for exactly one calendar month.
    @Query(value = """
            SELECT COALESCE(SUM((s.selling_price - s.buying_price) * s.quantity), 0)
            FROM sales s
            JOIN products p ON s.product_id = p.id
            JOIN categories c ON p.category_id = c.id
            WHERE c.owner_id = :ownerId
              AND EXTRACT(YEAR FROM s.sold_at) = :year
              AND EXTRACT(MONTH FROM s.sold_at) = :month
            """, nativeQuery = true)
    BigDecimal findProfitForMonth(
            @Param("ownerId") Long ownerId,
            @Param("year") int year,
            @Param("month") int month
    );

    // Month-by-month profit across one calendar year.
    @Query(value = """
            SELECT
                CAST(EXTRACT(YEAR FROM s.sold_at) AS integer) AS saleYear,
                CAST(EXTRACT(MONTH FROM s.sold_at) AS integer) AS saleMonth,
                COALESCE(SUM((s.selling_price - s.buying_price) * s.quantity), 0) AS totalProfit
            FROM sales s
            JOIN products p ON s.product_id = p.id
            JOIN categories c ON p.category_id = c.id
            WHERE c.owner_id = :ownerId
              AND EXTRACT(YEAR FROM s.sold_at) = :year
            GROUP BY EXTRACT(YEAR FROM s.sold_at), EXTRACT(MONTH FROM s.sold_at)
            ORDER BY saleMonth
            """, nativeQuery = true)
    List<MonthlyProfitProjection> findMonthlyProfitForYear(
            @Param("ownerId") Long ownerId,
            @Param("year") int year
    );
}